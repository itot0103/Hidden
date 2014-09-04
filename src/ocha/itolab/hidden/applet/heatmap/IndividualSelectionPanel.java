package ocha.itolab.hidden.applet.heatmap;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ocha.itolab.hidden.core.data.*;
import ocha.itolab.hidden.core.tool.*;


public class IndividualSelectionPanel extends JPanel {
	
	IndividualSet ps = null;
	
	Font font = new Font("Arial", Font.PLAIN, 16);;
	
	static int colorcount = 0;
	
	/* Selective canvas */
	IndividualCanvas icanvas;
	SPDimensionCanvas sdcanvas;
	SPIndividualCanvas sicanvas;
	SPSelectionPanel sselection;
	
	public JButton  fileOpenButton, viewResetButton;
	
	/* Action listener */
	ButtonListener bl = null;
	RadioButtonListener rbl = null;
	SliderListener sl = null;
	CheckBoxListener cbl = null;

	public IndividualSelectionPanel() {
		// super class init
		super();
		setSize(200, 600);
		
		
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(4,1));
		
		fileOpenButton = new JButton("File Open");
		p1.add(fileOpenButton);
		viewResetButton = new JButton("View Reset");
		p1.add(viewResetButton);

		
		add(p1);
		
		if (bl == null)
			bl = new ButtonListener();
		addButtonListener(bl);
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
	
	public void setSPSelectionPanel(Object c) {
		sselection = (SPSelectionPanel) c;
	}


	
	public void addRadioButtonListener(ActionListener actionListener) {
		
	}
	
	
	public void addButtonListener(ActionListener actionListener) {
		fileOpenButton.addActionListener(actionListener);
		viewResetButton.addActionListener(actionListener);
	}
	
	
	public void addSliderListener(ChangeListener changeListener) {
		
	}
	
	
	public void addCheckBoxListener(ActionListener actionListener) {
		
	}
	
	

	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton buttonPushed = (JButton) e.getSource();
			
			if(buttonPushed == fileOpenButton) {	
				FileOpener fileOpener = new FileOpener();
				ps = fileOpener.readFile();
				sselection.setIndividualSet(ps);
				icanvas.setIndividualSet(ps);
				sdcanvas.setIndividualSet(ps);
				sicanvas.setIndividualSet(ps);
				icanvas.display();
				sdcanvas.display();
				sicanvas.display();
			}	
			
			if(buttonPushed == viewResetButton) {
				icanvas.viewReset();
				icanvas.display();
				sdcanvas.viewReset();
				sdcanvas.display();
				sicanvas.viewReset();
				sicanvas.display();
			}
			
		}
	}
	
	
	class RadioButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			

		}
	}
	
	
	class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			
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