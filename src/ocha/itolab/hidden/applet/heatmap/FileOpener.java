package ocha.itolab.hidden.applet.heatmap;

import java.io.File;
import java.util.Vector;
import java.awt.*;

import javax.swing.*;

import ocha.itolab.hidden.core.data.*;
import ocha.itolab.hidden.core.tool.IndividualDissimilarity;

public class FileOpener {
	
	File currentDirectory, inputFile, outputFile;
	Component windowContainer;
	IndividualCanvas icanvas;
	SPDimensionCanvas sdcanvas;
	SPIndividualCanvas sicanvas;
	
	
	/**
	 * Container ���Z�b�g����
	 * @param c Component
	 */
	public void setContainer(Component c) {
		windowContainer = c;
	}
	
	
	/**
	 * Canvas ���Z�b�g����
	 * @param c Canvas
	 */
	public void setIndividualCanvas(IndividualCanvas ic) {
		icanvas = ic;
	}
	
	
	/**
	 * Canvas ���Z�b�g����
	 * @param c Canvas
	 */
	public void setSPDimensionCanvas(SPDimensionCanvas dc) {
		sdcanvas = dc;
	}
	
	/**
	 * Canvas ���Z�b�g����
	 * @param c Canvas
	 */
	public void setSPIndividualCanvas(SPIndividualCanvas dc) {
		sicanvas = dc;
	}
	

	/**
	 * �t�@�C���_�C�A���O�ɃC�x���g���������Ƃ��ɁA�Ή�����t�@�C������肷��
	 * @return �t�@�C��
	 */
	public File getFile() {
		JFileChooser fileChooser = new JFileChooser(currentDirectory);
		int selected = fileChooser.showOpenDialog(windowContainer);
		if (selected == JFileChooser.APPROVE_OPTION) { // open selected
			currentDirectory = fileChooser.getCurrentDirectory();
			System.out.println(currentDirectory);
			fileChooser.setCurrentDirectory(currentDirectory);
			return fileChooser.getSelectedFile();
		} else if (selected == JFileChooser.CANCEL_OPTION) { // cancel selected
			return null;
		} 
		
		return null;
	}
	
	
	/**
	 * �t�@�C����ǂݍ���
	 */
	public IndividualSet readFile() {
		IndividualSet iset;
		inputFile = getFile();
		if (inputFile == null) {
			iset = null;  return null;
		} 
		
		DataFileReader pfr = new DataFileReader();
		iset = pfr.read(inputFile.getAbsolutePath());
		IndividualDissimilarity.calculate(iset);
		
		return iset;
	}


}
