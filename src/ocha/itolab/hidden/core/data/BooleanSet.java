package ocha.itolab.hidden.core.data;

import java.util.ArrayList;

public class BooleanSet {
	public ArrayList<OneBoolean> dimensions;
	int numBoolean;
	
	/**
	 * Constructor
	 */
	public BooleanSet(IndividualSet iset) {
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
