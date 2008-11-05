package com.googlecode.mibible.browser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibLoader;
import net.percederberg.mibble.MibLoaderException;

public class Mediator
{
	private Map<String, MibTreeNode> oidToMibTreeNode
	    = new HashMap<String, MibTreeNode>();
	private Map<String, MibTreeNode> nameToMibTreeNode
    = new HashMap<String, MibTreeNode>();
	
    /** ステータスラベル */
    private JLabel statusLabel;
    /** Name Search field */
    private JTextField nameSearchField;
    /** OID Search field */
    private JTextField oidSearchField;
    /** MIB tree */
    private JTree mibTree;
	/** Description Area */
	private JTextArea descriptionArea;
	/**
	 * @param statusLabel the statusLabel to set
	 */
	public void setStatusLabel(JLabel statusLabel) {
		this.statusLabel = statusLabel;
		this.statusLabel.setText("Ready");
	}
	/**
	 * @param nameSearchField the nameSearchField to set
	 */
	public void setNameSearchField(JTextField nameSearchField) {
		this.nameSearchField = nameSearchField;
		this.nameSearchField.setText("");
	}
	/**
	 * @param oidSearchField the oidSearchField to set
	 */
	public void setOidSearchField(JTextField oidSearchField) {
		this.oidSearchField = oidSearchField;
		this.oidSearchField.setText("");
	}
	/**
	 * @param mibTree the mibTree to set
	 */
	public void setMibTree(JTree mibTree) {
		this.mibTree = mibTree;
	}
	/**
	 * @param descriptionArea the descriptionArea to set
	 */
	public void setDescriptionArea(JTextArea descriptionArea) {
		this.descriptionArea = descriptionArea;
		this.descriptionArea.setText("");
	}

	public void openMib(File file)
	{
        MibLoader loader = new MibLoader();
        loader.addDir(file.getParentFile());
        Mib mib;
        try {
			mib = loader.load(file);
			MibTreeNodeBuilder builder
			    = new MibTreeNodeBuilder(oidToMibTreeNode, nameToMibTreeNode);
			MibTreeNode mibroot = builder.mib2node(mib);
			MibTreeNode root = new MibTreeNode("mibible browser", null);
			root.add(mibroot);
			DefaultTreeModel model = new DefaultTreeModel(root);
			this.mibTree.setModel(model);
			model.reload();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (MibLoaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
	public void openMib(List<File> files)
	{
        File[] list = (File[]) files.toArray(new File[files.size()]);
		openMib(list);
	}
	public void openMib(File[] files)
	{
		for (File file : files)
		{
			openMib(file);
		}
	}
	public void searchNodeByOid()
	{
		String condition = this.oidSearchField.getText();
		MibTreeNode node = this.oidToMibTreeNode.get(condition);
		expandTree(node);
	}
	public void searchNodeByName()
	{
		String condition = this.nameSearchField.getText();
		MibTreeNode node = this.nameToMibTreeNode.get(condition);
		expandTree(node);
	}
	private void expandTree(MibTreeNode node)
	{
        if (node == null)
        {
            this.mibTree.clearSelection();
            return;
        }

        TreePath path = new TreePath(node.getPath());
        this.mibTree.expandPath(path);
        this.mibTree.scrollPathToVisible(path);
        this.mibTree.setSelectionPath(path);
        this.mibTree.repaint();
	}
	
	public void updateTreeSelection()
	{
		MibTreeNode node = (MibTreeNode) this.mibTree.getLastSelectedPathComponent();
        if (node == null)
        {
            this.descriptionArea.setText("");
        } else {
        	this.descriptionArea.setText(node.getDescription());
        	this.descriptionArea.setCaretPosition(0);
        }
//        communicationPanel.updateOid();
	}
	
}
