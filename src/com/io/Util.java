/* Copyright 2019-2020 by rafi612 */
package com.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.project.Project;

public class Util 
{
	public static void insert(String s,int p,JTextPane t) 
	{
		   try 
		   {
			  javax.swing.text.Document doc = t.getDocument();
		      doc.insertString(p, s, null);
		   } 
		   catch(BadLocationException exc) 
		   {
		      exc.printStackTrace();
		   }
	}
	
    
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
	
	public static void copyFileondrive(String input,String output)
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

//	
	public static void copyFile(String input,String output)
	{
        //File source = new File(input);
        InputStream is = Util.class.getResourceAsStream(input);
        File dest = new File(output);

        try (//FileInputStream fis = new FileInputStream(source);
        	FileOutputStream fos = new FileOutputStream(dest)) {

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

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
	}
	  //public  String readXml(String Scieszka,String ElementsByTagName,String tag)
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
		//System.out.println(String.join("/", files));
		return Paths.get("",files).toString();
	}
	
	public static String projectPath(String... files)
	{
		return Paths.get(Project.projectlocation,files).toString();
	}
//		   
//	public static String slash()
//	{
//			   return File.separator;
//	}
}
