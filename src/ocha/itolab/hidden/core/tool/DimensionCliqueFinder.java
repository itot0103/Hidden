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
		
		postprocess(allcliques);
		
		return allcliques;
	}

	
	
	/**
	 * Connect small cliques to other cliques if possible
	 */
	static void postprocess(ArrayList<ArrayList> allcliques) {

		// for each clique
		for(int i = 0; i < allcliques.size(); i++) {
			ArrayList<int[]> clique1 = allcliques.get(i);
			if(clique1.size() >= 3) continue;
			
			// for each other cliques
			for(int j = 0; j < allcliques.size(); j++) {
				if(i == j) continue;
				ArrayList<int[]> clique2 = allcliques.get(j);
				
				int[] id1a = clique1.get(0);
				int[] id1b = clique1.get(clique1.size() - 1);
				int[] id2a = clique2.get(0);
				int[] id2b = clique2.get(clique2.size() - 1);
				
				if(id1a[0] == id2a[0]) {
					clique2.add(0, id1b); allcliques.remove(clique1);
					i = -1;  break;
				}
				if(id1a[0] == id2b[0]) {
					clique2.add(id1b); allcliques.remove(clique1);
					i = -1;  break;
				}
				if(id1b[0] == id2a[0]) {
					clique2.add(0, id1a); allcliques.remove(clique1);
					i = -1;  break;
				}
				if(id1b[0] == id2b[0]) {
					clique2.add(id1a); allcliques.remove(clique1);
					i = -1;  break;
				}
			}
			
			
		}		
		
	}

}	   	
