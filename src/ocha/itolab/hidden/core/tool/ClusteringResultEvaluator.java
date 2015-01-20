package ocha.itolab.hidden.core.tool;

import java.util.*;
import ocha.itolab.hidden.core.data.*;

public class ClusteringResultEvaluator {
	static IndividualSet iset = null;
	static ArrayList<OneIndividual> representatives = new ArrayList<OneIndividual>();
	static double[] clusterSizes;
	
	/**
	 * Evaluate the quality of clustering result
	 */
	public static void evaluate(IndividualSet is, ArrayList<ArrayList> dimlist) {
		
		if(is == null) return;
		if(dimlist == null || dimlist.size() <= 0) return;
		clusterSizes = new double[dimlist.size()];
		double rsum = 0.0;
		iset = is;
		
		// select representative dimensions for each cluster
		representatives.clear();
		for(int i = 0; i < dimlist.size(); i++)
			selectRepresentative(dimlist, i);
		
		// calclate index
		int numk = 0;
		for(int i = 0; i < dimlist.size(); i++) {
			double r = calcIndex(dimlist, i);
			if(r > 0.0) {
				numk++;   rsum += r;
			}
		}
		if(numk >= 1) {
			double r = (double)rsum / (double)numk;
			
			int sumd = 0;
			for(int i = 0; i < dimlist.size(); i++) {
				ArrayList dims = dimlist.get(i);
				sumd += dims.size();
			}
			double r2 = r / (double)sumd;
			
			System.out.println(" *** Clustering quality = " + r + " normalized as " + r2);
		}
	}

	
	/**
	 * Select the representative dimension of the cluster
	 */
	static void selectRepresentative(ArrayList<ArrayList> dimlist, int did) {
		int rid = -1;
		OneIndividual rep = null;
		ArrayList<int[]> dims = dimlist.get(did);
		
		// if there are only one or two dimensions
		if(dims.size() <= 0) return;
		if(dims.size() <= 2) {
			rid = 0;   
			int id = dims.get(0)[0];
			rep = iset.getOneIndividual(id);
		}

		// if there are three or more dimensions
		else {
			double minSum = 1.0e+30;
			for(int i = 0; i < dims.size(); i++) {
				int id1 = dims.get(i)[0];
				OneIndividual n1 = iset.getOneIndividual(id1);
				double sum = 0.0;
				for(int j = 0; j < dims.size(); j++) {
					if(i == j) continue;
					int id2 = dims.get(j)[0];
					OneIndividual n2 = iset.getOneIndividual(id2);
					sum += n1.getDissimilarity(n2.getId());
				}
				if(minSum > sum) {
					minSum = sum;  rid = i;
				}
			}
			int id0 = dims.get(rid)[0];
			rep = iset.getOneIndividual(id0);
		}

		// register the representative dimension
		representatives.add(rep);
		if(dims.size() == 1) return;
		
		// calculate average distance
		double dsum = 0.0;
		for(int i = 0; i < dims.size(); i++) {
			if(i == rid) continue;
			int id2 = dims.get(i)[0];
			OneIndividual n2 = iset.getOneIndividual(id2);
			dsum += rep.getDissimilarity(n2.getId());
		}
		dsum /= (double)(dims.size() - 1);
		clusterSizes[did] = dsum;
		
	}
	
	
	/**
	 * Calculate index value of the cluster
	 */
	static double calcIndex(ArrayList<ArrayList> dimlist, int did) {
		double rmax = 0.0;
		OneIndividual rep1 = representatives.get(did);
		
		// for each other clusters
		for(int i = 0; i < dimlist.size(); i++) {
			if(i == did) continue;
			OneIndividual rep2 = representatives.get(i);
			double r = (clusterSizes[i] + clusterSizes[did]) / rep1.getDissimilarity(rep2.getId());
			if(r > rmax) rmax = r;
		}
		
		return rmax;
	}
	
}
 