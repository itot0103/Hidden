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
import javax.media.opengl.awt.GLJPanel;
import ocha.itolab.hidden.core.data.*;

public class IndividualCanvas extends JPanel {

	/* var */
	IndividualTransformer trans;
	IndividualDrawer drawer;
	GLCanvas glc;
	BufferedImage image = null;
	IndividualSet ps;
	
	boolean isMousePressed = false, isAnnotation = true;
	int dragMode, segmentMode, gridMode = 1;
	int width, height, mouseX, mouseY;
	double linewidth = 1.0, bgR = 0.0, bgG = 0.0, bgB = 0.0;

	int pressx,pressy,releasex,releasey;

	
	public IndividualCanvas(
		int width,
		int height,
		Color foregroundColor,
		Color backgroundColor) {

		this.width = width;
		this.height = height;
		setSize(width, height);
		setColors(foregroundColor, backgroundColor);
		dragMode = 1;
		
		glc = new GLCanvas();
		drawer = new IndividualDrawer(width, height, glc);
		trans = new IndividualTransformer();
		trans.viewReset();
		drawer.setTransformer(trans);
	}

	
	public IndividualCanvas(int width, int height) {
		this(width, height, Color.white, Color.white);
	}

	public GLCanvas getGLCanvas(){
		return this.glc;
	}

	
	public void setDrawer(IndividualDrawer d) {
		drawer = d;
	}
	
	public void setPlotTransformer(IndividualTransformer t) {
		trans = t;
	}

	
	public void setIndividualSet(IndividualSet p) {
		ps = p;
		drawer.setIndividualSet(p);
	}
	
	public void setDimensionList(ArrayList list) {
		drawer.setDimensionList(list);
	}
	
	
	public void setPosId(int x, int y) {
		drawer.setPosId(x, y);
	}

	public void setDrawType(int type) {
		drawer.setDrawType(type);
	}

	public void setDimensionCliques(ArrayList<ArrayList> cliques) {
		drawer.setDimensionCliques(cliques);
	}
	
	public void setNumClusters(int n) {
		drawer.setNumClusters(n);
	}
	
	public void setTransparency(double t) {
		drawer.setTransparency(t);
	}

	public void display() {
		GLAutoDrawable glAD = drawer.getGLAutoDrawable();

		if (drawer != null) {
			width = (int) getSize().getWidth();
			height = (int) getSize().getHeight();
			drawer.setWindowSize(width, height);
		}
		if (glAD == null)
			return;
		
		glAD.display();
	}
	

	public void saveImageFile(File file) {

		width = (int) getSize().getWidth();
		height = (int) getSize().getHeight();
		image = new BufferedImage(width, height, 
                BufferedImage.TYPE_INT_BGR);
		
		Graphics2D gg2 = image.createGraphics();
		gg2.clearRect(0, 0, width, height);
		//drawer.draw(gg2);
		try {
			ImageIO.write(image, "bmp", file);
		} catch(Exception e) {
			e.printStackTrace();
		}		
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
		drawer.mousePressed(x, y, dragMode, pressx, pressy);
		drawer.setMousePressSwitch(isMousePressed);
	}

	
	public void mouseReleased(int x,int y) {
		
		releasex = x;
		releasey = y;
		drawer.mouseReleased(x, y, dragMode, releasex, releasey);
		
		isMousePressed = false;
		drawer.setMousePressSwitch(isMousePressed);
		
		
	}

	
	public void drag(int xStart, int xNow, int yStart, int yNow) {
		int x = xNow - xStart;
		int y = yNow - yStart;
		if(dragMode >= 1)
			trans.drag(x, y, width, height, dragMode);
		else
			drawer.mouseDraged(xStart, xNow, yStart, yNow);
		display();
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


	public void addCursorListener(EventListener eventListener) {
		addMouseListener((MouseListener) eventListener);
		addMouseMotionListener((MouseMotionListener) eventListener);
	}
}
