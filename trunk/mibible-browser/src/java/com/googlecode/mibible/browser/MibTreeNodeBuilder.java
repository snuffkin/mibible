package com.googlecode.mibible.browser;

import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.JTree;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.MibValueSymbol;
import net.percederberg.mibble.value.ObjectIdentifierValue;

public class MibTreeNodeBuilder
{
    /**
     * Adds a MIB to the MIB tree.
     *
     * @param mib            the MIB to add
     */
    public MibTreeNode mib2node(Mib mib)
    {
        Iterator   iter = mib.getAllSymbols().iterator();
        MibSymbol  symbol;
        MibTreeNode    root;
        MibTreeNode    node;
        JTree      valueTree;

        // Create value sub tree
        node = new MibTreeNode("VALUES", null);
        while (iter.hasNext()) {
        	// valueTreeにMibSymbolを追加する
            symbol = (MibSymbol) iter.next();
            addSymbol(node, symbol);
        }

        // TODO: create TYPES sub tree

        // Add sub tree root to MIB tree
	    root = new MibTreeNode(mib.getName(), null);
	    root.add(node);
        
        return root;
    }

    /**
     * Adds a MIB symbol to a MIB tree model.
     *
     * @param model          the MIB tree model
     * @param symbol         the MIB symbol
     *
     * @see #addToTree
     */
    private void addSymbol(MibTreeNode root, MibSymbol symbol) {
        MibValue               value;
        ObjectIdentifierValue  oid;

        if (symbol instanceof MibValueSymbol) {
            value = ((MibValueSymbol) symbol).getValue();
            if (value instanceof ObjectIdentifierValue) {
                oid = (ObjectIdentifierValue) value;
                // OIDをTreeについか
                addToTree(root, oid);
            }
        }
    }

    /**
     * Adds an object identifier value to a MIB tree model.
     *
     * @param model          the MIB tree model
     * @param oid            the object identifier value
     *
     * @return the MIB tree node added
     */
    private MibTreeNode addToTree(MibTreeNode root, ObjectIdentifierValue oid) {
    	MibTreeNode  parent;
    	MibTreeNode  node;
        String   name;

        // Add parent node to tree (if needed)
        if (hasParent(oid)) {
            parent = addToTree(root, oid.getParent());
        } else {
            parent = root;
        }

        // Check if node already added
        Enumeration<MibTreeNode> enumeration = parent.children();
        while (enumeration.hasMoreElements())
        {
            node = enumeration.nextElement();
            if (node.getValue().equals(oid)) {
                return node;
            }
        }

        // Create new node
        name = oid.getName() + " (" + oid.getValue() + ")";
        node = new MibTreeNode(name, oid);
        parent.add(node);
        return node;
    }

    /**
     * Checks if the specified object identifier has a parent.
     *
     * @param oid            the object identifier to check
     *
     * @return true if the object identifier has a parent, or
     *         false otherwise
     */
    private boolean hasParent(ObjectIdentifierValue oid) {
        ObjectIdentifierValue  parent = oid.getParent();

        return oid.getSymbol() != null
            && oid.getSymbol().getMib() != null
            && parent != null
            && parent.getSymbol() != null
            && parent.getSymbol().getMib() != null
            && parent.getSymbol().getMib().equals(oid.getSymbol().getMib());
    }
}
