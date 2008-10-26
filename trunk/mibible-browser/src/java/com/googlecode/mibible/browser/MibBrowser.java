package com.googlecode.mibible.browser;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class MibBrowser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MibBrowser browser = new MibBrowser();
		browser.initialize();
	}
	
	public MibBrowser()
	{
        // Open browser frame
        try
        {
            String laf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(laf);
        }
        catch (Exception ex)
        {
        	ex.printStackTrace();
        }
	}
	
	public void initialize()
	{
		JFrame frame = getBrowserFrame();
		frame.setVisible(true);
	}
	
	private JFrame getBrowserFrame()
	{
		BrowserFrame frame = new BrowserFrame();
		frame.initialize();
		return frame;
	}

}
