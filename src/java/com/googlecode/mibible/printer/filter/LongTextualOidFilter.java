package com.googlecode.mibible.printer.filter;

import net.percederberg.mibble.value.ObjectIdentifierValue;

/**
 * OIDをlong textual formに整形するクラス。</br>
 * 整形例</br>
 * OIDにIF-MIBのifOperStatusを指定した場合、"%Text"という文字列を
 * 以下のフォーマットに整形する。</br>
 * iso.org.dod.internet.mgmt.mib-2.interfaces.ifTable.ifEntry.ifOperStatus
 * 
 * @author snuffkin
 * @since 0.2.0
 */
public class LongTextualOidFilter extends PrintFilter
{
    /**
     * コンストラクタ。
     */
    public LongTextualOidFilter()
    {
        super("%Text");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrintString(ObjectIdentifierValue oid)
    {
        ObjectIdentifierValue tmpOid = oid;
        String ret = "";
        
        // 末端からから上位の順に、textual formを作成する
        while (tmpOid != null)
        {
            if (ret.isEmpty())
            {
                ret = tmpOid.getName();
            }
            else
            {
                ret = tmpOid.getName() + "." + ret;
            }
            tmpOid = tmpOid.getParent();
        }
        
        return ret;
    }
}
