package ocha.itolab.hidden.core.data;

import java.util.ArrayList;

public class NumericSet {
	public ArrayList<OneNumeric> dimensions;
	public double min[], max[];
	
	
	/**
	 * Constructor
	 */
	public NumericSet(IndividualSet iset) {
		dimensions = new ArrayList();
		int counter = -1;
		for(int i = 0; i < iset.numNumeric; i++) {
			OneNumeric d = new OneNumeric(iset.numNumeric);
			d.id = i;
			dimensions.add(d);
			while(true) {
				counter++;
				if(counter >= iset.numTotal) break;
				if(iset.type[counter] == iset.TYPE_NUMERIC) {
					d.setName(iset.getValueName(counter));
					break;
				}
			}
		}
		
		min = new double[iset.numNumeric];
		max = new double[iset.numNumeric];
	}
	
	
	public int getNumNumeric() {
		return dimensions.size();
	}
	
}
