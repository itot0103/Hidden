package ocha.itolab.hidden.core.tool;

import java.util.*;


public class CliqueFinder {

	static void findCliques(
			ArrayList<int[]> potentialList,
			ArrayList<int[]> candidateList,
			ArrayList<int[]> alreadyList,
			ArrayList<int[]> connection,
			ArrayList<ArrayList> allcliques) {
		   
		   ArrayList<int[]> candidatesArray = new ArrayList<int[]>(candidateList);
		   if (!isEnd(candidateList, alreadyList, connection)) {

			   // for each candidate_node in candidates do
			   for (int[] candidate : candidatesArray) {
				   ArrayList<int[]> newCandidateList = new ArrayList<int[]>();
				   ArrayList<int[]> newAlreadyList = new ArrayList<int[]>();

				   // move candidate node to potential_clique
				   potentialList.add(candidate);
				   candidateList.remove(candidate);

				   // create new_candidates by removing nodes in candidates not
				   // connected to candidate node
				   for (int[] newCandidate : candidateList) {
		                if (isConnected(candidate, newCandidate, connection)) {
		                	newCandidateList.add(newCandidate);
		                } // of if
		           } // of for

				   // create new_already_found by removing nodes in already_found
				   // not connected to candidate node
				   for (int[] newFound : alreadyList) {
					   if (isConnected(candidate, newFound, connection)) {
						   newAlreadyList.add(newFound);
					   } // of if
				   } // of for

				   // if new_candidates and new_already_found are empty
				   if (newCandidateList.isEmpty() && newAlreadyList.isEmpty()) {
					   // potential_clique is maximal_clique
					   ArrayList potentials = new ArrayList(potentialList);
					   if(potentials.size() >= 2)
						   allcliques.add(potentials);
		                } // of if
				   else {
					   // recursive call
					   findCliques(potentialList, newCandidateList, newAlreadyList, connection, allcliques);
				   } // of else
				   
				   // move candidate_node from potential_clique to already_found;
				   alreadyList.add(candidate);
				   potentialList.remove(candidate);
			   } // of for
		   } // of if
	}
	 
	
	static boolean isEnd(
			ArrayList<int[]> candidateList,
			ArrayList<int[]> alreadyList,
			ArrayList<int[]> connection) {
	        
		// if a node in already_found is connected to all nodes in candidates
		boolean end = false;
		int edgecounter;
		for (int[] found : alreadyList) {
			edgecounter = 0;
			for (int[] candidate : candidateList) {
				if (isConnected(found, candidate, connection)) {
					edgecounter++;
	            } // of if
	        } // of for
			if (edgecounter == candidateList.size()) {
				end = true;
	        }
		} // of for
		return end;
	}
	
	
	static boolean isConnected(int[] d1, int[] d2, ArrayList<int[]> connection) {
		int id1 = d1[0];
		int id2 = d2[0];
		if(id1 > id2) {
			int tmp = id1;  id1 = id2;  id2 = tmp;
		}
		
		for(int i = 0; i < connection.size(); i++) {
			int array[] = connection.get(i);
			if(array[0] == id1 && array[1] == id2)
				return true;
		}
		
		return false;
	}
	
}
