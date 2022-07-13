package com.ope.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public class FallbackRenderer extends Renderer
{
	public static void resize()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glOrtho(0,size.x,size.y,0,-1, 1);
		glMatrixMode(GL_MODELVIEW);
	}
	
	public static void setViewMatrix(Matrix4f matrix)
	{
		 try (MemoryStack stack = MemoryStack.stackPush())
		 {
			 FloatBuffer matrixbuffer = stack.mallocFloat(16);
			 matrix.get(matrixbuffer);
			 
			 glLoadMatrixf(matrixbuffer);
		 }
	}
	
	public static void frect(float x,float y,float width,float height,Vector4f color)
	{
		glColor4f(color.x,color.y,color.z,color.w);
		
		glTranslatef(x,y,0);
		glRotatef(angleX,1,0,0);
		glRotatef(angleY,0,1,0);
		glRotatef(angleZ,0,0,1);
		
		glBegin(GL_QUADS);
		
		glVertex2f(0,0);
		glVertex2f(width,0);
		glVertex2f(width,height);
		glVertex2f(0, height);
			
		glEnd();
		
		glTranslatef(-x,-y,0);
		glRotatef(-angleX,1,0,0);
		glRotatef(-angleY,0,1,0);
		glRotatef(-angleZ,0,0,1);
		glFlush();
		
		reset();
	}
	
	public static void frectnofill(float x,float y,float width,float height,Vector4f color)
	{
		glColor4f(color.x,color.y,color.z,color.w);
		
		glTranslatef(x,y,0);
		glRotatef(angleX,1,0,0);
		glRotatef(angleY,0,1,0);
		glRotatef(angleZ,0,0,1);
		
		glBegin(GL_LINE_LOOP);
		
		glVertex2f(0,0);
		glVertex2f(width,0);
		glVertex2f(width,height);
		glVertex2f(0, height);
			
		glEnd();
		
		glTranslatef(-x,-y,0);
		glRotatef(-angleX,1,0,0);
		glRotatef(-angleY,0,1,0);
		glRotatef(-angleZ,0,0,1);
		
		glFlush();
		
		reset();
	}
	
	public static void drawImage(Texture image,float x,float y,float width,float height)
	{		
		glEnable(GL_TEXTURE_2D);
		
		image.bind();
		
		glTranslatef(x,y,0);
		glRotatef(angleX,1,0,0);
		glRotatef(angleY,0,1,0);
		glRotatef(angleZ,0,0,1);
		
		glColor4f(1,1,1,1);
		glBegin(GL_QUADS);
		
		glTexCoord2f(0,0);
		glVertex2f(0,0);
		
		glTexCoord2f(1, 0);
		glVertex2f(width,0);
		
		glTexCoord2f(1, 1);
		glVertex2f(width, height);
		
		glTexCoord2f(0, 1);
		glVertex2f(0,height);
		
		image.unbind();
		
		glEnd();
		
		glTranslatef(-x,-y,0);
		glRotatef(-angleX,1,0,0);
		glRotatef(-angleY,0,1,0);
		glRotatef(-angleZ,0,0,1);
		
		glDisable(GL_TEXTURE_2D);
		glFlush();
		
		reset();
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
		
		reset();
	}
}
