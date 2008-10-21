package com.googlecode.mibible.printer.filter;

import net.percederberg.mibble.value.ObjectIdentifierValue;

public abstract class PrintFilter
{
	private final String filterKey;
	protected PrintFilter(String filterKey)
	{
		this.filterKey = filterKey;
	}
	
	public String getFilterKey()
	{
		return this.filterKey;
	}
	
	public abstract String getPrintString(ObjectIdentifierValue oid);
}
