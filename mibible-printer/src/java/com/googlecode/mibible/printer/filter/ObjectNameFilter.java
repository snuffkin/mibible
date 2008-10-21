package com.googlecode.mibible.printer.filter;

import net.percederberg.mibble.value.ObjectIdentifierValue;

public class ObjectNameFilter extends PrintFilter
{
	public ObjectNameFilter()
	{
		super("%name");
	}
	
	@Override
	public String getPrintString(ObjectIdentifierValue oid)
	{
		return oid.getName();
	}
}
