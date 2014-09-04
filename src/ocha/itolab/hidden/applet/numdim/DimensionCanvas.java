package ocha.itolab.hidden.applet.numdim;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;

import ocha.itolab.hidden.core.data.*;



public class DimensionCanvas extends JPanel {

	/* var */
	DimensionTransformer trans;
	DimensionDrawer drawer;
	BufferedImage image = null;
	GLCanvas glc;
	IndividualSet ps;
	ArrayList dimensionlist = null;
	
	boolean isMousePressed = false, isAnnotation = true, 
		isShading = true, isWireframe = true, isPickedOnly = true, isClearFlag = false,
		roughSurfaceFlag = false, ligandFlag = false, boneFlag = false, isPotentialFlag = false, isHydroFlag = false;
	int dragMode,dimMode;
	int width, height, mouseX, mouseY;
	double linewidth = 1.0, bgR = 0.0, bgG = 0.0, bgB = 0.0;

	int pressx,pressy,releasex,releasey;

	
	
	public DimensionCanvas(
		int width,
		int height,
		Color foregroundColor,
		Color backgroundColor) {

		super(true);
		this.width = width;
		this.height = height;
		setSize(width, height);
		setColors(foregroundColor, backgroundColor);
		dragMode = 1;
		dimMode = 2;
	
		glc = new GLCanvas();
		
		drawer = new DimensionDrawer(width, height, glc);
		trans = new DimensionTransformer();
		trans.viewReset();
		drawer.setTransformer(trans);
		glc.addGLEventListener(drawer);
		
	}

	
	public DimensionCanvas(int width, int height) {
		this(width, height, Color.white, Color.white);
	}

	public GLCanvas getGLCanvas(){
		return this.glc;
	}
	
	
   public void setDrawer(DimensionDrawer d) {
		drawer = d;
	}

	
	public void setTransformer(DimensionTransformer t) {
		trans = t;
	}

	
	public void setIndividualSet(IndividualSet p) {
		ps = p;
		drawer.setIndividualSet(p);
	}
	

	public void setPosId(int x, int y) {
		drawer.setPosId(x, y);
	}


	public void setDrawType(int type) {
		drawer.setDrawType(type);
	}

	public void setEdgeRatio(double r) {
		drawer.setEdgeRatio(r);
	}
	
	public void display() {
		if (drawer == null) return;

		GLAutoDrawable glAD = null;
		width = (int) getSize().getWidth();
		height = (int) getSize().getHeight();

		glAD = drawer.getGLAutoDrawable();
		if (glAD == null) return;
				
		drawer.getGLAutoDrawable();
		drawer.setWindowSize(width, height);
		
		glAD.display();
		
	}
	

	public void setColors(Color foregroundColor, Color backgroundColor) {
		setForeground(foregroundColor);
		setBackground(backgroundColor);
	}


	public void mousePressed(int x, int y) {
		pressx = x;
		pressy = y;
		isMousePressed = true;
		trans.mousePressed();
		drawer.mousePressed(x, y, dragMode,pressx,pressy);
		drawer.setMousePressSwitch(isMousePressed);
	}

	
	public void mouseReleased(int x,int y) {
		releasex = x;
		releasey = y;
		drawer.mouseReleased(x, y, dragMode, releasex, releasey);
		isMousePressed = false;
		drawer.setMousePressSwitch(isMousePressed);
		
		if(dragMode == 4) {
			dimensionlist = drawer.specifyDimensions();
		}
	}

	public void drag(int xStart, int xNow, int yStart, int yNow) {
		int x = xNow - xStart;
		int y = yNow - yStart;

		// Scale or shift
		if(dragMode == 1 || dragMode == 2)
			trans.drag(x, y, width, height, dragMode);
		
		// Specify 
		if(dragMode == 4)
			drawer.drawStroke(xStart, xNow, yStart, yNow);
	}



	public void setBackground(double r, double g, double b) {
		bgR = r;
		bgG = g;
		bgB = b;
		setBackground(
			new Color((int) (r * 255), (int) (g * 255), (int) (b * 255)));
	}

	
	public void setDragMode(int newMode) {
		dragMode = newMode;
	}


	public void viewReset() {
		trans.viewReset();
	}

	
	public ArrayList getDimensionList() {
		return dimensionlist;
	}
	
	
	public void saveImageFile(File file) {

		width = (int) getSize().getWidth();
		height = (int) getSize().getHeight();
		image = new BufferedImage(width, height, 
                BufferedImage.TYPE_INT_BGR);
		
		/*
		Graphics2D gg2 = image.createGraphics();
		gg2.clearRect(0, 0, width, height);
		b_drawer.draw(gg2);
		d_drawer.draw(gg2);
		try {
			ImageIO.write(image, "bmp", file);
		} catch(Exception e) {
			e.printStackTrace();
		}	
		*/	
	}
	
	
	public void addCursorListener(EventListener eventListener) {
		addMouseListener((MouseListener) eventListener);
		addMouseMotionListener((MouseMotionListener) eventListener);
	}
}
