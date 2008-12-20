package com.googlecode.mibible.printer.filter;

import net.percederberg.mibble.value.ObjectIdentifierValue;

/**
 * OIDを文字列表現と数値表現混在の形式に整形するクラス。</br>
 * 整形例</br>
 * OIDにIF-MIBのifOperStatusを指定した場合、"%TN"という文字列を
 * 以下のフォーマットに整形する。</br>
 * iso(1).org(3).dod(6).internet(1).mgmt(2).mib-2(1).interfaces(2).ifTable(2).ifEnt
ry(1).ifOperStatus(8)
 * 
 * @author snuffkin
 * @since 0.2.0
 */
public class TextNumFilter extends PrintFilter {
    
    /**
     * コンストラクタ。
     */
    public TextNumFilter() {
        super("%TN");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrintString(ObjectIdentifierValue oid) {
        return oid.toDetailString();
    }
}
