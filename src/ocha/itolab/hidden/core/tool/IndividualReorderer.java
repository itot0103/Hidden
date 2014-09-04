package ocha.itolab.hidden.core.tool;

import java.util.*;
import ocha.itolab.hidden.core.data.*;

public class IndividualReorderer {

	public static void reorder(IndividualSet iset, ArrayList<ArrayList> allchains) {
		
		// for each chain of individualds
		for(int i = 0; i < allchains.size(); i++) {
			ArrayList<OneIndividual> chain = allchains.get(i);

			// copy the distance values
			double distance[][] = new double[chain.size()][];
			for(int j = 0; j < chain.size(); j++)
				distance[j] = new double[chain.size()];
			
			// Correlation (already calculated)
			/*
			for(int j = 0; j < chain.size(); j++) {
				OneIndividual p1 = chain.get(j);
				for(int jj = (j + 1); jj < chain.size(); jj++) {
					OneIndividual p2 = chain.get(jj);
					double d = p1.getDissimilarity(p2.getId());
					distance[j][jj] = distance[jj][j] = d;
				}
			}
			*/
			
			// Inner products between two individuals
			for(int j = 0; j < chain.size(); j++) {
				OneIndividual p1 = chain.get(j);
				double v1[] = p1.getNumericValues();
				for(int jj = (j + 1); jj < chain.size(); jj++) {
					OneIndividual p2 = chain.get(jj);
					double v2[] = p1.getNumericValues();
					
					double d = 0, d1 = 0, d2 = 0;
					for(int k = 0; k < v1.length; k++) {
						d += v1[k] * v2[k];
						d1 += v1[k] * v1[k];
						d2 += v2[k] * v2[k];
					}
					if(Math.abs(d1) < 1.0e-20 || Math.abs(d2) < 1.0e-20) {
						d = 2.0;
					}
					else {
						d /= Math.sqrt(d1) * Math.sqrt(d2);
						d = 1.0 - d;
					}
					distance[j][jj] = distance[jj][j] = d;
				}
			}
			
			
			
			
			// reorder
			int[] neworder = Reorderer.reorder(distance);
			
			// replace the order array
			ArrayList<OneIndividual> newchain = new ArrayList<OneIndividual>();
			for(int j = 0; j < neworder.length; j++) {
				OneIndividual p1 = chain.get(neworder[j]);
				newchain.add(p1);
			}
			allchains.remove(chain);
			allchains.add(newchain);
			
		}
		
	}
	
}
