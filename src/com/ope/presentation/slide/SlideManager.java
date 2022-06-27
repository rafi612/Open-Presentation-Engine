/* Copyright 2019-2020 by rafi612 */
package com.ope.presentation.slide;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

import com.ope.graphics.Renderer;
import com.ope.graphics.Texture;
import com.ope.io.Util;
import com.ope.io.XmlParser;
import com.ope.presentation.animation.Animation;
import com.ope.presentation.input.Keyboard;
import com.ope.presentation.main.Presentation;
import com.ope.project.Project;

public class SlideManager
{
	public ArrayList<Slide> slides;

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
		
		slides = new ArrayList<Slide>();
	}
	boolean ttsswitch = true;
	
	public void update()
	{
		//TODO: rewrite logic engine
		
		// if not 0
		if (slides.size() != 0)
		{
			//if slide not switching
			if (!switchslide)
			{
				//key switching
				if (Keyboard.getKeyOnce(GLFW_KEY_UP))
				{
					exitanimation = slides.get(choose).exitanimation;
					exitanimation.reset();
					exitanimation.start();
					switchslide = true;
					switchside = 0;
					
				}
				if (Keyboard.getKeyOnce(GLFW_KEY_DOWN))
				{
					exitanimation = slides.get(choose).exitanimation;
					exitanimation.reset();
					exitanimation.start();
					switchslide = true;
					switchside = 1;
				}
			}
			
			//new slide opened
			if (switchslide && (exitanimation.isEnding() || !exitanimation.isRunning()))
			{
				if (switchside == 0)
				{	
					if (choose < slides.size() - 1) choose++;
					startanimation = slides.get(choose).startanimation;
					startanimation.reset();
					
					switchside = -1;
					switchslide = false;
					ttsswitch = true;
				}
				if (switchside == 1)
				{
					if (choose > 0)choose--;
					startanimation = slides.get(choose).startanimation;
					startanimation.reset();
					switchside = -1;
					switchslide = false;
					ttsswitch = true;
				}
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
		XmlParser xml = new XmlParser(Util.projectPath(Project.PROJECT_XML_NAME));
		
		org.w3c.dom.Element[] elements = XmlParser.getElements(xml.getElementsByTagName("slide"));
		
		for (org.w3c.dom.Element element : elements)
		{
			Slide slide = new Slide();
			slide.load(element);
			
			slides.add(slide);
		}
		
		if (slides.size() > 0)
		{
			startanimation = slides.get(0).startanimation;
			exitanimation = slides.get(0).exitanimation;
		}

	}
	
	public void render()
	{
		if (slides.size() == 0) 
			Renderer.drawImage(empty, 0, 0, 1280,720);
		else
		{
			slides.get(choose).render();
			startanimation.render();
			exitanimation.render();
		}
	}
}