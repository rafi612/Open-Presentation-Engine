package com.ope.presentation.animation;

import com.ope.presentation.slide.Slide;

public class Animation 
{
	protected boolean isRunning = false;
	
	protected boolean switched = false;
	
	public Animation() 
	{
		
	}
	
	public void update()
	{
		
	}
	
	public boolean isRunning()
	{
		return isRunning;
	}
	
	public boolean isSwitched()
	{
		return switched;
	}
	
	public void start()
	{
		isRunning = true;
	}
	
	public void switchAnimation()
	{
		switched = true;
	}
	
	
	public void reset()
	{
		switched = false;
		isRunning = false;
	}

	public void render(Slide before,Slide target)
	{
		
	}
	
	public static Animation getAnimation(String a)
	{
		if (a.toLowerCase().equals("appearing")) return new Appearing(2.0f);
		return new None();
		
	}
}
