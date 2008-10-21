package com.googlecode.mibible.printer.filter;

import net.percederberg.mibble.value.ObjectIdentifierValue;

public class DetailStringFilter extends PrintFilter
{
	public DetailStringFilter()
	{
		super("%detail");
	}
	
	@Override
	public String getPrintString(ObjectIdentifierValue oid)
	{
		return oid.toDetailString();
	}}
