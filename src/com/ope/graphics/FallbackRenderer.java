package com.ope.graphics;

import static org.lwjgl.opengl.GL11.*;
import org.joml.Vector4f;

public class FallbackRenderer 
{
	public static void resize()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glOrtho(0,1280,720,0,-1, 1);
		glMatrixMode(GL_MODELVIEW);
	}
	
	public static void frect(float x,float y,float width,float height,Vector4f color)
	{
		glColor4f(color.x,color.y,color.z,color.w);
		glBegin(GL_QUADS);
		
		glVertex2f(x, y);
		glVertex2f(x + width,y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
			
		glEnd();
		glFlush();
		
		Renderer.reset();
	}
	
	public static void frectnofill(float x,float y,float width,float height,Vector4f color)
	{
		glColor4f(color.x,color.y,color.z,color.w);
		
		glBegin(GL_LINE_LOOP);
		
		glVertex2f(x, y);
		glVertex2f(x + width,y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
			
		glEnd();
		glFlush();
		
		Renderer.reset();
	}
	
	public static void drawImage(Texture image,float x,float y,float width,float height)
	{		
		glEnable(GL_TEXTURE_2D);
		
		if (image.id == 0)
			image.bind();
		
		glBindTexture(GL_TEXTURE_2D, image.id);
		
		glColor4f(1,1,1,1);
		glBegin(GL_QUADS);
		
		glTexCoord2f(0,0);
		glVertex2f(x, y);
		
		glTexCoord2f(1, 0);
		glVertex2f(x + width,y);
		
		glTexCoord2f(1, 1);
		glVertex2f(x + width, y + height);
		
		glTexCoord2f(0, 1);
		glVertex2f(x, y + height);
		
		glBindTexture(GL_TEXTURE_2D, 0);
		
		glEnd();
		
		glDisable(GL_TEXTURE_2D);
		glFlush();
		
		Renderer.reset();
	}
	
	public static void drawVerticalGradient(float x,float y,float width,float height,Vector4f color,Vector4f color2)
	{
		glBegin(GL_QUADS);
		
		glColor4f(color.x,color.y,color.z,color.w);
		glVertex2f(x, y);
		glVertex2f(x + width,y);
		glColor4f(color2.x,color2.y,color2.z,color2.w);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
			
		glEnd();
		glFlush();
		
		Renderer.reset();
	}
}
