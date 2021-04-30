package com.presentation.resource;

import com.gui.SlideCreator;
import com.jogamp.opengl.GL2;
import com.presentation.resource.elements.E_Image;
import com.presentation.resource.elements.E_Shape;

public class Element 
{
	public static final String[] types = {"Image","Text","Shape","Graph"};
	boolean editing;
	
	public int x;
	public int y;
	public int w;
	public int h;

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
	
	public void frame()
	{
		
	}

	public void render(GL2 gl)
	{
		
	}

}
