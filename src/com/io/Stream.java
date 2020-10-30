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

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jogamp.common.os.Platform.OSType;
import com.main.Main;

public class Stream 
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

	public static void loadinterpreterpath() 
	{
		if (!new File(System.getenv("APPDATA") + Stream.slash() + "ope.xml").exists())
		{
			createOPExml();
		}
		
		if (isWindows())
		{
			
			Main.interpreterpath = readXml(System.getenv("APPDATA") + Stream.slash() + "ope.xml", "settings", "pypath");
			Main.interpretertype = readXml(System.getenv("APPDATA") + Stream.slash() + "ope.xml", "settings", "pytype");
		}
		else if (isLinux())
		{
			Main.interpreterpath = readXml(System.getProperty("user.home") + Stream.slash() + ".ope.xml", "settings", "pypath");
			Main.interpretertype = readXml(System.getProperty("user.home") + Stream.slash() + ".ope.xml", "settings", "pytype");
		}
		
		switch (Main.interpretertype)
		{
			case "winpy":
				Main.winpy.setSelected(true);
			break;
			
			case "winsystem":
				Main.winsystem.setSelected(true);
			break;
			
			case "linux":
				Main.linux.setSelected(true);
			break;
				
			case "macos":
				Main.macos.setSelected(true);
			break;
			case "custom":
				Main.custom.setSelected(true);
				Main.custom.setText("Custom " + "(" + Main.interpreterpath + ")");
			break;
		}
	}
	
	public static void selectPy()
	{
		switch (Main.interpretertype)
		{
			case "winpy":
				Main.winpy.setSelected(true);
			break;
			
			case "winsystem":
				Main.winsystem.setSelected(true);
			break;
			
			case "linux":
				Main.linux.setSelected(true);
			break;
				
			case "macos":
				Main.macos.setSelected(true);
			break;
			case "custom":
				Main.custom.setSelected(true);
				Main.custom.setText("Custom " + "(" + Main.interpreterpath + ")");
			break;
		}
	}
	
	public static void createOPExml()
	{
		String path = "",type = "";
		
		//windows 
		if (isWindows())
		{
			//buildin
			if (new File("Python\\python.exe").exists())
			{
				path = "Python\\python.exe";
				type = "winpy";
			}
			//system
			else
			{
				path = "python.exe";
				type = "winsystem";
			}
		}
		else if (isLinux())
		{
			path = "python3.7";
			type = "linux";
		}
		else if (isMac())
		{
			path = "python";
			type = "macos";
		}
		else 
		{
			path = "";
			type = "custom";
		}
		
		
		if (isWindows())
			Stream.saveFile(System.getenv("APPDATA") + Stream.slash() + "ope.xml",
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" + 
				"<class>\n" +
				"<settings>\n" +
				"<pytype>" + type + "</pytype>\n" + 
				"<pypath>" + path + "</pypath>\n" + 
				"</settings>\n" +
				"</class>");
		else if (isLinux())
			Stream.saveFile(System.getProperty("user.home") + Stream.slash() + ".ope.xml",
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" + 
				"<class>\n" +
				"<settings>\n" +
				"<pytype>" + type + "</pytype>\n" + 
				"<pypath>" + path + "</pypath>\n" + 
				"</settings>\n" +
				"</class>");
	}
	
	public static void saveOPExml(String type,String path)
	{
		if (isWindows())
			Stream.saveFile(System.getenv("APPDATA") + Stream.slash() + "ope.xml",
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" + 
				"<class>\n" +
				"<settings>\n" +
				"<pytype>" + type + "</pytype>\n" + 
				"<pypath>" + path + "</pypath>\n" + 
				"</settings>\n" +
				"</class>");
		else if (isLinux())
			Stream.saveFile(System.getProperty("user.home") + Stream.slash() + ".ope.xml",
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" + 
				"<class>\n" +
				"<settings>\n" +
				"<pytype>" + type + "</pytype>\n" + 
				"<pypath>" + path + "</pypath>\n" + 
				"</settings>\n" +
				"</class>");
	}
//	
	public static void copyFile(String input,String output)
	{
        //File source = new File(input);
        InputStream is = Stream.class.getResourceAsStream(input);
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
//		               System.out.println(eElement.getAttribute("rollno"));
//		               System.out.println(eElement.getElementsByTagName("firstname").item(0).getTextContent());
//		               System.out.println(eElement .getElementsByTagName("lastname").item(0).getTextContent());
//		               System.out.println(eElement.getElementsByTagName("nickname").item(0).getTextContent());
		               return_ = eElement.getElementsByTagName(tag).item(0).getTextContent();
		            }
		         }
		      }
		      catch (Exception e)
		      {
		      }
			return return_;
		}
		   public static String getOsName()
		   {
			   return System.getProperty("os.name");
		   }
		
		   public static boolean isWindows()
		   {
			   return getOsName().startsWith("Windows");
		   }
		   
		   public static boolean isLinux()
		   {
			   return getOsName().contains("nux");
		   }
		   
		   public static boolean isMac()
		   {
		       return getOsName().contains("mac") || getOsName().contains("darwin");
		   }
		   
		   public static String slash()
		   {
			   return File.separator;
		   }
}
