package ocha.itolab.hidden.applet.booldim;

import java.awt.Color;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.gl2.GLUT;
import ocha.itolab.hidden.core.data.*;

public class IndividualPCP {

	private GL gl;
	private GLUT glut;
	private GLU glu;
	private GL2 gl2;
	private GLUgl2 glu2;
	GLAutoDrawable glAD;
	
	IndividualSet ps;
	double dsize = 1.5;
	
	/**
	 * Constructor
	 */
	public IndividualPCP(GL gl, GL2 gl2, GLU glu, GLUgl2 glu2, GLUT glut, GLAutoDrawable glAD) {
		this.gl = gl;
		this.gl2 = gl2;
		this.glu = glu;
		this.glu2 = glu2;
		this.glut = glut;
		this.glAD = glAD;
	}

	
	public void setIndividualSet(IndividualSet p) {
		ps = p;
	}

	
	public void draw() {
		if(ps == null) return;

		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
	
		gl2.glEnable(GL.GL_BLEND);
		gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl2.glDisable(GL.GL_DEPTH_TEST);
		
		gl2.glColor3d(0.5, 0.5, 0.5);
		for(int i = 0; i < ps.getNumNumeric(); i++) {
			gl2.glBegin(GL.GL_LINES);
			double x = (double)i / (double)(ps.getNumNumeric() - 1);
			x = x * dsize * 2.0 - dsize;
			gl2.glVertex3d(x, -dsize, 0.0);
			gl2.glVertex3d(x,  dsize, 0.0);
			gl2.glEnd();
			
			writeOneString(x, -(dsize * 1.1), df.format(ps.numerics.min[i]));
			writeOneString(x,  (dsize * 1.1), df.format(ps.numerics.max[i]));
		}	
		gl2.glColor4d(0.8, 0.8, 0.8,0.02);
		
	
		for(int i = 0; i < ps.getNumIndividual(); i++) {
			OneIndividual p = ps.getOneIndividual(i);
			gl2.glColor4d(0.0, 0.0, 1.0, 0.1);

			gl2.glBegin(GL.GL_LINE_STRIP);
			for(int j = 0; j < ps.getNumNumeric(); j++) {//draw the lines of a data
				double x = (double)j / (double)(ps.getNumNumeric() - 1);
				double numeric[] = p.getNumericValues();
				double y = (numeric[j] - ps.numerics.min[j]) / (ps.numerics.max[j] - ps.numerics.min[j]);
				x = x * dsize * 2.0 - dsize;
				y = y * dsize * 2.0 - dsize;
				gl2.glVertex3d(x,y, 0.0);	
			}
			
			gl2.glEnd();
		}
		
	}
	
	
	void writeOneString(double x, double y, String word) {
		gl2.glRasterPos3d(x, y, 0.0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, word);
	}
	
}
