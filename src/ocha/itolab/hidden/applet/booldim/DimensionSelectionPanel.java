package ocha.itolab.hidden.applet.booldim;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ocha.itolab.hidden.core.data.*;

public class DimensionSelectionPanel extends JPanel {
	
	IndividualSet ps = null;
	
	Font font = new Font("Arial", Font.PLAIN, 16);;
	
	// referred y ChildPhotoPanel.java
	// by ITOT 2011/5/15
	static int colorcount = 0;
	
	/* Selective canvas */
	IndividualCanvas icanvas;
	DimensionCanvas ocanvas;
	JTabbedPane pane = null;
	
	/* Action listener */
	ButtonListener bl = null;
	RadioButtonListener rbl = null;
	SliderListener sl = null;
	CheckBoxListener cbl = null;

	public DimensionSelectionPanel() {
		// super class init
		super();
		setSize(200, 600);
		
		pane = new JTabbedPane();
		//pane.add(pp);
		//pane.setTabComponentAt(0, new JLabel("Main"));
		this.add(pane);
		

	}
	
	
	public void setIndividualCanvas(Object c) {
		icanvas = (IndividualCanvas) c;
	}
	
	public void setDimensionCanvas(Object c) {
		ocanvas = (DimensionCanvas) c;
	}
	
	
	public void setPlotSet(IndividualSet p) {
		
	}
	
	

	public void addRadioButtonListener(ActionListener actionListener) {
		
	}
	
	
	public void addButtonListener(ActionListener actionListener) {
		
	}
	

	public void addSliderListener(ChangeListener changeListener) {
		
	}
	

	public void addCheckBoxListener(ActionListener actionListener) {
		
	}
	
	
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton buttonPushed = (JButton) e.getSource();
			
		}
	}
	
	
	class RadioButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton buttonPushed = (JRadioButton) e.getSource();
			

		}
	}
	
	
	class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			
		}
	}

	
    class CheckBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			icanvas.display();
			ocanvas.display();
		}
	}
}