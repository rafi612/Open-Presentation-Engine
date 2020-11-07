package com.presentation.animation;

import com.jogamp.opengl.GL2;

public class Animation 
{
	
	// polish comments: im from poland :)
	//pojawianie
	public static final int APPEARING = 0;
	//zanikanie
	public static final int DISAPPEARANCE = 1;
	//glitch
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

	public void render(GL2 gl)
	{
		
	}
}
