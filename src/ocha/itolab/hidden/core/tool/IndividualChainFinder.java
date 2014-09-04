package ocha.itolab.hidden.core.tool;

import java.util.*;
import ocha.itolab.hidden.core.data.*;

public class IndividualChainFinder {

	static double edgeratio;
	
	public static void setEdgeRatio(double r) {
		edgeratio = r;
	}
	
	public static ArrayList getChains(IndividualSet is) {
		ArrayList<ArrayList> allchains = new ArrayList<ArrayList>();
	
		// for each pair of individuals
		for(int i = 0; i < is.getNumIndividual(); i++) {
			OneIndividual p = is.getOneIndividual(i);
			for(int j = (i + 1); j < is.getNumIndividual(); j++) {
				if(p.getDissimilarity(j) > edgeratio) continue;
				addIndividualPair(allchains, i, j);
			}
		}
		
		return allchains;
	}
	
	static void addIndividualPair(ArrayList<ArrayList> allchains, int id1, int id2) {
		
		// for each chain
		ArrayList list1 = null, list2 = null;
		for(int i = 0; i < allchains.size(); i++) {
			ArrayList<int[]> list = allchains.get(i);
			for(int j = 0; j < list.size(); j++) {
				int[] id = list.get(j);
				if(id[0] == id1) {
					list1 = list;
				}
				if(id[0] == id2) {
					list2 = list;
				}
			}
		}
		
		// Strange situation ...
		if(list1 != null && list1 == list2) {
			return;
		}
		
		// just add one of the IDs to a chain and return
		if(list1 != null && list2 == null) {
			int id[] = new int[1];
			id[0] = id2;
			list1.add(id);
			return;
		}
		if(list2 != null && list1 == null) {
			int id[] = new int[1];
			id[0] = id1;
			list2.add(id);
			return;
		}
		
		// if two chains should be merged
		if(list1 != null && list2 != null) {
			for(int i = 0; i < list2.size(); i++) {
				list1.add(list2.get(i));
			}
			allchains.remove(list2);
			return;
		}
		
		// add a new chain
		if(list1 == null && list2 == null) {
			ArrayList<int[]> chain = new ArrayList<int[]>();
			int nid1[] = new int[1];
			int nid2[] = new int[1];
			nid1[0] = id1;   nid2[0] = id2;
			chain.add(nid1);    chain.add(nid2);
			allchains.add(chain);
		}
	}
	
}
