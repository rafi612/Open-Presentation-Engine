/* Copyright 2019-2020 by rafi612 */
package com.ope.presentation.slide;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

import com.ope.graphics.Renderer;
import com.ope.graphics.Texture;
import com.ope.io.Util;
import com.ope.main.Main;
import com.ope.presentation.animation.Animation;
import com.ope.presentation.input.Keyboard;
import com.ope.presentation.main.Presentation;

public class SlideManager
{
	public ArrayList<Slide> slide = new ArrayList<Slide>();
	
	public int slides;
	
	public int choose;
	Texture empty;
	
	boolean switchslide = false;
	int switchside;
	
	Animation startanimation;
	Animation exitanimation;
	
	public SlideManager()
	{
		choose = 0;
		empty = new Texture(SlideManager.class.getResourceAsStream("/images/empty.png"));
	}
	boolean ttsswitch = true;
	
	public void update()
	{
		// if not 0
		if (!(slides == 0))
		{
			//if slide not switching
			if (!switchslide)
			{
				//key switching
				if (Keyboard.getKeyOnce(GLFW_KEY_UP))
				{
					exitanimation = slide.get(choose).exitanimation;
					exitanimation.reset();
					exitanimation.start();
					switchslide = true;
					switchside = 0;
					
				}
				if (Keyboard.getKeyOnce(GLFW_KEY_DOWN))
				{
					exitanimation = slide.get(choose).exitanimation;
					exitanimation.reset();
					exitanimation.start();
					switchslide = true;
					switchside = 1;
				}
			}
			
			//new slide opened
			if (switchslide && (exitanimation.isEnding() || !exitanimation.isRunning()))
			{
				startanimation = slide.get(choose).startanimation;
				if (switchside == 0)
				{	
					if (choose < slide.size() - 1) choose++;
					startanimation.reset();
					//System.out.println(choose);
					switchside = -1;
					switchslide = false;
					ttsswitch = true;
				}
				if (switchside == 1)
				{
					if (choose > 0)choose--;
					startanimation.reset();
					switchside = -1;
					switchslide = false;
					ttsswitch = true;
				}
				glfwSetWindowTitle(Presentation.window,Presentation.TITLE + " - Slide: " + (choose + 1));
			}
			
			//tts auto
			if (Presentation.TTSKeyCode == -1)
			{
				if (slide.get(choose).tts != null)
					if (startanimation.isEnding() && ttsswitch)
				{
					slide.get(choose).tts.play();
					ttsswitch = false;
				}
			}
			//tts key
			else if (Keyboard.getKeyOnce(Presentation.TTSKeyCode))
			{
				slide.get(choose).tts.play();
			}

			//animation update
			if (!startanimation.isRunning())
			{
				startanimation.start();
				glfwSetWindowTitle(Presentation.window,Presentation.TITLE + " - Slide: " + (choose + 1));
			}
			if (startanimation.isRunning()) startanimation.update();
			
			
			if (exitanimation.isRunning()) exitanimation.update();
		}
		
	}
	
	public void load()
	{
		String path = "";
		if (Main.args.length < 1)
		{
			path = Util.projectPath("config.xml");
		}
		else
			path = Main.args[0];
		
		for (int i = 0;i < slides;i++)
		{
			Slide slideres = new Slide();
			slideres.setSlideImage(Util.readXml(path, "slide" + (i + 1), "path"));
			slideres.setBg(Util.readXml(path, "slide" + (i + 1), "bg"));
			slideres.setTTS(Util.readXml(path, "slide" + (i + 1), "tts"));
			slideres.setStartAnimation(Util.readXml(path, "slide" + (i + 1), "ent_ani"));
			slideres.setExitAnimation(Util.readXml(path, "slide" + (i + 1), "exi_ani"));
			
			slide.add(slideres);
		}
		
		if (slides > 0)
		{
			startanimation = slide.get(0).startanimation;
			exitanimation = slide.get(0).exitanimation;
		}
	}
	
	public void render()
	{
		
		if (slides == 0) Renderer.drawImage(empty, 0, 0, 1280,720);
		else
		{
			slide.get(choose).render();
			startanimation.render();
			exitanimation.render();
		}
//		if (inputalpha > 2) Screen.frect(0, 0, 1280, 720, new Color(0,0,0,inputalpha));
		//if (outputalpha <= 255) Screen.frect(0, 0, 1280, 720, new Color(0,0,0,outputalpha));
	}
}