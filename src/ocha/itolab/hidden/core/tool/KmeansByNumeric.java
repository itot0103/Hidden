package ocha.itolab.hidden.core.tool;

import java.util.ArrayList;

import ocha.itolab.common.kmeans.Kmeans;
import ocha.itolab.hidden.core.data.*;


public class KmeansByNumeric {

	public static void execute(IndividualSet is, int numclusters) {
		int numdim = is.numerics.dimensions.size();
		int numelm = is.getNumIndividual();
		
		// Reset if number of clusters is one of less
		if(numclusters <= 1) {
			for (int i = 0; i < numelm; i++) {
				OneIndividual oi = is.getOneIndividual(i);
				oi.setClusterId(-1);
			}
			return;
		}
		
		
		// Initialize Kmeans
		Kmeans km = new Kmeans();
		km.setNumCluster(numclusters);
		km.setNumDim(numdim);
		km.setNumElement(numelm);
		km.initKmenas();
		
		// Set values to Kmeans
		double values[];
		double nvalues[] = new double[numdim];
		
		// for each attribite
		for (int i = 0; i < numelm; i++) {
			OneIndividual oi = is.getOneIndividual(i);
			values = oi.getNumericValues();
			normalize(values, nvalues);
			for(int j = 0; j < numdim; j++) 
				km.setElementValue(i, j, values[j], nvalues[j]);
			km.setElementName(i, Integer.toString(i));
		}
		
		// Execute Kmeans
		km.initializeCenterPoints();
		for(int i = 0; i < 20; i++)
			km.executeKmeans();
		
		// Copy the Kmeans result
		for (int i = 0; i < numelm; i++) {
			OneIndividual oi = is.getOneIndividual(i);
			oi.setClusterId(km.getElementLabel(i));
		}
	}
	
	
	/**
	 * Normalize the multidimensional values
	 */
	static void normalize(double value[], double nvalue[]) {
		double length = 0.0;
		for(int i = 0; i < value.length; i++) {
			length += (value[i] * value[i]);
		}
		if(length < 1.0e-10) return;
		length = 1.0 / Math.sqrt(length);
		for(int i = 0; i < value.length; i++) {
			nvalue[i] = value[i] * length;
		}
	}

}
