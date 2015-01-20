package ocha.itolab.hidden.core.tool;

import ocha.itolab.hidden.core.data.*;

public class DimensionCullingProcessor {
	static double edgeratio;
	
	public static void setRatio(IndividualSet is, double r) {
		edgeratio = r;
		
		// if ratio is enough small
		if(r < 1.0e-3) {
			reset(is); return;
		}
		
		// for each numeric dimensions
		for(int i = 0; i < is.getNumNumeric(); i++) {
			OneNumeric on = is.numerics.dimensions.get(i);
			boolean isCloseExist = false;
			
			// for each pair of numeric dimensions
			for(int j = 0; j < is.getNumNumeric(); j++) {
				if(i == j) continue;
				//System.out.println("  r=" + edgeratio + " i=" + i + " j=" + j + " dis=" + on.getDissimilarity(j));
				if(on.getDissimilarity(j) > edgeratio) continue;
				
				// if the pair is too close
				isCloseExist = true;
				OneNumeric on2 = is.numerics.dimensions.get(j);
				if(on.getStatus() == on.STATUS_INVISIBLE || on2.getStatus() == on.STATUS_INVISIBLE)
					continue;
				if(on.getStatus() == on.STATUS_VISIBLE) {
					on2.setStatus(on.STATUS_INVISIBLE); continue;
				}
				if(on2.getStatus() == on.STATUS_VISIBLE) {
					on.setStatus(on.STATUS_INVISIBLE); continue;
				}
				
				on.setStatus(on.STATUS_VISIBLE);
				on2.setStatus(on.STATUS_INVISIBLE);
			}
			
			// reset the invisible status
			if(on.getStatus() == on.STATUS_INVISIBLE && isCloseExist == false)
				on.setStatus(on.STATUS_RESET);
		}
	}
	

	/**
	 * reset status of all numeric dimensions
	 * @param is
	 */
	static void reset(IndividualSet is) {
		for(int i = 0; i < is.getNumNumeric(); i++) {
			OneNumeric on = is.numerics.dimensions.get(i);
			on.setStatus(on.STATUS_RESET);
		}
	}
	
	
}
