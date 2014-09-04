package ocha.itolab.hidden.core.tool;

import java.util.*;
import ocha.itolab.hidden.core.data.*;

public class DimensionCliqueFinder {
	static double edgeratio;
	
	public static void setEdgeRatio(double r) {
		edgeratio = r;
	}
	
	public static ArrayList getCliques(IndividualSet is) {
		ArrayList<ArrayList> allcliques = new ArrayList<ArrayList>();
		
        ArrayList<int[]> potential = new ArrayList<int[]>();
        ArrayList<int[]> candidates = new ArrayList<int[]>();
        ArrayList<int[]> already = new ArrayList<int[]>();
		ArrayList<int[]> connection = new ArrayList<int[]>();
        
        // Set ID of the numeric dimensions
        NumericSet ns = is.numerics;
        for(int i = 0; i < ns.getNumNumeric(); i++) {
        	int id[] = new int[1];
        	id[0] = i;
        	candidates.add(id);
        }
        
        // Set connectivity between the numeric dimensions
        for(int i = 0; i < ns.getNumNumeric(); i++) {
        	OneNumeric on1 = ns.dimensions.get(i);
        	for(int j = (i + 1);  j < ns.getNumNumeric(); j++) {
        		if(on1.getDissimilarity(j) < edgeratio) {
        			int id[] = new int[2];
        			id[0] = i;    id[1] = j;
        			connection.add(id);
        		}
        	}
        }
        
		CliqueFinder.findCliques(potential, candidates, already, connection, allcliques);
		
		return allcliques;
	}

	
	

}	   	
