/* Copyright 2019-2020 by rafi612 */
package com.presentation.states;

import java.awt.Color;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;
import com.presentation.animation.Animation;
import com.presentation.animation.Appearing;
import com.presentation.animation.Disappearance;
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
	
	boolean switchslide = false;
	int switchside;
	
	Animation startanimation = new Appearing();
	Animation exitanimation = new Disappearance();
	
	public S_Slide()
	{
		choose = 0;
		empty = new ImageResource(S_Slide.class.getResourceAsStream("/images/empty.png"));
		startanimation = Presentation.slide.get(0).startanimation;
		exitanimation = Presentation.slide.get(0).exitanimation;
	}
	
	public void update()
	{
		
		if (!(SlideResource.slides == 0))
		{
			if (Keyboard.getKeyOnce(KeyEvent.VK_UP))
			{
				exitanimation = Presentation.slide.get(choose).exitanimation;
				exitanimation.reset();
				exitanimation.start();
				switchslide = true;
				switchside = 0;
				
			}
			if (Keyboard.getKeyOnce(KeyEvent.VK_DOWN))
			{
				exitanimation = Presentation.slide.get(choose).exitanimation;
				exitanimation.reset();
				exitanimation.start();
				switchslide = true;
				switchside = 1;
			}
		}
		
		if (switchslide && !exitanimation.isRunning())
		{
			startanimation = Presentation.slide.get(choose).startanimation;
			if (switchside == 0)
			{	
				if (choose < Presentation.slide.size() - 1) choose++;
				startanimation.reset();
				System.out.println(choose);
				Presentation.window.setTitle(Presentation.TITLE + " - Slide: " + (choose + 1));
				switchside = -1;
			}
			if (switchside == 1)
			{
				if (choose > 0)choose--;
				startanimation.reset();
				Presentation.window.setTitle(Presentation.TITLE + " - Slide: " + (choose + 1));
				switchside = -1;
			}
		}

		if (!startanimation.isRunning())
		{
			startanimation.start();
			Presentation.window.setTitle(Presentation.TITLE + " - Slide: " + (choose + 1));
		}
		if (startanimation.isRunning()) startanimation.update();
		
		
		if (exitanimation.isRunning()) exitanimation.update();
	}
	
	public void render(GL2 gl)
	{
		
		if (SlideResource.slides == 0) Screen.drawImage(empty, 0, 0, 1280,720);
		else
		{
				Presentation.slide.get(choose).render(gl);
		}
		
		startanimation.render();
		exitanimation.render();
//		if (inputalpha > 2) Screen.frect(0, 0, 1280, 720, new Color(0,0,0,inputalpha));
		//if (outputalpha <= 255) Screen.frect(0, 0, 1280, 720, new Color(0,0,0,outputalpha));
	}
	

}