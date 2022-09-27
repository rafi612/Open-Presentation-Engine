package com.ope.io;

import java.io.File;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.lwjgl.system.Platform;

import com.ope.io.xml.XmlParser;
import com.ope.main.Main;

public class Config 
{
	public static String lookandfeel;
	public static String configpath = "";
	
	public static void loadSettings() 
	{
		
		switch (Platform.get())
		{
		case WINDOWS:
			configpath = Util.path(System.getenv("APPDATA"),"ope.xml");
			break;
		case LINUX:
			configpath = Util.path(System.getProperty("user.home"),".config","ope.xml");
			break;
		case MACOSX:
			configpath = Util.path(System.getProperty("user.home"),"Library","Application Support","ope.xml");
			break;
		}
		
		if (!new File(configpath).exists())
			createOPExml();
		
		XmlParser xml = new XmlParser(configpath);
		
		lookandfeel = XmlParser.getElements(xml.getElementsByTagName("theme"))[0].getTextContent();
	

		if (lookandfeel.equals(getSystemTheme()))
			Main.menubar.m_system.setSelected(true);
		else if (lookandfeel.equals("javax.swing.plaf.metal.MetalLookAndFeel"))
			Main.menubar.m_metal.setSelected(true);
		else if (lookandfeel.equals("javax.swing.plaf.nimbus.NimbusLookAndFeel"))
			Main.menubar.m_nimbus.setSelected(true);
		
		reloadTheme();
	}
	
	public static void createOPExml()
	{
		lookandfeel = getSystemTheme();
		saveXML();
	}
	
	public static String getSystemTheme()
	{
		switch (Platform.get())
		{
		case LINUX:
			return "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
		default:
			return UIManager.getSystemLookAndFeelClassName();
		}
	}
	
	public static void saveXML()
	{
		Util.saveFile(configpath,
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" + 
			"<config>\n" +
			"<theme>" + lookandfeel + "</theme>\n" + 
			"</config>");
	}
	
	
	public static void reloadTheme()
	{
		try {
			UIManager.setLookAndFeel(Config.lookandfeel);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SwingUtilities.updateComponentTreeUI(Main.frame);
		SwingUtilities.updateComponentTreeUI(Main.menubar.aboutdialog);
	}

}