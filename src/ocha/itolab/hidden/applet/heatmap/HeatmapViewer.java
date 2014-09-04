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
	 * applet �����������A�e��f�[�^�\��������������
	 */
	public void init() {
		setSize(new Dimension(800,500));
		buildGUI();
	}

	/**
	 * applet �̊e�C�x���g�̎�t���X�^�[�g����
	 */
	public void start() {
	}

	/**
	 * applet �̊e�C�x���g�̎�t���X�g�b�v����
	 */
	public void stop() {
	}

	/**
	 * applet��������������
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

		// Canvas��ViewingPanel�̃��C�A�E�g
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

		// �E�B���h�E��̃��C�A�E�g
		windowContainer = this.getContentPane();
		windowContainer.setLayout(new BorderLayout());
		windowContainer.add(mainPanel, BorderLayout.CENTER);
		windowContainer.add(menuBar, BorderLayout.NORTH);

	}

	/**
	 * main�֐�
	 * @param args ���s���̈���
	 */
	public static void main(String[] args) {
		ocha.itolab.hidden.applet.Window window =
			new ocha.itolab.hidden.applet.Window(
				"HiddenViewer",800, 600, Color.lightGray); //Window���쐬
		HeatmapViewer nv = new HeatmapViewer(); //�V�X�e�����N��

		nv.init(); //�V�X�e��������
		window.getContentPane().add(nv); //window�ɃV�X�e����n��
		window.setVisible(true); //??

		nv.start(); //�V�X�e����������悤�ɂ��Ă���
	}

}
