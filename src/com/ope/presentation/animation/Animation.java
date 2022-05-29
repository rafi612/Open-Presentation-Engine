package com.ope.presentation.animation;

public class Animation 
{
	
	public static final int APPEARING = 0;
	public static final int DISAPPEARANCE = 1;
	public static final int GLITCH = 2;
	
	protected boolean isRunning = false;
	public int x,y;
	
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
	
	public boolean isEnding()
	{
		return true;
	}
	
	public void start()
	{
		isRunning = true;
	}
	
	public static Animation getAnimation(String a)
	{
		if (a.toLowerCase().equals("appearing")) return new Appearing();
		else if (a.toLowerCase().equals("disappearance")) return new Disappearance();
		return new None();
		
	}
	
	public void reset()
	{
		
	}

	public void render()
	{
		
	}
}
