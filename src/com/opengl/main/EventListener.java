package com.opengl.main;

import com.opengl.graphics.Screen;
import com.opengl.input.Keyboard;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.opengl.resource.ImageResource;
import com.opengl.resource.SlideResource;

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
		
		Presentation.sm.update();
		Presentation.sm.render(gl);
		
		Keyboard.update();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) 
	{
		for (int i = 0;i < Presentation.slide.size();i++)
		{
			Presentation.slide.get(0).image.getTexture().destroy(gl);
			Presentation.slide.get(0).image.image.flush();
		}
		
		Presentation.slide.clear();
		
		Presentation.animator.stop();
		//Presentation.window.destroy();
	}

	@Override
	public void init(GLAutoDrawable drawable) 
	{
		gl = drawable.getGL().getGL2();
		gl.glClearColor(0, 0, 0, 1);
		
		gl.glEnable(GL2.GL_TEXTURE_2D);
		
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		
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
