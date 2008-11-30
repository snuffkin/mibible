package com.googlecode.mibible.browser;

public class MibInfo
{
	private String oid;
	private String name;
	public MibInfo(String oid, String name)
	{
		this.oid = oid;
		this.name = name;
	}
	public String getOid() {
		return oid;
	}
	public String getName() {
		return name;
	}
	public String getUpperName() {
		return name.toUpperCase();
	}

}
