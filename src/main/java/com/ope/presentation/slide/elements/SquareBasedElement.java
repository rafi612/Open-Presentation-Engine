package com.ope.presentation.slide.elements;

import java.util.ArrayList;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	
	@Override
	public void update(SlideCreator sc)
	{
		//global dragged value
		dragged = sc.dragged;
		
		//check tracking on all elements
		boolean canTracked = true;
		//resizer update and track
		for (Resizer resizer : resizers)
		{
			resizer.track(this);
			resizer.update(sc);
			
			//if one is false, then all is false
			canTracked = canTracked && resizer.canTrakedElementMove;
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
		}
	}
	
	@Override
	public void frame()
	{
		super.frame();
	}
	
	@Override
	public void render()
	{
		if (colided)
			Renderer.frectnofill(x,y,w,h,new Vector4f(0,0,1,1));
		
		if (moving)
			Renderer.frectnofill(x,y,w,h,new Vector4f(1,0,0,1));
		
		for (Resizer resizer : resizers)
			resizer.render(colided);
	}
	
	public class Frame extends Element.Frame implements ChangeListener
	{
		private static final long serialVersionUID = 1L;
		
		public JSpinner sx,sy,sw,sh;
		
		SquareBasedElement element;
		
		public Frame(SquareBasedElement element,String title,int width,int height)
		{
			super(element,title,width,height);
			
			this.element = element;
			
			sx = new JSpinner(new SpinnerNumberModel(element.x,-Integer.MAX_VALUE,Integer.MAX_VALUE,1));
			sy = new JSpinner(new SpinnerNumberModel(element.y,-Integer.MAX_VALUE,Integer.MAX_VALUE,1));
			sw = new JSpinner(new SpinnerNumberModel(element.w,0,Integer.MAX_VALUE,1));
			sh = new JSpinner(new SpinnerNumberModel(element.h,0,Integer.MAX_VALUE,1));
		}
		
		@Override
		public void whenOpen()
		{
			super.whenOpen();
			update();
		}
		
		// setting JSpinner value form elements
		public void update()
		{
			sx.setValue(element.x);
			sy.setValue(element.y);
			sw.setValue(element.w);
			sh.setValue(element.h);
		}
		
		@Override
		public void stateChanged(ChangeEvent e) 
		{
			if (!element.dragged && editing)
			{
				element.x = (int) sx.getValue();
				element.y = (int) sy.getValue();
				element.w = (int) sw.getValue();
				element.h = (int) sh.getValue();
			}
		}
	}

}