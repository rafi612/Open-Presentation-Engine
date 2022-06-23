package com.ope.graphics;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;

public class Texture 
{
	public int id;
	public int width;
	public int height;
	
	ByteBuffer pixels;
	
	public Texture(String filename) throws IOException
	{
		load(new FileInputStream(filename));
	}
	
	
	public Texture(InputStream input) 
	{
		try 
		{
			load(input);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void load(InputStream input) throws IOException
	{
		IntBuffer w = MemoryUtil.memAllocInt(1);
		IntBuffer h = MemoryUtil.memAllocInt(1);
		IntBuffer comp = MemoryUtil.memAllocInt(1);
				
		byte[] pixels_raw = input.readAllBytes();
				
		ByteBuffer imageBuffer = MemoryUtil.memAlloc(pixels_raw.length);
		imageBuffer.put(pixels_raw);
		imageBuffer.flip();
			
		pixels = stbi_load_from_memory(imageBuffer, w, h, comp, STBI_rgb_alpha);
		MemoryUtil.memFree(imageBuffer);
				
		this.width = w.get();
		this.height = h.get();
		
		input.close();
	}
	
	public void bind()
	{		
		if (id == 0)
		{
			id = glGenTextures();
			
			glBindTexture(GL_TEXTURE_2D, id);

		    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		        
		    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
		    
		    stbi_image_free(pixels);
		}
		
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public void destroy()
	{
		if (id != 0)
			glDeleteTextures(id);
	}
}
