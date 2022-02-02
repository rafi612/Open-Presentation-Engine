package com.presentation.resource;

import com.gui.SlideCreator;
import com.io.XmlParser;
import com.presentation.resource.elements.E_Image;
import com.presentation.resource.elements.E_Shape;

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
		if (s.equals("Image")) return new E_Image("",1,1,200,200);
		if (s.equals("Shape")) return new E_Shape(1,1,200,200);
		return null;
	}
	
	public void load(XmlParser xml,int id)
	{
		
	}
	
	public String save()
	{
		return "	<element name=" +  name + " type=" + type + "></element>";
	}
	
	public void frame()
	{
		
	}

	public void render()
	{
		
	}
	

}
