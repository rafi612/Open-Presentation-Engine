package com.presentation.animation;

public class Animation 
{
	
	// polish comments: im from poland :)
	//pojawianie
	public static final int APPEARING = 0;
	//zanikanie
	public static final int DISAPPEARANCE = 1;
	
	protected boolean isRunning = false;
	public int x,y;
	int inputalpha = 255;
	public int outputalpha = 0;
	
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
