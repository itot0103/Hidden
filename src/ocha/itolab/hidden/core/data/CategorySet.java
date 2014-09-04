package ocha.itolab.hidden.core.data;

import java.util.*;

public class CategorySet {
	public String names[];
	public String categories[][];
	TreeSet treeset[];
	
	/**
	 * Constructor
	 */
	public CategorySet(IndividualSet iset) {
		
		// Copy names
		names = new String[iset.numCategory];
		for(int i = 0; i < names.length; i++) {
			names[i] = iset.getValueName(i + iset.numNumeric);
		}
		
		// For each category variable, list the possible values
		categories = new String[iset.numCategory][];
		treeset = new TreeSet[iset.numCategory];
		for(int i = 0; i < categories.length; i++) 
			treeset[i] = new TreeSet();
			
		// for each individual
		for(int i = 0; i < iset.plots.size(); i++) {
			OneIndividual p = (OneIndividual)iset.plots.get(i);
			
			// for each category
			for(int j = 0; j < iset.numCategory; j++)  {
				treeset[j].add(p.category[j]);
			}
		}
			
		// for each category
		for(int i = 0; i < iset.numCategory; i++) {
			categories[i] = new String[treeset[i].size()];
			for(int j = 0; j < categories[i].length; j++) {
				categories[i][j] = (String)treeset[i].first();
				treeset[i].remove(categories[i][j]);
			}
		}
		
	}
	
	
}
