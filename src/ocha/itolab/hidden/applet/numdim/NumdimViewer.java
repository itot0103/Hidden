package ocha.itolab.hidden.applet.numdim;


import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.media.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

public class NumdimViewer extends JApplet {

	// GUI element
	MenuBar menuBar;
	IndividualSelectionPanel  iSelection = null; 
	DimensionSelectionPanel dSelection = null; 
	CursorListener cl;
	IndividualCanvas icanvas;
	DimensionCanvas dcanvas;
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
		iSelection.setDimensionSelectionPanel(dSelection);
		
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

		// Canvas��ViewingPanel�̃��C�A�E�g
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(1,2));
		p1.add(glci);
		p1.add(glcd);
		mainPanel.add(p1, BorderLayout.CENTER);
		mainPanel.add(iSelection, BorderLayout.WEST);
		mainPanel.add(dSelection, BorderLayout.EAST);

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
		NumdimViewer nv = new NumdimViewer(); //�V�X�e�����N��

		nv.init(); //�V�X�e��������
		window.getContentPane().add(nv); //window�ɃV�X�e����n��
		window.setVisible(true); //??

		nv.start(); //�V�X�e����������悤�ɂ��Ă���
	}

}
