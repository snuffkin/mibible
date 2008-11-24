package com.googlecode.mibible.browser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoticeNodeHolder
{
	private Map<String, MibTreeNode> map
	    = new HashMap<String, MibTreeNode>();
	private List<MibTreeNode> list
	    = new ArrayList<MibTreeNode>();
	
	public void clear()
	{
		this.map.clear();
		this.list.clear();
	}
	public void addNode(MibTreeNode node)
	{
		this.map.put(node.getOid(), node);
		this.list.add(node);
	}
	
	public boolean isNotice(MibTreeNode node)
	{
		return this.map.containsKey(node.getOid());
	}

}
