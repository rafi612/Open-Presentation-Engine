package com.io;

import java.io.File;

import com.main.Main;

public class Config 
{
	public static String configwindows = System.getenv("APPDATA") + Stream.slash() + "ope.xml";
	public static String configlinux = System.getProperty("user.home") + Stream.slash() + ".ope.xml";
	
	public static void loadinterpreterpath() 
	{
		//windows
		if (Stream.isWindows())
			if (!new File(configwindows).exists())
				createOPExml();
		//linux
		if (Stream.isLinux())
			if (!new File(configlinux).exists())
				createOPExml();
		
		String conf = "";
		if (Stream.isWindows()) conf = configwindows;
		else if (Stream.isLinux()) conf = configlinux;
		
		Main.interpreterpath = Stream.readXml(conf, "settings", "pypath");
		Main.interpretertype = Stream.readXml(conf, "settings", "pytype");
		
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
		if (Stream.isWindows())
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
		else if (Stream.isLinux())
		{
			path = "python3";
			type = "linux";
		}
		else if (Stream.isMac())
		{
			path = "python";
			type = "macos";
		}
		else 
		{
			path = "";
			type = "custom";
		}
		saveOPExml(type, path);
	}
	
	public static void saveOPExml(String type,String path)
	{
		String configpath = "";
		if (Stream.isWindows()) configpath = configwindows;
		else if (Stream.isLinux()) configpath = configwindows;
		
		Stream.saveFile(configpath,
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" + 
			"<class>\n" +
			"<settings>\n" +
			"<pytype>" + type + "</pytype>\n" + 
			"<pypath>" + path + "</pypath>\n" + 
			"</settings>\n" +
			"</class>");
	}

}