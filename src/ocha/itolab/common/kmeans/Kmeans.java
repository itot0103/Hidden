package ocha.itolab.common.kmeans;

public class Kmeans {
	static int numdim;
	static int numcenter;
	static int numelement;
	
	static Element elements[];
	static Center centers[];
	
	/**
	 * 個々の要素を格納するクラス
	 */
	class Element {
		public String name;
		public double values[];
		public double nvalues[];
		public int label;
	}

	/**
	 * クラスタの中心点と所属要素数を格納するクラス
	 */
	class Center {
		public double values[];
		public int numelements;
	}
	
	
	public void setNumDim(int d) {
		numdim = d;
	}
	
	public void setNumCluster(int c) {
		numcenter = c;
	}
	
	public void setNumElement(int e) {
		numelement = e;
	}
	
	public void setElementName(int i, String name) {
		elements[i].name = name;
	}
	
	public void setElementValue(int i, int j, double v, double nv) {
		elements[i].values[j] = v;
		elements[i].nvalues[j] = nv;
	}
	
	public int getNumDim() {
		return numdim;
	}
	
	public int getNumCluster() {
		return numcenter;
	}
	
	public int getNumElement() {
		return numelement;
	}
	
	public int getElementLabel(int elementId) {
		return elements[elementId].label;
	}
	
	public String getElementName(int elementId) {
		return elements[elementId].name;
	}
	
	public double getElementValue(int i, int j) {
		return elements[i].values[j];
	}
	
	/**
	 * Kmeansに必要なメモリ領域を確保する 
	 */
	public void initKmenas() {

		/*
		 * 中心点をラベル数だけ確保する
		 */
		centers = new Center[numcenter];
		for(int i = 0; i < numcenter; i++) {
			Center c = centers[i] = new Center();
			c.values = new double[numdim];
			c.numelements = 0;
		}

		/*
		 * エレメントを確保する
		 */
		elements = new Element[numelement];
		for(int i = 0; i < numelement; i++) {
			Element e = elements[i] = new Element();
			e.values = new double[numdim];
			e.nvalues = new double[numdim];
			e.label = -1;
		}
	}



	/**
	 * 各クラスタの中心点を初期化する
	 */
	public void initializeCenterPoints() {

		// 仮の中心点を決定する
		for(int i = 0; i < numcenter; i++) {
			Center c = centers[i];
			Element e = elements[i];
			for(int j = 0; j < numdim; j++) {
				c.values[j] = e.nvalues[j];
			}
		}
	}

	/*
	 * K-means法により各エレメントのラベルを決定する
	 */
	public void executeKmeans() {
		
		/*
		 * 各エレメントごとに、どの中心点が最短かを求める
		 */
		for(int i = 0; i < numelement; i++) {
			Element e = elements[i];
			
			/*
			 * 各中心点ごとに距離を求める
			 */	
			double mindist = 1.0e+30;
			for(int j = 0; j < numcenter; j++) {
				Center c = centers[j];
				double dist = 0.0;
				for(int k = 0; k < numdim; k++) {
					dist += (c.values[k] - e.nvalues[k]) * (c.values[k] - e.nvalues[k]);
				}
				if(mindist > dist) {
					mindist = dist;  e.label = j;
					
				}
			} 
		}
		
		/*
		 * 各エレメントごとに、その座標値を合算することで、中心点の位置を再算出する
		 */
		for(int j = 0; j < numcenter; j++) {
			Center c = centers[j];
			c.numelements = 0;
			for(int k = 0; k < numdim; k++)
				c.values[k] = 0.0;	
		}
		for(int i = 0; i < numelement; i++) {
			Element e = elements[i];
			Center c = centers[e.label];
			(c.numelements)++;
			for(int k = 0; k < numdim; k++)
				c.values[k] += e.nvalues[k];
		}
		for(int j = 0; j < numcenter; j++) {
			Center c = centers[j];
			for(int k = 0; k < numdim; k++)
				c.values[k] /= (double)c.numelements;
			//System.out.println(" center" + j + ": num=" + c.numelements);
		}
	}
}
