package com.googlecode.mibible.browser;

import java.io.File;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;

public class Mediator
{
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
		String filenames = this.descriptionArea.getText() + "\n" + file.getAbsolutePath();
		this.descriptionArea.setText(filenames);
	}
	public void openMib(List<File> files)
	{
		for (File file : files)
		{
			openMib(file);
		}
	}
}
