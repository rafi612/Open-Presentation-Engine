/* Copyright 2019-2020 by rafi612 */
package com.presentation.states;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;
import com.presentation.animation.Animation;
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
	
	Animation startanimation;
	Animation exitanimation;
	
	public S_Slide()
	{
		choose = 0;
		empty = new ImageResource(S_Slide.class.getResourceAsStream("/images/empty.png"));
		if (Presentation.slide.size() > 0)
		{
			startanimation = Presentation.slide.get(0).startanimation;
			exitanimation = Presentation.slide.get(0).exitanimation;
		}
	}
	boolean ttsswitch = true;
	
	public void update()
	{
		// if not 0
		if (!(SlideResource.slides == 0))
		{
			//if slide not switching
			if (!switchslide)
			{
				//key switching
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
			
			//new slide opened
			if (switchslide && (exitanimation.isEnding() || !exitanimation.isRunning()))
			{
				startanimation = Presentation.slide.get(choose).startanimation;
				if (switchside == 0)
				{	
					if (choose < Presentation.slide.size() - 1) choose++;
					startanimation.reset();
					//System.out.println(choose);
					Presentation.window.setTitle(Presentation.TITLE + " - Slide: " + (choose + 1));
					switchside = -1;
					switchslide = false;
					ttsswitch = true;
				}
				if (switchside == 1)
				{
					if (choose > 0)choose--;
					startanimation.reset();
					Presentation.window.setTitle(Presentation.TITLE + " - Slide: " + (choose + 1));
					switchside = -1;
					switchslide = false;
					ttsswitch = true;
				}
			}
			
			//tts auto
			if (Presentation.TTSKeyCode == -1)
			{
				if (Presentation.slide.get(choose).tts != null)
					if (startanimation.isEnding() && ttsswitch)
				{
					Presentation.slide.get(choose).tts.playInBg();
					ttsswitch = false;
				}
			}
			//tts key
			else if (Keyboard.getKeyOnce(Presentation.TTSKeyCode))
			{
				Presentation.slide.get(choose).tts.playInBg();
			}

			//animation update
			if (!startanimation.isRunning())
			{
				startanimation.start();
				Presentation.window.setTitle(Presentation.TITLE + " - Slide: " + (choose + 1));
			}
			if (startanimation.isRunning()) startanimation.update();
			
			
			if (exitanimation.isRunning()) exitanimation.update();
		}
		
	}
	
	public void render(GL2 gl)
	{
		
		if (SlideResource.slides == 0) Screen.drawImage(empty, 0, 0, 1280,720);
		else
		{
			Presentation.slide.get(choose).render(gl,0,0);
			startanimation.render(gl);
			exitanimation.render(gl);
		}
//		if (inputalpha > 2) Screen.frect(0, 0, 1280, 720, new Color(0,0,0,inputalpha));
		//if (outputalpha <= 255) Screen.frect(0, 0, 1280, 720, new Color(0,0,0,outputalpha));
	}
}