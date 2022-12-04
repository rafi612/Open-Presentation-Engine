package com.ope.core.animation;

import com.ope.core.slide.Slide;

public class None extends Animation
{
	public None()
	{
		super("None");
	}
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

	public void render(Slide before,Slide target) {}
}
