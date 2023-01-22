/* Copyright 2019-2020 by rafi612 */
package com.ope.io;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import com.ope.main.Main;
import com.ope.project.Project;

public class Util 
{ 
	public static String getFileExtension(File file)
	{
		// convert the file name into string
		String fileName = file.toString();
	
		int index = fileName.lastIndexOf('.');
		if (index > 0) 
		{
			String extension = fileName.substring(index + 1);
			return extension;
		}
		return "";
	}
	
	public static void copyFile(String input,String output) throws IOException
	{
        try (FileInputStream fis = new FileInputStream(new File(input));
        	FileOutputStream fos = new FileOutputStream(new File(output))) 
        {

            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) > 0)
                fos.write(buffer, 0, length);
        } 
	}
	
	public static void copyFileFromJar(InputStream input,String output) throws IOException
	{
        File dest = new File(output);

        try (FileOutputStream fos = new FileOutputStream(dest))
        {
            byte[] buffer = new byte[1024];
            int length;

            while ((length = input.read(buffer)) > 0)
            {
                fos.write(buffer, 0, length);
            }
        } 
	}
	
	public static String getPathFromProject(File s)
	{
		return new File(Project.projectlocation).toURI().relativize(s.toURI()).getPath();
	}
	
	public static void saveFile(String path,String text) throws IOException
	{
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) 
        {
            bw.write(text);
        } 
	}
	
	public static String readFile(InputStream in) throws IOException
	{
		try (BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in)))
		{
			return bufferReader.lines().collect(Collectors.joining("\n"));
		} 
	}
	
	public static void errorDialog(Exception ex)
	{
		JOptionPane.showMessageDialog(Main.frame, "Error:" + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
	
	
	public static String getLicense()
	{
		try 
		{
			return readFile(ResourceLoader.load("/LICENSE.txt"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static String path(String... files)
	{
		return Paths.get("",files).toString();
	}
	
	public static String projectPath(String... files)
	{
		return Paths.get(Project.projectlocation,files).toString();
	}
	
	
	public static Image loadIcon(String path)
	{
		try
		{
			return ImageIO.read(Main.class.getResourceAsStream(path));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static ByteBuffer loadImageToBuffer(InputStream in,IntBuffer w,IntBuffer h,IntBuffer comp) throws IOException
	{
		byte[] pixels_raw = in.readAllBytes();
			
		ByteBuffer imageBuffer = MemoryUtil.memAlloc(pixels_raw.length);
		imageBuffer.put(pixels_raw);
		imageBuffer.flip();
		ByteBuffer result = STBImage.stbi_load_from_memory(imageBuffer, w, h, comp, STBImage.STBI_rgb_alpha);
			
		MemoryUtil.memFree(imageBuffer);
		in.close();
			
		return result;
	}
}
