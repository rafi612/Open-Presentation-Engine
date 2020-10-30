/* Copyright 2019-2020 by rafi612 */
package com.opengl.graphics;

import java.awt.Color;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.opengl.main.EventListener;
import com.opengl.resource.ImageResource;

public class Screen
{
	public static void frect(float x,float y,float width,float height,Color color)
	{	
		GL2 gl = EventListener.gl;
		
		gl.glDisable(GL2.GL_TEXTURE_2D);
		
		gl.glColor4f((float) color.getRed() / 255,(float) color.getGreen() / 255,(float) color.getBlue() / 255,(float) color.getAlpha() / 255);
		gl.glBegin(GL2.GL_QUADS);
		
		gl.glVertex2f(x, y);
		gl.glVertex2f(x + width,y);
		gl.glVertex2f(x + width, y + height);
		gl.glVertex2f(x, y + height);
			
		gl.glEnd();
		gl.glFlush();
		
		gl.glEnable(GL2.GL_TEXTURE_2D);
	}
	
	public static void drawImage(ImageResource image,float x,float y,float width,float height)
	{
		GL2 gl = EventListener.gl;
		
		Texture tex = image.getTexture();
		
		if (tex != null) 
		{
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex.getTextureObject());
		}
		
		gl.glColor4f(1,1,1,1);
		gl.glBegin(GL2.GL_QUADS);
		
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
		
		gl.glTexCoord2f(0,0);
		gl.glVertex2f(x, y);
		
		gl.glTexCoord2f(1, 0);
		gl.glVertex2f(x + width,y);
		
		gl.glTexCoord2f(1, 1);
		gl.glVertex2f(x + width, y + height);
		
		gl.glTexCoord2f(0, 1);
		gl.glVertex2f(x, y + height);
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		
		gl.glEnd();
		gl.glFlush();
	}
	
	public static void drawColoredImage(ImageResource image,float x,float y,float width,float height,Color color)
	{
		GL2 gl = EventListener.gl;
		
		Texture tex = image.getTexture();
		
		if (tex != null) 
		{
			gl.glBindTexture(GL2.GL_TEXTURE_2D, tex.getTextureObject());
		}
		
		gl.glColor4f((float) color.getRed() / 255,(float) color.getGreen() / 255,(float) color.getBlue() / 255,(float) color.getAlpha() / 255);
		gl.glBegin(GL2.GL_QUADS);
		
		gl.glTexCoord2f(0,0);
		gl.glVertex2f(x, y);
		
		gl.glTexCoord2f(1, 0);
		gl.glVertex2f(x + width,y);
		
		gl.glTexCoord2f(1, 1);
		gl.glVertex2f(x + width, y + height);
		
		gl.glTexCoord2f(0, 1);
		gl.glVertex2f(x, y + height);
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		
		gl.glEnd();
		gl.glFlush();
	}

}
