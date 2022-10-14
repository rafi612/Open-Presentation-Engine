/* Copyright 2019-2020 by rafi612 */
package com.ope.presentation.input;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.IntBuffer;

import org.lwjgl.system.MemoryStack;

public class Mouse
{
	public static int X,Y,Xpixel,Ypixel;
	public static boolean button_left,button_right,button_clicked_left,button_clicked_right;
	private static boolean b1 = false,b2 = false;
	
	public static int screenWidth = 1280,screenHeight = 720;

	public static void update() 
	{
		if(!b1 && button_left)
		{
			b1 = true;
			button_clicked_left = true;
		}
		else
			button_clicked_left = false;
		
		if(!button_left) 
			b1 = false;

		if(!b2 && button_right)
		{
			b2 = true;
			button_clicked_right = true;
		}
		else
			button_clicked_right = false;
			
		if(!button_right)
			b2 = false;
	
	}
	
	public static void mouseButton(long window,int button, int action, int mods)
	{
		boolean stat = false;
		if (action == GLFW_PRESS)
			stat = true;
		
		if (button == GLFW_MOUSE_BUTTON_1)
			button_left = stat;
		else if (button == GLFW_MOUSE_BUTTON_2)
			button_right = stat;
	}
	
    public static void mouseMove(long window, double xpos, double ypos) 
    {
    	try (MemoryStack stack = MemoryStack.stackPush())
    	{
			X = (int)xpos;
			Y = (int)ypos;
			
			IntBuffer wbuf = stack.mallocInt(1);
			IntBuffer hbuf = stack.mallocInt(1);
			glfwGetWindowSize(window, wbuf, hbuf);
			int w = wbuf.get();
			int h = hbuf.get();
			
			float aspectRatio = (float)w / (float)h;
			float targetAspect = (float)screenWidth / (float)screenHeight;
			
			if (aspectRatio >= targetAspect)
			{
				float calculatedW = (targetAspect / aspectRatio ) * w;
				float x = (int)((w / 2) - (calculatedW / 2));
				
				Xpixel = (int)(((xpos-x)*((screenWidth) / calculatedW)));
				Ypixel = (int)((ypos*((screenHeight) / ((float)h))));
			}
			else
			{
		        float calculatedH = (aspectRatio / targetAspect) * h;
		        float y = (int)((h/ 2) - (calculatedH / 2));
		        
				Xpixel = (int)(xpos*((screenWidth) / ((float)w)));
				Ypixel = (int)((ypos-y)*((screenHeight) / calculatedH));
			}
    	}
    }

}
