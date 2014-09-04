package ocha.itolab.hidden.applet.booldim;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ocha.itolab.hidden.core.data.*;



public class IndividualSelectionPanel extends JPanel {
	
	IndividualSet iset = null;
	int numn;
	public JRadioButton xButtons[], yButtons[];
	JPanel spanel = null;
	
	Font font = new Font("Arial", Font.PLAIN, 16);;
	
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

	public IndividualSelectionPanel() {
		// super class init
		super();
		setSize(200, 600);
		
		pane = new JTabbedPane();
		this.add(pane);
	}
	
	
	public void setIndividualCanvas(Object c) {
		icanvas = (IndividualCanvas) c;
	}
	
	public void setDimensionCanvas(Object c) {
		ocanvas = (DimensionCanvas) c;
	}
	

	public void setIndividualSet(IndividualSet iset) {
		this.iset = iset;
		numn = iset.getNumNumeric();
		ButtonGroup group1 = new ButtonGroup();
		ButtonGroup group2 = new ButtonGroup();
		

		if (spanel != null) {
			spanel.setVisible(false);
			spanel = null;
			pane.remove(0);
		}
		
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(numn, 1));
		JPanel p2 = new JPanel();
		p2.setLayout(new GridLayout(numn, 1));
		
		xButtons = new JRadioButton[numn];
		yButtons = new JRadioButton[numn];
		for(int i = 0; i < numn; i++) {
			OneNumeric on = (OneNumeric)iset.numerics.dimensions.get(i);
			xButtons[i] = new JRadioButton(" ");
			group1.add(xButtons[i]);
			p1.add(xButtons[i]);
			yButtons[i] = new JRadioButton(on.getName());
			group2.add(yButtons[i]);
			p2.add(yButtons[i]);
		}
		
		spanel = new JPanel();
		spanel.setLayout(new FlowLayout());
		spanel.add(p1);
		spanel.add(p2);
		
		pane.add(spanel);
		pane.setTabComponentAt(0, new JLabel("Selection"));
		pane.setMnemonicAt(0, KeyEvent.VK_0);
		
		if (rbl == null)
			rbl = new RadioButtonListener();
		addRadioButtonListener(rbl);
	}
	

	
	public void addRadioButtonListener(ActionListener actionListener) {
		for (int i = 0; i < numn; i++) {
			xButtons[i].addActionListener(actionListener);
			yButtons[i].addActionListener(actionListener);
		}
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
			
			for (int i = 0; i < numn; i++) {
				if(buttonPushed == xButtons[i]) {
					icanvas.setXdimensionId(i);
				}
				if(buttonPushed == yButtons[i]) {
					icanvas.setYdimensionId(i);
				}
			}
			icanvas.display();
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