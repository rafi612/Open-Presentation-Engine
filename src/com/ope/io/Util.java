/* Copyright 2019-2020 by rafi612 */
package com.ope.io;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ope.main.Main;
import com.ope.project.Project;

public class Util 
{ 
	public static String FileExtension(File file)
	{
		// convert the file name into string
		String fileName = file.toString();
	
		int index = fileName.lastIndexOf('.');
		if(index > 0) 
		{
			String extension = fileName.substring(index + 1);
			return extension;
		}
		return "";
	}
	
	public static void copyFile(String input,String output)
	{
        File source = new File(input);
        File dest = new File(output);

        try (FileInputStream fis = new FileInputStream(source);
        	FileOutputStream fos = new FileOutputStream(dest)) {

            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) > 0)
            {

                fos.write(buffer, 0, length);
            }
        } 
        catch (FileNotFoundException e) 
        {
			e.printStackTrace();
		} 
        catch (IOException e)
        {
			e.printStackTrace();
		}
	}
	
	public static void copyFileFromJar(String input,String output)
	{
        InputStream is = Util.class.getResourceAsStream(input);
        File dest = new File(output);

        try (FileOutputStream fos = new FileOutputStream(dest))
        {
            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0)
            {
                fos.write(buffer, 0, length);
            }
        } 
        catch (FileNotFoundException e) 
        {
			e.printStackTrace();
		} 
        catch (IOException e)
        {
			e.printStackTrace();
		}
	}
	
	public static String getPathFromProject(File s)
	{
		return new File(Project.projectlocation).toURI().relativize(s.toURI()).getPath();
	}
	
	public static void saveFile(String path,String text)
	{
        try (FileWriter writer = new FileWriter(path);
             BufferedWriter bw = new BufferedWriter(writer)) 
        {
            bw.write(text);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
	}
	
	public static String readFile(String path)
	{
		String text = "";
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader(new File(path)));
			String str;
			while ((str = in.readLine()) != null) 
			{
				text += str + "\n";
			}
			in.close();
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return text;
	}
	
	public static String readFileFromJar(String path)
	{
		String text = "";
		try 
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(Util.class.getResourceAsStream(path)));
			String str;
			while ((str = in.readLine()) != null) 
			{
				text += str + "\n";
			}
			in.close();
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		return text;
	}
	
	
	public static String getLicense()
	{
		return readFileFromJar("/LICENSE.txt");
	}

	//TODO: Remove
	public static String readXml(String path,String ElementsByTagName,String tag)
	{
		String return_ = null;
		try 
		{
			File inputFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName(ElementsByTagName);
			
			for (int temp = 0; temp < nList.getLength(); temp++)
			{
				Node nNode = nList.item(temp);
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eElement = (Element) nNode;
//					System.out.println(eElement.getAttribute("rollno"));
//					System.out.println(eElement.getElementsByTagName("firstname").item(0).getTextContent());
//					System.out.println(eElement .getElementsByTagName("lastname").item(0).getTextContent());
//					System.out.println(eElement.getElementsByTagName("nickname").item(0).getTextContent());
					return_ = eElement.getElementsByTagName(tag).item(0).getTextContent();
				}
			}
		}
		catch (Exception e){}
		return return_;
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
	
	
	public static ByteBuffer loadImageToBuffer(InputStream in,IntBuffer w,IntBuffer h,IntBuffer comp)
	{
		try
		{
			byte[] pixels_raw = in.readAllBytes();
			
			ByteBuffer imageBuffer = MemoryUtil.memAlloc(pixels_raw.length);
			imageBuffer.put(pixels_raw);
			imageBuffer.flip();
			ByteBuffer result =  STBImage.stbi_load_from_memory(imageBuffer, w, h, comp, STBImage.STBI_rgb_alpha);
			
			MemoryUtil.memFree(imageBuffer);
			in.close();
			
			return result;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
