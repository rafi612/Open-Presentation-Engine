/* Copyright 2019-2020 by rafi612 */
package com.presentation.main;

import com.io.Stream;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.presentation.graphics.Screen;
import com.presentation.input.Keyboard;
import com.presentation.resource.ImageResource;
import com.presentation.resource.SlideResource;

public class EventListener implements GLEventListener
{
	public static GL2 gl = null;
	
	@Override
	public void display(GLAutoDrawable drawable)
	{
		gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);		
		
		if (Keyboard.getKeyOnce(KeyEvent.VK_ESCAPE))
			Presentation.stop();
		
		if (Keyboard.getKeyOnce(KeyEvent.VK_F11))
		{
			Presentation.fullscreen = !Presentation.fullscreen;
			Presentation.window.setFullscreen(Presentation.fullscreen);
		}
		
		Presentation.sm.update();
		Presentation.sm.render(gl);
		
		Keyboard.update();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) 
	{
		for (int i = 0;i < Presentation.slide.size();i++)
		{
			Presentation.slide.get(i).image.getTexture().destroy(gl);
			Presentation.slide.get(i).image.image.flush();
		}
		
		Presentation.slide.clear();
		Presentation.animator.stop();
	}

	@Override
	public void init(GLAutoDrawable drawable) 
	{
		gl = drawable.getGL().getGL2();
		gl.glClearColor(0, 0, 0, 1);
		
		gl.glEnable(GL2.GL_TEXTURE_2D);
		
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		
		System.out.println("========================================");
		System.out.println("Open Presentation Engine by rafi612");
		System.out.println("========================================");
		System.out.println("System: " + Stream.getOsName());
		System.out.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
		System.out.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
		System.out.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));
		System.out.println("========================================");
		for (int i = 0;i < SlideResource.slides;i++)
		{
			System.out.println("Slide " + (i + 1) + " Image: " + Presentation.slide.get(i).imagepath);
			System.out.println("Slide " + (i + 1) + " Bg: " + Presentation.slide.get(i).bgpath);
			System.out.println("Slide " + (i + 1) + " TTS: " + Presentation.slide.get(i).ttspath);
			System.out.println("Slide " + (i + 1) + " Start animation: " + Presentation.slide.get(i).startanimation);
			System.out.println("Slide " + (i + 1) + " Exit animation: " + Presentation.slide.get(i).exitanimation);
			System.out.println("=============================");
		}
		
		//System.out.println(gl.glGetString(GL.GL_VENDOR) + "," + gl.glGetString(GL.GL_RENDERER) + "," + gl.glGetString(GL.GL_VERSION));
		
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) 
	{
		gl = drawable.getGL().getGL2();
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glOrtho(0,1280,720,0,-1, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

}
