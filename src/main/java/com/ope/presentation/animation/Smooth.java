package com.ope.presentation.animation;

import com.ope.graphics.FrameBuffer;
import com.ope.graphics.Renderer;
import com.ope.presentation.slide.Slide;

public class Smooth extends Animation
{
	float time,step,mix;
	
	FrameBuffer fb1,fb2;
	
	public Smooth(float time)
	{
		this.time = time;
		
		step = 1 / (60 * time);
		
		fb1 = new FrameBuffer(Renderer.getSize().x,Renderer.getSize().y);
		fb2 = new FrameBuffer(Renderer.getSize().x,Renderer.getSize().y);
		
		reset();
	}
	
	public void update()
	{
		if (!switched)
		{
			if (mix < 0.5)
				mix += step;
			else 
				switchAnimation();
		}
		else
		{
			if (mix < 1)
				mix += step;
			else stop();
		}
	}
	
	public void start()
	{
		super.start();
	}
	
	public void switchAnimation()
	{
		super.switchAnimation();
	}
	
	
	public void reset()
	{
		super.stop();
		
		mix = 0;
	}

	public void render(Slide before,Slide target)
	{
		fb1.bind();
		before.render();
		fb1.unbind();
		
		fb2.bind();
		target.render();
		fb2.unbind();
		
		Renderer.mix2Textures(fb1.getTextureID(), fb2.getTextureID(), mix, 0,0,Renderer.getSize().x,Renderer.getSize().y);
	}
	
	public void destroy()
	{
		fb1.destroy();
		fb2.destroy();
	}

}
