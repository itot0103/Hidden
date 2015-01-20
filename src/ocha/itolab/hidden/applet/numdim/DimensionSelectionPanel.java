package ocha.itolab.hidden.applet.numdim;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.*;
import java.lang.Thread.UncaughtExceptionHandler;

import ocha.itolab.hidden.applet.numdim.CursorListener.IndividualWheelThread;
import ocha.itolab.hidden.core.data.*;
import ocha.itolab.hidden.core.tool.*;

public class DimensionSelectionPanel extends JPanel {
	
	IndividualSet ps = null;
	
	Font font = new Font("Arial", Font.PLAIN, 16);;
	
	// referred y ChildPhotoPanel.java
	// by ITOT 2011/5/15
	static int colorcount = 0;
	
	/* Selective canvas */
	IndividualCanvas icanvas;
	DimensionCanvas ocanvas;
	IndividualTextPanel itext;
	JTabbedPane pane = null;
	
	JSlider slider1, slider2;
	
	/* Action listener */
	ButtonListener bl = null;
	RadioButtonListener rbl = null;
	SliderListener sl = null;
	CheckBoxListener cbl = null;

	public DimensionSelectionPanel() {
		// super class init
		super();
		setSize(200, 600);
		
		//pane = new JTabbedPane();
		//pane.add(pp);
		//pane.setTabComponentAt(0, new JLabel("Main"));
		//this.add(pane);
		
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(2,1));
		slider1 = new JSlider(JSlider.VERTICAL, 0, 100, 0);
		p1.add(slider1);
		slider2 = new JSlider(JSlider.VERTICAL, 0, 100, 0);
		p1.add(slider2);
		
		this.add(p1);

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
	
	
	public void setIndividualSet(IndividualSet p) {
		ps = p;
	}
	
	
	public void setIndividualTextPanel(IndividualTextPanel p) {
		itext = p;
	}
	
	

	public void addRadioButtonListener(ActionListener actionListener) {
		
	}
	
	
	public void addButtonListener(ActionListener actionListener) {
		
	}
	

	public void addSliderListener(ChangeListener changeListener) {
		slider1.addChangeListener(changeListener);
		slider2.addChangeListener(changeListener);
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

	static long now1 = 0, now2 = 0;
	CatchException catchException = new CatchException();
	
	class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			
			JSlider sliderChanged = (JSlider) e.getSource();
			if (sliderChanged == slider1) {
				if(ps == null) return;
				
				long now = System.currentTimeMillis();
				now1 = now;
				
				Slider1Thread st1 = new Slider1Thread(now);
				st1.setUncaughtExceptionHandler(catchException);
				st1.start();
			}
			if (sliderChanged == slider2) {
				if(ps == null) return;
				
				long now = System.currentTimeMillis();
				now2 = now;
				
				Slider2Thread st2 = new Slider2Thread(now);
				st2.setUncaughtExceptionHandler(catchException);
				st2.start();
			}
		}
	}
	
	
	class Slider1Thread extends Thread {
		long now;
		Slider1Thread(long n) {
             this.now = n;
        }
 
         public void run() {
        	 try {
        		 Thread.sleep(300);
        	 } catch(Exception e) {
        	 	e.printStackTrace();
        	 }
        	 if(now != now1) return;
        	 
        	 if(ps.getDimSelectMode() == ps.DIMSELECT_CORRELATION) {
        		 
        		 double edgeratio = (double)slider1.getValue() / 100.0;
        		 ocanvas.setEdgeRatio(edgeratio);
        		 ocanvas.display();
			
        		 DimensionCliqueFinder.setEdgeRatio(edgeratio);
        		 ArrayList<ArrayList> cliques = DimensionCliqueFinder.getCliques(ps);
        		 icanvas.setDimensionCliques(cliques);
        		 itext.updateDimensionInfo(cliques);
        		 icanvas.display();
        	 }
				
        	 if(ps.getDimSelectMode() == ps.DIMSELECT_CLASS_PURELITY) {
        		 double confidence = 1.0 - (double)slider1.getValue() / 100.0;
        		 DimensionClassSeparativeness.setClassConfidence(confidence);
        		 ArrayList<ArrayList> cliques = DimensionClassSeparativeness.extract(ps);
        		 icanvas.setDimensionCliques(cliques);
        		 itext.updateDimensionInfo(cliques);
        		 icanvas.display();
			}	
			
         }
	}

	
	class Slider2Thread extends Thread {
		long now;
		Slider2Thread(long n) {
             this.now = n;
        }
 
         public void run() {
        	 try {
        		 Thread.sleep(300);
        	 } catch(Exception e) {
        	 	e.printStackTrace();
        	 }
        	 if(now != now2) return;
        	 
        	 if(ps.getDimSelectMode() == ps.DIMSELECT_CORRELATION) {
        		 
        	 }
				
        	 if(ps.getDimSelectMode() == ps.DIMSELECT_CLASS_PURELITY) {
        		 double support = 1.0 - (double)slider2.getValue() / 100.0;
        		 DimensionClassSeparativeness.setClassSupport(support);
        		 ArrayList<ArrayList> cliques = DimensionClassSeparativeness.extract(ps);
        		 icanvas.setDimensionCliques(cliques);
        		 itext.updateDimensionInfo(cliques);
        		 icanvas.display();
			}	
			
         }
	}

	class CatchException implements UncaughtExceptionHandler {
		public void uncaughtException(Thread t, Throwable e) {
			System.out.println(t.getName());
			icanvas.unlockDisplay();
		}
	}

	
    class CheckBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			icanvas.display();
			ocanvas.display();
		}
	}
}