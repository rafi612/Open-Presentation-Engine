package com.ope.presentation.animation;

import org.joml.Matrix4f;

import com.ope.graphics.Renderer;
import com.ope.presentation.slide.Slide;

public class Rotating extends Animation 
{
	float time,rotatespeed;
	
	float step,scale,rotate;
	
	public Rotating(float time,float rotatecycles)
	{
		this.time = time;
		
		step = 1 / (60 * (time / 2));
		rotatespeed = (rotatecycles * 360 * 2) / (time * 60);
	}
	
	public void update()
	{
		if (!switched)
		{
			if (scale > 0.01f)
				scale -= step;
			else switchAnimation();
			
			rotate += rotatespeed;
		}
		else
		{
			if (scale < 1)
				scale += step;
			else if (rotate == 0)
				stop();
			
			rotate -= rotatespeed;
		}
		
		if (rotate > 360 || rotate < -360)
			rotate = 0;
	}
	
	public void start()
	{
		super.start();
	}
	
	public void switchAnimation()
	{
		super.switchAnimation();
		scale = 0.01f;
	}
	
	
	public void reset()
	{
		super.reset();
		
		scale = 1;
		rotate = 0;
	}
	
	public void stop()
	{
		super.stop();
		
		Renderer.setViewMatrix(Renderer.EMPTY_VIEW_MATRIX);
	}

	public void render(Slide before,Slide target)
	{
		Matrix4f matrix = new Matrix4f()
				.translate(0.5f * Renderer.getSize().x, 0.5f * Renderer.getSize().y, 0.0f)
				.rotateZ((float)Math.toRadians(rotate))
				.scale(scale)
				.translate(-0.5f * Renderer.getSize().x, -0.5f * Renderer.getSize().y, 0.0f);
		
		Renderer.setViewMatrix(matrix);
		
		if (!switched)
			before.render();
		else target.render();
	}
}
