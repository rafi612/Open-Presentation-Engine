package com.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Renderer 
{
    static Shader texture_shader,rect_shader,gradient_shader;
    
    static float angle;
    
    static Matrix4f projection = new Matrix4f().ortho(0, 1280, 720, 0, -1280,1280);
    
    static Mesh quad,line_strip;
    
	public static void init() 
	{
		texture_shader = new Shader("/shaders/texture.vs","/shaders/texture.fs");
		rect_shader = new Shader("/shaders/rect.vs","/shaders/rect.fs");
		gradient_shader = new Shader("/shaders/gradient.vs","/shaders/gradient.fs");
		
	    // configure VAO/VBO
	    float vertices[] = { 
	        // pos 
	        0.0f, 1.0f, 0.0f,
	        1.0f, 0.0f, 0.0f,
	        0.0f, 0.0f, 0.0f,
	        1.0f, 1.0f, 0.0f
	    };
	    
	    // configure tex coords
	    float tex[] = { 
	        0.0f, 1.0f,
	        1.0f, 0.0f,
	        
	        0.0f, 0.0f, 
	        1.0f, 1.0f,
	    };
	    
	    int indices[] = {  // note that we start from 0!
	    	    0, 2, 1,   // first triangle
	    	    0, 3, 1    // second triangle
	    }; 
	    
	    quad = new Mesh(vertices,indices);
	    quad.storeInAttributes(1, 2, tex);
	    
	    float line_vertecies[] = { 
		        // pos
		        1.0f, 0.0f, 0.0f,
		        0.0f, 0.0f, 0.0f,
		        0.0f, 1.0f, 0.0f,
		        1.0f, 1.0f, 0.0f,
		};
	    
	    int line_indices[] = {
	    	    0,1,
	    	    2,3,
	    	    3,0
	    };
	    
	    line_strip = new Mesh(line_vertecies,line_indices);
	}
	
	public static void rotate(float ang)
	{
		angle = (float)Math.toRadians(ang);
	}
	
	public static Vector4f color(int color)
	{
		float red = (float)((color >> 16) & 0xFF);
		float green = (float)((color >> 8) & 0xFF);
		float blue = (float)(color & 0xFF);
		float alpha = (float)((color >> 24) & 0xFF);
		
		return new Vector4f(red/255,green/255,blue/255,alpha/255);
	}
	
	public static void reset()
	{
		angle = 0;
	}
	
	public static void drawImage(Texture tex,float x,float y,float w,float h)
	{
		Matrix4f model = new Matrix4f()
				.translate(x,y,1.0f)
				.translate(0.5f * w, 0.5f * h, 0.0f)
				.rotate(angle, 0.0f,0.0f,1.0f)
				.translate(-0.5f * w, -0.5f * h, 0.0f)
				.scale(w,h, 0);
		
		texture_shader.use();
		texture_shader.setMatrix4("model", model);
		texture_shader.setVector4f("spriteColor", new Vector4f(1,1,1,1));
		texture_shader.setMatrix4("projection", projection);
		
		glActiveTexture(GL_TEXTURE0);
		
		tex.bind();
		quad.draw();
		
	    glBindTexture(GL_TEXTURE_2D, 0);
	    
	    reset();
	}
	
	public static void frect(float x,float y,float w,float h,Vector4f color)
	{
		Matrix4f model = new Matrix4f()
				.translate(x,y,1.0f)
				.translate(0.5f * w, 0.5f * h, 0.0f)
				.rotate(angle, 0.0f,1.0f,0.0f)
				.translate(-0.5f * w, -0.5f * h, 0.0f)
				.scale(w,h,0);
		
		rect_shader.use();
		rect_shader.setMatrix4("model", model);
		rect_shader.setVector4f("rectColor", color);
		rect_shader.setMatrix4("projection", projection);
	    
	    quad.draw();
		
	    reset();
	}
	
	public static void frectnofill(float x,float y,float w,float h,Vector4f color)
	{
		Matrix4f model = new Matrix4f()
				.translate(x,y,1.0f)
				.translate(0.5f * w, 0.5f * h, 0.0f)
				.rotate(angle, 0.0f,0.0f,1.0f)
				.translate(-0.5f * w, -0.5f * h, 0.0f)
				.scale(w,h, 0);
		
		rect_shader.use();
		rect_shader.setMatrix4("model", model);
		rect_shader.setVector4f("rectColor", color);
		rect_shader.setMatrix4("projection", projection);
		
		glBindVertexArray(line_strip.VAO);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, line_strip.EBO);
	    glDrawElements(GL_LINE_STRIP,line_strip.indices_size,GL_UNSIGNED_INT,0);
	    
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	    glBindVertexArray(0);
	    
	    reset();
	}
	public static void drawVerticalGradient(float x,float y,float w,float h,Vector4f color,Vector4f color2)
	{
		Matrix4f model = new Matrix4f()
				.translate(x,y,1.0f)
				.translate(0.5f * w, 0.5f * h, 0.0f)
				.rotate(angle, 0.0f,1.0f,0.0f)
				.translate(-0.5f * w, -0.5f * h, 0.0f)
				.scale(w,h,0);
		
		gradient_shader.use();
		gradient_shader.setMatrix4("model", model);
		gradient_shader.setMatrix4("projection", projection);
		gradient_shader.setVector4f("col1", color);
		gradient_shader.setVector4f("col2", color2);
		gradient_shader.setVector2f("dimension_", new Vector2f(w,h));
	    
	    quad.draw();
		
	    reset();
	}
	

}
