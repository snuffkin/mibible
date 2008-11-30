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
import java.io.File;
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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

public class TreePanel extends JPanel
{
	/** Mediator */
	private Mediator mediator;
	
    public TreePanel(Mediator mediator)
    {
    	this.mediator = mediator;
    }
    public void initialize()
    {
        // レイアウトを設定する
        this.setLayout(new GridBagLayout());

        // レイアウト設定用クラス
        GridBagConstraints gbc;

        // Add Name Search label
        JLabel nameSearchLabel = new JLabel("Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = BrowserFrame.DEFAULT_INSETS;
        this.add(nameSearchLabel, gbc);
        
        // Add Name Search field
        JTextField nameSearchField = new JTextField(30);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.weightx = 1.0d;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = BrowserFrame.DEFAULT_INSETS;
        this.add(nameSearchField, gbc);
        
        // Add Name Search button
        JButton nameSearchButton = new JButton("Search");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = BrowserFrame.DEFAULT_INSETS;
        this.add(nameSearchButton, gbc);

        // Add OID Search label
        JLabel oidSearchLabel = new JLabel("OID:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = BrowserFrame.DEFAULT_INSETS;
        this.add(oidSearchLabel, gbc);
        
        // Add OID Search field
        JTextField oidSearchField = new JTextField(30);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 1.0d;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = BrowserFrame.DEFAULT_INSETS;
        this.add(oidSearchField, gbc);
        
        // Add OID Search button
        JButton oidSearchButton = new JButton("Search");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = BrowserFrame.DEFAULT_INSETS;
        this.add(oidSearchButton, gbc);
        
        // Add MIB tree
//        JTree mibTree = new JTree(new Object[0]);
        JTree mibTree = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode()));
//        JTree mibTree = new JTree();
        // TODO
        mibTree.setToolTipText("");
        mibTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        mibTree.setRootVisible(false);
        mibTree.setCellRenderer(new MibTreeCellRenderer(this.mediator.getHolder()));

        mibTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
            	TreePanel.this.mediator.updateTreeSelection();
            }
        });
        JScrollPane treePane = new JScrollPane(mibTree);
        treePane.setPreferredSize(new Dimension(430, 633));

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0d;
        gbc.weighty = 1.0d;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = BrowserFrame.DEFAULT_INSETS;
        this.add(treePane, gbc);        
        
        // Associate labels
        nameSearchLabel.setLabelFor(nameSearchField);
        oidSearchLabel.setLabelFor(oidSearchField);

        // Add DnD action listeners
        DropTarget dropTarget = new DropTarget();
        dropTarget.setComponent(mibTree);
        try
        {
            dropTarget.addDropTargetListener(new DropTargetListener()
            {
                public void dragEnter(DropTargetDragEvent e){} 
                public void dragExit(DropTargetEvent e){} 
                public void dragOver(DropTargetDragEvent e){}
                public void dropActionChanged(DropTargetDragEvent e){} 
                public void drop(DropTargetDropEvent e)
                {
                    try
                    {
                        Transferable transfer = e.getTransferable();
                        if (transfer.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
                        {
                            e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                            List<File> files
                            = (List<File>)(transfer.getTransferData(DataFlavor.javaFileListFlavor));
                            openMib(files);
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });
        }
        catch (TooManyListenersException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        // Add button action listeners
        nameSearchButton.setToolTipText("Search Name and Expand Tree");
        nameSearchButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	// TODO 使いわけ
//            	TreePanel.this.mediator.searchNodeByName();
            	TreePanel.this.mediator.searchNodeByUpperName();
            }
        });
        
        oidSearchButton.setToolTipText("Search OID and Expand Tree");
        oidSearchButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	TreePanel.this.mediator.searchNodeByOid();
            }
        });

        // Mediatorにコンポーネントを設定する
        this.mediator.setNameSearchField(nameSearchField);
        this.mediator.setOidSearchField(oidSearchField);
        this.mediator.setMibTree(mibTree);
    }
    
    private void openMib(List<File> files)
    {
    	this.mediator.openMib(files);
    }
}
