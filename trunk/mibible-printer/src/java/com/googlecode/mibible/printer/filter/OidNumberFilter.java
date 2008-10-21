package com.googlecode.mibible.printer.filter;

import net.percederberg.mibble.value.ObjectIdentifierValue;

public class OidNumberFilter extends PrintFilter
{
	public OidNumberFilter()
	{
		super("%oidnumber");
	}
	
	@Override
	public String getPrintString(ObjectIdentifierValue oid)
	{
		ObjectIdentifierValue tmpOid = oid;
		String ret = "";
		while (tmpOid != null)
		{
			if (ret.isEmpty())
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
