package com.ope.core.slide.elements;

import org.joml.Vector2i;
import org.joml.Vector4f;

import com.ope.core.slide.Element;
import com.ope.graphics.Renderer;
import com.ope.gui.slide.SlideCreator;

public class Resizer 
{
	public int x,y,w = 20,h = 20;
	public boolean colided,moving,dragged;
	
	public boolean canTrakedElementMove;
	
	Vector2i clickpoint = new Vector2i();
	
	Type type;
	
	public enum Type
	{
		UP_LEFT,UP_RIGHT,DOWN_LEFT,DOWN_RIGHT,CUSTOM
	}
	
	public Resizer(Type type)
	{
		this.type = type;
	}
	
	public void update(SlideCreator sc)
	{
		dragged = sc.dragged;
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
					clickpoint.set(sc.xPixel, sc.yPixel);
				}
			}
			//executing on moving
			if (moving)
			{
		        int xMoved = (x + sc.xPixel) - (x + clickpoint.x);
		        int yMoved = (y + sc.yPixel) - (y + clickpoint.y);
		        x += xMoved;
		        y += yMoved;
			}
	        
	        clickpoint.set(sc.xPixel,sc.yPixel);
		}
		
		// reseting "moving" after release mouse
		if (!sc.dragged)
		{
			moving = false;
			//sc.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	int lastx = x,lasty = y;
	
	public void track(Element e)
	{
		//resize if move track element if not move
		if (moving) 
		{
			canTrakedElementMove = false;
			
				//resizing by type
			switch (type)
			{
				case DOWN_RIGHT:
					e.w = x + w - e.x;
					e.h = y + h - e.y;
				break;
				case DOWN_LEFT:
					e.x = x;
					e.w -= (x - lastx);
					
					e.h = y + h - e.y;
				break;
				case UP_LEFT:
					e.x = x;
					e.w -= (x - lastx);
					
					e.y = y;
					e.h -= (y - lasty);
				break;
				case UP_RIGHT:
					e.w = x + w - e.x;
					
					e.y = y;
					e.h -= (y - lasty);
				break;
				case CUSTOM:
					break;
			}
		}
		else
		{
			canTrakedElementMove = true;
			
			//tracking by type
			switch (type)
			{
				case DOWN_RIGHT:
					x = e.x + e.w - w;
					y = e.y + e.h - h;
				break;
				case DOWN_LEFT:
					x = e.x;
					y = e.y + e.h - h;
				break;
				case UP_LEFT:
					x = e.x;
					y = e.y;
				break;
				case UP_RIGHT:
					x = e.x + e.w - w;
					y = e.y;
				break;
				case CUSTOM:
					break;
			}
		}
		
		// getting last positions to specific resizers
		lastx = x;
		lasty = y;
	}
	
	//boolean render is for making visible if cursor colided with element
	public void render(boolean render)
	{		
		if (render || colided)
			Renderer.frect(x, y, w, h, new Vector4f(0,0,1,1));
	}

}
