package ocha.itolab.hidden.applet.numdim;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import ocha.itolab.hidden.core.data.*;

public class IndividualTextPanel extends JPanel {
	JTextArea textArea;
	IndividualSet iset;
	
	public IndividualTextPanel(IndividualSet is) {
		super();
		iset = is;
		
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(1,1));
		textArea = new JTextArea();
		textArea.setPreferredSize(new Dimension(500, 500));
		JScrollPane scroll1 = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
	        	JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll1.setViewportView(textArea);
		scroll1.setPreferredSize(new Dimension(250, 500));
		p1.add(scroll1);
		
		this.add(p1);
	}

	
	public void updateDimensionInfo(ArrayList<ArrayList> dimlist) {
		if(dimlist == null) return;
		String text = "";
		
		// for each list of dimensions (corresponding to one PCP)
		for(ArrayList list : dimlist) {
			for(int i = 0; i < list.size(); i++) {
				int[] numid = (int[])list.get(i);
				text += (iset.getValueName(numid[0]) + '\n');
			}
			text += '\n';
		}
		
		textArea.setText(text);
		textArea.copy();
		System.out.println(text);
	}

	
	

}
