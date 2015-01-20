package ocha.itolab.hidden.applet.numdim;

import java.nio.*;
import java.util.*;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.gl2.GLUgl2;

import ocha.itolab.hidden.core.data.*;

import com.jogamp.opengl.util.gl2.GLUT;


public class DimensionSP {

	private GL gl;
	private GLUT glut;
	private GLU glu;
	private GL2 gl2;
	private GLUgl2 glu2;
	GLAutoDrawable glAD;

	int xposId = 0, yposId = 1;
	IndividualSet ps;
	boolean isRect = false;

	IntBuffer view;
	DoubleBuffer model, proj;
	
	int recx1 = 0, recx2 = 0, recy1 = 0, recy2 = 0;
	double edgeratio = 0.0;
	
	
		
	/**
	 * Constructor
	 */
	public DimensionSP(GL gl, GL2 gl2, GLU glu, GLUgl2 glu2, GLUT glut, GLAutoDrawable glAD) {
		this.gl = gl;
		this.gl2 = gl2;
		this.glu = glu;
		this.glu2 = glu2;
		this.glut = glut;
		this.glAD = glAD;
	}
	


	public void setPosId(int x, int y) {
		xposId = x;   yposId = y;
	}
	
	
	public void setIndividualSet(IndividualSet p) {
		ps = p;
	
	}
	
	
	public void setEdgeRatio(double r) {
		edgeratio = r;
	}

	
	public void draw(IntBuffer v, DoubleBuffer m, DoubleBuffer p) {
		if(ps == null) return;
		
		view = v;
		model = m;
		proj = p;
		
		gl2.glLineWidth(1.0f);
		gl2.glColor3d(0.5, 0.5, 0.5);
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(-1.0, 1.0, 0.0);
		gl2.glVertex3d(-1.0, -1.0, 0.0);
		gl2.glVertex3d(1.0, -1.0, 0.0);
		gl2.glVertex3d(1.0, 1.0, 0.0);
		gl2.glEnd();
		
		// Draw plots
		gl2.glColor3d(0.0, 0.5, 0.5);
		gl2.glPointSize(6.0f);
		gl2.glBegin(GL.GL_POINTS);
		ArrayList dims =  ps.numerics.dimensions;
		for(int i = 0; i < dims.size(); i++) {
			OneNumeric d = (OneNumeric)dims.get(i);
			if(d.getStatus() == d.STATUS_INVISIBLE) continue;
			double x = d.getX();
			double y = d.getY();
			gl2.glVertex3d(x, y, 0.0);
		}
		gl2.glEnd();
		
		// Draw edges
		gl2.glLineWidth(1.0f);
		gl2.glColor3d(0.7, 0.7, 0.7);
		for(int i = 0; i < dims.size(); i++) {
			OneNumeric d = (OneNumeric)dims.get(i);
			if(d.getStatus() == d.STATUS_INVISIBLE) continue;
			for(int j = (i + 1); j < dims.size(); j++) {
				OneNumeric d2 = (OneNumeric)dims.get(j);
				if(d2.getStatus() == d2.STATUS_INVISIBLE) continue;
				double dissim = d.getDissimilarity(j);
				if(dissim < edgeratio) {
					gl2.glBegin(gl2.GL_LINES);
					gl2.glVertex3d(d.getX(), d.getY(), -0.01);
					gl2.glVertex3d(d2.getX(), d2.getY(), -0.01);
					gl2.glEnd();
				}
			}
		}
		
		// Draw names of dimensions
		for(int i = 0; i < dims.size(); i++) {
			OneNumeric d = (OneNumeric)dims.get(i);
			if(d.getStatus() == d.STATUS_INVISIBLE) continue;
			double x = d.getX();
			double y = d.getY();
			writeOneString(x, y, ps.getValueName(i));
		}
	
		// Draw rectangle
		if(isRect == true) {
			double pos[] = specifyRectanglePosition3D();
			gl2.glColor3d(1.0, 0.0, 0.0);
			gl2.glBegin(GL.GL_LINE_LOOP);
			gl2.glVertex3d(pos[0], pos[2], 0.0);
			gl2.glVertex3d(pos[0], pos[3], 0.0);
			gl2.glVertex3d(pos[1], pos[3], 0.0);
			gl2.glVertex3d(pos[1], pos[2], 0.0);
			gl2.glEnd();
		}
		
	}
	
	
	
	void writeOneString(double x, double y, String word) {
		gl2.glRasterPos3d(x, y, 0.0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, word);
	}
	
	
	
	void drawStroke(int xStart, int xNow, int yStart, int yNow) {
		drawDraggedRectangle(xStart, xNow, yStart, yNow);
	}
	
	
	void drawDraggedRectangle(int xStart, int xNow, int yStart, int yNow) {
		recx1 = xStart;   recx2 = xNow;
		recy1 = view.get(3) - yStart;   recy2 = view.get(3) - yNow;
		isRect = true;
	}

	
	ArrayList specifyDimensions() {
		if(isRect == false) return null;
		double pos2[] = new double[4];
		
		ArrayList list = new ArrayList();
		
		// specify the vertices of the drawn rectangle
		double pos[] = specifyRectanglePosition3D();
		if(pos[0] > pos[1]) {
			pos2[0] = pos[1];   pos2[1] = pos[0];
		}
		else {
			pos2[0] = pos[0];   pos2[1] = pos[1];
		}
		if(pos[2] > pos[3]) {
			pos2[2] = pos[3];   pos2[3] = pos[2];
		}
		else {
			pos2[2] = pos[2];   pos2[3] = pos[3];
		}	
		
		// In-out determination 
		ArrayList dims =  ps.numerics.dimensions;
		for(int i = 0; i < dims.size(); i++) {
			OneNumeric d = (OneNumeric)dims.get(i);
			double x = d.getX();
			double y = d.getY();
			if(x < pos2[0] || x > pos2[1] || y < pos2[2] || y > pos2[3])
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
		
		ret[0] = x1 * 2.0; // xmin
		ret[1] = x2 * 2.0; // xmax
		ret[2] = y1 * 2.0; // ymin
		ret[3] = y2 * 2.0; // ymax

		return ret;
	}
}
