package ocha.itolab.hidden.applet.booldim;


import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.media.opengl.awt.GLCanvas;

import ocha.itolab.hidden.core.data.OneBoolean;

import com.jogamp.opengl.util.Animator;

public class BooldimViewer extends JApplet {

	// GUI element
	MenuBar menuBar;
	IndividualSelectionPanel  iSelection = null; 
	DimensionSelectionPanel dSelection = null; 
	CursorListener cl;
	IndividualCanvas icanvas;
	DimensionCanvas dcanvas;
	Container windowContainer ;

	public static double plotcolors[][] = {
		{1.0, 0.0, 0.0},
		{1.0, 0.5, 0.0},
		{0.5, 1.0, 0.0},
		{0.0, 1.0, 0.5},
		{0.0, 0.5, 1.0}
	};
	
	
	
	
	/**
	 * applet を初期化し、各種データ構造を初期化する
	 */
	public void init() {
		setSize(new Dimension(800,500));
		buildGUI();
	}

	/**
	 * applet の各イベントの受付をスタートする
	 */
	public void start() {
	}

	/**
	 * applet の各イベントの受付をストップする
	 */
	public void stop() {
	}

	/**
	 * applet等を初期化する
	 */
	private void buildGUI() {

		// Canvas
		icanvas = new IndividualCanvas(512, 512);
		icanvas.requestFocus();
		dcanvas = new DimensionCanvas(512, 512);
		dcanvas.requestFocus();
		
		GLCanvas glci = icanvas.getGLCanvas();
		GLCanvas glcd = dcanvas.getGLCanvas();

		// ViewingPanel
		iSelection = new IndividualSelectionPanel();
		iSelection.setIndividualCanvas(icanvas);
		iSelection.setDimensionCanvas(dcanvas);
		dSelection = new DimensionSelectionPanel();
		dSelection.setIndividualCanvas(icanvas);
		dSelection.setDimensionCanvas(dcanvas);
		
		// MenuBar
		menuBar = new MenuBar();
		menuBar.setIndividualCanvas(icanvas);
		menuBar.setDimensionCanvas(dcanvas);
		menuBar.setIndividualSelectionPanel(iSelection);
		menuBar.setDimensionSelectionPanel(dSelection);

		// CursorListener
		cl = new CursorListener();
		cl.setIndividualCanvas(icanvas, glci);
		cl.setDimensionCanvas(dcanvas, glcd);
		icanvas.addCursorListener(cl);
		dcanvas.addCursorListener(cl);

		// CanvasとViewingPanelのレイアウト
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(1,2));
		p1.add(glci);
		p1.add(glcd);
		mainPanel.add(p1, BorderLayout.CENTER);
		mainPanel.add(iSelection, BorderLayout.WEST);
		mainPanel.add(dSelection, BorderLayout.EAST);

		// ウィンドウ上のレイアウト
		windowContainer = this.getContentPane();
		windowContainer.setLayout(new BorderLayout());
		windowContainer.add(mainPanel, BorderLayout.CENTER);
		windowContainer.add(menuBar, BorderLayout.NORTH);

	}

	/**
	 * main関数
	 * @param args 実行時の引数
	 */
	public static void main(String[] args) {
		ocha.itolab.hidden.applet.Window window =
			new ocha.itolab.hidden.applet.Window(
				"HiddenViewer",800, 600, Color.lightGray); //Windowを作成
		BooldimViewer bv = new BooldimViewer(); //システムを起動

		bv.init(); //システム初期化
		window.getContentPane().add(bv); //windowにシステムを渡す
		window.setVisible(true); //??

		bv.start(); //システムを扱えるようにしている
	}

}
