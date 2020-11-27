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

import com.io.Stream;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import com.main.Main;
import com.presentation.main.Presentation;
import com.slidecreator.SlideCreator;

public class ImageResource {
	
	// The OpenGL texture object
	private Texture texture = null;
	
	// The bufferedimage of this image
	public BufferedImage image = null;
	
	int getfrom = 0;
	public static final int PRESENTATION = 0;
	public static final int CREATOR = 1;
	
	@SuppressWarnings("deprecation")
	public ImageResource(String path) 
	{
		URL url = null;
		getfrom = PRESENTATION;
//		if (path.charAt(0) == '/') 
//			url = ImageResource.class.getResource(path);
//		else
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
	
	@SuppressWarnings("deprecation")
	public ImageResource(String path,int getfrom) 
	{
		URL url = null;
		this.getfrom = getfrom;

//		if (path.charAt(0) == '/') 
//			url = ImageResource.class.getResource(path);
//		else
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
			if (getfrom == PRESENTATION) 
				texture = AWTTextureIO.newTexture(Presentation.getProfile(), image, true);
			else
				texture = AWTTextureIO.newTexture(SlideCreator.getProfile(), image, true);
		}
		
		return texture;
	}
	
}