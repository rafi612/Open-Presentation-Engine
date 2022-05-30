/* Copyright 2019-2020 by rafi612 */
package com.ope.presentation.main;

import com.ope.graphics.Renderer;
import com.ope.presentation.input.Keyboard;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.system.Platform;

import static org.lwjgl.glfw.GLFW.*;

public class MainLoop
{
	public static void display()
	{
		glClear(GL_COLOR_BUFFER_BIT);		
		
		if (Keyboard.getKeyOnce(GLFW_KEY_ESCAPE))
			glfwSetWindowShouldClose(Presentation.window, true);
		
		if (Keyboard.getKeyOnce(GLFW_KEY_F11))
		{
			Presentation.fullscreen = !Presentation.fullscreen;
			Presentation.Fullscreen(Presentation.fullscreen);
		}
		
		if (Presentation.running)
		{
			Presentation.sm.update();
			Presentation.sm.render();
			Keyboard.update();
		}
	}

	public static void dispose() 
	{
//		for (int i = 0;i < Presentation.sm.slide.size();i++)
//		{
//			Presentation.sm.slide.get(i).image.image.flush();
//		}
		
		Presentation.sm.slide.clear();
	}

	public static void init() 
	{
		
		glClearColor(0, 0, 0, 1);
		
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
		for (int i = 0;i < Presentation.sm.slides;i++)
		{
			System.out.println("Slide " + (i + 1) + " Image: " + Presentation.sm.slide.get(i).imagepath);
			System.out.println("Slide " + (i + 1) + " Bg: " + Presentation.sm.slide.get(i).bgpath);
			System.out.println("Slide " + (i + 1) + " TTS: " + Presentation.sm.slide.get(i).ttspath);
			System.out.println("Slide " + (i + 1) + " Start animation: " + Presentation.sm.slide.get(i).startanimation);
			System.out.println("Slide " + (i + 1) + " Exit animation: " + Presentation.sm.slide.get(i).exitanimation);
			System.out.println("=============================");
		}
		
		//System.out.println(gl.glGetString(GL.GL_VENDOR) + "," + gl.glGetString(GL.GL_RENDERER) + "," + gl.glGetString(GL.GL_VERSION));c
	}

	public static void reshape(long window,int width, int height) 
	{
		if (Renderer.isFallback())
			Renderer.fallbackResize();
		
		glViewport(0, 0, width, height);
	}

}