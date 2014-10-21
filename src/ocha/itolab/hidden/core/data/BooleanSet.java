package ocha.itolab.hidden.core.data;

import java.util.ArrayList;

public class BooleanSet {
	public String names[];
	public ArrayList<OneBoolean> dimensions;
	int numBoolean;
	
	/**
	 * Constructor
	 */
	public BooleanSet(IndividualSet iset) {

		// Copy names
		names = new String[iset.numBoolean];
		for(int i = 0; i < names.length; i++) {
			names[i] = iset.getValueName(i + iset.numNumeric + iset.numCategory);
		}
		
		numBoolean = iset.numBoolean;
		dimensions = new ArrayList();
		int counter = -1;
		for(int i = 0; i < numBoolean; i++) {
			OneBoolean d = new OneBoolean(numBoolean);
			dimensions.add(d);
			while(true) {
				counter++;
				if(iset.type[counter] == iset.TYPE_BOOLEAN) {
					d.setName(iset.getValueName(counter));
					break;
				}
				else
					if(counter >= iset.numTotal) break;
			}
		}
	}
	
	
}
