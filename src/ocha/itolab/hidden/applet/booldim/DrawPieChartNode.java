package ocha.itolab.hidden.applet.booldim;

import java.util.*;
import java.awt.*;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;


public class DrawPieChartNode {
	double x20[] = new double[20]; // 2,4,5の倍数をサポート
	double x21[] = new double[21]; // 3,7の倍数をサポート
	double x24[] = new double[24]; // 6,8の倍数をサポート
	double y20[] = new double[20]; // 2,4,5の倍数をサポート
	double y21[] = new double[21]; // 3,7の倍数をサポート
	double y24[] = new double[24]; // 3,7の倍数をサポート
	Color color[];
	float cvalue[] = new float[4];
	double radius = 0.02;
	GL gl;
	GL2 gl2;
	
	Vector bclist = new Vector();
	
	/**
	 * コンストラクタ
	 */
	DrawPieChartNode() {
		
		// 円周を多角形で近似する
		for(int i = 0; i < 20; i++) {
			double rad = (double)i / 10.0 * Math.PI;
			x20[i] = Math.cos(rad);
			y20[i] = Math.sin(rad);
		}
		for(int i = 0; i < 21; i++) {
			double rad = (double)i / 10.5 * Math.PI;
			x21[i] = Math.cos(rad);
			y21[i] = Math.sin(rad);
		}
		for(int i = 0; i < 24; i++) {
			double rad = (double)i / 12.0 * Math.PI;
			x24[i] = Math.cos(rad);
			y24[i] = Math.sin(rad);
		}
		
	}
	
	

	
	/**
	 * 円グラフ風にNodeを描画する
	 */
	public void paintNode(double x, double y, ArrayList colorlist, GL gl, GL2 gl2) {
		this.gl = gl;
		this.gl2 = gl2;
		
		if(colorlist.size() == 0) {
			paintNode0(x, y);
			return;
		}
		
		if(colorlist.size() == 1) {
			double color[] = new double[3];
			int bid[] = (int[])colorlist.get(0);
			color[0] = BooldimViewer.plotcolors[bid[0]][0];
			color[1] = BooldimViewer.plotcolors[bid[0]][1];
			color[2] = BooldimViewer.plotcolors[bid[0]][2];
			paintNode1(x, y, color);
			return;
		}
		
		if(colorlist.size() <= 8) {
			paintNode2(x, y, colorlist);
		}
		
	}
	
	
	/**
	 * 色指定が0色のときの描画処理
	 */
	public void paintNode0(double x, double y) {
		double color[] = {0.5, 0.5, 0.5};
		gl2.glColor3d(color[0], color[1], color[2]);
		gl2.glBegin(GL2.GL_POLYGON);
		gl2.glNormal3d(0.0, 0.0, 1.0);
		for(int i = 0; i < 20; i++) {
			double x1 = x + x20[i] * radius * 0.5;
			double y1 = y + y20[i] * radius * 0.5;
			gl2.glVertex3d(x1, y1, -0.1);
		}
		gl2.glEnd();
	}
		
	
	
	/**
	 * 色指定が1色のときの描画処理
	 */
	public void paintNode1(double x, double y, double color[]) {
		
		gl2.glColor3d(color[0], color[1], color[2]);
		gl2.glBegin(GL2.GL_POLYGON);
		gl2.glNormal3d(0.0, 0.0, 1.0);
		for(int i = 0; i < 20; i++) {
			double x1 = x + x20[i] * radius;
			double y1 = y + y20[i] * radius;
			gl2.glVertex3d(x1, y1, 0.0);
		}
		gl2.glEnd();
	}
		
		
	/**
	 * 色指定が2色以上のときの描画処理
	 */
	public void paintNode2(double x, double y, ArrayList colorlist) {
		int nump = 20;
		
		if(colorlist.size() == 3 || colorlist.size() == 7)
			nump = 21;
		if(colorlist.size() == 6 || colorlist.size() == 8)
			nump = 24;
		
		// 扇型の各部分について
		int countp = 0;
		for(int i = 0; i < colorlist.size(); i++) {
			double color[] = new double[3];
			int bid[] = (int[])colorlist.get(i);
			color[0] = BooldimViewer.plotcolors[bid[0]][0];
			color[1] = BooldimViewer.plotcolors[bid[0]][1];
			color[2] = BooldimViewer.plotcolors[bid[0]][2];
			gl2.glColor3d(color[0], color[1], color[2]);
			
			gl2.glNormal3d(0.0, 0.0, 1.0);
			
			double x1 = 0.0, x2 = 0.0, y1 = 0.0, y2 = 0.0;
			int numpp = nump / colorlist.size();
			
			// 各頂点の座標値と法線を決定する		
			for(int j = 0; j < numpp; j++, countp++)  {
				int countp1 = (countp == nump - 1) ? 0 : (countp + 1);
			
				if(nump == 20) {
					x1 = x + x20[countp] * radius;
					y1 = y + y20[countp] * radius;
					x2 = x + x20[countp1] * radius;
					y2 = y + y20[countp1] * radius;

				}
				else if(nump == 21) {
					x1 = x + x21[countp] * radius;
					y1 = y + y21[countp] * radius;
					x2 = x + x21[countp1] * radius;
					y2 = y + y21[countp1] * radius;
				}
				else if(nump == 24) {
					x1 = x + x24[countp] * radius;
					y1 = y + y24[countp] * radius;
					x2 = x + x24[countp1] * radius;
					y2 = y + y24[countp1] * radius;
				}
			
				gl2.glBegin(GL2.GL_POLYGON);
				gl2.glVertex3d(x1, y1, 0.0);
				gl2.glVertex3d(x2, y2, 0.0);
				gl2.glVertex3d(x, y, 0.0);
				gl2.glEnd();
			}
		}
	}
}
