package com.googlecode.mibible.browser;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * the main class of mibible browser.
 * @author snuffkin
 */
public class MibBrowser {

	/**
	 * execute mibible browser.
	 * @param args invalid
	 */
	public static void main(String[] args) {
		MibBrowser browser = new MibBrowser();
		browser.initialize();
	}
	
	/**
	 * set look and feel.
	 */
	public MibBrowser() {
        try {
            String laf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(laf);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
	}
	
	/**
	 * initialize frame.
	 */
	public void initialize() {
		JFrame frame = createBrowserFrame();
		frame.setVisible(true);
	}
	
	/**
	 * create browser frame.
	 * @return browser frame
	 */
	private JFrame createBrowserFrame() {
		BrowserFrame frame = new BrowserFrame();
		frame.initialize();
		return frame;
	}
}
