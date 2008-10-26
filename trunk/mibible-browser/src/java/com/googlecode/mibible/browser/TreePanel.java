package com.googlecode.mibible.browser;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.googlecode.mibible.browserold.MibTreeBuilder;

public class TreePanel extends JPanel
{
    /** MIB tree */
    private JTree mibTree = null;

    /** Name Search label */
    private JLabel nameSearchLabel = new JLabel("Name:"); 
    /** Name Search field */
    private JTextField nameSearchField = new JTextField("", 30);
    /** Name Search button */
    private JButton nameSearchButton = new JButton("Search");

    /** OID Search label */
    private JLabel oidSearchLabel = new JLabel("OID:"); 
    /** OID Search field */
    private JTextField oidSearchField = new JTextField("", 30);
    /** OID Search button */
    private JButton oidSearchButton = new JButton("Search");
	
	
    public void initialize()
    {
        this.setLayout(new GridBagLayout());
        GridBagConstraints  c;

        // Add Name Search label
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;
        c.insets = BrowserFrame.DEFAULT_INSETS;
        this.add(nameSearchLabel, c);
        
        // Add Name Search field
        c = new GridBagConstraints();
        c.gridx = 2;
        c.weightx = 1.0d;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = BrowserFrame.DEFAULT_INSETS;
        this.add(nameSearchField, c);
        
        // Add Name Search button
        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;
        c.insets = BrowserFrame.DEFAULT_INSETS;
        this.add(nameSearchButton, c);

        // Add OID Search label
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.NONE;
        c.insets = BrowserFrame.DEFAULT_INSETS;
        this.add(oidSearchLabel, c);
        
        // Add OID Search field
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 1.0d;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = BrowserFrame.DEFAULT_INSETS;
        this.add(oidSearchField, c);
        
        // Add OID Search button
        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 2;
        c.fill = GridBagConstraints.NONE;
        c.insets = BrowserFrame.DEFAULT_INSETS;
        this.add(oidSearchButton, c);
        
        // Add MIB tree
        mibTree = MibTreeBuilder.getInstance().getTree();
        mibTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
            	// TODO
//                updateTreeSelection();
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
        c.insets = BrowserFrame.DEFAULT_INSETS;
        this.add(treePane, c);        
        
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
                            // TODO
//                            loadMib(fileList);
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
            	// TODO
//                searchName();
            }
        });
        
        oidSearchButton.setToolTipText("Search OID and Expand Tree");
        oidSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// TODO
//                searchOID();
            }
        });

        // TODO
//        return treePanel;
    }
}
