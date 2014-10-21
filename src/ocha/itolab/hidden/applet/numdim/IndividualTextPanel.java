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

	
	boolean setDimLock = false;
	
	public void updateDimensionInfo(ArrayList<ArrayList> dimlist) {
		
		if(iset.getDimSelectMode() == iset.DIMSELECT_CORRELATION) {
			textArea.setText("");
			textArea.copy();
			return;
		}
		
		if(setDimLock == true) return;
		setDimLock = true;

		if(dimlist == null) return;
		String text = "";
		boolean isCategory = false;
		
		int classId = iset.getClassId();
		if(classId < 0 || classId >= (iset.getNumCategory() + iset.getNumBoolean()))
			return;
		if(classId < iset.getNumCategory()) 
			isCategory = true;
		
		
		// for each list of dimensions (corresponding to one PCP)
		for(int i = 0; i < dimlist.size(); i++) {
			ArrayList list = dimlist.get(i);
			String  cvalue = "";
			if(isCategory == true)
				cvalue = iset.categories.categories[classId][i];
			else 
				cvalue = (i == 0) ? "true" : "false";
			text += "*** " + cvalue + "\n";
			for(int j = 0; j < list.size(); j++) {
				int[] numid = (int[])list.get(j);
				text += (iset.getValueName(numid[0]) + '\n');
			}
			text += '\n';
		}
		
		textArea.setText(text);
		textArea.copy();
		//System.out.println(text);
	
		setDimLock = false;
	}

	
	

}
