package ocha.itolab.hidden.applet.heatmap;


import java.awt.event.MouseEvent;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.media.opengl.awt.GLCanvas;
import java.lang.Exception;


public class CursorListener implements MouseListener, MouseMotionListener, MouseWheelListener  {
	
	IndividualCanvas icanvas = null;
	SPDimensionCanvas sdcanvas = null;
	SPIndividualCanvas sicanvas = null;
	GLCanvas glci = null;
	GLCanvas glcsd = null;
	GLCanvas glcsi = null;
	int initX = 0, initY = 0, itotal = 0, sdtotal = 0, sitotal = 0;
	long icount = 0, sdcount = 0, sicount = 0;
	
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
	
	public void setSPDimensionCanvas(Object c, Object glc) {
		sdcanvas = (SPDimensionCanvas) c;
		glcsd = (GLCanvas) glc;
		glcsd.addMouseListener(this);
		glcsd.addMouseMotionListener(this);
		glcsd.addMouseWheelListener(this);
	}
	
	public void setSPIndividualCanvas(Object c, Object glc) {
		sicanvas = (SPIndividualCanvas) c;
		glcsi = (GLCanvas) glc;
		glcsi.addMouseListener(this);
		glcsi.addMouseMotionListener(this);
		glcsi.addMouseWheelListener(this);
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
		if (e.getComponent() == glcsd) {
		}
		if (e.getComponent() == glcsi) {
		}
	}

	
	public void mousePressed(MouseEvent e) {
		initX = e.getX();
		initY = e.getY();
		
		if (e.getComponent() == glci)
			icanvas.mousePressed(initX, initY);
		if (e.getComponent() == glcsd)
			sdcanvas.mousePressed(initX, initY);
		if (e.getComponent() == glcsi)
			sicanvas.mousePressed(initX, initY);
	}
	
	
	public void mouseReleased(MouseEvent e) {
		initX = e.getX();
		initY = e.getY();
		
		if (e.getComponent() == glci) {
			icanvas.mouseReleased(initX,initY);
			icanvas.display();
			sdcanvas.display();
			sicanvas.display();
		}
		if (e.getComponent() == glcsd) {
			sdcanvas.mouseReleased(initX,initY);
			
			int m = e.getModifiers();
			if((m & MouseEvent.BUTTON1_MASK) != 0) {
				icanvas.setSpecifiedDimensionList(sdcanvas.getDimensionList());
			}
			
			sdcanvas.display();
			sicanvas.display();
			icanvas.display();
		}
		if (e.getComponent() == glcsi) {
			sicanvas.mouseReleased(initX,initY);
			
			int m = e.getModifiers();
			if((m & MouseEvent.BUTTON1_MASK) != 0) {
				icanvas.setSpecifiedIndividualList(sicanvas.getIndividualList());
			}
			
			sdcanvas.display();
			sicanvas.display();
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
		if (e.getComponent() == glcsd) {
			if((m & MouseEvent.BUTTON1_MASK) != 0) 
				sdcanvas.setDragMode(4); // ZOOM mode
			else if((m & MouseEvent.BUTTON3_MASK) != 0) 
				sdcanvas.setDragMode(2); // SHIFT mode
			sdcanvas.drag(initX, cX, initY, cY);
			sdcanvas.display();
		}
		if (e.getComponent() == glcsi) {
			if((m & MouseEvent.BUTTON1_MASK) != 0) 
				sicanvas.setDragMode(4); // ZOOM mode
			else if((m & MouseEvent.BUTTON3_MASK) != 0) 
				sicanvas.setDragMode(2); // SHIFT mode
			sicanvas.drag(initX, cX, initY, cY);
			sicanvas.display();
		}
	}
	
	
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(icanvas == null || glci == null) return;
		if(sdcanvas == null || glcsd == null) return;
		if(sicanvas == null || glcsi == null) return;
		
		
		if (e.getComponent() == glci) {
			icount++;
			icanvas.mousePressed(initX, initY);
			icanvas.setDragMode(1); // ZOOM mode
			int r = e.getWheelRotation();
			itotal -= (r * 3);
			icanvas.drag(0, itotal, 0, itotal);
			icanvas.display();
			IndividualWheelThread wt = new IndividualWheelThread(icount);
			wt.start();
		}
		
		if (e.getComponent() == glcsd) {
			sdcount++;
			sdcanvas.mousePressed(initX, initY);
			sdcanvas.setDragMode(1); // ZOOM mode
			int r = e.getWheelRotation();
			sdtotal -= (r * 3);
			sdcanvas.drag(0, sdtotal, 0, sdtotal);
			sdcanvas.display();
			SPDimensionWheelThread wt = new SPDimensionWheelThread(sdcount);
			wt.start();
		}
		
		if (e.getComponent() == glcsi) {
			sicount++;
			sicanvas.mousePressed(initX, initY);
			sicanvas.setDragMode(1); // ZOOM mode
			int r = e.getWheelRotation();
			sitotal -= (r * 3);
			sicanvas.drag(0, sitotal, 0, sitotal);
			sicanvas.display();
			SPIndividualWheelThread wt = new SPIndividualWheelThread(sdcount);
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
	
	class SPDimensionWheelThread extends Thread {
		long count;
		SPDimensionWheelThread(long c) {
             this.count = c;
        }
 
         public void run() {
        	 try {
        		 Thread.sleep(100);
        	 } catch(Exception e) {
        	 	e.printStackTrace();
        	 }
        	 if(count != sdcount) return;
        	
        	 sdtotal = 0;
         }
	}
	
	class SPIndividualWheelThread extends Thread {
		long count;
		SPIndividualWheelThread(long c) {
             this.count = c;
        }
 
         public void run() {
        	 try {
        		 Thread.sleep(100);
        	 } catch(Exception e) {
        	 	e.printStackTrace();
        	 }
        	 if(count != sicount) return;
        	
        	 sitotal = 0;
         }
	}
}
