/*
 * BrowserFrame.java
 *
 * This work is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This work is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 *
 * Copyright (c) 2004-2005 Per Cederberg. All rights reserved.
 */

package com.googlecode.mibible.browser;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import net.percederberg.mibble.MibLoaderException;
import net.percederberg.mibble.MibValueSymbol;

/**
 * The main MIB browser application window (frame).
 *
 * @author   Per Cederberg, <per at percederberg dot net>
 * @version  2.6
 * @since    2.5
 */
public class BrowserFrame extends JFrame {

    /** The default component insets */
    private static final Insets DEFAULT_INSETS = new Insets(2, 5, 2, 5);

    /**
     * The browser application.
     */
    private MibBrowser browser;

    /**
     * The menu bar.
     */
    private JMenuBar menuBar = new JMenuBar();

    /**
     * The SNMP version 1 menu item.
     */
    private JCheckBoxMenuItem snmpV1Item =
        new JCheckBoxMenuItem("SNMP version 1");

    /**
     * The SNMP version 2c menu item.
     */
    private JCheckBoxMenuItem snmpV2Item =
        new JCheckBoxMenuItem("SNMP version 2c");

    /**
     * The SNMP version 3 menu item.
     */
    private JCheckBoxMenuItem snmpV3Item =
        new JCheckBoxMenuItem("SNMP version 3");

    /**
     * The description text area.
     */
    private JTextArea descriptionArea = new JTextArea();

    /**
     * The status label.
     */
    private JLabel statusLabel = new JLabel("Ready");

    /**
     * The MIB tree component.
     */
    private JTree mibTree = null;

    /** Trap Name Search label */
    private JLabel nameSearchLabel = new JLabel("Name:"); 
    /** Trap Name Search field */
    private JTextField nameSearchField = new JTextField("", 30);
    /** The Name Search button */
    private JButton nameSearchButton = new JButton("Search");

    /** Trap OID Search label */
    private JLabel oidSearchLabel = new JLabel("OID:"); 
    /** Trap OID Search field */
    private JTextField oidSearchField = new JTextField("", 30);
    /** The OID Search button */
    private JButton oidSearchButton = new JButton("Search");

    /**
     * The SNMP operations panel.
     */
    private SnmpPanel snmpPanel = null;

    /**
     * The current MIB file directory.
     */
    private File currentDir = new File(".");

    /**
     * Creates a new browser frame.
     *
     * @param browser        the browser application
     */
    public BrowserFrame(MibBrowser browser) {
        String  dir;

        this.browser = browser;
        dir = System.getProperty("user.dir");
        if (dir != null) {
            currentDir = new File(dir);
        }
        initialize();
    }

    /**
     * Initializes the frame components.
     */
    private void initialize() {
        Rectangle           bounds = new Rectangle();
        Dimension           size;
        JSplitPane          horizontalSplitPane = new JSplitPane();
        JSplitPane          verticalSplitPane = new JSplitPane();
        GridBagConstraints  c;

        // Set title, size and menus
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("mibible browser");
        size = Toolkit.getDefaultToolkit().getScreenSize();
        bounds.width = (int) (size.width * 0.75);
        bounds.height = (int) (size.height * 0.75);
        bounds.x = (size.width - bounds.width) / 2;
        bounds.y = (size.height - bounds.height) / 2;
        setBounds(bounds);
        setJMenuBar(menuBar);
        initializeMenu();
        getContentPane().setLayout(new GridBagLayout());

        // Add horizontal split pane
        horizontalSplitPane.setDividerLocation((int) (bounds.width * 0.45));
        c = new GridBagConstraints();
        c.weightx = 1.0d;
        c.weighty = 1.0d;
        c.fill = GridBagConstraints.BOTH;
        getContentPane().add(horizontalSplitPane, c);

        // Add status label
        c = new GridBagConstraints();
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(2, 5, 2, 5);
        getContentPane().add(statusLabel, c);

        // Add MIB tree
        mibTree = MibTreeBuilder.getInstance().getTree();
        mibTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                updateTreeSelection();
            }
        });
        horizontalSplitPane.setLeftComponent(initializeTree());

        // Add description area & SNMP panel
        verticalSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        verticalSplitPane.setDividerLocation((int) (bounds.height * 0.40));
        verticalSplitPane.setOneTouchExpandable(true);
        descriptionArea.setEditable(false);
        verticalSplitPane.setLeftComponent(new JScrollPane(descriptionArea));
        snmpPanel = new SnmpPanel(this);
        verticalSplitPane.setRightComponent(snmpPanel);
        horizontalSplitPane.setRightComponent(verticalSplitPane);
    }

    /**
     * Creates and initializes the Tree Panel.
     *
     * @return the panel containing the Tree
     */
    private JPanel initializeTree()
    {
        JPanel  treePanel    = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        treePanel.setLayout(layout);
        GridBagConstraints  c;

        // Add Name Search field
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;
        c.insets = DEFAULT_INSETS;
        treePanel.add(nameSearchLabel, c);
        
        c = new GridBagConstraints();
        c.gridx = 2;
        c.weightx = 1.0d;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = DEFAULT_INSETS;
        treePanel.add(nameSearchField, c);
        
        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;
        c.insets = DEFAULT_INSETS;
        treePanel.add(nameSearchButton, c);

        // Add OID Search field
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.NONE;
        c.insets = DEFAULT_INSETS;
        treePanel.add(oidSearchLabel, c);
        
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 1.0d;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = DEFAULT_INSETS;
        treePanel.add(oidSearchField, c);
        
        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 2;
        c.fill = GridBagConstraints.NONE;
        c.insets = DEFAULT_INSETS;
        treePanel.add(oidSearchButton, c);
        
        // Add MIB tree
        mibTree = MibTreeBuilder.getInstance().getTree();
        mibTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                updateTreeSelection();
            }
        });
        JScrollPane treePane = new JScrollPane(mibTree);
        treePane.setPreferredSize(new Dimension(430, 633));

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 3;
        c.weightx = 1.0d;
        c.weighty = 1.0d;
        c.fill = GridBagConstraints.BOTH;
        c.insets = DEFAULT_INSETS;
        treePanel.add(treePane, c);        
        
        // Associate labels
        nameSearchLabel.setLabelFor(nameSearchField);
        oidSearchLabel.setLabelFor(oidSearchField);

        // Add DnD action listeners
        DropTarget dropTarget = new DropTarget();
        dropTarget.setComponent(mibTree);
        try {
            dropTarget.addDropTargetListener(new DropTargetListener() {
                public void dragEnter(DropTargetDragEvent e){} 
                public void dragExit(DropTargetEvent e){} 
                public void dragOver(DropTargetDragEvent e){}
                public void dropActionChanged(DropTargetDragEvent e){} 
                public void drop(DropTargetDropEvent e) {
                    try
                    {
                        Transferable transfer = e.getTransferable();
                        if (transfer.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
                        {
                            e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                            List fileList
                            = (List)(transfer.getTransferData(DataFlavor.javaFileListFlavor));
                            loadMib(fileList);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (TooManyListenersException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        // Add button action listeners
        nameSearchButton.setToolTipText("Search Name and Expand Tree");
        nameSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchName();
            }
        });
        
        oidSearchButton.setToolTipText("Search OID and Expand Tree");
        oidSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchOID();
            }
        });


        return treePanel;
    }
    
    /**
     * Initializes the frame menu.
     */
    private void initializeMenu() {
        JMenu              menu;
        JMenuItem          item;
        JCheckBoxMenuItem  checkBox;

        // Create file menu
        menu = new JMenu("File");
        item = new JMenuItem("Load MIB...");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadMib();
            }
        });
        menu.add(item);
        item = new JMenuItem("Unload MIB");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                unloadMib();
            }
        });
        menu.add(item);
        menu.addSeparator();
        item = new JMenuItem("Exit");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(item);
        menuBar.add(menu);

        // Create SNMP menu
        menu = new JMenu("SNMP");
        snmpV1Item.setSelected(true);
        snmpV1Item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setSnmpVersion(1);
            }
        });
        menu.add(snmpV1Item);
        snmpV2Item.setSelected(false);
        snmpV2Item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setSnmpVersion(2);
            }
        });
        menu.add(snmpV2Item);
        snmpV3Item.setSelected(false);
        snmpV3Item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setSnmpVersion(3);
            }
        });
        menu.add(snmpV3Item);
        menu.addSeparator();
        checkBox = new JCheckBoxMenuItem("Show result in tree");
        checkBox.setSelected(true);
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem  src = (JCheckBoxMenuItem) e.getSource();

                setSnmpFeedback(src.isSelected());
            }
        });
        menu.add(checkBox);
        menuBar.add(menu);

        // Create help menu
        menu = new JMenu("Help");
/**
        item = new JMenuItem("License...");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLicense();
            }
        });
        menu.add(item);
*/
        item = new JMenuItem("About...");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAbout();
            }
        });
        menu.add(item);
        menuBar.add(menu);
    }

    /**
     * Blocks or unblocks GUI operations in this frame. This method
     * is used when performing long-running operations to inactivate
     * the user interface.
     *
     * @param blocked        the blocked flag
     */
    public void setBlocked(boolean blocked) {
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            menuBar.getMenu(i).setEnabled(!blocked);
        }
        snmpPanel.setBlocked(blocked);
    }

    /**
     * Opens the load MIB files.
     * @param fileList MIB files.
     */
    protected void loadMib(List fileList) {
        Loader        loader;
        File[] files = (File[]) fileList.toArray(new File[fileList.size()]);

        if (files.length > 0) {
            currentDir = files[0].getParentFile();
            descriptionArea.setText("");
            loader = new Loader(files);
            loader.start();
        }
    }

    /**
     * Opens the load MIB dialog.
     */
    protected void loadMib() {
        JFileChooser  dialog = new JFileChooser();
        Loader        loader;
        File[]        files;
        int           result;

        dialog.setCurrentDirectory(currentDir);
        dialog.setMultiSelectionEnabled(true);
        result = dialog.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            files = dialog.getSelectedFiles();
            if (files.length > 0) {
                currentDir = files[0].getParentFile();
                descriptionArea.setText("");
                loader = new Loader(files);
                loader.start();
            }
        }
    }

    /**
     * Loads a MIB file from a specified source.
     *
     * @param src            the MIB file or URL
     */
    public void loadMib(String src) {
        ByteArrayOutputStream  output;
        String                 message = null;

        setStatus("Loading " + src + "...");
        try {
            browser.loadMib(src);
        } catch (FileNotFoundException e) {
            message = "Failed to load " + e.getMessage();
        } catch (IOException e) {
            message = "Failed to load " + src + ": " + e.getMessage();
        } catch (MibLoaderException e) {
            message = "Failed to load " + src;
            output = new ByteArrayOutputStream();
            e.getLog().printTo(new PrintStream(output));
            descriptionArea.append(output.toString());
        }
        if (message != null) {
            JOptionPane.showMessageDialog(this,
                                          message,
                                          "MIB Loading Error",
                                          JOptionPane.ERROR_MESSAGE);
        }
        setStatus(null);
    }

    /**
     * Unloads the MIB file from the currently selected symbol.
     */
    protected void unloadMib() {
        MibNode  node = getSelectedNode();

        if (node == null) {
            return;
        }
        while (node.getLevel() > 1) {
            node = (MibNode) node.getParent();
        }
        browser.unloadMib(node.getName());
        refreshTree();
    }

    /**
     * Refreshes the MIB tree.
     */
    public void refreshTree() {
        ((DefaultTreeModel) mibTree.getModel()).reload();
        mibTree.repaint();
    }

    /**
     * Sets the SNMP version to use.
     *
     * @param version        the new version number
     */
    public void setSnmpVersion(int version) {
        snmpV1Item.setSelected(false);
        snmpV2Item.setSelected(false);
        snmpV3Item.setSelected(false);
        if (version == 1) {
            snmpV1Item.setSelected(true);
        } else if (version == 2) {
            snmpV2Item.setSelected(true);
        } else if (version == 3) {
            snmpV3Item.setSelected(true);
        }
        snmpPanel.setVersion(version);
    }

    /**
     * Sets the SNMP feedback flag.
     *
     * @param feedback       the feedback flag
     */
    public void setSnmpFeedback(boolean feedback) {
        snmpPanel.setFeedback(feedback);
    }

    /**
     * Returns the currently selected MIB node.
     *
     * @return the currently selected MIB node, or
     *         null for none
     */
    public MibNode getSelectedNode() {
        return (MibNode) mibTree.getLastSelectedPathComponent();
    }

    /**
     * Sets the selected node based on the specified OID. The MIB
     * that will be searched is based on the currently selected MIB
     * node.
     *
     * @param oid            the OID to select
     */
    public void setSelectedNode(String oid) {
        MibNode         node = getSelectedNode();
        MibValueSymbol  symbol;
        TreePath        path;

        // Find matching symbol
        if (node == null || node.getSymbol() == null) {
            return;
        }
        symbol = node.getSymbol().getMib().getSymbolByOid(oid);
        if (symbol == null) {
            mibTree.clearSelection();
            return;
        }

        // Select tree node
        node = MibTreeBuilder.getInstance().getNode(symbol);
        path = new TreePath(node.getPath());
        mibTree.expandPath(path);
        mibTree.scrollPathToVisible(path);
        mibTree.setSelectionPath(path);
        mibTree.repaint();
    }

    /**
     * Sets the status label text.
     *
     * @param text           the status label text (or null)
     */
    public void setStatus(String text) {
        if (text != null) {
            statusLabel.setText(text);
        } else {
            statusLabel.setText("Ready");
        }
    }

    /**
     * Shows the about dialog.
     */
    protected void showAbout() {
        AboutDialog  dialog = new AboutDialog(this);

        dialog.setVisible(true);
    }

    /**
     * Shows the license dialog.
     */
    protected void showLicense() {
        LicenseDialog  dialog = new LicenseDialog(this);

        dialog.setVisible(true);
    }

    /**
     * Search Name and Expand Tree.
     */
    protected void searchName()
    {
        // Search tree node
        String name = nameSearchField.getText();
        MibNode searchedNode = searchMibNodeByName(name);

        // Select tree node
        if (searchedNode == null)
        {
            mibTree.clearSelection();
            return;
        }
        TreePath path = new TreePath(searchedNode.getPath());
        mibTree.expandPath(path);
        mibTree.scrollPathToVisible(path);
        mibTree.setSelectionPath(path);
        mibTree.repaint();
    }
    
    /**
     * Search OID and Expand Tree.
     */
    protected void searchOID()
    {
        // Search tree node
        String oid = oidSearchField.getText();
        MibNode searchedNode = searchMibNodeByOID(oid);

        // Select tree node
        if (searchedNode == null)
        {
            mibTree.clearSelection();
            return;
        }
        TreePath path = new TreePath(searchedNode.getPath());
        mibTree.expandPath(path);
        mibTree.scrollPathToVisible(path);
        mibTree.setSelectionPath(path);
        mibTree.repaint();
    }
    
    /**
     * Search MibNode by name.
     * 
     * @param name
     * @return Searched MibNode, or null if not found.
     */
    private MibNode searchMibNodeByName(String name)
    {
        MibNode         root = (MibNode) mibTree.getModel().getRoot();
        MibValueSymbol  symbol = null;

        Enumeration e = root.children();
        while (e.hasMoreElements())
        {
            MibNode mib = (MibNode)e.nextElement();
            MibNode real = getRealMibNode(mib);
            if (real == null)
            {
                continue;
            }
            symbol = (MibValueSymbol) real.getSymbol().getMib().getSymbol(name);
            if (symbol != null)
            {
                break;
            }
        }

        if (symbol == null)
        {
            return null;
        }

        // Select tree node
        MibNode searchedNode = MibTreeBuilder.getInstance().getNode(symbol);
        return searchedNode;
    }
    
    /**
     * Search MibNode by OID.
     * 
     * @param oid
     * @return Searched MibNode, or null if not found.
     */
    private MibNode searchMibNodeByOID(String oid)
    {
        MibNode         root = (MibNode) mibTree.getModel().getRoot();
        MibValueSymbol  symbol = null;

        Enumeration e = root.children();
        while (e.hasMoreElements())
        {
            MibNode mib = (MibNode)e.nextElement();
            MibNode real = getRealMibNode(mib);
            if (real == null)
            {
                continue;
            }
            symbol = real.getSymbol().getMib().getSymbolByOid(oid);
            if (symbol != null)
            {
                break;
            }
        }

        if (symbol == null)
        {
            return null;
        }

        // Select tree node
        MibNode searchedNode = MibTreeBuilder.getInstance().getNode(symbol);
        return searchedNode;
    }
    
    private MibNode getRealMibNode(MibNode node)
    {
        if (   node.getSymbol() != null
            && node.getSymbol().getMib() != null)
        {
            return node;
        }

        if (node.isLeaf() == true)
        {
            return null;
        }
        
        Enumeration e = node.children();
        while (e.hasMoreElements())
        {
            MibNode child = (MibNode)e.nextElement();
            MibNode realMib = getRealMibNode(child);
            if (realMib != null)
            {
                return realMib;
            }
        }
        return null;
    }

    /**
     * Updates the tree selection.
     */
    protected void updateTreeSelection() {
        MibNode  node = getSelectedNode();

        if (node == null) {
            descriptionArea.setText("");
        } else {
            descriptionArea.setText(node.getDescription());
            descriptionArea.setCaretPosition(0);
        }
        snmpPanel.updateOid();
    }


    /**
     * A background MIB loader. This class is needed in order to
     * implement the runnable interface to be able to load MIB files
     * in a background thread.
     */
    private class Loader implements Runnable {

        /**
         * The MIB files to load.
         */
        private File[] files;

        /**
         * Creates a new background MIB loader.
         *
         * @param files          the MIB files to load
         */
        public Loader(File[] files) {
            this.files = files;
        }

        /**
         * Starts the background loading thread.
         */
        public void start() {
            Thread  thread;

            if (files.length > 0) {
                thread = new Thread(this);
                thread.start();
            }
        }

        /**
         * Runs the MIB loading. This method should only be called by
         * the thread created through a call to start().
         */
        public void run() {
            setBlocked(true);
            for (int i = 0; i < files.length; i++) {
                loadMib(files[i].toString());
            }
            refreshTree();
            setBlocked(false);
        }
    }
}
