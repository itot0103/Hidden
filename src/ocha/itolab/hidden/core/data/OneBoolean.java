package ocha.itolab.hidden.core.data;

public class OneBoolean {
	String name;
	double dissim[];
	double x, y;
	
	public OneBoolean(int num) {
		dissim = new double[num];
	}

	public void setPosition(double x, double y) {
		this.x = x;   this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDissimilarity(double v, int id) {
		dissim[id] = v;
	}
	
	public double getDissimilarity(int id) {
		return dissim[id];
	}
}
