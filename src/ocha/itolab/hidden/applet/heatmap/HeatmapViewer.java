package ocha.itolab.hidden.applet.heatmap;


import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.media.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

public class HeatmapViewer extends JApplet {

	// GUI element
	MenuBar menuBar;
	IndividualSelectionPanel  iSelection = null; 
	SPSelectionPanel sSelection = null; 
	CursorListener cl;
	IndividualCanvas icanvas;
	SPDimensionCanvas sdcanvas;
	SPIndividualCanvas sicanvas;
	Container windowContainer ;

	
	
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
		sdcanvas = new SPDimensionCanvas(512, 512);
		sdcanvas.requestFocus();
		sicanvas = new SPIndividualCanvas(512, 512);
		sicanvas.requestFocus();
		
		GLCanvas glci = icanvas.getGLCanvas();
		GLCanvas glcsd = sdcanvas.getGLCanvas();
		GLCanvas glcsi = sicanvas.getGLCanvas();

		// ViewingPanel
		iSelection = new IndividualSelectionPanel();
		iSelection.setIndividualCanvas(icanvas);
		iSelection.setSPDimensionCanvas(sdcanvas);
		iSelection.setSPIndividualCanvas(sicanvas);
		sSelection = new SPSelectionPanel();
		sSelection.setIndividualCanvas(icanvas);
		sSelection.setSPDimensionCanvas(sdcanvas);
		sSelection.setSPIndividualCanvas(sicanvas);
		iSelection.setSPSelectionPanel(sSelection);
		
		// MenuBar
		menuBar = new MenuBar();
		menuBar.setIndividualCanvas(icanvas);
		menuBar.setSPDimensionCanvas(sdcanvas);
		menuBar.setSPIndividualCanvas(sicanvas);
		menuBar.setIndividualSelectionPanel(iSelection);
		menuBar.setSPSelectionPanel(sSelection);

		// CursorListener
		cl = new CursorListener();
		cl.setIndividualCanvas(icanvas, glci);
		cl.setSPDimensionCanvas(sdcanvas, glcsd);
		cl.setSPIndividualCanvas(sicanvas, glcsi);
		icanvas.addCursorListener(cl);
		sdcanvas.addCursorListener(cl);
		sicanvas.addCursorListener(cl);

		// CanvasとViewingPanelのレイアウト
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(1,2));
		p1.add(glci);
		JPanel p11 = new JPanel();
		p11.setLayout(new GridLayout(2,1));
		p11.add(glcsd);
		p11.add(glcsi);
		p1.add(p11);
		mainPanel.add(p1, BorderLayout.CENTER);
		mainPanel.add(iSelection, BorderLayout.WEST);
		mainPanel.add(sSelection, BorderLayout.EAST);

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
		HeatmapViewer nv = new HeatmapViewer(); //システムを起動

		nv.init(); //システム初期化
		window.getContentPane().add(nv); //windowにシステムを渡す
		window.setVisible(true); //??

		nv.start(); //システムを扱えるようにしている
	}

}
