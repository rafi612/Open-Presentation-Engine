package com.ope.presentation.slide;

import com.ope.gui.SlideCreator;
import com.ope.presentation.slide.elements.E_Image;

public class Element 
{
	public static final String[] types = {"Image","Text","Shape","Graph"};
	public boolean editing,moving,colided;
	
	public int x;
	public int y;
	public int w;
	public int h;
	
	public int id;
	
	public String name,type;

	public Element() 
	{
		
	}
	
	public void update(SlideCreator sc)
	{
		
	}
	
	public static Element getElementsByName(String s)
	{
		if (s.equals("Image")) return new E_Image("",0,0,200,200);
		return null;
	}
	
	public void load(org.w3c.dom.Element element)
	{
		
	}
	
	public String save()
	{
		return "	<element name=" +  name + " type=" + type + "></element>";
	}
	
	public void frame()
	{
		
	}
	
	public void destroy()
	{
		
	}

	public void render()
	{
		
	}
}