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
			ret = "." + tmpOid.getName() + ret;
			tmpOid = tmpOid.getParent();
		}
		
		return ret;
	}
}
