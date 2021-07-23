package com.presentation.resource.elements;

import java.awt.Color;
import java.awt.Point;
import java.io.File;

import com.gui.SlideCreator;
import com.io.XmlParser;
import com.jogamp.opengl.GL2;
import com.presentation.graphics.Screen;
import com.presentation.resource.Element;
import com.presentation.resource.ImageResource;
import com.presentation.resource.e_frames.E_ImageFrame;

public class E_Image extends Element
{
	public String path;
	
	public int id;
	
	public ImageResource image;
	
	public boolean editing,moving,colided;
	public boolean dragged;
	E_ImageFrame frame;
	
    public Point clickpoint = new Point(0,0);
	
	public E_Image()
	{
		type = "Image";
		frame();
	}

	public E_Image(String path,int x,int y,int w,int h)
	{
		super();
		this.path = path;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		type = "Image";
		
		if (!path.equals(""))
			this.image = new ImageResource(path);
		
		frame = new E_ImageFrame(this);
	}
	
	public void update(SlideCreator sc)
	{
		
		//grobal dragged value
		dragged = sc.dragged;
		//checking AABB colision on click
		if (x < sc.xPixel && x + w > sc.xPixel && y < sc.yPixel && y + h > sc.yPixel)
			colided = true;
		else
			colided = false;
		//executing on dragging
		if (sc.dragged)
		{
			//if colided
			if (colided)
			{
				colided = true;
				if (!moving)
				{
					moving = true;
					//sc.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					clickpoint.setLocation(sc.xPixel, sc.yPixel);
				}
			}
			//executing on moving
			if (moving)
			{
		        int xMoved = (x + sc.xPixel) - (x + clickpoint.x);
		        int yMoved = (y + sc.yPixel) - (y + clickpoint.y);
		        x += xMoved;
		        y += yMoved;
				frame.update();
			}
	        
	        clickpoint = new Point(sc.xPixel,sc.yPixel);
		}
		
		// reseting "moving" after release mouse
		if (!sc.dragged)
		{
			moving = false;
			//sc.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	public void load(XmlParser xml,int id)
	{
		//getting tag element from id
		org.w3c.dom.Element element = xml.getElements(xml.getElementsByTagName("element"))[id];
		
		if (element.getAttribute("type").equals("Image"))
		{
			type = element.getAttribute("type");
			name = element.getAttribute("name");
			
			//[0] getting first element from array
			org.w3c.dom.Element data = xml.getElementsFromElementByName(element,"data")[0];
			
			//loading image
			path = data.getAttribute("src");
			image = new ImageResource(path);
			
			x = Integer.parseInt(data.getAttribute("x"));
			y = Integer.parseInt(data.getAttribute("y"));
			w = Integer.parseInt(data.getAttribute("width"));
			h = Integer.parseInt(data.getAttribute("height"));
		}
	}
	
	public String save()
	{
		//creating xml tag with element data
		return "	<element name=\"" +  name + "\" type=\"" + type + "\">\n"
				+ "		<data src=\"" + path.replaceAll(File.separator, "/") +"\" x=\"" + x + "\" y=\"" + y + "\" width=\"" + w + "\" height=\"" + h + "\"></data>\n"
				+ "	</element>\n";
	}
	
	public void render(GL2 gl)
	{
		if (path.equals(""))
			Screen.frectnofill(x,y,w,h,Color.BLACK);
		else
			Screen.drawImage(image,x,y,w,h);
		
		if (colided)
			Screen.frectnofill(x,y,w,h,Color.BLUE);
		
		if (dragged && colided)
			Screen.frectnofill(x,y,w,h,Color.RED);
	}
	
	
	public void frame()
	{
		editing = true;
		frame.setVisible(true);
	}
}
