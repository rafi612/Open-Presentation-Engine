/* Copyright 2019-2020 by rafi612 */
package com.ope.presentation.input;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class Keyboard extends GLFWKeyCallback
{
	private static final int Count = 385;
	private static boolean keys[] = new boolean[Count];
	private static boolean keys_prev[] = new boolean[Count];

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods)
	{
		if (key == GLFW_KEY_UNKNOWN)
			return;
		
		if (action == GLFW_PRESS)
			keys[key] = true;
		else if (action == GLFW_RELEASE)
			keys[key] = false;
	}
	
	public static void update()
	{
		for(int i = 0; i < Count;i++){
			if(!keys[i])
				keys_prev[i] = false;
		}
	}
	
	public static boolean getKey(int Key)
	{
		return keys[Key];
	}
	
	public static boolean getKeyOnce(int Key)
	{
		if(!keys_prev[Key] && keys[Key]){
			keys_prev[Key] = true;
			return true;
		}
		
		return false;
	}

}
