package ocha.itolab.hidden.applet.heatmap;

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
import ocha.itolab.hidden.core.tool.IndividualReorderer;
import ocha.itolab.hidden.core.tool.NumericDimensionReorderer;

public class IndividualHeatmap {

	private GL gl;
	private GLUT glut;
	private GLU glu;
	private GL2 gl2;
	private GLUgl2 glu2;
	GLAutoDrawable glAD;
	
	IndividualSet ps;
	double dsize = 1.5;
	int numclusters = 1;
	int totalnumBelts = 1;
	int drawnDims[] = null;
	ArrayList specifiedInds = null;
	ArrayList specifiedDims = null;
	ArrayList<ArrayList> indCliques = null;
	ArrayList<ArrayList> drawnInds = null;
	
	
	/**
	 * Constructor
	 */
	public IndividualHeatmap(GL gl, GL2 gl2, GLU glu, GLUgl2 glu2, GLUT glut, GLAutoDrawable glAD) {
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
	
	
	public void setIndividualSet(IndividualSet is) {
		ps = is;
		
		setDrawnDimensions(null);
		
		drawnInds = new ArrayList<ArrayList>();
		ArrayList<OneIndividual> allinds = new ArrayList<OneIndividual>();
		for(int i = 0; i < ps.getNumIndividual(); i++) {
			OneIndividual p = ps.getOneIndividual(i);
			allinds.add(p);
		}
		drawnInds.add(allinds);
		IndividualReorderer.reorder(ps, drawnInds);
		calcNumBelts();
	}

	
	public void setSpecifiedDimensionList(ArrayList list) {
		specifiedDims = list;
		setDrawnDimensions(list);
	}
  	
	
	public void setSpecifiedIndividualList(ArrayList list) {
		specifiedInds = list;
		if(list != null) {
			drawnInds = new ArrayList<ArrayList>();
			drawnInds.add(specifiedInds);
			IndividualReorderer.reorder(ps, drawnInds);
		}
		else {
			 setIndividualSet(ps);
		}
		calcNumBelts();
	}
	
	
	void calcNumBelts() {
		totalnumBelts = 0;
		if(drawnInds == null) return;

		for(int i = 0; i < drawnInds.size(); i++) {
			if(i > 0) totalnumBelts++;
			ArrayList inds = drawnInds.get(i);
			totalnumBelts += inds.size();
		}
	}
	
	
	void setDrawnDimensions(ArrayList list) {
		if(ps == null) {
			drawnDims = null;  return;
		}
		else if(list == null) {
			drawnDims = new int[ps.getNumNumeric()];
			for(int i = 0; i < drawnDims.length; i++)
				drawnDims[i] = i;
		}
		else {
			drawnDims = new int[list.size()];
			for(int i = 0; i < drawnDims.length; i++) {
				OneNumeric d = (OneNumeric)list.get(i);
				drawnDims[i] = d.getId();
			}
		}
		
	}
	
	
	
	public void setIndividualCliques(ArrayList<ArrayList> cliques) {
		indCliques = cliques;
		drawnInds = new ArrayList<ArrayList>();
		if(indCliques == null || indCliques.size() <= 0) {
			setIndividualSet(ps);
		}
		else {
			for(int i = 0; i < indCliques.size(); i++) {
				ArrayList inds1 = indCliques.get(i);
				ArrayList<OneIndividual> inds2 = new ArrayList<OneIndividual>();
				for(int j = 0; j < inds1.size(); j++) {
					int[] id = (int[])inds1.get(j);
					OneIndividual p = ps.getOneIndividual(id[0]);
					inds2.add(p);
				}
				drawnInds.add(inds2);
			}
			IndividualReorderer.reorder(ps, drawnInds);
		}
		calcNumBelts();
	}
	
	
	public void draw() {
		if(ps == null) return;

		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
	
		gl2.glEnable(GL.GL_BLEND);
		gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl2.glDisable(GL.GL_DEPTH_TEST);
		
		drawHeatmap();
	}
	
	
	void drawHeatmap() {
		if(drawnDims == null) return;
		
		double beltHeight = 1.0 / (double)totalnumBelts;
		int ycount = 0;
		
		// for each individual
		for(int i = 0; i < drawnInds.size(); i++) {
			ArrayList<OneIndividual> inds = drawnInds.get(i);
			if(i > 0) ycount++;

			for(int ii = 0; ii < inds.size(); ii++) {
				OneIndividual p = inds.get(ii);
				double numeric[] = p.getNumericValues();
				double y1 = ycount * beltHeight;
				double y2 = (ycount + 1) * beltHeight;
				y1 = y1 * 2.0 - 1.0;
				y2 = y2 * 2.0 - 1.0;
				ycount++;
				
				// for each numeric value
				for(int j = 0; j < drawnDims.length; j++) {
					int jj = drawnDims[j];
					double value = (numeric[jj] - ps.numerics.min[jj]) / (ps.numerics.max[jj] - ps.numerics.min[jj]);
					double x1 = (double)j / (double)(drawnDims.length - 1);
					double x2 = (double)(j + 1) / (double)(drawnDims.length  - 1);
					x1 = x1 * 2.0 - 1.0;
					x2 = x2 * 2.0 - 1.0;
				
					Color color = calcColor(value);
					double rr = (double)color.getRed() / 255.0;
					double gg = (double)color.getGreen() / 255.0;
					double bb = (double)color.getBlue() / 255.0;
					gl2.glColor3d(rr, gg, bb);
					gl2.glBegin(gl2.GL_POLYGON);
					gl2.glVertex3d(x1, y1, 0.0);
					gl2.glVertex3d(x2, y1, 0.0);
					gl2.glVertex3d(x2, y2, 0.0);
					gl2.glVertex3d(x1, y2, 0.0);
					gl2.glEnd();
				}
			}
		}
	}
	
	
	Color calcColor(double value) {
		double hue, intensity;
		double colorCoef = 0.4;
		Color color;
		
		if(value <= 1.0){
			hue = (1.0 - value) * 160.0 / 240.0;
			intensity = 0.2 + 0.8 * value;
	
			if(value > 0.0001) {
				intensity = Math.pow(intensity, colorCoef);
				if(intensity < 0.2) intensity = 0.2;
			}
	
			color = Color.getHSBColor((float)hue, 1.0f, (float)intensity);
		}
	
		// 集計値が無効のとき灰色を返す
		else {
			color = new Color(20, 20, 20);
		}
		
		return color;
	}
		
		
	void writeOneString(double x, double y, String word) {
		gl2.glRasterPos3d(x, y, 0.0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, word);
	}
	
}
