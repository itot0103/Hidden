package ocha.itolab.hidden.applet.booldim;

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
	
	IntBuffer view;
	DoubleBuffer model, proj;
	
	int recx1 = 0, recx2 = 0, recy1 = 0, recy2 = 0;
	ArrayList selectlist = null;
	
		
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
	


	public void setXdimensionId(int id) {
		xposId = id;
	}
	
	
	public void setYdimensionId(int id) {
		yposId = id;
	}
	
	public void setIndividualSet(IndividualSet p) {
		ps = p;
	
	}
	
	
	public void draw(IntBuffer v, DoubleBuffer m, DoubleBuffer p) {
		if(ps == null) return;
		
		view = v;
		model = m;
		proj = p;
		
		gl2.glColor3d(0.5, 0.5, 0.5);
		gl2.glBegin(GL.GL_LINE_STRIP);
		gl2.glVertex3d(-1.0, 1.0, 0.0);
		gl2.glVertex3d(-1.0, -1.0, 0.0);
		gl2.glVertex3d(1.0, -1.0, 0.0);
		gl2.glEnd();
		
		// Draw plots
		gl2.glPointSize(4.0f);
		gl2.glBegin(GL.GL_POINTS);
		ArrayList dims =  ps.booleans.dimensions;
		for(int i = 0; i < dims.size(); i++) {
			OneBoolean d = (OneBoolean)dims.get(i);

			gl2.glColor3d(0.5, 0.5, 0.5);
			if(selectlist != null) {
				for(int j = 0; j < selectlist.size(); j++) {
					int id[] = (int[])selectlist.get(j);
					if(id[0] == i) {
						gl2.glColor3d(BooldimViewer.plotcolors[j][0],
									  BooldimViewer.plotcolors[j][1],
									  BooldimViewer.plotcolors[j][2]);
						break;
					}
				}
			}
			double x = d.getX();
			double y = d.getY();
			gl2.glVertex3d(x, y, 0.0);
		}
		gl2.glEnd();
		
		// Draw names of dimensions
		gl2.glColor3d(0.5, 0.0, 0.5);
		for(int i = 0; i < dims.size(); i++) {
			OneBoolean d = (OneBoolean)dims.get(i);
			double x = d.getX();
			double y = d.getY();
			OneBoolean ob = (OneBoolean)ps.booleans.dimensions.get(i);
			writeOneString(x, y, ob.getName());
		}
	
		
	}
	
	
	
	void writeOneString(double x, double y, String word) {
		gl2.glRasterPos3d(x, y, 0.0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, word);
	}
	
	
	void select(int xStart, int xNow, int yStart, int yNow, ArrayList list, IntBuffer v, DoubleBuffer m, DoubleBuffer p) {
		selectlist = list;

		view = v;
		model = m;
		proj = p;
		
		DoubleBuffer ppos1 = DoubleBuffer.allocate(3);
		glu2.gluUnProject(xNow, yNow, 0.0, model, proj, view, ppos1);
		
		ArrayList dims =  ps.booleans.dimensions;
		for(int i = 0; i < dims.size(); i++) {
			if(selectlist.size() >= BooldimViewer.plotcolors.length) return;
			OneBoolean d = (OneBoolean)dims.get(i);

			for(int j = 0; j < selectlist.size(); j++) {
				int id[] = (int[])selectlist.get(j);
				if(id[0] == i) {
					d = null; break;
				}
			}
			if(d == null) continue;
			
			double x = d.getX();
			double y = d.getY();
			double xx = ppos1.get(0) * 2.0;
			double yy = -ppos1.get(1) * 2.0;
			double dist2 = (x - xx) * (x - xx) + (y - yy) * (y - yy);
			if(dist2 < 0.03) {
				int id[] = new int[1];
				id[0] = i;
				selectlist.add(id);
			}
			
			
		}
			
	}
	


}
