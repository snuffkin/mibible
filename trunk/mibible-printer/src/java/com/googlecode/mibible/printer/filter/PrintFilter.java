package com.googlecode.mibible.printer.filter;

import net.percederberg.mibble.value.ObjectIdentifierValue;

/**
 * MIB情報を出力するときにフォーマットを整形するパターンの基底クラス。
 * 
 * @author snuffkin
 * @since 0.2.0
 */
public abstract class PrintFilter
{
    /** 本クラスでフォーマットを整形する部分を指定するためのキー文字列 */
    private final String filterKey;
    
    /**
     * 整形する部分を指定するキー文字列を指定して、初期化する。
     * @param filterKey キー文字列
     */
    protected PrintFilter(String filterKey)
    {
        this.filterKey = filterKey;
    }
    
    /**
     * 整形する部分を指定するためのキー文字列を返す。
     * @return 整形する部分を指定するためのキー文字列
     */
    public String getFilterKey()
    {
        return this.filterKey;
    }
    
    /**
     * 指定したOIDを本クラスを利用して整形する。</br>
     * 継承先のクラスで実装すること。
     * @param oid 整形するOID
     * @return　整形後の文字列
     */
    public abstract String getPrintString(ObjectIdentifierValue oid);
}
