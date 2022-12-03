package com.ope.core.animation;

import org.joml.Matrix4f;

import com.ope.core.slide.Slide;
import com.ope.graphics.Renderer;

public class Departure extends Animation 
{	
	public float scaleacceleration,slideacceleration;
	public float scalevelocity,slidevelocity;
	
	public float scale,x;
	
	public Departure()
	{		
		reset();
	}
	
	public void update()
	{
		if (!switched)
		{
			if (scale >= 0.5)
			{
				scalevelocity += scaleacceleration;
				scale -= scalevelocity;
			}
			else if (x >= -Renderer.getSize().x)
			{
				slidevelocity += slideacceleration;
				x -= slidevelocity;
			}
			else switchAnimation();
		}
		else
		{
			if (x > 0)
			{
				slidevelocity += slideacceleration;
				x += slidevelocity;
			}
			else if (scale <= 1)
			{
				scalevelocity -= scaleacceleration;
				scale += scalevelocity;
			}
			else stop();
		}
	}
	
	public void start()
	{
		super.start();
	}
	
	public void stop()
	{
		super.stop();
	}
	
	public void switchAnimation()
	{
		super.switchAnimation();
		
		//switch
		scale = 0.5f;
		scalevelocity = 0.033f;
		
		slidevelocity = -52.0f;
		x = Renderer.getSize().x;
	}
	
	
	public void reset()
	{
		super.reset();
		
		scale = 1;
		x = 0;
		
		scaleacceleration = 0.001f;
		slideacceleration = 1;
		
		slidevelocity = 0;
		scalevelocity = 0;
	}

	public void render(Slide before,Slide target)
	{
		Matrix4f scalematrix = new Matrix4f()
				.translate(x,0,0)
				.translate(0.5f * Renderer.getSize().x, 0.5f * Renderer.getSize().y, 0.0f)
				.scale(scale)
				.translate(-0.5f * Renderer.getSize().x, -0.5f * Renderer.getSize().y, 0.0f);
		Renderer.setViewMatrix(scalematrix);
		
		if (!switched)
			before.render();
		else
			target.render();
		
		Renderer.setViewMatrix(Renderer.EMPTY_VIEW_MATRIX);
	}
}
