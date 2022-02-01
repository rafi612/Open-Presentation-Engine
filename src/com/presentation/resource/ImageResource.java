/* Copyright 2019-2020 by rafi612 */
package com.presentation.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import com.presentation.graphics.Renderer;
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
		try 
		{
			//System.out.println(url);
			image = ImageIO.read(new File(path));
			profile = Renderer.getProfile();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "File" + path +" not found");
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
			profile = Renderer.getProfile();
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
			texture = AWTTextureIO.newTexture(profile, image, true);
		}
		
		return texture;
	}
	
}