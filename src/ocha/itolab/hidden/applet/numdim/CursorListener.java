package ocha.itolab.hidden.applet.numdim;


import java.awt.event.MouseEvent;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.media.opengl.awt.GLCanvas;
import java.lang.Exception;


public class CursorListener implements MouseListener, MouseMotionListener, MouseWheelListener  {
	
	IndividualCanvas icanvas = null;
	DimensionCanvas dcanvas = null;
	GLCanvas glci = null;
	GLCanvas glcd = null;
	int initX = 0, initY = 0, itotal = 0, dtotal = 0;
	long icount = 0, dcount = 0;
	
	/**
	 * @param c Canvas
	 */
	public void setIndividualCanvas(Object c, Object glc) {
		icanvas = (IndividualCanvas) c;
		glci = (GLCanvas) glc;
		glci.addMouseListener(this);
		glci.addMouseMotionListener(this);
		glci.addMouseWheelListener(this);
	}
	
	public void setDimensionCanvas(Object c, Object glc) {
		dcanvas = (DimensionCanvas) c;
		glcd = (GLCanvas) glc;
		glcd.addMouseListener(this);
		glcd.addMouseMotionListener(this);
		glcd.addMouseWheelListener(this);
	}
	

	public void mouseEntered(MouseEvent e) {
	}
	
	public void mouseExited(MouseEvent e) {
	}
	
	
	public void mouseClicked(MouseEvent e) {
		initX = e.getX();
		initY = e.getY();
		
		if (e.getComponent() == glci) {
		}
		if (e.getComponent() == glcd) {
		}
	}

	
	public void mousePressed(MouseEvent e) {
		initX = e.getX();
		initY = e.getY();
		
		if (e.getComponent() == glci)
			icanvas.mousePressed(initX, initY);
		if (e.getComponent() == glcd)
			dcanvas.mousePressed(initX, initY);
	}
	
	
	public void mouseReleased(MouseEvent e) {
		initX = e.getX();
		initY = e.getY();
		
		if (e.getComponent() == glci) {
			icanvas.mouseReleased(initX,initY);
			icanvas.display();
			dcanvas.display();
		}
		if (e.getComponent() == glcd) {
			dcanvas.mouseReleased(initX,initY);
			
			int m = e.getModifiers();
			if((m & MouseEvent.BUTTON1_MASK) != 0) {
				icanvas.setDimensionList(dcanvas.getDimensionList());
			}
			
			dcanvas.display();
			icanvas.display();
		}
	}
	
	
	public void mouseMoved(MouseEvent e){
		
	}
	
	
	public void mouseDragged(MouseEvent e) {
		int cX = e.getX();
		int cY = e.getY();
		int m = e.getModifiers();
		
		if (e.getComponent() == glci) {
			if((m & MouseEvent.BUTTON1_MASK) != 0) 
				icanvas.setDragMode(1); // ZOOM mode
			else if((m & MouseEvent.BUTTON3_MASK) != 0) 
				icanvas.setDragMode(2); // SHIFT mode	
			icanvas.drag(initX, cX, initY, cY);
			icanvas.display();
		}
		if (e.getComponent() == glcd) {
			if((m & MouseEvent.BUTTON1_MASK) != 0) 
				dcanvas.setDragMode(4); // ZOOM mode
			else if((m & MouseEvent.BUTTON3_MASK) != 0) 
				dcanvas.setDragMode(2); // SHIFT mode
			dcanvas.drag(initX, cX, initY, cY);
			dcanvas.display();
		}
	}
	
	
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(icanvas == null || glci == null) return;
		if(dcanvas == null || glcd == null) return;
		
		
		if (e.getComponent() == glci) {
			icount++;
			icanvas.mousePressed(initX, initY);
			icanvas.setDragMode(1); // ZOOM mode
			int r = e.getWheelRotation();
			itotal -= (r * 20);
			icanvas.drag(0, itotal, 0, itotal);
			icanvas.display();
			IndividualWheelThread wt = new IndividualWheelThread(icount);
			wt.start();
		}
		
		if (e.getComponent() == glcd) {
			dcount++;
			dcanvas.mousePressed(initX, initY);
			dcanvas.setDragMode(1); // ZOOM mode
			int r = e.getWheelRotation();
			dtotal -= (r * 20);
			dcanvas.drag(0, dtotal, 0, dtotal);
			dcanvas.display();
			DimensionWheelThread wt = new DimensionWheelThread(dcount);
			wt.start();
		}
		
	}
	
	class IndividualWheelThread extends Thread {
		long count;
		IndividualWheelThread(long c) {
             this.count = c;
        }
 
         public void run() {
        	 try {
        		 Thread.sleep(100);
        	 } catch(Exception e) {
        	 	e.printStackTrace();
        	 }
        	 if(count != icount) return;
        	 
        	 itotal = 0;
         }
	}
	
	class DimensionWheelThread extends Thread {
		long count;
		DimensionWheelThread(long c) {
             this.count = c;
        }
 
         public void run() {
        	 try {
        		 Thread.sleep(100);
        	 } catch(Exception e) {
        	 	e.printStackTrace();
        	 }
        	 if(count != dcount) return;
        	
        	 dtotal = 0;
         }
	}
}
