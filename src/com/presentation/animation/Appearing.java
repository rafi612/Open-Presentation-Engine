package com.presentation.animation;

import java.awt.Color;

import com.presentation.graphics.Screen;

public class Appearing extends Animation 
{

	public Appearing() {}
	
	public void update()
	{
		if (inputalpha > 2)
		{
			inputalpha-=2;
		}
		else isRunning = false;
	}
	
	public void reset()
	{
		isRunning = false;
		inputalpha = 255;
	}

	public void render()
	{
		if (isRunning)
		{
			if (inputalpha > 2) Screen.frect(0, 0, 1280, 720, new Color(0,0,0,inputalpha));
		}
	}

}
