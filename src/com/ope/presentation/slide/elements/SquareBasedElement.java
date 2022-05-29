package com.ope.presentation.slide.elements;

import java.util.ArrayList;

import org.joml.Vector2i;
import org.joml.Vector4f;

import com.ope.graphics.Renderer;
import com.ope.gui.SlideCreator;
import com.ope.presentation.slide.Element;

public class SquareBasedElement extends Element
{
	public boolean moving,colided;
	public boolean dragged;
	
    public Vector2i clickpoint = new Vector2i(0,0);
    
    ArrayList<Resizer> resizers;
    
    public SquareBasedElement()
    {
		resizers = new ArrayList<Resizer>();
		resizers.add(new Resizer(Resizer.Type.DOWN_RIGHT));
		resizers.add(new Resizer(Resizer.Type.DOWN_LEFT));
		resizers.add(new Resizer(Resizer.Type.UP_LEFT));
		resizers.add(new Resizer(Resizer.Type.UP_RIGHT));
    }
	
	public void update(SlideCreator sc)
	{
		//global dragged value
		dragged = sc.dragged;
		
		//check tracking on all elements
		boolean canTracked = true;
		//resizer update and track
		for (int i = 0; i < resizers.size(); i++)
		{
			resizers.get(i).track(this);
			resizers.get(i).update(sc);
			
			//if one is false, then all is false
			canTracked = canTracked && resizers.get(i).canTrakedElementMove;
		}
		
		//checking AABB colision on click
		if (x < sc.xPixel && x + w > sc.xPixel && y < sc.yPixel && y + h > sc.yPixel 
				&& sc.currentColidedID <= id && sc.currentMovedID == -1)
		{
			colided = true;
			sc.currentColidedID = id;
		}
		else
		{
			colided = false;
			if (sc.currentColidedID == id)
				sc.currentColidedID = -1;
		}
		
		//executing on dragging
		if (sc.dragged && canTracked)
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
				sc.currentColidedID = id;
				sc.currentMovedID = id;
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
			sc.currentMovedID = -1;
			//sc.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	public void render()
	{
		if (colided)
			Renderer.frectnofill(x,y,w,h,new Vector4f(0,0,1,1));
		
		if (moving)
			Renderer.frectnofill(x,y,w,h,new Vector4f(1,0,0,1));
		
		for (int i = 0; i < resizers.size(); i++)
			resizers.get(i).render(colided);
	}

}
