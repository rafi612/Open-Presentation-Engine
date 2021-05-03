/* Copyright 2019-2020 by rafi612 */
package com.presentation.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import com.presentation.main.Presentation;

public class ImageResource {
	
	// The OpenGL texture object
	private Texture texture = null;
	
	// The bufferedimage of this image
	public BufferedImage image = null;
	
	GLProfile profile;
	
	@SuppressWarnings("deprecation")
	public ImageResource(String path) 
	{
		URL url = null;
		profile = Presentation.getProfile();
		try 
		{
			url = new File(path).toURL();
		} 
		catch (MalformedURLException e1) 
		{
			e1.printStackTrace();
		}
		
		try 
		{
			//System.out.println(url);
			image = ImageIO.read(url);
		} 
		catch (IOException e) 
		{
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null, "File " + path + " not found");
		}
		
		if (image != null)
		{
			image.flush();
		}
	}
	
	
	public ImageResource(InputStream i) 
	{		
		try 
		{
			//System.out.println(url);
			image = ImageIO.read(i);
			profile = Presentation.getProfile();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "File in jar not found");
		}
		
		if (image != null)
		{
			image.flush();
		}
	}
	
	
	public Texture getTexture() 
	{
		if (image == null) 
		{
			return null;
		}
		
		if (texture == null) 
		{
			texture = AWTTextureIO.newTexture(Presentation.getProfile(), image, true);
		}
		
		return texture;
	}
	
}