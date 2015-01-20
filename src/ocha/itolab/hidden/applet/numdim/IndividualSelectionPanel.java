package ocha.itolab.hidden.applet.numdim;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ocha.itolab.hidden.applet.numdim.DimensionSelectionPanel.SliderListener;
import ocha.itolab.hidden.core.data.*;
import ocha.itolab.hidden.core.tool.*;


public class IndividualSelectionPanel extends JPanel {
	
	IndividualSet iset = null;
	
	Font font = new Font("Arial", Font.PLAIN, 16);;
	
	static int colorcount = 0;
	
	/* Selective canvas */
	IndividualCanvas icanvas;
	DimensionCanvas ocanvas;
	DimensionSelectionPanel dselection;
	IndividualColoringPanel icoloring;
	IndividualTextPanel itext;
	
	public JButton  fileOpenButton, viewResetButton, clusteringButton;
	public JTextField numClusterField;
	public JRadioButton correlationButton, separativenessButton;
	public JSlider transparencySlider, cullingSlider;
	JTabbedPane pane = null;
	
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
		p1.setLayout(new GridLayout(10,1));
		
		fileOpenButton = new JButton("File Open");
		p1.add(fileOpenButton);
		viewResetButton = new JButton("View Reset");
		p1.add(viewResetButton);
		p1.add(new JLabel("Transparency"));
		transparencySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		p1.add(transparencySlider);
		p1.add(new JLabel("Culling"));
		cullingSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		p1.add(cullingSlider);
		
		JPanel p11 = new JPanel();
		p11.setLayout(new GridLayout(1,2));
		clusteringButton = new JButton("Clustering");
		p11.add(clusteringButton);
		numClusterField = new JTextField("1");
		p11.add(numClusterField);
		p1.add(p11);
		
		ButtonGroup group1 = new ButtonGroup();
		correlationButton = new JRadioButton("Correlation");
		separativenessButton = new JRadioButton("Class Separativeness");
		group1.add(correlationButton);
		group1.add(separativenessButton);
		p1.add(correlationButton);
		p1.add(separativenessButton);
		
		pane = new JTabbedPane();
		pane.add(p1);
		pane.setTabComponentAt(0, new JLabel("Main"));
		this.add(pane);
		
		if (bl == null)
			bl = new ButtonListener();
		addButtonListener(bl);
		
		if (rbl == null)
			rbl = new RadioButtonListener();
		addRadioButtonListener(rbl);
		
		if (sl == null)
			sl = new SliderListener();
		addSliderListener(sl);
	}
	
	
	public void setIndividualCanvas(Object c) {
		icanvas = (IndividualCanvas) c;
	}
	
	public void setDimensionCanvas(Object c) {
		ocanvas = (DimensionCanvas) c;
	}
	
	public void setDimensionSelectionPanel(Object c) {
		dselection = (DimensionSelectionPanel) c;
	}


	/**
	 * タブで区切られた別のパネルを作る
	 */
	public void generatePanels() {
		if(iset == null) return;
		
		if (itext != null) {
			itext.setVisible(false);
			itext = null;
			pane.remove(2);
		}
		
		if (icoloring != null) {
			icoloring.setVisible(false);
			icoloring = null;
			pane.remove(1);
		}

		icoloring = new IndividualColoringPanel(iset);
		icoloring.setIndividualCanvas((Object) icanvas);
		icoloring.setDimensionCanvas((Object) ocanvas);
		pane.add(icoloring);
		pane.setTabComponentAt(1, new JLabel("Coloring"));

		itext = new IndividualTextPanel(iset);
		pane.add(itext);
		pane.setTabComponentAt(2, new JLabel("Text"));

		icoloring.setIndividualTextPanel(itext);
		dselection.setIndividualTextPanel(itext);
	}
	
	
	
	public void addRadioButtonListener(ActionListener actionListener) {
		correlationButton.addActionListener(actionListener);
		separativenessButton.addActionListener(actionListener);
	}
	
	
	public void addButtonListener(ActionListener actionListener) {
		fileOpenButton.addActionListener(actionListener);
		viewResetButton.addActionListener(actionListener);
		clusteringButton.addActionListener(actionListener);
	}
	
	
	public void addSliderListener(ChangeListener changeListener) {
		transparencySlider.addChangeListener(changeListener);
		cullingSlider.addChangeListener(changeListener);
	}
	
	
	public void addCheckBoxListener(ActionListener actionListener) {
		
	}
	
	

	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton buttonPushed = (JButton) e.getSource();
			
			if(buttonPushed == fileOpenButton) {	
				FileOpener fileOpener = new FileOpener();
				iset = fileOpener.readFile();
				generatePanels();
				dselection.setIndividualSet(iset);
				icanvas.setIndividualSet(iset);
				ocanvas.setIndividualSet(iset);
				icanvas.display();
				ocanvas.display();
			
			}	
			
			if(buttonPushed == viewResetButton) {
				icanvas.viewReset();
				icanvas.display();
				ocanvas.viewReset();
				ocanvas.display();
			}
			
			if(buttonPushed == clusteringButton) {
				int num = Integer.parseInt(numClusterField.getText());
				if(iset != null) {
					KmeansByNumeric.execute(iset, num);
					icanvas.setNumClusters(num);
					icanvas.display();
				}
			}
		}
	}
	
	
	class RadioButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton buttonPushed = (JRadioButton) e.getSource();
			if (buttonPushed == correlationButton) {
				iset.setDimSelectMode(iset.DIMSELECT_CORRELATION);
				ocanvas.display();
			}
			if (buttonPushed == separativenessButton) {
				iset.setDimSelectMode(iset.DIMSELECT_CLASS_PURELITY);
				ocanvas.display();
			}
		}
	}
	
	
	class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			JSlider sliderChanged = (JSlider) e.getSource();
			if (sliderChanged == transparencySlider) {
				double t = (double)transparencySlider.getValue() / 100.0;
				icanvas.setTransparency(t);
				icanvas.display();
			}
			if (sliderChanged == cullingSlider) {
				double c = (double)cullingSlider.getValue() / 100.0;
				DimensionCullingProcessor.setRatio(iset, c);
				icanvas.setIndividualSet(iset);
				icanvas.display();
				ocanvas.display();
			}
		}
	}
	
	
    class CheckBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			icanvas.display();
			ocanvas.display();
		}
	}
}