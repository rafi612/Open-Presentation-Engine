package com.presentation.resource;

import com.jogamp.opengl.GL2;
import com.presentation.resource.elements.E_Image;
import com.presentation.resource.elements.E_Shape;

public class Element 
{

	public Element() 
	{
		
	}
	
	public void update()
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
