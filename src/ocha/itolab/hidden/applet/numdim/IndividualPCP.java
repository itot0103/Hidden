package ocha.itolab.hidden.applet.numdim;

import java.awt.Color;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.text.DecimalFormat;
import java.util.*;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.gl2.GLUgl2;

import com.jogamp.opengl.util.gl2.GLUT;

import ocha.itolab.hidden.core.data.*;
import ocha.itolab.hidden.core.tool.NumericDimensionReorderer;

public class IndividualPCP {

	private GL gl;
	private GLUT glut;
	private GLU glu;
	private GL2 gl2;
	private GLUgl2 glu2;
	GLAutoDrawable glAD;
	
	IndividualSet ps;
	double dsize = 1.5;
	double transparency = 0.1;
	int numclusters = 1;
	ArrayList specifiedlist = null;
	ArrayList<ArrayList> cliques = null;
	ArrayList<ArrayList> drawndims = null;
	
	
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

	
	public void setNumClusters(int n) {
		numclusters = n;
	}
	
	
	public void setTransparency(double t) {
		transparency = t;
	}
	
	
	public void setIndividualSet(IndividualSet p) {
		ps = p;
		drawndims = new ArrayList<ArrayList>();
		ArrayList<OneNumeric> alldims = new ArrayList<OneNumeric>();
		for(int i = 0; i < ps.getNumNumeric(); i++) {
			OneNumeric on = ps.numerics.dimensions.get(i);
			alldims.add(on);
		}
		if(alldims.size() < 20)
			alldims = NumericDimensionReorderer.reorder(alldims);
		drawndims.add(alldims);
	}

	
	public void setDimensionList(ArrayList list) {
		specifiedlist = list;
		if(list != null) {
			drawndims = new ArrayList<ArrayList>();
			drawndims.add(NumericDimensionReorderer.reorder(list));
		}
		else {
			 setIndividualSet(ps);
		}
	}
  	
	
	public void setDimensionCliques(ArrayList<ArrayList> list) {
		cliques = list;
		drawndims = new ArrayList<ArrayList>();
		if(cliques == null || cliques.size() <= 0) {
			setIndividualSet(ps);
		}
		else {
			for(int i = 0; i < cliques.size(); i++) {
				ArrayList<int[]> oneclique = cliques.get(i);
				ArrayList<OneNumeric> dims = new ArrayList<OneNumeric>();
				for(int j = 0; j < oneclique.size(); j++) {
					int id[] = oneclique.get(j);
					dims.add(ps.numerics.dimensions.get(id[0]));
				}
				dims = NumericDimensionReorderer.reorder(dims);
				drawndims.add(dims);
			}
		}
	}
	
	
	public void draw() {
		if(ps == null) return;

		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
	
		gl2.glEnable(GL.GL_BLEND);
		gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl2.glDisable(GL.GL_DEPTH_TEST);
		
		if(cliques == null || cliques.size() <= 1)
			drawSinglePCP();
		else
			drawGridPCP();
	}
	
	
	void drawSinglePCP() {
		ArrayList<OneNumeric> dimensions = new ArrayList<OneNumeric>();
		
		double shift1[] = {0.0, 0.0};
		double scale[] = {1.0, 1.0};
		drawComponentPCP(drawndims.get(0), shift1, scale, -1);
	}
	
	
	void drawGridPCP() {
		double SHIFT4 = 1.0;
		double SHIFT9 = 1.25;
		
		double shift2[][] = {
			{0.0, -SHIFT4}, {0.0, SHIFT4},
		};
		
		double shift4[][] = {
			{-SHIFT4, -SHIFT4}, {-SHIFT4,  SHIFT4},
			{ SHIFT4, -SHIFT4}, { SHIFT4,  SHIFT4}
		};
		double shift6[][] = {
			{-SHIFT4, -SHIFT9}, {-SHIFT4, 0.0}, {-SHIFT4, SHIFT9},
			{SHIFT4, -SHIFT9}, {SHIFT4, 0.0}, {SHIFT4, SHIFT9},
		};
		double shift9[][] = {
			{-SHIFT9, -SHIFT9}, {-SHIFT9, 0.0}, {-SHIFT9, SHIFT9},
			{0.0, -SHIFT9}, {0.0, 0.0}, {0.0, SHIFT9},
			{SHIFT9, -SHIFT9}, {SHIFT9, 0.0}, {SHIFT9, SHIFT9},
		};
		
		double scale[] = new double[2];
		
		if(drawndims.size() <= 2) {
			for(int i = 0; i < 2; i++) {
				ArrayList clique = drawndims.get(i);
				scale[0] = 1.0;    scale[1] = 0.5;
				drawComponentPCP(clique, shift2[i], scale, i);
			}
		}
		else if(drawndims.size() <= 4) {
			for(int i = 0; i < drawndims.size(); i++) {
				ArrayList clique = drawndims.get(i);
				scale[0] = scale[1] = 0.5;
				drawComponentPCP(clique, shift4[i], scale, i);
			}
		}
		else if(drawndims.size() <= 6) {
			for(int i = 0; i < drawndims.size(); i++) {
				ArrayList clique = drawndims.get(i);
				scale[0] = 0.5;    scale[1] = 0.333333333;
				drawComponentPCP(clique, shift6[i], scale, i);
			}
		}
		else if(drawndims.size() <= 9) {
			for(int i = 0; i < drawndims.size(); i++) {
				ArrayList clique = drawndims.get(i);
				scale[0] = scale[1] = 0.333333333;
				drawComponentPCP(clique, shift9[i], scale, i);
			}
		}
		else  {
			for(int i = 0; i < 9; i++) {
				ArrayList clique = drawndims.get(i);
				scale[0] = scale[1] = 0.333333333;
				drawComponentPCP(clique, shift9[i], scale, i);
			}
		}
		
	}

	
	void drawComponentPCP(ArrayList<OneNumeric> dimensions, double shift[], double scale[], int cid) {	 
		if(ps.getDimSelectMode() == ps.DIMSELECT_CLASS_PURELITY
				&& ps.getClassId() >= 0 && cid >= 0) {
			drawComponentPCPWithClassEmphasis(dimensions, shift, scale, cid);
			return;
		}
		
		DecimalFormat df = new DecimalFormat("####.00");  
		
		gl2.glColor3d(0.5, 0.5, 0.5);
		for(int i = 0; i < dimensions.size(); i++) {
			gl2.glBegin(GL.GL_LINES);
			double x = (double)i / (double)(dimensions.size() - 1);
			x = x * dsize * 2.0 - dsize;
			
			double x0 = x * scale[0] + shift[0];
			double y1 = -dsize * scale[1] + shift[1];
			double y2 =  dsize * scale[1] + shift[1];
			
			gl2.glVertex3d(x0, y1, 0.0);
			gl2.glVertex3d(x0, y2, 0.0);
			gl2.glEnd();
			
			OneNumeric on = dimensions.get(i);
			int id = on.getId();
			double y3 = -dsize * scale[1] * 1.2 + shift[1];
			double y4 = -dsize * scale[1] * 1.1 + shift[1];
			double y5 =  dsize * scale[1] * 1.1 + shift[1];
			writeOneString(x0, y3, on.getName());
			writeOneString(x0, y4, df.format(ps.numerics.min[id]));
			writeOneString(x0, y5, df.format(ps.numerics.max[id]));
		}	
			
		gl2.glColor4d(0.0, 0.0, 1.0, 0.1);
		for(int i = 0; i < ps.getNumIndividual(); i++) {
			OneIndividual p = ps.getOneIndividual(i);
			
			if(numclusters >= 2) {
				float hue = (float)p.getClusterId() / (float)numclusters;
				Color color = Color.getHSBColor(hue, 1.0f, 1.0f);
				double rr = (double)color.getRed() / 255.0;
				double gg = (double)color.getGreen() / 255.0;
				double bb = (double)color.getBlue() / 255.0;
				gl2.glColor4d(rr, gg, bb, transparency);
			}
				
			gl2.glBegin(GL.GL_LINE_STRIP);
			for(int j = 0; j < dimensions.size(); j++) {//draw the lines of a data
				OneNumeric on = dimensions.get(j);
				int id = on.getId();
				
				double x = (double)j / (double)(dimensions.size() - 1);
				double numeric[] = p.getNumericValues();
				double y = (numeric[id] - ps.numerics.min[id]) / (ps.numerics.max[id] - ps.numerics.min[id]);
				x = x * dsize * 2.0 - dsize;
				y = y * dsize * 2.0 - dsize;
				x = x * scale[0] + shift[0];
				y = y * scale[1] + shift[1];
				gl2.glVertex3d(x,y, 0.0);	
			}
			
			gl2.glEnd();
		}
		
	}
	
	
	void drawComponentPCPWithClassEmphasis(
			ArrayList<OneNumeric> dimensions, double shift[], double scale[], int cid) {

		
		DecimalFormat df = new DecimalFormat("####.00");  
		
		gl2.glColor3d(0.5, 0.5, 0.5);
		for(int i = 0; i < dimensions.size(); i++) {
			gl2.glBegin(GL.GL_LINES);
			double x = (double)i / (double)(dimensions.size() - 1);
			x = x * dsize * 2.0 - dsize;
			
			double x0 = x * scale[0] + shift[0];
			double y1 = -dsize * scale[1] + shift[1];
			double y2 =  dsize * scale[1] + shift[1];
			
			gl2.glVertex3d(x0, y1, 0.0);
			gl2.glVertex3d(x0, y2, 0.0);
			gl2.glEnd();
			
			OneNumeric on = dimensions.get(i);
			int id = on.getId();
			double y3 = -dsize * scale[1] * 1.2 + shift[1];
			double y4 = -dsize * scale[1] * 1.1 + shift[1];
			double y5 =  dsize * scale[1] * 1.1 + shift[1];
			writeOneString(x0, y3, on.getName());
			writeOneString(x0, y4, df.format(ps.numerics.min[id]));
			writeOneString(x0, y5, df.format(ps.numerics.max[id]));
		}	
			
		gl2.glColor4d(0.0, 0.0, 1.0, 0.1);
		
		if(numclusters >= 2) {
			for(int i = 0; i < ps.getNumIndividual(); i++) {
				OneIndividual p = ps.getOneIndividual(i);
				
				if(p.getClusterId() == cid) continue;
				float hue = (float)p.getClusterId() / (float)numclusters;
				Color color = Color.getHSBColor(hue, 1.0f, 1.0f);
				double rr = (double)color.getRed() / 255.0;
				double gg = (double)color.getGreen() / 255.0;
				double bb = (double)color.getBlue() / 255.0;
				gl2.glColor4d(rr, gg, bb, transparency * 0.25);
				
				gl2.glBegin(GL.GL_LINE_STRIP);
				for(int j = 0; j < dimensions.size(); j++) {//draw the lines of a data
					OneNumeric on = dimensions.get(j);
					int id = on.getId();
				
					double x = (double)j / (double)(dimensions.size() - 1);
					double numeric[] = p.getNumericValues();
					double y = (numeric[id] - ps.numerics.min[id]) / (ps.numerics.max[id] - ps.numerics.min[id]);
					x = x * dsize * 2.0 - dsize;
					y = y * dsize * 2.0 - dsize;
					x = x * scale[0] + shift[0];
					y = y * scale[1] + shift[1];
					gl2.glVertex3d(x,y, 0.0);	
				}
			}
		}
		
		for(int i = 0; i < ps.getNumIndividual(); i++) {
			OneIndividual p = ps.getOneIndividual(i);
			
			if(numclusters >= 2) {
				if(p.getClusterId() != cid) continue;
				float hue = (float)p.getClusterId() / (float)numclusters;
				Color color = Color.getHSBColor(hue, 1.0f, 1.0f);
				double rr = (double)color.getRed() / 255.0;
				double gg = (double)color.getGreen() / 255.0;
				double bb = (double)color.getBlue() / 255.0;
				gl2.glColor4d(rr, gg, bb, transparency);
			}
			
			gl2.glBegin(GL.GL_LINE_STRIP);
			for(int j = 0; j < dimensions.size(); j++) {//draw the lines of a data
				OneNumeric on = dimensions.get(j);
				int id = on.getId();
					
				double x = (double)j / (double)(dimensions.size() - 1);
				double numeric[] = p.getNumericValues();
				double y = (numeric[id] - ps.numerics.min[id]) / (ps.numerics.max[id] - ps.numerics.min[id]);
				x = x * dsize * 2.0 - dsize;
				y = y * dsize * 2.0 - dsize;
				x = x * scale[0] + shift[0];
				y = y * scale[1] + shift[1];
				gl2.glVertex3d(x,y, 0.0);	
			}
			
			gl2.glEnd();
		}		
		
	}
	
	void writeOneString(double x, double y, String word) {
		gl2.glRasterPos3d(x, y, 0.0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, word);
	}
	
}
