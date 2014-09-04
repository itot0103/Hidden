package ocha.itolab.hidden.applet.numdim;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.gl2.GLUgl2;

import ocha.itolab.hidden.core.data.*;
import com.jogamp.opengl.util.gl2.GLUT;


public class IndividualSP {

	private GL gl;
	private GLUT glut;
	private GLU glu;
	private GL2 gl2;
	private GLUgl2 glu2;
	GLAutoDrawable glAD;

	int xposId = 0, yposId = 1;
	IndividualSet ps;
	
	private boolean flag = false; 
	
	/**
	 * Constructor
	 */
	public IndividualSP(GL gl, GL2 gl2, GLU glu, GLUgl2 glu2, GLUT glut, GLAutoDrawable glAD) {
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
	
	
	public void draw() {
		if(ps == null) return;
		
		gl2.glColor3d(0.5, 0.5, 0.5);
		gl2.glBegin(GL.GL_LINE_STRIP);
		gl2.glVertex3d(-1.0, 1.0, 0.0);
		gl2.glVertex3d(-1.0, -1.0, 0.0);
		gl2.glVertex3d(1.0, -1.0, 0.0);
		gl2.glEnd();
		
		gl2.glColor3d(0.8, 0.8, 0.8);
		gl2.glPointSize(2.0f);
		gl2.glBegin(GL.GL_POINTS);
		for(int i = 0; i < ps.getNumIndividual(); i++) {
			OneIndividual p = ps.getOneIndividual(i);
			double numeric[] = p.getNumericValues();
			double x = (numeric[xposId] - ps.numerics.min[xposId]) / (ps.numerics.max[xposId] - ps.numerics.min[xposId]);
			double y = (numeric[yposId] - ps.numerics.min[yposId]) / (ps.numerics.max[yposId] - ps.numerics.min[yposId]);
			x = x * 2.0 - 1.0;
			y = y * 2.0 - 1.0;
			
			gl2.glVertex3d(x, y, 0.0);
		}
		flag = false;
		gl2.glEnd();
		
	}
	
}
