package ocha.itolab.hidden.applet.numdim;

import ocha.itolab.hidden.core.data.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;

public class IndividualParettePanel extends JPanel {
	IndividualSet iset = null;
	
	public IndividualParettePanel(IndividualSet is) {
		// super class init
		super();
		setSize(150, 150);
		iset = is;
	}
	
	
	
	/**
	 * 再描画
	 */
	public void draw(int clusterId) {
		Graphics g = getGraphics();
		if (g == null)
			return;
		paintComponent(g);
	}

	
	/**
	 * 描画を実行する
	 * @param g Graphics
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // clear the background
		Graphics2D g2 = (Graphics2D) g;
		
		// クラスなし
		if(iset.getClassId() >= iset.getNumBoolean() + iset.getNumCategory())
			return;
		
		// Booleanクラス
		if(iset.getClassId() >= iset.getNumCategory()) {
			for(int i = 0; i < 2; i++) {
				Color color = calcColor(i, 2);
				g2.setPaint(color);
				g.fillRect(10, 10 + (i * 20), 20, 20);
				g2.setPaint(Color.BLACK);
				if(i == 0) g.drawString("False", 40, (25 + (i * 20)));
				if(i == 1) g.drawString("True", 40, (25 + (i * 20)));
			}
			return;
		}
		
		// Categoryクラス
		if(iset.getClassId() >= 0) {
			String categories[] = iset.categories.categories[iset.getClassId()];
			int num = (categories.length >= 10) ? 10 : categories.length;
			for(int i = 0; i < num; i++) {
				Color color = calcColor(i, categories.length);
				g2.setPaint(color);
				g.fillRect(10, 10 + (i * 20), 20, 20);
				g2.setPaint(Color.BLACK);
				g.drawString(categories[i], 40, (25 + (i * 20)));
			}
		}
	}			
	
	
	Color calcColor(int id, int num) {
		float hue = (float)id / (float)num;
		Color color = Color.getHSBColor(hue, 1.0f, 1.0f);
		return color;
	}
}
