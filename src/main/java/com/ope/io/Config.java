package com.ope.io;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.lwjgl.system.Platform;
import org.xml.sax.SAXException;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.ope.io.xml.XmlReader;
import com.ope.io.xml.XmlWriter;
import com.ope.main.Main;

public class Config 
{
	public static LookAndFeel lookandfeel;
	public static String configpath = "";
	
	public enum LookAndFeel
	{
		SYSTEM(() -> setLAF(getSystemTheme()),Main.menubar.m_system),
		METAL(() -> setLAF("javax.swing.plaf.metal.MetalLookAndFeel"),Main.menubar.m_metal),
		NIMBUS(() -> setLAF("javax.swing.plaf.nimbus.NimbusLookAndFeel"),Main.menubar.m_nimbus),
		FLATLAF_LIGHT(() -> { FlatLightLaf.setup(); update(); }, Main.menubar.m_flatlaf_light),
		FLATLAF_DARK(() -> { FlatDarkLaf.setup(); update(); },Main.menubar.m_flatlaf_dark);
		
		Runnable set;
		JRadioButtonMenuItem themeradio;
		
		private static void setLAF(String classname)
		{
			try 
			{
				UIManager.setLookAndFeel(classname);
				update();
			} 
			catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) 
			{
				e.printStackTrace();
			}
		}
		
		private static void update()
		{
			SwingUtilities.updateComponentTreeUI(Main.frame);
			SwingUtilities.updateComponentTreeUI(Main.menubar.aboutdialog);
		}
		
		private static String getSystemTheme()
		{
			switch (Platform.get())
			{
			case LINUX:
				return "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
			default:
				return UIManager.getSystemLookAndFeelClassName();
			}
		}
		
		public void set()
		{
			set.run();
			themeradio.setSelected(true);
			
			lookandfeel = this;
		}
		
		LookAndFeel(Runnable set,JRadioButtonMenuItem themeradio)
		{
			this.set = set;
			this.themeradio = themeradio;
			
			themeradio.addActionListener((event) -> {
				set();
				saveXML();
			});
		}
		
		public static LookAndFeel getByName(String name)
		{
			for (LookAndFeel laf : values())
				if (laf.name().equals(name))
					return laf;
			return null;
		}
	}
	
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
			
			lookandfeel = LookAndFeel.getByName(xml.getTags("theme")[0].getText());
			lookandfeel.set();
		} 
		catch (IOException | SAXException e) 
		{
			JOptionPane.showMessageDialog(Main.frame, "Error loading settings file: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		} 
	}
	
	public static void createOPExml()
	{
		lookandfeel = LookAndFeel.SYSTEM;
		saveXML();
	}
	
	public static void saveXML()
	{
		XmlWriter xml = new XmlWriter();
		
		xml.openTag("config");
		xml.addTagText("theme", lookandfeel.name());
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

}