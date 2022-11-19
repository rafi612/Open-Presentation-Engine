package com.ope.io;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.lwjgl.system.Platform;
import org.xml.sax.SAXException;

import com.ope.io.xml.XmlReader;
import com.ope.io.xml.XmlWriter;
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
		
		try 
		{
			XmlReader xml = new XmlReader(configpath);
			lookandfeel = xml.getTags("theme")[0].getText();
			
			if (lookandfeel.equals(getSystemTheme()))
				Main.menubar.m_system.setSelected(true);
			else if (lookandfeel.equals("javax.swing.plaf.metal.MetalLookAndFeel"))
				Main.menubar.m_metal.setSelected(true);
			else if (lookandfeel.equals("javax.swing.plaf.nimbus.NimbusLookAndFeel"))
				Main.menubar.m_nimbus.setSelected(true);
			
			reloadTheme();
		} 
		catch (IOException | SAXException e) 
		{
			JOptionPane.showMessageDialog(Main.frame, "Error loading settings file: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		} 
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
		XmlWriter xml = new XmlWriter();
		
		xml.openTag("config");
		xml.addTagText("theme", lookandfeel);
		xml.closeTag();
		
		try
		{
			Util.saveFile(configpath,xml.get());
		} 
		catch (IOException e) 
		{
			Util.errorDialog(e);
		}
	}
	
	
	public static void reloadTheme()
	{
		try 
		{
			UIManager.setLookAndFeel(Config.lookandfeel);
		} 
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) 
		{
			e.printStackTrace();
		}
		
		SwingUtilities.updateComponentTreeUI(Main.frame);
		SwingUtilities.updateComponentTreeUI(Main.menubar.aboutdialog);
	}

}