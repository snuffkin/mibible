package com.googlecode.mibible.browser;

import java.awt.Color;
import java.awt.Component;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibType;
import net.percederberg.mibble.snmp.SnmpAccess;
import net.percederberg.mibble.snmp.SnmpNotificationGroup;
import net.percederberg.mibble.snmp.SnmpNotificationType;
import net.percederberg.mibble.snmp.SnmpObjectGroup;
import net.percederberg.mibble.snmp.SnmpObjectType;
import net.percederberg.mibble.type.SequenceOfType;
import net.percederberg.mibble.type.SequenceType;

/**
 * TreeCellRenderer for MIB Tree
 */
public class MibTreeCellRenderer extends DefaultTreeCellRenderer
{
    /** Last tree the renderer was painted in. */
    // TODO is this trash code?
    private JTree tree;
    
    // TODO what is isDropCell?
//    private boolean isDropCell;
    
    private static Icon mibIcon_NotificationGroup;
    private static Icon mibIcon_NotificationType;
    private static Icon mibIcon_ObjectGroup;
    private static Icon mibIcon_ObjectType;
    private static Icon mibIcon_ObjectType_na;
    private static Icon mibIcon_ObjectType_rc;
    private static Icon mibIcon_ObjectType_ro;
    private static Icon mibIcon_ObjectType_rw;
    private static Icon mibIcon_ObjectType_SequenceOfType;
    private static Icon mibIcon_ObjectType_SequenceType;

    static
    {
        URL url;
        
        url = MibTreeCellRenderer.class.getResource("SnmpNotificationGroup.gif");
        if (url != null)
        {
            mibIcon_NotificationGroup = new ImageIcon(url);
        }
        
        url = MibTreeCellRenderer.class.getResource("SnmpNotificationType.gif");
        if (url != null)
        {
            mibIcon_NotificationType = new ImageIcon(url);
        }
        
        url = MibTreeCellRenderer.class.getResource("SnmpObjectGroup.gif");
        if (url != null)
        {
            mibIcon_ObjectGroup = new ImageIcon(url);
        }
        
        url = MibTreeCellRenderer.class.getResource("SnmpObjectType.gif");
        if (url != null)
        {
            mibIcon_ObjectType = new ImageIcon(url);
        }
        
        url = MibTreeCellRenderer.class.getResource("SnmpObjectType_na.gif");
        if (url != null)
        {
            mibIcon_ObjectType_na = new ImageIcon(url);
        }
        
        url = MibTreeCellRenderer.class.getResource("SnmpObjectType_rc.gif");
        if (url != null)
        {
            mibIcon_ObjectType_rc = new ImageIcon(url);
        }
        
        url = MibTreeCellRenderer.class.getResource("SnmpObjectType_ro.gif");
        if (url != null)
        {
            mibIcon_ObjectType_ro = new ImageIcon(url);
        }
        
        url = MibTreeCellRenderer.class.getResource("SnmpObjectType_rw.gif");
        if (url != null)
        {
            mibIcon_ObjectType_rw = new ImageIcon(url);
        }
        
        url = MibTreeCellRenderer.class.getResource("SnmpObjectType_SequenceOfType.gif");
        if (url != null)
        {
            mibIcon_ObjectType_SequenceOfType = new ImageIcon(url);
        }
        
        url = MibTreeCellRenderer.class.getResource("SnmpObjectType_SequenceType.gif");
        if (url != null)
        {
            mibIcon_ObjectType_SequenceType = new ImageIcon(url);
        }
    }

    /**
     * Configures the renderer based on the passed in components.
     * The value is set from messaging the tree with
     * <code>convertValueToText</code>, which ultimately invokes
     * <code>toString</code> on <code>value</code>.
     * The foreground color is set based on the selection and the icon
     * is set based on the <code>leaf</code> and <code>expanded</code>
     * parameters.
     */
   public Component getTreeCellRendererComponent(JTree tree, Object value,
                          boolean sel,
                          boolean expanded,
                          boolean leaf, int row,
                          boolean hasFocus) {
    String         stringValue = tree.convertValueToText(value, sel,
                      expanded, leaf, row, hasFocus);

       this.tree = tree;
    this.hasFocus = hasFocus;
    setText(stringValue);

       Color fg = null;
/**       isDropCell = false;

       JTree.DropLocation dropLocation = tree.getDropLocation();
       if (dropLocation != null
               && dropLocation.getChildIndex() == -1
               && tree.getRowForPath(dropLocation.getPath()) == row) {

           Color col = UIManager.getColor("Tree.dropCellForeground");
           if (col != null) {
               fg = col;
           } else {
               fg = getTextSelectionColor();
           }

           isDropCell = true;
       } else */if (sel) {
           fg = getTextSelectionColor();
       } else {
           fg = getTextNonSelectionColor();
       }

       setForeground(fg);

    // There needs to be a way to specify disabled icons.
    if (!tree.isEnabled()) {
        setEnabled(false);
        if (leaf) {
        setDisabledIcon(convertMibNodeToIcon((MibNode)value, getDefaultLeafIcon()));
//        setDisabledIcon(getLeafIcon());
        } else if (expanded) {
        	setDisabledIcon(convertMibNodeToIcon((MibNode)value, getOpenIcon()));
//        setDisabledIcon(getOpenIcon());
        } else {
        	setDisabledIcon(convertMibNodeToIcon((MibNode)value, getClosedIcon()));
//        setDisabledIcon(getClosedIcon());
        }
    }
    else {
        setEnabled(true);
        if (leaf) {
        setIcon(convertMibNodeToIcon((MibNode)value, getDefaultLeafIcon()));
//        setIcon(getLeafIcon());
        } else if (expanded) {
        setIcon(convertMibNodeToIcon((MibNode)value, getOpenIcon()));
//        setIcon(getOpenIcon());
        } else {
            setIcon(convertMibNodeToIcon((MibNode)value, getClosedIcon()));
//        setIcon(getClosedIcon());
        }
    }
       setComponentOrientation(tree.getComponentOrientation());
        
    selected = sel;

    return this;
   }
    
    /**
     * convert MibNode To Tree Icon for Rendering.
     * 
     * @param node MibNode
     * @return Icon
     */
    private Icon convertMibNodeToIcon(MibNode node, Icon defaultIcon)
    {
        if (node == null)
        {
            return defaultIcon;
        }
        
        // MibSymbolがnullの場合はDefaultのLeafIconを利用する
        MibSymbol symbol = node.getSymbol();
        if (symbol == null)
        {
            return defaultIcon;
        }

        // MibTypeがnullの場合はDefaultのLeafIconを利用する
        MibType type = node.getSymbol().getType();
        if (type == null)
        {
            return defaultIcon;
        }
        
        if (type instanceof SnmpNotificationType)
        {
            // NotificationType
            return mibIcon_NotificationType;
        }
        else if (type instanceof SnmpObjectType)
        {
            // ObjectType
            SnmpObjectType objectType = (SnmpObjectType) type;
            SnmpAccess access = objectType.getAccess();
            MibType syntax = objectType.getSyntax();
            
             // SequenceType、SequenceOfTypeはsyntaxでアイコンを変化させる
            if (syntax instanceof SequenceType)
            {
                // SequenceType
                 return mibIcon_ObjectType_SequenceType;
            }
            else if (syntax instanceof SequenceOfType)
            {
                // SequenceOfType
                 return mibIcon_ObjectType_SequenceOfType;
            }
            
            // ObjectTypeはaccessibleでアイコンを変化させる
            if (access == SnmpAccess.NOT_ACCESSIBLE)
            {
                // not-accessible
                return mibIcon_ObjectType_na;
            }
            else if (access == SnmpAccess.READ_CREATE)
            {
                // read-create
                return mibIcon_ObjectType_rc;
            }
            else if (access == SnmpAccess.READ_ONLY)
            {
                // read-only
                return mibIcon_ObjectType_ro;
            }
            else if (access == SnmpAccess.READ_WRITE)
            {
                // read-write
                return mibIcon_ObjectType_rw;
            }
            else
            {
                // default
               return mibIcon_ObjectType;
            }
        }
        else if (type instanceof SnmpNotificationGroup)
        {
            // SnmpNotificationGroup
             return mibIcon_NotificationGroup;
        }
        else if (type instanceof SnmpObjectGroup)
        {
            // SnmpObjectGroup
             return mibIcon_ObjectGroup;
        }
        else
        {
            // Other
            return defaultIcon;
        }
    }
}
