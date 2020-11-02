/* Copyright 2019-2020 by rafi612 */
package com.presentation.states;

import java.awt.Color;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;
import com.presentation.graphics.Screen;
import com.presentation.input.Keyboard;
import com.presentation.main.Presentation;
import com.presentation.resource.ImageResource;
import com.presentation.resource.SlideResource;
import com.presentation.states.State;

public class S_Slide extends State 
{
	public int choose;
	ImageResource empty;

	int inputalpha = 255;
	int outputalpha = 0;
	
	boolean switchslide = true;
	
	public S_Slide()
	{
		choose = 0;
		empty = new ImageResource(S_Slide.class.getResourceAsStream("/images/empty.png"));
	}
	
	public void update()
	{
		//preview funcition
		if (inputalpha > 2)
		{
			inputalpha-=2;
		}
		else switchslide = false;
		
		if (!(SlideResource.slides == 0))
		{
			if (Keyboard.getKeyOnce(KeyEvent.VK_UP))
			{
				if (choose < Presentation.slide.size() - 1) choose++;
				System.out.println(choose);
				switchslide = true;
			}
			if (Keyboard.getKeyOnce(KeyEvent.VK_DOWN))
			{
				if (choose > 0)choose--;
				System.out.println(choose);
				switchslide = true;
			}
			
		}
		
//		if (switchslide)
//		{
//			outputalpha+=2;
//		}
//		if (outputalpha >= 255)
//		{
//			switchslide = false;
//			if (choose < Presentation.slide.size() - 1) choose++;
//		}
	}
	
	public void render(GL2 gl)
	{
		
		if (SlideResource.slides == 0) Screen.drawImage(empty, 0, 0, 1280,720);
		else
		{
			Presentation.slide.get(choose).render(gl);
		}
		
		if (inputalpha > 2) Screen.frect(0, 0, 1280, 720, new Color(0,0,0,inputalpha));
		//if (outputalpha <= 255) Screen.frect(0, 0, 1280, 720, new Color(0,0,0,outputalpha));
	}
	

}
