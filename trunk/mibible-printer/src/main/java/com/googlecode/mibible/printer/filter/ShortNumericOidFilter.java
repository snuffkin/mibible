package com.googlecode.mibible.printer.filter;

import net.percederberg.mibble.value.ObjectIdentifierValue;

/**
 * OIDをshort textual formに整形するクラス。</br>
 * 整形例</br>
 * OIDにIF-MIBのifOperStatusを指定した場合、"%num"という文字列を
 * 以下のフォーマットに整形する。</br>
 * 8
 * 
 * @author snuffkin
 * @since 0.2.0
 */
public class ShortNumericOidFilter extends PrintFilter {
	
    /**
     * コンストラクタ。
     */
    public ShortNumericOidFilter() {
        super("%num");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrintString(ObjectIdentifierValue oid) {
        return String.valueOf(oid.getValue());
    }
}
