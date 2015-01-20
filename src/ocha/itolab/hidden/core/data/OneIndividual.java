package ocha.itolab.hidden.core.data;

public class OneIndividual {
	double numeric[];
	String category[];
	boolean bool[];
	int id;
	int clusterId;
	double x, y;
	double dissim[];
	
	
	/**
	 * Constructor
	 * @param numTotal
	 */
	public OneIndividual(int numNumeric, int numCategory, int numBoolean, int id) {
		numeric = new double[numNumeric];
		category = new String[numCategory];
		bool = new boolean[numBoolean];
		dissim = null;
		this.id = id;
		this.clusterId = -1;		
	}
	
	
	public void setNumericValue(int id, double v) {
		numeric[id] = v;
	}
	
	public void setCategoryValue(int id, String v) {
		category[id] = v;
	}
	
	public void setBooleanValue(int id, boolean v) {
		bool[id] = v;
	}
	
	public double[] getNumericValues() {
		return numeric;
	}
	
	public String[] getCategoricalValues() {
		return category;
	}
	
	public boolean[] getBooleanValues() {
		return bool;
	}
	
	public int getId() {
		return id;
	}
	
	public int getClusterId() {
		return clusterId;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setClusterId(int id) {
		clusterId = id;
	}
	
	public void setPosition(double x, double y) {
		this.x = x;   this.y = y;
	}
	
	
	public void setDissimilarity(double v, int id, int num) {
		if(dissim == null || dissim.length != num)
			dissim = new double[num];
		dissim[id] = v;
	}
	
	public double getDissimilarity(int id) {
		return dissim[id];
	}
}
