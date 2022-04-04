package com.presentation.animation;

import java.awt.Color;

import org.joml.Vector4f;

import com.graphics.Renderer;

public class Appearing extends Animation 
{
	int inputalpha = 255;

	public Appearing() {}
	
	public void update()
	{
		if (inputalpha > 2)
		{
			inputalpha-=2;
		}
		else isRunning = false;
	}
	
	public boolean isEnding()
	{
		return inputalpha <= 2;
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
			if (inputalpha > 2) Renderer.frect(0, 0, 1280, 720, new Vector4f(0,0,0,(float)inputalpha/255));
		}
	}

}
