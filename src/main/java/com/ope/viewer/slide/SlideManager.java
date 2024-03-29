/* Copyright 2019-2020 by rafi612 */
package com.ope.viewer.slide;

import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.SAXException;

import static org.lwjgl.glfw.GLFW.*;

import com.ope.core.animation.Animation;
import com.ope.core.slide.Slide;
import com.ope.graphics.Renderer;
import com.ope.graphics.Texture;
import com.ope.io.ResourceLoader;
import com.ope.io.xml.Tag;
import com.ope.io.xml.XmlReader;
import com.ope.viewer.Presentation;
import com.ope.viewer.input.Keyboard;

public class SlideManager
{
	public ArrayList<Slide> slides = new ArrayList<Slide>();

	public int choose = 0;
	private Texture empty,endimage;
	
	private boolean switchslide = false;
	private boolean end = false;
	private int switchside;
	
	private int beforeslide,targetslide;
	
	private Animation animation;
	
	public SlideManager()
	{
		empty = new Texture(ResourceLoader.load("/images/empty.png"));
		endimage  = new Texture(ResourceLoader.load("/images/slideend.png"));
	}
	
	public void update()
	{		
		if (slides.size() != 0 && !end)
		{
			glfwSetWindowTitle(Presentation.window,Presentation.TITLE + " - Slide: " + (choose + 1));
			//if slide not switching
			if (!switchslide)
			{
				//key switching
				if (Keyboard.getKeyOnce(GLFW_KEY_UP))
				{
					if (choose == slides.size() - 1)
					{
						end = true;
						return;
					}
					
					animation = slides.get(choose + 1).animation;
					animation.reset();
					animation.start();
					
					beforeslide = choose;
					targetslide = choose + 1;
					
					switchslide = true;
					switchside = -1;
					
				}
				if (Keyboard.getKeyOnce(GLFW_KEY_DOWN))
				{
					//return if in 0 slide
					if (choose == 0)
						return;
					
					animation = slides.get(choose - 1).animation;
					animation.reset();
					animation.start();
					
					beforeslide = choose;
					targetslide = choose - 1;
					
					switchslide = true;
					switchside = 1;
				}
			}
			
			//new slide opened
			if (switchslide && animation.isSwitched())
			{
				if (switchside == -1)
					choose++;
				else if (switchside == 1)
					choose--;
				
				switchside = 0;
				switchslide = false;
			}
 
			if (animation.isRunning())
				animation.update();
		}
		
		if (end)
		{
			//get back on down arrow click
			glfwSetWindowTitle(Presentation.window,Presentation.TITLE + " - End");
			if (Keyboard.getKeyOnce(GLFW_KEY_DOWN))
				end = false;
			//close window on up arrow click
			if (Keyboard.getKeyOnce(GLFW_KEY_UP))
				glfwSetWindowShouldClose(Presentation.window, true);
		}
		
	}
	
	public void load(XmlReader xml) throws IOException, SAXException
	{		
		Tag[] tags = xml.getTags("slide");
		
		for (Tag tag : tags)
		{
			Slide slide = Slide.load(tag);
			
			slides.add(slide);
		}
		
		if (slides.size() > 0)
		{
			animation = slides.get(0).animation;
			animation.switchAnimation();
			animation.start();
			
			beforeslide = 0;
			targetslide = 0;
		}
	}
	
	public void render()
	{		
		if (slides.size() == 0) 
			Renderer.drawImage(empty, 0, 0, Renderer.getSize().x,Renderer.getSize().y);
		else if (end)		
			Renderer.drawImage(endimage, 0,0,300,50);
		else
		{
			if (animation.isRunning()) 
				animation.render(slides.get(beforeslide),slides.get(targetslide));
			else 
				slides.get(choose).render();
		}
	}
	
	public void destroy()
	{
		for (Slide slide : slides)
			slide.destroy();
		
		empty.destroy();
		endimage.destroy();
		
		slides.clear();
	}
}