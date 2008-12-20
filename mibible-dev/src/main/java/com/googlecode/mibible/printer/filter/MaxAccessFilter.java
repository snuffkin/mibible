package com.googlecode.mibible.printer.filter;

import net.percederberg.mibble.MibType;
import net.percederberg.mibble.snmp.SnmpAccess;
import net.percederberg.mibble.snmp.SnmpObjectType;
import net.percederberg.mibble.value.ObjectIdentifierValue;

/**
 * OIDをMAX-ACCESSに整形するクラス。</br>
 * 対象が"OBJECT-TYPE"でない場合は空文字列を返す。</br>
 * 整形例</br>
 * OIDにIF-MIBのifOperStatusを指定した場合、"%access"という文字列を
 * 以下のフォーマットに整形する。</br>
 * read-only
 * 
 * @author snuffkin
 * @since 0.3.0
 */
public class MaxAccessFilter extends PrintFilter {
    
    /**
     * コンストラクタ。
     */
    public MaxAccessFilter() {
        super("%access");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrintString(ObjectIdentifierValue oid) {
        MibType type = oid.getSymbol().getType();
        if (type instanceof SnmpObjectType) {
            SnmpObjectType objType = (SnmpObjectType) type;
            SnmpAccess access = objType.getAccess();
            
            if (access == SnmpAccess.READ_ONLY) {
                return "read-only";
            } else if (access == SnmpAccess.READ_WRITE) {
                return "read-write";
            } else if (access == SnmpAccess.READ_CREATE) {
                return "read-create";
            } else if (access == SnmpAccess.WRITE_ONLY) {
                return "write-only";
            } else if (access == SnmpAccess.NOT_IMPLEMENTED) {
                return "not-implemented";
            } else if (access == SnmpAccess.NOT_ACCESSIBLE) {
                return "not-accessible";
            } else if (access == SnmpAccess.ACCESSIBLE_FOR_NOTIFY) {
                return "accessible-for-notify";
            }
        }
        return "";
    }
}
