package com.presentation.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import com.presentation.resource.ImageResource;
public class Renderer
{	
	public static void frect(float x,float y,float width,float height,Color color)
	{
		
		glColor4f((float) color.getRed() / 255,(float) color.getGreen() / 255,(float) color.getBlue() / 255,(float) color.getAlpha() / 255);
		glBegin(GL_QUADS);
		
		glVertex2f(x, y);
		glVertex2f(x + width,y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
			
		glEnd();
		glFlush();
	}
	
	public static void frectnofill(float x,float y,float width,float height,Color color)
	{
		
		glColor4f((float) color.getRed() / 255,(float) color.getGreen() / 255,(float) color.getBlue() / 255,(float) color.getAlpha() / 255);
		
		glBegin(GL_LINE_LOOP);
		
		glVertex2f(x, y);
		glVertex2f(x + width,y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
			
		glEnd();
		glFlush();
	}
	
	public static void drawImage(ImageResource image,float x,float y,float width,float height)
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
	}
	
	public static void drawVerticalGradient(float x,float y,float width,float height,Color color,Color color2)
	{
		glBegin(GL_QUADS);
		
		glColor4f((float) color.getRed() / 255,(float) color.getGreen() / 255,(float) color.getBlue() / 255,(float) color.getAlpha() / 255);
		glVertex2f(x, y);
		glVertex2f(x + width,y);
		glColor4f((float) color2.getRed() / 255,(float) color2.getGreen() / 255,(float) color2.getBlue() / 255,(float) color2.getAlpha() / 255);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
			
		glEnd();
		glFlush();
	}

}