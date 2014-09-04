package ocha.itolab.hidden.applet;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * Applet 内部に立ち上げる Window に関するクラス
 * @author itot
 */
public class Window extends JFrame {

	/**
	 * Constructor
	 * @param name
	 * @param isTerminatable
	 * @param width
	 * @param height
	 * @param bgColor
	 */
	public Window(String name, boolean isTerminatable, 
				  int width, int height, Color bgColor) {
		// init
		super(name); // constructor of Frame class
		// setup
		setBackground(bgColor);
		setSize(width, height);
		setVisible(true);
		addWindowListener(new WindowEventListener(isTerminatable));
	}

	/**
	 * Constructor
	 * @param name
	 * @param isTerminatable
	 */
	public Window(String name, boolean isTerminatable) {
		//		this(name, 640, 480, Color.lightGray); // VGA: 640x480
		this(name, isTerminatable, 800, 600, Color.lightGray); // SVGA: 800x600
	}

	/**
	 * Constructor
	 * @param name
	 * @param width
	 * @param height
	 * @param bgColor
	 */
	public Window(String name, int width, int height, Color bgColor) {
		this(name, true, width, height, bgColor);
	}

	/**
	 * Constructor
	 * @param name
	 */
	public Window(String name) {
		//		this(name, true, 640, 480, Color.lightGray); // VGA: 640x480
		this(name, true, 800, 600, Color.lightGray); // SVGA: 800x600
	}

	/**
	 * ウィンドウのイベントを検知する WindowAdapter
	 * @author itot
	 */
	private class WindowEventListener extends WindowAdapter {

		/* var */
		boolean isTerminatable = false;

		/**
		 * Constructor
		 * @param isTerminatable
		 */
		WindowEventListener(boolean isTerminatable) {
			setTermination(isTerminatable);
		}

		/**
		 * Windowを閉じる
		 */
		public void windowClosing(WindowEvent e) {
			setVisible(false);
			// dispose();
			if (isTerminatable) {
				System.exit(0);
			}
		}

		/**
		 * 終了可能を表すパラメータをセットする
		 * @param isTerminatable
		 */
		public void setTermination(boolean isTerminatable) {
			this.isTerminatable = isTerminatable;
		}
	}	
}
