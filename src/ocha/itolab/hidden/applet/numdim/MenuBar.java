package ocha.itolab.hidden.applet.numdim;

import java.awt.event.*;
import javax.swing.*;
import ocha.itolab.hidden.core.data.*;


/*
 * HeianView のためのMenuBarを構築する
 * @author itot
 */
public class MenuBar extends JMenuBar {

	/* var */
	// file menu 
	public JMenuItem openMenuItem, exitMenuItem, helpMenuItem;

	// help menu 
	public JMenu fileMenu, helpMenu;

	// Listener
	MenuItemListener ml;
	
	// component
	IndividualCanvas icanvas = null;
	DimensionCanvas dcanvas = null;
	IndividualSelectionPanel  iSelection = null;
	DimensionSelectionPanel dSelection = null;
	
	/**
	 * Constructor
	 * @param withReadyMadeMenu 通常はtrue
	 */
	public MenuBar(boolean withReadyMadeMenu) {
		super();
		if (withReadyMadeMenu) {
			buildFileMenu();
			buildHelpMenu();
		}
		
		ml = new MenuItemListener();
		this.addMenuListener(ml);
	}

	/**
	 * Constructor
	 */
	public MenuBar() {
		this(true);
	}

	
	/**
	 * Panelをセットする
	 */
	public void setIndividualSelectionPanel(IndividualSelectionPanel p) {
		iSelection = p;
	}
	
	public void setDimensionSelectionPanel(DimensionSelectionPanel p) {
		dSelection = p;
	}
	 
	/**
	 * Fileに関するメニューを構築する
	 */
	public void buildFileMenu() {

		// create file menu
		fileMenu = new JMenu("File");
		add(fileMenu);

		// add menu item
		openMenuItem = new JMenuItem("Open");
		fileMenu.add(openMenuItem);
		exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);
	}



	/**
	 * Appearance に関するメニューを構築する
	 */
	public void buildHelpMenu() {

		// create help menu
		helpMenu = new JMenu("Help");
		add(helpMenu);

		// add menu item
		helpMenuItem = new JMenuItem("Help...");
		helpMenu.add(helpMenuItem);

	}

	
	/**
	 * Canvas をセットする
	 */
	public void setIndividualCanvas(IndividualCanvas c) {
		icanvas = c;
	}
	
	public void setDimensionCanvas(DimensionCanvas c) {
		dcanvas = c;
	}

	/**
	 * 選択されたメニューアイテムを返す
	 * @param name 選択されたメニュー名
	 * @return JMenuItem 選択されたメニューアイテム
	 */
	public JMenuItem getMenuItem(String name) {

		if (openMenuItem.getText().equals(name))
			return openMenuItem;
		if (exitMenuItem.getText().equals(name))
			return exitMenuItem;
		if (helpMenuItem.getText().equals(name))
			return helpMenuItem;

		// other
		return null;
	}

	/**
	 * メニューに関するアクションの検知を設定する
	 * @param actionListener ActionListener
	 */
	public void addMenuListener(ActionListener actionListener) {
		openMenuItem.addActionListener(actionListener);
		exitMenuItem.addActionListener(actionListener);
		helpMenuItem.addActionListener(actionListener);
	}
	
	/**
	 * メニューの各イベントを検出し、それに対応するコールバック処理を呼び出す
	 * 
	 * @author itot
	 */
	class MenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JMenuItem menuItem = (JMenuItem) e.getSource();
			if(openMenuItem == menuItem) {
				FileOpener fileOpener = new FileOpener();
				IndividualSet ps = fileOpener.readFile();
				//iSelection.setIndividualSet(ps);
				dSelection.setIndividualSet(ps);
				icanvas.setIndividualSet(ps);
				dcanvas.setIndividualSet(ps);
				icanvas.display();
				dcanvas.display();
			}
			if(exitMenuItem == menuItem) 
				System.exit(0);
		}
	}



}
