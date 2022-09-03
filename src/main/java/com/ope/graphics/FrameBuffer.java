package com.ope.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public class FrameBuffer 
{
	int FBO,RBO,texture;
	
	int viewport_x,viewport_y,viewport_w,viewport_h;
	
	int width,height;
	
	Matrix4f projection,projection_old;
	
	public FrameBuffer(int width,int height)
	{
		this.width = width;
		this.height = height;
		
		//created "inverted" projection matrix
		projection = new Matrix4f().ortho(0,Renderer.getSize().x,0,Renderer.getSize().y,-Renderer.getSize().x,Renderer.getSize().x);
        
        projection_old = new Matrix4f();
        
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width,height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	    
        FBO = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, FBO);
        
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);
        
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

	}
	
	public void bind()
	{
		try(MemoryStack stack = MemoryStack.stackPush())
		{
			IntBuffer viewport = stack.mallocInt(4);
			glGetIntegerv(GL_VIEWPORT, viewport);	
			
			viewport_x = viewport.get(0);
			viewport_y = viewport.get(1);
			viewport_w = viewport.get(2);
			viewport_h = viewport.get(3);
			
			projection_old.set(Renderer.getProjectionMatrix());
			Renderer.setProjectionMatrix(projection);
			
			if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
				System.out.println("Not complete");
			
		    glBindFramebuffer(GL_FRAMEBUFFER, FBO);
		    
		    glEnable(GL_DEPTH_TEST);
		    
		    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		    
	        glEnable(GL_BLEND);
	        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		    
		    glViewport(0,0,width,height);
		}
	    
	}
	
	public void clearColor(float r,float g,float b,float a)
	{
	    float color[] = { r,g,b,a };
	    glClearBufferfv(GL_COLOR, 0, color);
	}
	
	public void unbind()
	{
	    glDisable(GL_DEPTH_TEST);
	    glBindFramebuffer(GL_FRAMEBUFFER, 0);
	    
	    Renderer.setProjectionMatrix(projection_old);
	    
	    glViewport(viewport_x,viewport_y,viewport_w,viewport_h);
	}
	
	public void draw(int x,int y,int w,int h)
	{
		Matrix4f model = new Matrix4f()
				.translate(x,y,0)
				.scale(w,h, 0);
		
		Renderer.texture_shader.use();
		Renderer.texture_shader.setMatrix4("transformMatrix", model);
		Renderer.texture_shader.setMatrix4("viewMatrix", Renderer.getViewMatrix());
		Renderer.texture_shader.setMatrix4("projectionMatrix", Renderer.getProjectionMatrix());
		
		drawWithCustomShader();
	}
	
	public Matrix4f getProjection()
	{
		return projection;
	}
	
	public void bindTexture()
	{
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	public int getTextureID()
	{
		return texture;
	}
	
	public void drawMesh()
	{
	    Renderer.quad.draw();
	}
	
	public void drawWithCustomShader()
	{   
		bindTexture();
	    
		drawMesh();
	    glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void destroy()
	{
		glDeleteFramebuffers(FBO);
		glDeleteTextures(texture);
	}

}
