package com.presentation.slide.elements;

import java.awt.Color;

import com.graphics.Renderer;
import com.graphics.Texture;
import com.io.XmlParser;
import com.presentation.slide.Element;
import com.presentation.slide.e_frames.E_ShapeFrame;

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
		Renderer.frectnofill(x,y,w,h,Color.RED);
	}
	
	public void frame()
	{
		new E_ShapeFrame(this);
	}
}