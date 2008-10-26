package com.googlecode.mibible.browser;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class BrowserFrame extends JFrame
{
    /** The default component insets */
    public static final Insets DEFAULT_INSETS = new Insets(2, 5, 2, 5);

    public void initialize()
    {
    	// Frameの描画領域
        Rectangle bounds = new Rectangle();
        Dimension size;
        // レイアウト
        GridBagConstraints  c;

    	// Frameの初期表示領域の設定
        size = Toolkit.getDefaultToolkit().getScreenSize();
        bounds.width = (int) (size.width * 0.8);
        bounds.height = (int) (size.height * 0.8);
        bounds.x = (size.width - bounds.width) / 2;
        bounds.y = (size.height - bounds.height) / 2;
        setBounds(bounds);
    	
        // ウィンドウタイトルの設定
        setTitle("mibible browser");
        // ウィンドウのXボタンで閉じる
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // メニューを設定する
        setJMenuBar(getMenu());
        // Frameにレイアウトを設定する
        getContentPane().setLayout(new GridBagLayout());
        
        // Frame内部を左右に分けるPaneのレイアウトを設定する
        JSplitPane letfRightSplitPane = new JSplitPane();
        letfRightSplitPane.setDividerLocation((int) (bounds.width * 0.45));
        c = new GridBagConstraints();
        c.weightx = 1.0d;
        c.weighty = 1.0d;
        c.fill = GridBagConstraints.BOTH;
        getContentPane().add(letfRightSplitPane, c);

        // 右側のPane内部をを上下に分けるPaneのレイアウトを設定する
        JSplitPane topBottomSplitPane = new JSplitPane();
        topBottomSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        topBottomSplitPane.setDividerLocation((int) (bounds.height * 0.40));
        topBottomSplitPane.setOneTouchExpandable(true);
        
        // 右側のPane内部をを上下に分けるPaneの中身を設定する
        letfRightSplitPane.setLeftComponent(getDescriptionPanel());
        letfRightSplitPane.setRightComponent(getSnmpPanel());
        
        // Frame内部を左右に分けるPaneの中身を設定する
        letfRightSplitPane.setLeftComponent(getTreePanel());
        letfRightSplitPane.setRightComponent(topBottomSplitPane);
    }
    
    private JMenuBar getMenu()
    {
    	JMenuBar menuBar = new JMenuBar();
    	
        // Create file menu
    	JMenu file = new JMenu("File");
        
    	// Create Open MIB item
    	JMenuItem open = new JMenuItem("Open MIB...");
    	open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// TODO
//                loadMib();
            }
        });
        file.add(open);
        
    	// Create Close MIB item
        JMenuItem close = new JMenuItem("Close MIB");
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// TODO
//                unloadMib();
            }
        });
        file.add(close);
        
        // Create Separator
        file.addSeparator();
        
    	// Create Exit item
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        file.add(exit);
        
        // Add file to menu bar
        menuBar.add(file);
    	
    	return menuBar;
    }
    
    private JPanel getTreePanel()
    {
    	TreePanel panel = new TreePanel();
    	panel.initialize();
    	return panel;
    }
    private JPanel getDescriptionPanel()
    {
    	DescriptionPanel panel = new DescriptionPanel();
    	panel.initialize();
    	return panel;
    }
    private JPanel getSnmpPanel()
    {
    	SnmpPanel panel = new SnmpPanel();
    	panel.initialize();
    	return panel;
    }
    
}
