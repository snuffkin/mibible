package com.googlecode.mibible.browser;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;

public class BrowserFrame extends JFrame
{
    /** デフォルトのInsets */
    public static final Insets DEFAULT_INSETS = new Insets(2, 5, 2, 5);

    /** Mediator */
    private Mediator mediator = new Mediator();
    
    /** File Menu */
    private JMenuBar menuBar = new JMenuBar();
    
    /**
     * Frameの初期表示を行う。
     */
    public void initialize()
    {
        // Mediatorへの設定
        this.mediator.setBrowserFrame(this);
        
        // Frameの初期表示領域の設定
        Rectangle bounds = new Rectangle();
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        bounds.width = (int) (size.width * 0.8);
        bounds.height = (int) (size.height * 0.8);
        bounds.x = (size.width - bounds.width) / 2;
        bounds.y = (size.height - bounds.height) / 2;
        setBounds(bounds);
        
        // ウィンドウタイトルの設定
        setTitle("mibible browser");
        // ウィンドウのXボタンで閉じる
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowListener(){
            public void windowActivated(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {}
            public void windowClosing(WindowEvent e) {
                BrowserFrame.this.mediator.exit();
            }
            public void windowDeactivated(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowOpened(WindowEvent e) {}
        });
        // メニューを設定する
        this.menuBar = getMenu();
        setJMenuBar(this.menuBar);
        // Frameにレイアウトを設定する
        getContentPane().setLayout(new GridBagLayout());
        
        // Frame Layout
        // +----------------------------------+
        // | Tree Panel | Description Panel   |
        // +            |---------------------+
        // |            | Communication Panel |
        // +----------------------------------+
        
        // レイアウト
        GridBagConstraints  gbc;
        
        // Frame内部を左右に分けるPaneのレイアウトを設定する
        JSplitPane leftRightSplitPane = new JSplitPane();
        leftRightSplitPane.setDividerLocation((int) (bounds.width * 0.40));
        gbc = new GridBagConstraints();
        gbc.weightx = 1.0d;
        gbc.weighty = 1.0d;
        gbc.fill = GridBagConstraints.BOTH;
        getContentPane().add(leftRightSplitPane, gbc);

        // 右側のPane内部をを上下に分けるPaneのレイアウトを設定する
        JSplitPane topBottomSplitPane = new JSplitPane();
        topBottomSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        // TODO communication　panelができるまでの暫定処置
//        topBottomSplitPane.setDividerLocation((int) (bounds.height * 0.40));
        topBottomSplitPane.setDividerLocation((int) (bounds.height));
        topBottomSplitPane.setOneTouchExpandable(true);
        
        // 右側のPane内部をを上下に分けるPaneの中身を設定する
        topBottomSplitPane.setLeftComponent(getDescriptionPanel(mediator));
        topBottomSplitPane.setRightComponent(getCommunicationPanel(mediator));
        
        // Frame内部を左右に分けるPaneの中身を設定する
        leftRightSplitPane.setLeftComponent(getTreePanel(mediator));
        leftRightSplitPane.setRightComponent(topBottomSplitPane);
        
        // ステータスフィールドを設定する
        JTextField statusField = new JTextField();
        statusField.setEditable(false);
        statusField.setBorder(new EmptyBorder(new Insets(2, 5, 2, 5)));
        statusField.setBackground(getBackground());
        gbc = new GridBagConstraints();
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 5, 2, 5);
        getContentPane().add(statusField, gbc);
        
        // Mediatorにコンポーネントを設定する
        this.mediator.setStatusField(statusField);
    }

    /**
     * メニューバーを作成する。
     * @return メニューバー
     */
    private JMenuBar getMenu()
    {
        JMenuBar menuBar = new JMenuBar();
        
        // Fileメニューを追加する
        menuBar.add(getFileMenu());
        // Helpメニューを追加する
        // TODO
//        menuBar.add(getHelpMenu());
        
        return menuBar;
    }
    
    /**
     * メニューの「File」部分を作成する。
     * @return Fileメニュー
     */
    private JMenu getFileMenu()
    {
        // Create File menu
        JMenu menu = new JMenu("File");
        menu.setMnemonic('F');
        this.updateFileMenu(menu);
        
        return menu;
    }
    
    private void updateFileMenu(JMenu menu) {
        // Create Open MIB item
        JMenuItem open = new JMenuItem("Open MIB...");
        open.setMnemonic('O');
        open.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Properties prop = BrowserFrame.this.mediator.getProperties();
                String openDirectory = prop.getProperty(Mediator.FILE_CHOOSER_DIRECTORY, ".");
                JFileChooser  dialog = new JFileChooser(new File(openDirectory));
                File[]        files;
                int           result;

                dialog.setMultiSelectionEnabled(true);
                result = dialog.showOpenDialog(BrowserFrame.this);
                if (result == JFileChooser.APPROVE_OPTION)
                {
                    files = dialog.getSelectedFiles();
                    BrowserFrame.this.mediator.openMib(files);
                    // TODO
//                    BrowserFrdescriptionArea.setText("");
                    if (files.length > 0)
                    {
                        openDirectory = files[0].getParent();
                        prop.setProperty(Mediator.FILE_CHOOSER_DIRECTORY, openDirectory);
                    }
                }
            }
        });
        menu.add(open);
        
        // Create Close MIB item
        JMenuItem close = new JMenuItem("Close MIB");
        close.setMnemonic('C');
        close.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                BrowserFrame.this.mediator.unloadMib();
            }
        });
        menu.add(close);
        
        // Create Separator
        menu.addSeparator();
        
        // Create History
        Properties prop = BrowserFrame.this.mediator.getProperties();
        String historyStr = prop.getProperty("mibbrowser.history", "0");
        int history = Integer.valueOf(historyStr);
        boolean displayHistory = false; // ヒストリがある場合だけseparatorを引く
        for (int index = 1; index <= history; index++)
        {
            String fileName = prop.getProperty("mibbrowser.history." + index, "");
            if (fileName.equals(""))
            {
                continue;
            }
            File file = new File(fileName);
            String historyMenuStr;
            try {
                historyMenuStr = index + " " + file.getName() + " [" + file.getCanonicalPath() + "]";
                JMenuItem historyMenu = new JMenuItem(historyMenuStr);
                if (index < 10)
                {
                    historyMenu.setMnemonic(String.valueOf(index).charAt(0));
                }
                menu.add(historyMenu);
                displayHistory = true;
                historyMenu.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        // TODO パースが汚い
                        String fileName = e.getActionCommand();
                        String[] tmp = fileName.split("\\[");
                        if (tmp.length > 0)
                        {
                            String[] tmp2 = tmp[1].split("\\]");
                            BrowserFrame.this.mediator.openMib(new File(tmp2[0]));
                        }
                    }
                });
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        if (displayHistory)
        {
            // Create Separator
            menu.addSeparator();
        }
        
        // Create Exit item
        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic('E');
        exit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                BrowserFrame.this.mediator.exit();
            }
        });
        menu.add(exit);
    }
    
    public void updateHistoryMenu()
    {
        JMenu menu = this.menuBar.getMenu(0);
        menu.removeAll();
        
        // Create Open MIB item
        this.updateFileMenu(menu);
    }
    
    /**
     * メニューの「Help」部分を作成する。
     * @return Helpメニュー
     */
    private JMenu getHelpMenu()
    {
        // Create Help menu
        JMenu menu = new JMenu("Help");
        menu.setMnemonic('H');
        
        // Create Open MIB item
        JMenuItem about = new JMenuItem("About mibible");
        about.setMnemonic('A');
        about.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // TODO
                JOptionPane.showMessageDialog(BrowserFrame.this, "sorry. under construction.");
            }
        });
        menu.add(about);
        
        return menu;
    }

    /**
     * Mediatorと関連付けを行ったTreePanelを作成する。
     * @param mediator
     * @return　作成したTreePanel
     */
    private JPanel getTreePanel(Mediator mediator)
    {
        TreePanel panel = new TreePanel(mediator);
        panel.initialize();
        return panel;
    }

    /**
     * Mediatorと関連付けを行ったDescriptionPanelを作成する。
     * @param mediator
     * @return　作成したDescriptionPanel
     */
    private JPanel getDescriptionPanel(Mediator mediator)
    {
        DescriptionPanel panel = new DescriptionPanel(mediator);
        panel.initialize();
        return panel;
    }

    /**
     * Mediatorと関連付けを行ったCommunicationPanelを作成する。
     * @param mediator
     * @return　作成したCommunicationPanel
     */
    private JPanel getCommunicationPanel(Mediator mediator)
    {
        CommunicationPanel panel = new CommunicationPanel(mediator);
        panel.initialize();
        return panel;
    }
}
