package ocha.itolab.hidden.core.data;

public class OneNumeric {
	String name;
	double dissim[];
	double x, y;
	int id;
	
	public OneNumeric(int num) {
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
	
	public int getId() {
		return id;
	}
	
	public void setDissimilarity(double v, int id) {
		dissim[id] = v;
	}
	
	public double getDissimilarity(int id) {
		return dissim[id];
	}
}
