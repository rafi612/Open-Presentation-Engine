package com.graphics;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.swing.JOptionPane;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import static org.lwjgl.opengl.GL11.*;

public class Texture 
{
	public int id = 0;
	public int width;
	public int height;
	
	ByteBuffer pixels;
	
	public Texture(String filename)
	{
		try 
		{
			load(new FileInputStream(filename));
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "File" + filename +" not found");
		}
	}
	
	public Texture(InputStream input)
	{
		try 
		{
			load(input);
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "File in jar not found");
		}
	}
	
	private void load(InputStream input) throws IOException
	{		
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);
			
		byte[] pixels_raw = input.readAllBytes();
			
		ByteBuffer imageBuffer = BufferUtils.createByteBuffer(pixels_raw.length);
		imageBuffer.put(pixels_raw);
		imageBuffer.flip();
		
		pixels = STBImage.stbi_load_from_memory(imageBuffer, w, h, comp, STBImage.STBI_rgb_alpha);
			
		this.width = w.get();
		this.height = h.get();
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
		    
		    STBImage.stbi_image_free(pixels);
		}
		
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public void destroy()
	{
		glDeleteTextures(id);
	}
	
	public static ByteBuffer load_image(InputStream in,IntBuffer w,IntBuffer h,IntBuffer comp)
	{
		try
		{
			byte[] pixels_raw = in.readAllBytes();
			
			ByteBuffer imageBuffer = BufferUtils.createByteBuffer(pixels_raw.length);
			imageBuffer.put(pixels_raw);
			imageBuffer.flip();
			return STBImage.stbi_load_from_memory(imageBuffer, w, h, comp, STBImage.STBI_rgb_alpha);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
