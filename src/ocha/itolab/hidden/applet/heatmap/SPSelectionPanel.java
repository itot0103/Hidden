package ocha.itolab.hidden.applet.heatmap;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.*;

import ocha.itolab.hidden.core.data.*;
import ocha.itolab.hidden.core.tool.*;

public class SPSelectionPanel extends JPanel {
	
	IndividualSet ps = null;
	
	Font font = new Font("Arial", Font.PLAIN, 16);;
	
	// referred y ChildPhotoPanel.java
	// by ITOT 2011/5/15
	static int colorcount = 0;
	
	/* Selective canvas */
	IndividualCanvas icanvas;
	SPDimensionCanvas sdcanvas;
	SPIndividualCanvas sicanvas;
	JTabbedPane pane = null;
	
	JSlider dimEdgeSlider, indEdgeSlider;
	
	/* Action listener */
	ButtonListener bl = null;
	RadioButtonListener rbl = null;
	SliderListener sl = null;
	CheckBoxListener cbl = null;

	public SPSelectionPanel() {
		// super class init
		super();
		setSize(200, 600);
		
		//pane = new JTabbedPane();
		//pane.add(pp);
		//pane.setTabComponentAt(0, new JLabel("Main"));
		//this.add(pane);
		
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(2, 1));
		dimEdgeSlider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
		p1.add(dimEdgeSlider);
		indEdgeSlider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
		p1.add(indEdgeSlider);
		
		
		this.add(p1);

		if (sl == null)
			sl = new SliderListener();
		addSliderListener(sl);
	}
	
	
	public void setIndividualCanvas(Object c) {
		icanvas = (IndividualCanvas) c;
	}
	
	public void setSPDimensionCanvas(Object c) {
		sdcanvas = (SPDimensionCanvas) c;
	}
	
	public void setSPIndividualCanvas(Object c) {
		sicanvas = (SPIndividualCanvas) c;
	}
	
	public void setIndividualSet(IndividualSet p) {
		ps = p;
	}
	
	

	public void addRadioButtonListener(ActionListener actionListener) {
		
	}
	
	
	public void addButtonListener(ActionListener actionListener) {
		
	}
	

	public void addSliderListener(ChangeListener changeListener) {
		dimEdgeSlider.addChangeListener(changeListener);
		indEdgeSlider.addChangeListener(changeListener);
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
			JSlider sliderChanged = (JSlider) e.getSource();
			
			if (sliderChanged == dimEdgeSlider) {
				double edgeratio = (double)dimEdgeSlider.getValue() / 100.0;
				sdcanvas.setDimensionEdgeRatio(edgeratio);
				sdcanvas.display();
				
			}
			
			if (sliderChanged == indEdgeSlider) {
				double edgeratio = (double)indEdgeSlider.getValue() / 100.0;
				sicanvas.setIndividualEdgeRatio(edgeratio);
				sicanvas.display();
				
				if(ps != null) {
					IndividualChainFinder.setEdgeRatio(edgeratio);
					ArrayList<ArrayList> cliques = IndividualChainFinder.getChains(ps);
					icanvas.setIndividualCliques(cliques);
					icanvas.display();
				}
			}
		}
	}

    class CheckBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			icanvas.display();
			sdcanvas.display();
			sicanvas.display();
		}
	}
}