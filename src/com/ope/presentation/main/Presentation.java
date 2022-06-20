/* Copyright 2019-2020 by rafi612 */
package com.ope.presentation.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import com.ope.audio.Sound;
import com.ope.io.Util;
import com.ope.presentation.input.Keyboard;
import com.ope.presentation.input.Mouse;
import com.ope.presentation.slide.SlideManager;
import com.ope.project.Project;

public class Presentation 
{
	public static boolean fullscreen = false;
	
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	public static final String TITLE = "OPE Presentation";
	
	public static long window = NULL;
	
	public static boolean running = false;
	
	private static SlideManager sm;
	
	public static void start(String configpath)
	{
		//setting project location
		Project.projectlocation = new File(configpath).getParent();
		
		sm = new SlideManager();
		
		if (window != NULL)
			stop();
		
		Sound.init();
		
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit())
			System.err.println("Error Init GLFW");

	    glfwDefaultWindowHints();
	    
	    glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
	    glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		
	    window = glfwCreateWindow(1280, 720, TITLE, NULL, NULL);
	    if (window == NULL)
	      throw new RuntimeException("Failed to create the GLFW window");
        
	    GLFWImage.Buffer icon = icon("/images/icon.png");
	    glfwSetWindowIcon(window,icon);
	    
	    glfwSetKeyCallback(window, Keyboard::invoke);
	    glfwSetMouseButtonCallback(window,Mouse::mouseButton);
	    glfwSetCursorPosCallback(window,Mouse::mouseMove);
	    
	    glfwMakeContextCurrent(window);
	    GL.createCapabilities();
	    
	    glfwSwapInterval(1);
	    
		load(sm);
	    MainLoop.init(sm);
	    
	    glfwShowWindow(window);
	    
	    MainLoop.reshape(window,WIDTH,HEIGHT);
	    
	    glfwSetWindowSizeCallback(window,MainLoop::reshape);
	    
	    running = true;
	    
	    while (!glfwWindowShouldClose(window) && running) 
	    {
	    	MainLoop.display(sm);
	    	
	    	glfwSwapBuffers(window);
	    	glfwPollEvents();
	    }
	    
	    stop();	
	}
	
	private static GLFWImage.Buffer icon(String path)
	{
		try (MemoryStack stack = MemoryStack.stackPush()) 
		{
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			
			ByteBuffer imgbuff = Util.loadImageToBuffer(Presentation.class.getResourceAsStream(path),w,h,comp);
			GLFWImage image = GLFWImage.malloc(); 
			GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
			image.set(w.get(),h.get(),imgbuff);
			imagebf.put(0, image);
			
			return imagebf;
		}
	}
	
	static IntBuffer lastx,lasty,lastw,lasth;
	public static void Fullscreen(boolean b)
	{
		long monitor = glfwGetPrimaryMonitor();
		GLFWVidMode mode = glfwGetVideoMode(monitor);
		
	    if (fullscreen)
	    {
			lastx = BufferUtils.createIntBuffer(1);
			lasty = BufferUtils.createIntBuffer(1);
			lastw = BufferUtils.createIntBuffer(1);
			lasth = BufferUtils.createIntBuffer(1);
			
	    	glfwGetWindowPos(window, lastx, lasty);
	    	glfwGetWindowSize(window, lastw, lasth);
	    	
	    	glfwSetWindowMonitor(window,monitor,0,0,mode.width(),mode.height(),GLFW_DONT_CARE);
	    }
	    else
	    {
	        // restore last window size and position
	        glfwSetWindowMonitor(window, NULL, lastx.get(),lasty.get(),lastw.get(),lasth.get(),GLFW_DONT_CARE);
	    }
	}
	
	private static void load(SlideManager sm)
	{		
	    sm.load();
	}
	
	public static void stop()
	{
		if (running)
		{
			running = false;
			MainLoop.dispose(sm);
			
			glfwDestroyWindow(window);
			window = NULL;
			glfwTerminate();
		}
	}

}
