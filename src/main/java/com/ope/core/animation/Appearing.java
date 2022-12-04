package com.ope.core.animation;

import org.joml.Vector4f;

import com.ope.core.slide.Slide;
import com.ope.graphics.Renderer;

public class Appearing extends Animation
{	
	public float time,step,alpha;
	
	public Appearing(float time)
	{
		super("Appearing");
		this.time = time;
		
		step = 1 / (60 * (time / 2));
	}

	public void update()
	{
		if (!switched)
		{
			if (alpha <= 1)
			{
				alpha+=step;
			}
			else switchAnimation();
		}
		else
		{
			if (alpha > step)
			{
				alpha-=step;
			}
			else isRunning = false;
		}
	}
	
	
	public void start()
	{
		super.start();
	}
	
	public void reset()
	{
		super.reset();
		
		alpha = 0;
	}
	
	public void switchAnimation()
	{
		super.switchAnimation();
		
		alpha = 1;
	}

	public void render(Slide before,Slide target)
	{
		if (!switched)
		{
			before.render();
			
			Renderer.frect(0, 0,Renderer.getSize().x,Renderer.getSize().y,new Vector4f(0,0,0,alpha));
		}
		else
		{
			target.render();
			
			Renderer.frect(0, 0,Renderer.getSize().x,Renderer.getSize().y,new Vector4f(0,0,0,alpha));
		}
			
	}
}
