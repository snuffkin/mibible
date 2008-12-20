package com.googlecode.mibible.printer.filter;

import net.percederberg.mibble.value.ObjectIdentifierValue;

/**
 * OIDをshort textual formに整形するクラス。</br>
 * 整形例</br>
 * OIDにIF-MIBのifOperStatusを指定した場合、"%text"という文字列を
 * 以下のフォーマットに整形する。</br>
 * ifOperStatus
 * 
 * @author snuffkin
 * @since 0.2.0
 */
public class ShortTextualOidFilter extends PrintFilter {
	
    /**
     * コンストラクタ。
     */
    public ShortTextualOidFilter() {
        super("%text");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrintString(ObjectIdentifierValue oid) {
        return oid.getName();
    }
}
