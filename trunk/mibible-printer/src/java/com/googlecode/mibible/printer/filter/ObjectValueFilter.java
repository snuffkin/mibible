package com.googlecode.mibible.printer.filter;

import net.percederberg.mibble.value.ObjectIdentifierValue;

public class ObjectValueFilter extends PrintFilter
{
	public ObjectValueFilter()
	{
		super("%value");
	}
	
	@Override
	public String getPrintString(ObjectIdentifierValue oid)
	{
		return String.valueOf(oid.getValue());
	}
}
