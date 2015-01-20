package ocha.itolab.hidden.core.tool;

import java.util.*;

import ocha.itolab.hidden.core.data.*;


public class DimensionClassSeparativeness {
	static int NUMDIV = 10;
	static int MAX_NUM_CLASS = 20;
	
	static double support = 0.075;
	static double confidence = 0.1;
	static IndividualSet iset = null;
	static int classId = -1, numclass = 0;
	static boolean visiblearray[];
	
	
	public static void setClassConfidence(double c) {
		confidence = c;
	}
	
	public static void setClassSupport(double s) {
		support = s;
	}
	

	/**
	 * 
	 */
	public static ArrayList<ArrayList> extract(IndividualSet is) {
		ArrayList<ArrayList> allsets = new ArrayList<ArrayList>();
		iset = is;
		classId = iset.getClassId();
		iset.message = "";
		
		// if classId is invalid 
		if(classId < 0 || classId >= (iset.getNumCategory() + iset.getNumBoolean()))
			return null;
		
		// Classes are defined by category variable
		if(classId < iset.getNumCategory()) {
			numclass = is.categories.categories[classId].length;
			if(numclass > MAX_NUM_CLASS)
				return null;
		}
		
		// Classes are defined by boolean variable
		else if(classId < (iset.getNumCategory() + iset.getNumBoolean()))
			numclass = 2;

		// Reset the visible arrray
		visiblearray = new boolean[iset.getNumNumeric()];
		for(int i = 0; i < iset.getNumNumeric(); i++) {
			OneNumeric on = iset.numerics.dimensions.get(i);
			visiblearray[i] = (on.getStatus() == on.STATUS_INVISIBLE) ? false : true;
		}
		
		// Extract corresponding dimensions for each class 
		for(int i = 0; i < numclass; i++) {
			ArrayList<OneNumeric> list = new ArrayList<OneNumeric>();
			allsets.add(list);
		}
		
		// find numeric dimensions well-seperating classes
		findClassSeparatingDimensions(allsets);
		
		return allsets;
	}


	/**
	 * find numeric dimensions well-seperating classes
	 */
	static void findClassSeparatingDimensions(ArrayList<ArrayList> allsets) {
		int supnum = (int)(support * (double)iset.getNumIndividual() / (double)NUMDIV); 
		supnum = (supnum <= 3) ? 3 : supnum;
		
		// allocate counters
		int counter[][] = new int[iset.getNumNumeric()][];
		for(int i = 0; i < counter.length; i++)
			counter[i] = new int[NUMDIV];
		
		// for each individual
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			OneIndividual p = iset.getOneIndividual(i);
			double numeric[] = p.getNumericValues();
			
			// for each numeric dimension
			for(int j = 0; j < iset.getNumNumeric(); j++) {
				if(visiblearray[j] == false) continue;
				double v = (numeric[j] - iset.numerics.min[j]) / (iset.numerics.max[j] - iset.numerics.min[j]);
				int id = (int)(v * (double)NUMDIV);
				id = (id < 0) ? 0 : id;
				id = (id >= NUMDIV) ? (NUMDIV - 1) : id;
				counter[j][id]++;
			}
		}
		
	
			
		// for each class
		for(int k = 0; k < numclass; k++) {
			
			// Class name definition
			String classname1 = "";
			if(classId < iset.getNumCategory()) 
				classname1 = iset.categories.names[classId];
			else
				classname1 = iset.booleans.names[classId - iset.getNumCategory()];
			String classname2 = "";		
			if(classId < iset.getNumCategory()) 
				classname2 = iset.categories.categories[classId][k];
			else
				classname2 = (k == 0) ? "false" : "true";
			
			// Array for counter
			int counter2[][] = new int[iset.getNumNumeric()][];
			for(int kk = 0; kk < counter2.length; kk++)
				counter2[kk] = new int[NUMDIV];
			
			// for each individual
			for(int i = 0; i < iset.getNumIndividual(); i++) {
				OneIndividual p = iset.getOneIndividual(i);
				int classId = p.getClusterId();
				if(classId != k) continue;
				double numeric[] = p.getNumericValues();
				
				// for each numeric dimension
				for(int j = 0; j < iset.getNumNumeric(); j++) {
					if(visiblearray[j] == false) continue;
					double v = (numeric[j] - iset.numerics.min[j]) / (iset.numerics.max[j] - iset.numerics.min[j]);
					int id = (int)(v * (double)NUMDIV);
					id = (id < 0) ? 0 : id;
					id = (id >= NUMDIV) ? (NUMDIV - 1) : id;
					counter2[j][id]++;
				}
			}
			
			// for each numeric dimension
			for(int j = 0; j < iset.getNumNumeric(); j++) {
				if(visiblearray[j] == false) continue;
				String axisname = iset.getValueName(j);
				
				for(int jj = 0; jj < NUMDIV; jj++) {
					double confratio = (double)counter2[j][jj] / (double)counter[j][jj];
					
					// if the k-th class occupies the large amount
					// in the jj-th rank of the j-th dimension
					if(confratio > confidence && counter2[j][jj] >= supnum) {
						
						double v1 = iset.numerics.min[j] - (iset.numerics.max[j] - iset.numerics.min[j]) * (double)jj / (double)NUMDIV;
						double v2 = iset.numerics.min[j] - (iset.numerics.max[j] - iset.numerics.min[j]) * (double)(jj + 1) / (double)NUMDIV;
						//iset.message += axisname + " [" + v1 + "," + v2 + "] -> " + classname1 + ":" + classname2 + "\n";						
						iset.message += axisname + "\n";
						
						ArrayList<int[]> set = allsets.get(k);
						boolean already = false;
						for(int jjj = 0; jjj < set.size(); jjj++) {
							int[] id = set.get(jjj);
							if(id[0] == j) {
								already = true;  break;
							}
						}
						if(already == false) {
							int[] id = new int[1];
							id[0] = j;
							set.add(id);
						}
					}
				}
			}
			
		}
		
		System.out.println("==========");
		System.out.println(iset.message);
		System.out.println("==========");
		
	}
	
	
	
}
