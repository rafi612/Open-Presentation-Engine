package com.ope.presentation.slide.elements;

import org.joml.Vector4f;

import com.ope.graphics.Renderer;
import com.ope.graphics.Texture;
import com.ope.io.XmlParser;
import com.ope.presentation.slide.Element;

public class E_Shape extends Element
{
	String path;
	
	Texture image;
	
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
	
	public void render()
	{
		Renderer.frectnofill(x,y,w,h,new Vector4f(1,0,0,1));
	}
	
	public void frame()
	{
		
	}
}
