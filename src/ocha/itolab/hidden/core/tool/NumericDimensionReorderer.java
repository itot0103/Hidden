package ocha.itolab.hidden.core.tool;

import ocha.itolab.hidden.core.data.*;
import java.util.*;

public class NumericDimensionReorderer {
	private static int numdim = 0;
	
	public static ArrayList<OneNumeric> reorder(ArrayList<OneNumeric> dimlist) {
		
		ArrayList<OneNumeric> newlist = new ArrayList<OneNumeric>();
		TSPEnvironment tspEnvironment = new TSPEnvironment();

		numdim = dimlist.size();
		tspEnvironment.distances = new int[dimlist.size()][];
		int[] currSolution = new int[dimlist.size() + 1];
		for(int i = 0; i < dimlist.size(); i++) {
			tspEnvironment.distances[i] = new int[dimlist.size()];	
			currSolution[i] = i;
		}
		currSolution[dimlist.size()] = 0;
		
		for(int i = 0; i < dimlist.size(); i++) {
			OneNumeric on1 = dimlist.get(i);
			for(int j = (i + 1); j < dimlist.size(); j++) {
				tspEnvironment.distances[i][j] = 
				tspEnvironment.distances[j][i] = (int)(1.0e+5 * on1.getDissimilarity(j)); 
			}
		}

        int numberOfIterations = 100;
        int tabuLength = numdim;
        TabuList tabuList = new TabuList(tabuLength);

        int[] bestSol = new int[currSolution.length]; //this is the best Solution So Far
        System.arraycopy(currSolution, 0, bestSol, 0, bestSol.length);
        int bestCost = tspEnvironment.getObjectiveFunctionValue(bestSol);

        for (int i = 0; i < numberOfIterations; i++) { // perform iterations here

            currSolution = TabuSearch.getBestNeighbour(tabuList, tspEnvironment, currSolution);
            //printSolution(currSolution);
            int currCost = tspEnvironment.getObjectiveFunctionValue(currSolution);

            //System.out.println("   " + i + ": Current best cost = " + tspEnvironment.getObjectiveFunctionValue(currSolution));

            if (currCost < bestCost) {
                System.arraycopy(currSolution, 0, bestSol, 0, bestSol.length);
                bestCost = currCost;
            }
        }

        
        for(int i = 0; i < (bestSol.length - 1); i++) {
        	//System.out.println("    bestSol[" + i + "]=" + bestSol[i]);
        	OneNumeric on = dimlist.get(bestSol[i]);
        	newlist.add(on);
        }
        
        
        return newlist;
	}
	
	
	
	
	
	 static class TabuList {
		    int [][] tabuList ;

		    TabuList(int numCities){
		        tabuList = new int[numCities][numCities]; //city 0 is not used here, but left for simplicity
		    }
		    
		    void tabuMove(int city1, int city2){ //tabus the swap operation
		        tabuList[city1][city2]+= 1;
		        tabuList[city2][city1]+= 1;    
		    }
		    
		    void decrementTabu(){
		        for(int i = 0; i<tabuList.length; i++) {
		        	for(int j = 0; j<tabuList.length; j++){
		        		tabuList[i][j]-=tabuList[i][j]<=0?0:1;
		        	} 
		        }
		    }
	 }
	 
	 
	 
	 static class TSPEnvironment { //Tabu Search Environment		    
		    int [][] distances;
		    
		    int getObjectiveFunctionValue(int solution[]){ //returns the path cost
		        //the first and the last cities'
		        //  positions do not change.
		        // example solution : {0, 1, 3, 4, 2, 0}
		      
		        int cost = 0;
		   
		        for(int i = 0 ; i < solution.length-1; i++){
		            cost+= distances[solution[i]][solution[i+1]];
		        }
		   
		        return cost;
		    }
		}


	 
	 static class TabuSearch {
		 
		 static int[] getBestNeighbour(
				 	TabuList tabuList,
		            TSPEnvironment tspEnviromnet,
		            int[] initSolution) {


		        int[] bestSol = new int[initSolution.length]; //this is the best Solution So Far
		        System.arraycopy(initSolution, 0, bestSol, 0, bestSol.length);
		        int bestCost = tspEnviromnet.getObjectiveFunctionValue(initSolution);
		        int city1 = 0;
		        int city2 = 0;
		        boolean firstNeighbor = true;

		        for (int i = 1; i < bestSol.length - 1; i++) {
		            for (int j = 2; j < bestSol.length - 1; j++) {
		                if (i == j) continue;

		                int[] newBestSol = new int[bestSol.length]; //this is the best Solution So Far
		                System.arraycopy(bestSol, 0, newBestSol, 0, newBestSol.length);

		                newBestSol = swapOperator(i, j, initSolution); //Try swapping cities i and j
		                // , maybe we get a bettersolution
		                int newBestCost = tspEnviromnet.getObjectiveFunctionValue(newBestSol);



		                if ((newBestCost > bestCost || firstNeighbor) && tabuList.tabuList[i][j] == 0) { //if better move found, store it
		                    firstNeighbor = false;
		                    city1 = i;
		                    city2 = j;
		                    System.arraycopy(newBestSol, 0, bestSol, 0, newBestSol.length);
		                    bestCost = newBestCost;
		                }
		            }
		        }

		        if (city1 != 0) {
		            tabuList.decrementTabu();
		            tabuList.tabuMove(city1, city2);
		        }
		        
		        return bestSol;


		    }

		    //swaps two cities
		    static int[] swapOperator(int city1, int city2, int[] solution) {
		        int temp = solution[city1];
		        solution[city1] = solution[city2];
		        solution[city2] = temp;
		        return solution;
		    }
		}
}

