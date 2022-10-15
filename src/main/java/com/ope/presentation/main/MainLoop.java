/* Copyright 2019-2020 by rafi612 */
package com.ope.presentation.main;

import com.ope.graphics.Renderer;
import com.ope.presentation.input.Keyboard;
import com.ope.presentation.input.Mouse;
import com.ope.presentation.slide.SlideManager;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

public class MainLoop
{
	public static int screenWidth = 1280,screenHeight = 720;
	
	public static void display(SlideManager sm)
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glClearColor(0,0,0,1);
		
		if (Keyboard.getKeyOnce(GLFW_KEY_ESCAPE))
			glfwSetWindowShouldClose(Presentation.window, true);
		
		if (Keyboard.getKeyOnce(GLFW_KEY_F11))
		{
			Presentation.fullscreen = !Presentation.fullscreen;
			Presentation.setFullscreen(Presentation.fullscreen);
		}
		
		sm.update();
		
		//update keyboard and mouse events
		Keyboard.update();
		Mouse.update();
		
		sm.render();
	}

	public static void dispose(SlideManager sm) 
	{	
		sm.destroy();
	}

	public static void init(SlideManager sm) 
	{
		glClearColor(0,0,0,1);
		
		glEnable(GL_TEXTURE_2D);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		Renderer.init(screenWidth,screenHeight);
		//init mouse with screen size to properly get mouse clicks
		Mouse.init(screenWidth, screenHeight);
		
		System.out.println("========================================");
		System.out.println("Open Presentation Engine by rafi612");
		System.out.println("========================================");
		System.out.println("System: " + System.getProperty("os.name"));
		System.out.println("GL_VENDOR: " + glGetString(GL_VENDOR));
		System.out.println("GL_RENDERER: " + glGetString(GL_RENDERER));
		System.out.println("GL_VERSION: " + glGetString(GL_VERSION));
		System.out.println("========================================");

	}

	public static void reshape(long window,int width, int height) 
	{
		if (Renderer.isFallback())
			Renderer.fallbackResize();
		
		float aspectRatio = (float)width / (float)height;
		float targetAspect = (float)screenWidth / (float)screenHeight;
		
		if (aspectRatio >= targetAspect)
		{
			float calculatedW = (targetAspect / aspectRatio ) * width;
	        glViewport((int)((width / 2) - (calculatedW / 2)),0,(int)calculatedW,(int)height);
		}
		else
		{
	        float calculatedH = (aspectRatio / targetAspect) * height;
	        glViewport(0,(int)((height / 2) - (calculatedH / 2)),(int)width,(int)calculatedH);
		}
	}

}
