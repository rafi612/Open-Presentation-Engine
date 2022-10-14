package com.ope.presentation.animation;

import com.ope.presentation.slide.Slide;

public class None extends Animation
{		
	public void start()
	{
		switchAnimation();
	}
	
	public void switchAnimation()
	{
		super.switchAnimation();
	}
	
	
	public void reset()
	{
		stop();
		switchAnimation();
	}
	
	public void stop()
	{
		super.stop();
	}

	public void render(Slide before,Slide target)
	{
		
	}
}
