package com.opengl.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import com.opengl.main.Presentation;

public class ImageResource {
	
	// The OpenGL texture object
	private Texture texture = null;
	
	// The bufferedimage of this image
	public BufferedImage image = null;
	
	@SuppressWarnings("deprecation")
	public ImageResource (String path) 
	{
		URL url = null;

		if (path.charAt(0) == '/') url = ImageResource.class.getResource(path);
		else
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