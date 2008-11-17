package com.googlecode.mibible.browser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
	private static final String MIBIBLE_HOME = System.getenv("MIBIBLE_HOME");
	
	public static final String FILE_CHOOSER_DIRECTORY = "fileChooserDirectory";
	private static final String PROP_FILE = MIBIBLE_HOME + "/conf/mibbrowser.properties";
	private Properties prop;

	private Map<String, MibTreeNode> oidToMibTreeNode
	    = new HashMap<String, MibTreeNode>();
	private Map<String, MibTreeNode> nameToMibTreeNode
        = new HashMap<String, MibTreeNode>();
	private NoticeNodeHolder holder = new NoticeNodeHolder();

	/** Status Label */
    private JLabel statusLabel;
    /** Name Search field */
    private JTextField nameSearchField;
    /** OID Search field */
    private JTextField oidSearchField;
    /** MIB tree */
    private JTree mibTree;
	/** Description Area */
	private JTextArea descriptionArea;
	
	public Mediator()
	{
        this.prop = new Properties();
		try {
			this.prop.load(new FileInputStream(PROP_FILE));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return
	 */
    public NoticeNodeHolder getHolder() {
		return holder;
	}
	/**
	 * @param statusLabel the statusLabel to set
	 */
	public void setStatusLabel(JLabel statusLabel) {
		this.statusLabel = statusLabel;
		this.statusLabel.setText("Ready.");
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
	public void unloadMib()
	{
		// TODO　複数MIB表示に対応する必要あり
		// 内部のデータ構造をクリアする
		this.oidToMibTreeNode.clear();
		this.nameToMibTreeNode.clear();
		this.holder.clear();
		MibInfoDao.getInstance().deleteAll();
		
		// ツリー表示をクリアする
		MibTreeNode root = new MibTreeNode("mibible browser", null);
		DefaultTreeModel model = new DefaultTreeModel(root);
		this.mibTree.setModel(model);
		model.reload();
	}
	
	public void searchNodeByOid()
	{
		this.holder.clear();
		String condition = this.oidSearchField.getText();
		List<MibInfo> list = MibInfoDao.getInstance().selectByOid(condition);
		if (list.size() == 0)
		{
			this.oidSearchField.grabFocus();
			this.oidSearchField.setCaretPosition(0);
			this.statusLabel.setText("No item hits.");
		}
		
		boolean isFirst = true;
		for (MibInfo info : list)
		{
			String oid =info.getOid();
			MibTreeNode node = this.oidToMibTreeNode.get(oid);
			this.holder.addNode(node);
			if (isFirst)
			{
				expandTree(node, true);
				this.statusLabel.setText(node.getName() + " " + node.getOid());
				isFirst = false;
			}
			else
			{
				expandTree(node, false);
			}
		}
		this.mibTree.repaint();
	}
	public void searchNodeByName()
	{
		this.holder.clear();
		String condition = this.nameSearchField.getText();
		List<MibInfo> list = MibInfoDao.getInstance().selectByName(condition);
		if (list.size() == 0)
		{
			this.nameSearchField.grabFocus();
			this.nameSearchField.setCaretPosition(0);
			this.statusLabel.setText("No item hits.");
		}
		
		boolean isFirst = true;
		for (MibInfo info : list)
		{
			String oid =info.getOid();
			MibTreeNode node = this.oidToMibTreeNode.get(oid);
			this.holder.addNode(node);
			if (isFirst)
			{
				expandTree(node, true);
				this.statusLabel.setText(node.getName() + " " + node.getOid());
				isFirst = false;
			}
			else
			{
				expandTree(node, false);
			}
		}
		this.mibTree.repaint();
	}
	
	private void expandTree(MibTreeNode node, boolean isSelection)
	{
        if (node == null)
        {
            this.mibTree.clearSelection();
            return;
        }

        TreePath path = new TreePath(node.getPath());
        this.mibTree.expandPath(path);
        if (isSelection)
         {
            this.mibTree.scrollPathToVisible(path);
            this.mibTree.setSelectionPath(path);
            this.mibTree.grabFocus();
        }
        this.mibTree.repaint();
	}
	
	public void updateTreeSelection()
	{
		MibTreeNode node = (MibTreeNode) this.mibTree.getLastSelectedPathComponent();
        if (node == null)
        {
            this.descriptionArea.setText("");
            this.statusLabel.setText("");
        }
        else
        {
        	this.descriptionArea.setText(node.getDescription());
        	this.descriptionArea.setCaretPosition(0);
            this.statusLabel.setText(node.getName() + " " + node.getOid());
        }
        // TODO
//        communicationPanel.updateOid();
	}
	
	public Properties getProperties()
	{
		return this.prop;
	}
	
	public void exit()
	{
		try {
			this.prop.store(new FileOutputStream(PROP_FILE), "");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
	
}
