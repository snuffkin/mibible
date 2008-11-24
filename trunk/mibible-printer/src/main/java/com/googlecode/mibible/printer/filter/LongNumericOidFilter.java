package com.googlecode.mibible.printer.filter;

import net.percederberg.mibble.value.ObjectIdentifierValue;

/**
 * OIDをlong numeric formに整形するクラス。</br>
 * 整形例</br>
 * OIDにIF-MIBのifOperStatusを指定した場合、"%Num"という文字列を
 * 以下のフォーマットに整形する。</br>
 * 1.3.6.1.2.1.2.2.1.8
 * 
 * @author snuffkin
 * @since 0.2.0
 */
public class LongNumericOidFilter extends PrintFilter
{
    /**
     * コンストラクタ。
     */
    public LongNumericOidFilter()
    {
        super("%Num");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrintString(ObjectIdentifierValue oid)
    {
        ObjectIdentifierValue tmpOid = oid;
        String ret = "";
        
        // 末端からから上位の順に、numeric formを作成する
        while (tmpOid != null)
        {
            if (ret.equals(""))
            {
                ret = String.valueOf(tmpOid.getValue());
            }
            else
            {
                ret = tmpOid.getValue() + "." + ret;
            }
            tmpOid = tmpOid.getParent();
        }
        
        return ret;
    }
}
