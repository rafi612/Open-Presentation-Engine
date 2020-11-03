package com.presentation.animation;

import java.awt.Color;

import com.presentation.graphics.Screen;

public class Disappearance extends Animation 
{
	
	public Disappearance() {}
	
	public void update()
	{
		if (outputalpha <= 255)
		{
			outputalpha+=2;
		}
		else isRunning = false;
	}
	
	public void reset()
	{
		isRunning = false;
		outputalpha = 0;
	}

	public void render()
	{
		if (isRunning)
		{
			if (outputalpha <= 255) Screen.frect(0, 0, 1280, 720, new Color(0,0,0,outputalpha));
		}
	}

}
