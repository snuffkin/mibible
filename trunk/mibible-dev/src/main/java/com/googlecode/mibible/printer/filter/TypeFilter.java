package com.googlecode.mibible.printer.filter;

import net.percederberg.mibble.value.ObjectIdentifierValue;

/**
 * OIDを型情報に整形するクラス。</br>
 * 整形例</br>
 * OIDにIF-MIBのifOperStatusを指定した場合、"%type"という文字列を
 * 以下のフォーマットに整形する。</br>
 * OBJECT-TYPE
 * 
 * @author snuffkin
 * @since 0.2.0
 */
public class TypeFilter extends PrintFilter {
    
    /**
     * コンストラクタ。
     */
    public TypeFilter() {
        super("%type");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrintString(ObjectIdentifierValue oid) {
        return oid.getSymbol().getType().getName();
    }
}
