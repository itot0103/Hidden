package ocha.itolab.hidden.core.tool;

import java.util.*;

import mdsj.MDSJ;
import ocha.itolab.hidden.core.data.*;


public class IndividualDissimilarity {
	static IndividualSet iset;
	
	/**
	 * Calculate dissimilarity among dimensions
	 * @param iset
	 */
	public static void calculate(IndividualSet is) {
		iset = is;
		
		// for each pair of dimensions
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			for(int j = (i + 1); j < iset.getNumIndividual(); j++)  {
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
		double count = 0;
		ArrayList<double[]> list1 = new ArrayList();
		ArrayList<double[]> list2 = new ArrayList();
		
		
		// for each numeric dimension 
		OneIndividual p1 = iset.getOneIndividual(id1);
		OneIndividual p2 = iset.getOneIndividual(id2);
		double numeric1[] = p1.getNumericValues();
		double numeric2[] = p2.getNumericValues();
		for(int i = 0; i < iset.getNumNumeric(); i++) {			
			double s1[] = new double[1];
			double s2[] = new double[1];
			s1[0] = numeric1[i];
			s2[0] = numeric2[i];
			list1.add(s1);
			list2.add(s2);
			n++;
		}
		
		// determine the dissimilarity
		for(int i = 0; i < n - 1; i ++){
			for(int j = i + 1; j < n; j ++){
				double f0[] = list1.get(i);
				double f1[] = list1.get(j);
				double f2[] = list2.get(i);
				double f3[] = list2.get(j);
				if (f0[0] > f1[0]) {
					if(f2[0] > f3[0]){
						count ++;
					}
					if(f2[0] < f3[0]){
						count --;
					}
				}
				if (f0[0] < f1[0]) {
					if(f2[0] < f3[0]){
						count ++;
					}
					if(f2[0] > f3[0]){
						count --;
					}
				}
			}
		}
	
		// calculate the rank correlation
		double num_t = (double)(n * (n - 1) / 2);
		double r = 1.0 - Math.abs((double)count / num_t);
		
		//System.out.println("   id1=" + id1 + " id2=" + id2 + " dissim=" + r);
		
		// substitute the correlation
		p1.setDissimilarity(r, id2, iset.getNumIndividual());
		p2.setDissimilarity(r, id1, iset.getNumIndividual());
		
	}
	
	
	/**
	 * Calculate positions applying MDS
	 */
	static void applyMDS() {

		// Copy dissimilarity to an allocated array
		double input[][] = new double[iset.getNumIndividual()][];
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			input[i] = new double[iset.getNumIndividual()];
		}
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			OneIndividual p1 = iset.getOneIndividual(i);
			for(int j = 0; j < iset.getNumIndividual(); j++) {
				input[i][j] = p1.getDissimilarity(j);
			}
		}
		
		// Apply MDS
		double[][] output = MDSJ.classicalScaling(input);
		
		// Calculate positions
		double min1 = +1.0e+30, max1 = -1.0e+30;
		double min2 = +1.0e+30, max2 = -1.0e+30;
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			min1 = (min1 < output[0][i]) ? min1 : output[0][i];
			max1 = (max1 > output[0][i]) ? max1 : output[0][i];
			min2 = (min2 < output[1][i]) ? min2 : output[1][i];
			max2 = (max2 > output[1][i]) ? max2 : output[1][i];
		}
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			OneIndividual p = iset.getOneIndividual(i);
			double x = ((output[0][i] - min1) / (max1 - min1)) * 2.0 - 1.0;
			double y  = ((output[1][i] - min2) / (max2 - min2)) * 2.0 - 1.0;
			p.setPosition(x, y);
		}
	}
	
}
