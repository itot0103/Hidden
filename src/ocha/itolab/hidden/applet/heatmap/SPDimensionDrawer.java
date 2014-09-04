package ocha.itolab.hidden.applet.heatmap;

import java.awt.image.BufferedImage;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.nio.Buffer;
import java.util.*;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.gl2.GLUT;

import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.gl2.GLUgl2;

import ocha.itolab.hidden.core.data.*;;


public class SPDimensionDrawer implements GLEventListener {
	
	public static int PCP = 1, SP = 2;
	
	private GL gl;
	private GLUT glut;
	private GLU glu;
	private GL2 gl2;
	private GLUgl2 glu2;
	GLAutoDrawable glAD;

	private int dragMode;
	
	SPDimensionTransformer trans = null;
	IndividualSet ps;

	int imageSize[] = new int[2];
	double dimEdgeratio = 0.0;
	int recx1 = 0, recx2 = 0, recy1 = 0, recy2 = 0;
	boolean isRect = false;
		
	boolean isMousePressed = false/*, isAnnotation = true, isLigandFlag = true*/;
	int xposId = 0, yposId = 1;
	
	DrawerUtility du = null;
	GLCanvas glcanvas;	

	double minmaxdiff[];
	
	IntBuffer view = IntBuffer.allocate(4);
	DoubleBuffer model = DoubleBuffer.allocate(16);
	DoubleBuffer proj = DoubleBuffer.allocate(16);
	DoubleBuffer objPos = DoubleBuffer.allocate(3);


	/**
	 */
	public SPDimensionDrawer(int width, int height, GLCanvas glc){
		glcanvas = glc;
		imageSize[0] = width;
		imageSize[1] = height;
		du = new DrawerUtility(width, height);
		
		view = IntBuffer.allocate(4);
		model = DoubleBuffer.allocate(16);
		proj = DoubleBuffer.allocate(16);

		glcanvas.addGLEventListener(this);
	}
	
	public GLAutoDrawable getGLAutoDrawable(){
		return glAD;
	}
	
	

	public void setTransformer(SPDimensionTransformer view) {
		this.trans = view;
//		du.setTransformer(view);
//		trans.setDrawerUtility(du);
	}
	

	public void setWindowSize(int width, int height) {
		imageSize[0] = width;
		imageSize[1] = height;
		du.setWindowSize(width, height);
	}
 
	public void setMousePressSwitch(boolean isMousePressed) {
		this.isMousePressed = isMousePressed;
	}
		

	public void setPosId(int x, int y) {
		xposId = x;   yposId = y;
	}
	

	public void setIndividualSet(IndividualSet p) {
		ps = p;
	}
	
	
	public void setDimensionEdgeRatio(double r) {
		dimEdgeratio = r;
	}

	
	public void init(GLAutoDrawable drawable) {
		gl = drawable.getGL();
		glut = new GLUT();
		glu = new GLU();
		gl2= drawable.getGL().getGL2();
		glu2 = new GLUgl2();
		this.glAD = drawable;
		
		gl.glEnable(GL.GL_RGBA);
		gl2.glEnable(GL2.GL_DEPTH);
		gl2.glEnable(GL2.GL_DOUBLE);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl2.glEnable(GL2.GL_NORMALIZE);
		gl.glEnable(GL.GL_CULL_FACE);
		gl2.glDisable(GL2.GL_LIGHTING);
		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
	
	}
	
	
	public void reshape(GLAutoDrawable drawable,
			int x, int y,
			int width, int height) {
		
		imageSize[0] = width;
		imageSize[1] = height;
		float ratio = (float)height / (float)width;
		
		gl.glViewport(0, 0, width, height);
		
		gl2.glMatrixMode(GL2.GL_PROJECTION);
		gl2.glLoadIdentity();
		gl2.glFrustum(-1.0f, 1.0f, -ratio, ratio, 5.0f, 40.0f);
		//gl.glOrtho(-2.0, 2.0, -2.0, 2.0, 0.0, 100.0);
		
		gl2.glMatrixMode(GL2.GL_MODELVIEW);
		gl2.glLoadIdentity();
		gl2.glTranslatef(0.0f, 0.0f, -20.0f);
		
		gl.glGetIntegerv(GL.GL_VIEWPORT, view);
		gl2.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, model);
		gl2.glGetDoublev(GL2.GL_PROJECTION_MATRIX, proj);
		
	}
	
	public void mousePressed(int x, int y, int dragMode,int pressx,int pressy) {
		this.dragMode = dragMode;
	}
	
	public void mouseReleased(int x,int y,int dragMode,int releasex,int releasey) {
		this.dragMode = dragMode;
	}
	
	
	public void display(GLAutoDrawable drawable) {
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		gl2.glLoadIdentity();
		glu.gluLookAt( 0.0, 0.0, 20.0,
		           0.0, 0.0, 0.0,
		           0.0, 1.0, 0.0 );

		double shiftX = trans.getViewShift(0);
		double shiftY = trans.getViewShift(1);
		double scaleX = trans.getViewScaleX() * (double)imageSize[0] / 300.0;
		double scaleY = trans.getViewScaleY() * (double)imageSize[1] / 300.0;

		gl2.glPushMatrix();
		
		gl2.glTranslated(shiftX, shiftY, 0.0);
		gl2.glScaled(scaleX, scaleY, 1.0);
		
		gl.glGetIntegerv(GL.GL_VIEWPORT, view);
		gl2.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, model);
		gl2.glGetDoublev(GL2.GL_PROJECTION_MATRIX, proj);
	
		drawDimensionPlots();
		
		gl2.glPopMatrix();
		
	}
	
	
	
	void drawDimensionPlots() {
		if(ps == null) return;
		double SHIFT_Y = 0.0;
		
		gl2.glColor3d(0.5, 0.5, 0.5);
		gl2.glBegin(GL.GL_LINE_STRIP);
		gl2.glVertex3d(-1.0, ( 1.0 + SHIFT_Y), 0.0);
		gl2.glVertex3d(-1.0, (-1.0 + SHIFT_Y), 0.0);
		gl2.glVertex3d(1.0,  (-1.0 + SHIFT_Y), 0.0);
		gl2.glEnd();
		
		// Draw plots
		gl2.glColor3d(0.0, 0.5, 0.5);
		gl2.glPointSize(4.0f);
		gl2.glBegin(GL.GL_POINTS);
		ArrayList dims =  ps.numerics.dimensions;
		for(int i = 0; i < dims.size(); i++) {
			OneNumeric d = (OneNumeric)dims.get(i);
			double x = d.getX();
			double y = d.getY();
			gl2.glVertex3d(x, (y + SHIFT_Y), 0.0);
		}
		gl2.glEnd();
		
		// Draw edges
		gl2.glColor3d(0.7, 0.7, 0.7);
		for(int i = 0; i < dims.size(); i++) {
			OneNumeric d1 = (OneNumeric)dims.get(i);
			for(int j = (i + 1); j < dims.size(); j++) {
				OneNumeric d2 = (OneNumeric)dims.get(j);
				double dissim = d1.getDissimilarity(j);
				if(dissim < dimEdgeratio) {
					gl2.glBegin(gl2.GL_LINES);
					gl2.glVertex3d(d1.getX(), (d1.getY() + SHIFT_Y), -0.01);
					gl2.glVertex3d(d2.getX(), (d2.getY() + SHIFT_Y), -0.01);
					gl2.glEnd();
				}
			}
		}
		
		// Draw names of dimensions
		for(int i = 0; i < dims.size(); i++) {
			OneNumeric d = (OneNumeric)dims.get(i);
			double x = d.getX();
			double y = d.getY() + SHIFT_Y;
			writeOneString(x, y, ps.getValueName(i));
		}
	
		// Draw rectangle
		if(isRect == true) {
			
			double pos[] = specifyRectanglePosition3D();
			
			gl2.glColor3d(1.0, 0.0, 0.0);
			gl2.glBegin(GL.GL_LINE_LOOP);
			gl2.glVertex3d(pos[0], pos[2] + SHIFT_Y, 0.0);
			gl2.glVertex3d(pos[0], pos[3] + SHIFT_Y, 0.0);
			gl2.glVertex3d(pos[1], pos[3] + SHIFT_Y, 0.0);
			gl2.glVertex3d(pos[1], pos[2] + SHIFT_Y, 0.0);
			gl2.glEnd();
		}
		
	}
	
	
	
	void writeOneString(double x, double y, String word) {
		gl2.glRasterPos3d(x, y, 0.0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, word);
	}
	
	

	public void drawStroke(int xStart, int xNow, int yStart, int yNow) {
		drawDraggedRectangle(xStart, xNow, yStart, yNow);
	}
	
	
	void drawDraggedRectangle(int xStart, int xNow, int yStart, int yNow) {
		recx1 = xStart;   recx2 = xNow;
		recy1 = view.get(3) - yStart;   recy2 = view.get(3) - yNow;
		isRect = true;
	}

	
	ArrayList specifyDimensions() {
		if(isRect == false) return null;
		
		ArrayList list = new ArrayList();
		
		// specify the vertices of the drawn rectangle
		double pos[] = specifyRectanglePosition3D();
		
		// In-out determination 
		ArrayList dims =  ps.numerics.dimensions;
		for(int i = 0; i < dims.size(); i++) {
			OneNumeric d = (OneNumeric)dims.get(i);
			double x = d.getX();
			double y = d.getY();
			if(x < pos[0] || x > pos[1] || y < pos[2] || y > pos[3])
				continue;
			list.add(d);
		}
		
		if(list.size() < 2) return null;
		return list;
	}
	
	double[] specifyRectanglePosition3D() {
		double ret[] = new double[4];
		
		DoubleBuffer p1 = DoubleBuffer.allocate(3);
		DoubleBuffer p2 = DoubleBuffer.allocate(3);
		glu2.gluUnProject(recx1, recy1, 0, model, proj, view, p1);
		glu2.gluUnProject(recx2, recy2, 0, model, proj, view, p2);
		
		double x1 = p1.get(0);
		double y1 = p1.get(1);
		double x2 = p2.get(0);
		double y2 = p2.get(1);
		if(x1 > x2) {
			double xx = x1;    x1 = x2;  x2 = xx;
		}
		if(y1 > y2) {
			double yy = y1;    y1 = y2;  y2 = yy;
		}
		
		ret[0] = x1 * 4.0; // xmin
		ret[1] = x2 * 4.0; // xmax
		ret[2] = y1 * 4.0; // ymin
		ret[3] = y2 * 4.0; // ymax

		return ret;
	}
	
	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}


}