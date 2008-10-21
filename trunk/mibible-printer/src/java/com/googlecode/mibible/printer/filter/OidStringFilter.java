package com.googlecode.mibible.printer.filter;

import net.percederberg.mibble.value.ObjectIdentifierValue;

public class OidStringFilter extends PrintFilter
{
	public OidStringFilter()
	{
		super("%oidstring");
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
