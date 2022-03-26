/* Copyright 2019-2020 by rafi612 */
package com.presentation.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import com.audio.Sound;
import com.io.Util;
import com.main.Main;
import com.presentation.input.Keyboard;
import com.presentation.input.Mouse;
import com.presentation.resource.ImageResource;
import com.presentation.slide.SlideManager;
import com.project.Project;

public class Presentation 
{
	
	public static String generalMusic = "null";
	public static boolean fullscreen = false;
	public static int TTSKeyCode = -1;
	
	//info
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	public static final String TITLE = "OPE Presentation";
	
	//okno
	public static long window = NULL;
	
	public static SlideManager sm;
	
	public static boolean running = false;
	
	public static void init()
	{
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit())
			System.err.println("Error Init GLFW");

	    glfwDefaultWindowHints();
	    
	    glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
	    glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
	}
	
	public static void start()
	{
		if (window != NULL)
			stop();
		
	    window = glfwCreateWindow(1280, 720, TITLE, NULL, NULL);
	    if (window == NULL)
	      throw new RuntimeException("Failed to create the GLFW window");
        
	    GLFWImage.Buffer icon = icon("/images/icon.png");
	    glfwSetWindowIcon(window,icon);
	    
	    glfwSetKeyCallback(window, new Keyboard());
	    glfwSetMouseButtonCallback(window, (wind,button,action,mods) -> Mouse.mouseButton(button, action, mods));
	    glfwSetCursorPosCallback(window,(wind,xpos,ypos) -> Mouse.mouseMove(wind, xpos, ypos));
	    
	    glfwMakeContextCurrent(window);
	    GL.createCapabilities();
	    
	    glfwSwapInterval(1);
	    
		sm = new SlideManager();
		load();
	    
	    EventListener.init();
	    
	    glfwShowWindow(window);
	    
	    EventListener.reshape(window,WIDTH,HEIGHT);
	    
	    glfwSetWindowSizeCallback(window, (window,w,h) -> EventListener.reshape(window, w, h));
	    
	    running = true;
	    
	    while (!glfwWindowShouldClose(window) && running) 
	    {
	    	EventListener.display();
	    	
	    	glfwSwapBuffers(window);
	    	glfwPollEvents();
	    }
	    
	    stop();
	
		
	}
	
	private static GLFWImage.Buffer icon(String path)
	{
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
		
		ByteBuffer imgbuff = ImageResource.load_image(Presentation.class.getResourceAsStream(path),w,h,comp);
		GLFWImage image = GLFWImage.malloc(); 
		GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
        image.set(w.get(),h.get(),imgbuff);
        imagebf.put(0, image);
        
        return imagebf;
	}
	
	static IntBuffer lastx,lasty,lastw,lasth;
	public static void Fullscreen(boolean b)
	{
		long monitor = glfwGetPrimaryMonitor();
		GLFWVidMode mode = glfwGetVideoMode(monitor);
		
	    if ( fullscreen )
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
	
	private static void load()
	{
		String path;
		if (Main.args.length < 1)
			path = Project.projectlocation + File.separator;
		else path = "";
		
		String config = path + "config.xml";
		
		sm.slides = Integer.parseInt(Util.readXml(config, "summary", "slides"));
		generalMusic = Util.readXml(config, "summary", "general_music");
		fullscreen = Boolean.parseBoolean(Util.readXml(config, "summary", "fullscreen"));
		
		String key = Util.readXml(config, "summary", "ttskey");
		if (!key.equals("auto"))
			TTSKeyCode = KeyEvent.getExtendedKeyCodeForChar(key.charAt(0));
		else TTSKeyCode = -1;
		
		
	    sm.load();
	}
	
	public static void stop()
	{
		if (running)
		{
			running = false;
			EventListener.dispose();
			
			glfwDestroyWindow(window);
			window = NULL;
			glfwTerminate();
		}
	}

}
