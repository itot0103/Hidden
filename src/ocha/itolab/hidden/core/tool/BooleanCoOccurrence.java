package ocha.itolab.hidden.core.tool;

import java.util.*;
import mdsj.MDSJ;
import ocha.itolab.hidden.core.data.*;


public class BooleanCoOccurrence {
	static IndividualSet iset;
	
	/**
	 * Calculate dissimilarity among dimensions
	 * @param iset
	 */
	public static void calculate(IndividualSet is) {
		iset = is;
		if(is.getNumBoolean() < 2) return;
		
		// for each pair of boolean dimensions
		for(int i = 0; i < is.getNumBoolean(); i++) {
			for(int j = i + 1; j < is.getNumBoolean(); j++) {
				calculateOnePair(i, j);
			}
		}
		
		// calculate positions applying MDS
		applyMDS();
	}

	
	/**
	 * Calculate rank correlation for one pair of dimensions
	 */
	static void calculateOnePair(int id1, int id2) {
		int n = 0;
		
		// for each individual
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			OneIndividual p = (OneIndividual)iset.getOneIndividual(i);
			boolean bool[] = p.getBooleanValues();
			boolean b1 = bool[id1];
			boolean b2 = bool[id2];
			if(b1 == true && b2 == true) {
				n++;
			}
		}

		double r = (double)(iset.getNumIndividual() - n) / (double)iset.getNumIndividual();
		ArrayList<OneBoolean> dimensions = iset.booleans.dimensions;
		
		// substitute the correlation
		OneBoolean d1 = dimensions.get(id1);
		d1.setDissimilarity(r, id2);
		OneBoolean d2 = dimensions.get(id2);
		d2.setDissimilarity(r, id1);
		
	}
		
		
	
	/**
	 * Calculate positions applying MDS
	 */
	static void applyMDS() {
		
		// Copy dissimilarity to an allocated array
		double input[][] = new double[iset.getNumBoolean()][];
		for(int i = 0; i < iset.getNumBoolean(); i++) {
			input[i] = new double[iset.getNumBoolean()];
		}
		for(int i = 0; i < iset.getNumBoolean(); i++) {
			OneBoolean d1 = (OneBoolean)iset.booleans.dimensions.get(i);
			for(int j = 0; j < iset.getNumBoolean(); j++) {
				input[i][j] = d1.getDissimilarity(j);
			}
		}
		
		// Apply MDS
		double[][] output = MDSJ.classicalScaling(input);
		
		// Calculate positions
		double min1 = +1.0e+30, max1 = -1.0e+30;
		double min2 = +1.0e+30, max2 = -1.0e+30;
		for(int i = 0; i < iset.getNumBoolean(); i++) {
			min1 = (min1 < output[0][i]) ? min1 : output[0][i];
			max1 = (max1 > output[0][i]) ? max1 : output[0][i];
			min2 = (min2 < output[1][i]) ? min2 : output[1][i];
			max2 = (max2 > output[1][i]) ? max2 : output[1][i];
		}
		for(int i = 0; i < iset.getNumBoolean(); i++) {
			OneBoolean d1 = (OneBoolean)iset.booleans.dimensions.get(i);
			double x  = ((output[0][i] - min1) / (max1 - min1)) * 2.0 - 1.0;
			double y = ((output[1][i] - min2) / (max2 - min2)) * 2.0 - 1.0;
			d1.setPosition(x, y);
		}
	}
	
}
