package com.googlecode.mibible.browser;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.MibValueSymbol;
import net.percederberg.mibble.value.ObjectIdentifierValue;

public class MibTreeNodeBuilder
{
    private Map<String, MibTreeNode> oidToMibTreeNode
        = new HashMap<String, MibTreeNode>();
    private Map<String, MibTreeNode> nameToMibTreeNode
        = new HashMap<String, MibTreeNode>();

    public MibTreeNodeBuilder(
            Map<String, MibTreeNode> oidToMibTreeNode,
            Map<String, MibTreeNode> nameToMibTreeNode)
    {
        this.oidToMibTreeNode = oidToMibTreeNode;
        this.nameToMibTreeNode = nameToMibTreeNode;
    }
    
    /**
     * Adds a MIB to the MIB tree.
     *
     * @param mib            the MIB to add
     */
    public MibTreeNode mib2node(Mib mib)
    {
        Iterator<MibSymbol> iter = mib.getAllSymbols().iterator();
        MibSymbol  symbol;
        MibTreeNode    root;
        List<MibInfo>  list = new ArrayList<MibInfo>();

        // Create value sub tree
        root = new MibTreeNode(mib.getName(), null);
        while (iter.hasNext()) {
            // valueTreeにMibSymbolを追加する
            symbol = iter.next();
            addSymbol(root, symbol, list);
        }
        
        // MIB情報を内部のテーブルに保存する
        MibInfoDao dao = MibInfoDao.getInstance();
        dao.deleteAll();
        dao.insert(mib.getName(), list);

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
    private void addSymbol(MibTreeNode root, MibSymbol symbol, List<MibInfo> list) {
        MibValue               value;
        ObjectIdentifierValue  oid;

        if (symbol instanceof MibValueSymbol) {
            value = ((MibValueSymbol) symbol).getValue();
            if (value instanceof ObjectIdentifierValue) {
                oid = (ObjectIdentifierValue) value;
                // OIDをTreeに追加
                addToTree(root, oid, list);
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
    private MibTreeNode addToTree(MibTreeNode root, ObjectIdentifierValue oid, List<MibInfo> list) {
        MibTreeNode  parent;
        MibTreeNode  node;
        String   name;

        // Add parent node to tree (if needed)
        if (hasParent(oid)) {
            parent = addToTree(root, oid.getParent(), list);
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
        name = oid.getName();
        node = new MibTreeNode(name, oid);
        parent.add(node);
        
        this.oidToMibTreeNode.put(oid.toString(), node);
        this.nameToMibTreeNode.put(name, node);
        list.add(new MibInfo(oid.toString(), name));
        
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
