/* Copyright 2019-2020 by rafi612 */
package com.ope.presentation.main;

import com.ope.graphics.Renderer;
import com.ope.presentation.input.Keyboard;
import com.ope.presentation.input.Mouse;
import com.ope.presentation.slide.SlideManager;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.system.Platform;

public class MainLoop
{
	public static void display(SlideManager sm)
	{
		glClear(GL_COLOR_BUFFER_BIT);
		
		glClearColor(1, 1, 1, 1);
		
		if (Keyboard.getKeyOnce(GLFW_KEY_ESCAPE))
			glfwSetWindowShouldClose(Presentation.window, true);
		
		if (Keyboard.getKeyOnce(GLFW_KEY_F11))
		{
			Presentation.fullscreen = !Presentation.fullscreen;
			Presentation.Fullscreen(Presentation.fullscreen);
		}
		
		if (Presentation.running)
		{
			sm.update();
			sm.render();
			
			Keyboard.update();
			Mouse.update();
		}
	}

	public static void dispose(SlideManager sm) 
	{
		sm.slides.clear();
	}

	public static void init(SlideManager sm) 
	{
		glClearColor(1, 1, 1, 1);
		
		glEnable(GL_TEXTURE_2D);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		Renderer.init();
		
		System.out.println("========================================");
		System.out.println("Open Presentation Engine by rafi612");
		System.out.println("========================================");
		System.out.println("System: " + Platform.get());
		System.out.println("GL_VENDOR: " + glGetString(GL_VENDOR));
		System.out.println("GL_RENDERER: " + glGetString(GL_RENDERER));
		System.out.println("GL_VERSION: " + glGetString(GL_VERSION));
		System.out.println("========================================");

	}

	public static void reshape(long window,int width, int height) 
	{
		if (Renderer.isFallback())
			Renderer.fallbackResize();
		
		glViewport(0, 0, width, height);
	}

}
