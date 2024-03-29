/* Copyright 2019-2020 by rafi612 */
package com.ope.viewer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.xml.sax.SAXException;

import com.ope.audio.Sound;
import com.ope.io.Util;
import com.ope.io.xml.Tag;
import com.ope.io.xml.XmlReader;
import com.ope.project.Project;
import com.ope.viewer.input.Keyboard;
import com.ope.viewer.input.Mouse;
import com.ope.viewer.slide.SlideManager;

public class Presentation 
{
	public static boolean fullscreen = false;
	
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	public static final String TITLE = "OPE Presentation";
	
	public static long window = NULL;
	
	public static boolean running = false;
	
	private static SlideManager sm;
	
	public static void start(String projectpath)
	{
		try
		{
			//setting project location
			Project.projectLocation = new File(projectpath).getCanonicalPath();
			
			if (!new File(Util.projectPath(Project.PROJECT_XML_NAME)).exists())
			{
				System.err.println("ERROR: Folder " + Project.projectLocation + " is not OPE project folder");
				System.exit(1);
			}
			
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
			
			glfwGetWindowPos(window, lastx, lasty);
			glfwGetWindowSize(window, lastw, lasth);
			
			glfwMakeContextCurrent(window);
			GL.createCapabilities();
			
			glfwSwapInterval(1);
			
			sm = new SlideManager();
			
			MainLoop.init(sm);
			
			load();
			
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
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void load()
	{
		try 
		{
			XmlReader xml = new XmlReader(Util.projectPath(Project.PROJECT_XML_NAME));
			
			Tag global = xml.getTags("global")[0];
			
			setFullscreen(Boolean.parseBoolean(global.getAttribute("fullscreen")));
			
			sm.load(xml);
		}
		catch (IOException | SAXException e) 
		{
			tinyfd_messageBox("Error","Error loading presentation: " 
					//replace quotes to prevent tinyfd errors
					+ e.getMessage().replaceAll("\"", ""),
					"ok",
					"error",
					true);
			stop();
			System.exit(-1);
		}

	}
	
	private static GLFWImage.Buffer icon(String path) throws IOException
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
	
	private static IntBuffer lastx = BufferUtils.createIntBuffer(1),
		lasty = BufferUtils.createIntBuffer(1),
		lastw = BufferUtils.createIntBuffer(1),
		lasth = BufferUtils.createIntBuffer(1);
	
	public static void setFullscreen(boolean full)
	{
		long monitor = glfwGetPrimaryMonitor();
		GLFWVidMode mode = glfwGetVideoMode(monitor);
		
		if (full)
		{
			lastx = BufferUtils.createIntBuffer(1);
			lasty = BufferUtils.createIntBuffer(1);
			lastw = BufferUtils.createIntBuffer(1);
			lasth = BufferUtils.createIntBuffer(1);
			
			fullscreen = true;
			glfwGetWindowPos(window, lastx, lasty);
			glfwGetWindowSize(window, lastw, lasth);
			glfwSetWindowMonitor(window,monitor,0,0,mode.width(),mode.height(),GLFW_DONT_CARE);
		}
		else
		{
			fullscreen = false;
			
			// restore last window size and position
			glfwSetWindowMonitor(window, NULL, lastx.get(),lasty.get(),lastw.get(),lasth.get(),GLFW_DONT_CARE);
		}
	}
	
	
	public static void stop()
	{
		if (running)
		{
			running = false;
			MainLoop.dispose(sm);
			
			glfwDestroyWindow(window);
			glfwTerminate();
		}
	}

}
