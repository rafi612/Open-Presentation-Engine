package com.ope.io.xml;

import java.util.ArrayList;

public class XmlWriter 
{
	private String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	private ArrayList<String> tags = new ArrayList<>();
	
	private int lastTag = -1;
	
	public XmlWriter() {}
	
	public void openTag(String tag,String... attributes)
	{
		for (int i = 0;i < tags.size();i++)
			xml += "\t";
		
		tags.add(tag);
		lastTag++;
		
		xml += "<" + tag;
		for (String attrib : attributes)
		{
			String[] attr = attrib.split("=");
			xml += " " + attr[0] + "=\"" + attr[1] + "\"";
		}
		xml += ">\n";
	}
	
	public void addTag(String tag,String... attributes)
	{
		for (int i = 0;i < tags.size();i++)
			xml += "\t";
		
		xml += "<" + tag;
		for (String attrib : attributes)
		{
			String[] attr = attrib.split("=");
			xml += " " + attr[0] + "=\"" + attr[1] + "\"";
		}
		xml += "/>\n";
	}
	
	public void addTagText(String tag,String text,String... attributes)
	{
		for (int i = 0;i < tags.size();i++)
			xml += "\t";
		
		xml += "<" + tag;
		for (String attrib : attributes)
		{
			String[] attr = attrib.split("=");
			xml += " " + attr[0] + "=\"" + attr[1] + "\"";
		}
		xml += ">" + text + "</" + tag + ">\n";
	}
	
	
	public void closeTag()
	{
		for (int i = 0;i < tags.size() - 1;i++)
			xml += "\t";
		
		xml += "</" + tags.get(lastTag) + ">\n";
		
		tags.remove(lastTag);
		lastTag--;
	}
	
	public String get()
	{
		return xml;
	}
}