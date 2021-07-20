package com.presentation.resource.elements;

import java.awt.Color;

import com.io.XmlParser;
import com.jogamp.opengl.GL2;
import com.presentation.graphics.Screen;
import com.presentation.resource.Element;
import com.presentation.resource.ImageResource;
import com.presentation.resource.e_frames.E_ShapeFrame;

public class E_Shape extends Element
{
	String path;
	
	ImageResource image;
	
	public E_Shape()
	{
		type = "Shape";
		frame();
	}

	public E_Shape(int x,int y,int w,int h)
	{
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		type = "Shape";
	}
	
	public void load(XmlParser xml,int id)
	{
		
	}
	
	public String save()
	{
		return "	<element name=" +  name + " type=" + type + "></element>";
	}
	
	public void render(GL2 gl)
	{
		Screen.frectnofill(x,y,w,h,Color.RED);
	}
	
	public void frame()
	{
		new E_ShapeFrame(this);
	}
}
