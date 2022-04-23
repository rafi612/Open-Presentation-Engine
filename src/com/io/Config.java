package com.io;

import java.io.File;

import org.lwjgl.system.Platform;

import com.main.Main;

public class Config 
{
	public static String configwindows = Platform.get() == Platform.WINDOWS ?
			Util.path(System.getenv("APPDATA"),"ope.xml") : "";
	public static String configlinux = Platform.get() == Platform.LINUX ? 
			Util.path(System.getProperty("user.home"),".config",".ope.xml") : "";
	
	public static void loadSettings() 
	{
		String conf = "";
		
		//windows
		if (Platform.get() == Platform.WINDOWS)
		{
			if (!new File(configwindows).exists())
				createOPExml();
			conf = configwindows;
		}
		//linux
		if (Platform.get() == Platform.LINUX)
		{
			if (!new File(configlinux).exists())
				createOPExml();
			conf = configlinux;
		}
		
		Main.interpreterpath = Util.readXml(conf, "settings", "pypath");
		Main.interpretertype = Util.readXml(conf, "settings", "pytype");
		
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
		if (Platform.get() == Platform.WINDOWS)
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
		else if (Platform.get() == Platform.LINUX)
		{
			path = "python3";
			type = "linux";
		}
		else if (Platform.get() == Platform.MACOSX)
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
		String confpath = "";
		if (Platform.get() == Platform.WINDOWS)
			confpath = configwindows;
		else if(Platform.get() == Platform.LINUX)
			confpath = configlinux;
		
		Util.saveFile(confpath,
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" + 
			"<class>\n" +
			"<settings>\n" +
			"<pytype>" + type + "</pytype>\n" + 
			"<pypath>" + path + "</pypath>\n" + 
			"</settings>\n" +
			"</class>");
	}

}