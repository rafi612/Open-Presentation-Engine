package com.presentation.animation;

import java.awt.Color;

import com.presentation.graphics.Renderer;

public class Disappearance extends Animation 
{
	public int outputalpha = 0;
	
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
	
	public boolean isEnding()
	{
		return outputalpha >= 250;
	}

	public void render()
	{
		if (isRunning)
		{
			if (outputalpha <= 255) Renderer.frect(0, 0, 1280, 720, new Color(0,0,0,outputalpha));
		}
	}

}
