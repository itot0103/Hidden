package ocha.itolab.hidden.core.data;

public class OneNumeric {
	String name;
	double dissim[];
	double x, y;
	int id;
	int status;
	
	public static int STATUS_RESET = 0;
	public static int STATUS_VISIBLE = 1;
	public static int STATUS_INVISIBLE = -1;
	
	public OneNumeric(int num) {
		dissim = new double[num];
		status = STATUS_RESET;
	}

	public void setPosition(double x, double y) {
		this.x = x;   this.y = y;
	}

	public void setStatus(int s) {
		status = s;
	}
	
	public void setDissimilarity(double v, int id) {
		dissim[id] = v;
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
	
	public int getStatus() {
		return status;
	}

	public double getDissimilarity(int id) {
		return dissim[id];
	}
}
