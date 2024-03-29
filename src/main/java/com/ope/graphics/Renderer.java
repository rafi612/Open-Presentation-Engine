package com.ope.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

public class Renderer 
{
	static boolean fallback = false;
	
	static Shader texture_shader,rect_shader,gradient_shader,mix_shader;
	
	static Matrix4f projectionMatrix,viewMatrix;
	public static final Matrix4f EMPTY_VIEW_MATRIX = new Matrix4f();
	
	static Mesh quad,line_strip;
	
	static float angleX,angleY,angleZ;
	static Vector2i size;
	
	public static void init(int width,int height) 
	{
		size = new Vector2i(width,height);
		
		projectionMatrix = new Matrix4f().ortho(0, width, height, 0, -width,height);
		viewMatrix = new Matrix4f();
		
		if (!isFallback())
		{
			texture_shader = new Shader("/shaders/texture_vs.glsl","/shaders/texture_fs.glsl");
			rect_shader = new Shader("/shaders/rect_vs.glsl","/shaders/rect_fs.glsl");
			gradient_shader = new Shader("/shaders/gradient_vs.glsl","/shaders/gradient_fs.glsl");
			mix_shader = new Shader("/shaders/mixtextures_vs.glsl","/shaders/mixtextures_fs.glsl");
			
			// configure VAO/VBO
			float[] vertices = { 
				// pos 
				0.0f, 1.0f, 0.0f,
				1.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 0.0f,
				1.0f, 1.0f, 0.0f
			};
			
			// configure tex coords
			float[] tex = { 
				0.0f, 1.0f,
				1.0f, 0.0f,
				
				0.0f, 0.0f, 
				1.0f, 1.0f,
			};
			
			int[] indices = {  // note that we start from 0!
					0, 2, 1,   // first triangle
					0, 3, 1	// second triangle
			}; 
			
			quad = new Mesh(vertices,indices);
			quad.storeInAttributes(1, 2, tex);
			
			float[] line_vertecies = { 
					// pos
					1.0f, 0.0f, 0.0f,
					0.0f, 0.0f, 0.0f,
					0.0f, 1.0f, 0.0f,
					1.0f, 1.0f, 0.0f,
			};
			
			int[] line_indices = {
					0,1,
					2,3,
					3,0
			};
			
			line_strip = new Mesh(line_vertecies,line_indices);
		}
	}
	
	public static Vector2i getSize()
	{
		return size;
	}
	
	public static void setSize(int x,int y)
	{
		size.set(x, y);
	}
	
	public static Matrix4f getViewMatrix()
	{
		return viewMatrix;
	}
	
	public static void setViewMatrix(Matrix4f matrix)
	{
		if (isFallback())
		{
			FallbackRenderer.setViewMatrix(matrix);
			return;
		}
		viewMatrix.set(matrix);
	}
	
	public static Matrix4f getProjectionMatrix()
	{
		return projectionMatrix;
	}
	
	public static void setProjectionMatrix(Matrix4f matrix)
	{
		projectionMatrix.set(matrix);
	}
	
	public static void rotateX(float ang)
	{
		angleX = (float)Math.toRadians(ang);
	}
	
	public static void rotateY(float ang)
	{
		angleY = (float)Math.toRadians(ang);
	}
	
	public static void rotateZ(float ang)
	{
		angleZ = (float)Math.toRadians(ang);
	}
	
	public static void reset()
	{
		angleX = 0;
		angleY = 0;
		angleZ = 0;
	}

	public static boolean isFallback()
	{
		return !GL.getCapabilities().OpenGL30 || fallback;
	}
	
	public static void fallbackResize()
	{
		FallbackRenderer.resize();
	}
	
	public static void clearColor(Vector4f color)
	{
		glClearColor(color.x,color.y,color.z,color.w);
	}
	
	public static Vector4f color(int color)
	{
		float red = (float)((color >> 16) & 0xFF);
		float green = (float)((color >> 8) & 0xFF);
		float blue = (float)(color & 0xFF);
		float alpha = (float)((color >> 24) & 0xFF);
		
		return new Vector4f(red/255,green/255,blue/255,alpha/255);
	}
	
	public static void drawImage(Texture tex,float x,float y,float w,float h)
	{
		if (isFallback())
		{
			FallbackRenderer.drawImage(tex, x, y, w, h);
			return;
		}
		
		Matrix4f transformMatrix = new Matrix4f()
				.translate(x,y,1.0f)
				.translate(0.5f * w, 0.5f * h, 0.0f)
				
				.rotate(angleX, 1.0f,0.0f,0.0f)
				.rotate(angleY, 0.0f,1.0f,0.0f)
				.rotate(angleZ, 0.0f,0.0f,1.0f)
				
				.translate(-0.5f * w, -0.5f * h, 0.0f)
				.scale(w,h, 0);
		
		texture_shader.use();
		texture_shader.setMatrix4("transformMatrix", transformMatrix);
		texture_shader.setMatrix4("viewMatrix", viewMatrix);
		texture_shader.setMatrix4("projectionMatrix", projectionMatrix);
		
		glActiveTexture(GL_TEXTURE0);
		
		tex.bind();
		quad.draw();
		tex.unbind();
		
		reset();
	}
	
	public static void frect(float x,float y,float w,float h,Vector4f color)
	{
		if (isFallback())
		{
			FallbackRenderer.frect(x, y, w, h, color);
			return;
		}
		Matrix4f transformMatrix = new Matrix4f()
				.translate(x,y,1.0f)
				.translate(0.5f * w, 0.5f * h, 0.0f)
				
				.rotate(angleX, 1.0f,0.0f,0.0f)
				.rotate(angleY, 0.0f,1.0f,0.0f)
				.rotate(angleZ, 0.0f,0.0f,1.0f)
				
				.translate(-0.5f * w, -0.5f * h, 0.0f)
				.scale(w,h,0);
		
		rect_shader.use();
		rect_shader.setMatrix4("transformMatrix", transformMatrix);
		rect_shader.setMatrix4("viewMatrix", viewMatrix);
		rect_shader.setMatrix4("projectionMatrix", projectionMatrix);
		rect_shader.setVector4f("color", color);
		
		quad.draw();
		
		reset();
	}
	
	public static void mix2Textures(int tex1,int tex2,float mix,float x,float y,float w,float h)
	{	    
	    glActiveTexture(GL_TEXTURE0);
	    glBindTexture(GL_TEXTURE_2D, tex1);
	    
	    glActiveTexture(GL_TEXTURE1);
	    glBindTexture(GL_TEXTURE_2D, tex2);
	    
		Matrix4f transformMatrix = new Matrix4f()
				.translate(0,0,1.0f)
				.scale(w,h,0);
		
		mix_shader.use();
		mix_shader.setMatrix4("transformMatrix", transformMatrix);
		mix_shader.setMatrix4("viewMatrix", new Matrix4f());
		mix_shader.setMatrix4("projectionMatrix", Renderer.getProjectionMatrix());
		
		mix_shader.setInt("image", 0);
		mix_shader.setInt("image2", 1);
		mix_shader.setFloat("mixAmount",mix);
		
		quad.draw();
		
		reset();
	}
	
	public static void frectnofill(float x,float y,float w,float h,Vector4f color)
	{
		if (isFallback())
		{
			FallbackRenderer.frectnofill(x, y, w, h, color);
			return;
		}
		
		Matrix4f transformMatrix = new Matrix4f()
				.translate(x,y,1.0f)
				.translate(0.5f * w, 0.5f * h, 0.0f)
				
				.rotate(angleX, 1.0f,0.0f,0.0f)
				.rotate(angleY, 0.0f,1.0f,0.0f)
				.rotate(angleZ, 0.0f,0.0f,1.0f)
				
				.translate(-0.5f * w, -0.5f * h, 0.0f)
				.scale(w,h, 0);
		
		rect_shader.use();
		rect_shader.setMatrix4("transformMatrix", transformMatrix);
		rect_shader.setMatrix4("viewMatrix", viewMatrix);
		rect_shader.setMatrix4("projectionMatrix", projectionMatrix);
		rect_shader.setVector4f("color", color);
		
		line_strip.drawLines();
		
		reset();
	}
	public static void drawVerticalGradient(float x,float y,float w,float h,Vector4f color,Vector4f color2)
	{
		if (isFallback())
		{
			FallbackRenderer.drawVerticalGradient(x, y, w, h, color, color2);
			return;
		}
		
		Matrix4f transformMatrix = new Matrix4f()
				.translate(x,y,1.0f)
				.translate(0.5f * w, 0.5f * h, 0.0f)
				.rotate(angleZ, 0.0f,1.0f,0.0f)
				.translate(-0.5f * w, -0.5f * h, 0.0f)
				.scale(w,h,0);
		
		gradient_shader.use();
		gradient_shader.setMatrix4("transformMatrix", transformMatrix);
		gradient_shader.setMatrix4("viewMatrix", viewMatrix);
		gradient_shader.setMatrix4("projectionMatrix", projectionMatrix);
		gradient_shader.setVector4f("color1", color);
		gradient_shader.setVector4f("color2", color2);
		gradient_shader.setVector2f("size", new Vector2f(w,h));
		
		quad.draw();
		
		reset();
	}
	
}
