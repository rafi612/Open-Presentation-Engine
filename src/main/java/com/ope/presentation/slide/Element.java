package com.ope.presentation.slide;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import com.ope.gui.SlideCreator;
import com.ope.io.Util;
import com.ope.io.xml.Tag;
import com.ope.io.xml.XmlWriter;
import com.ope.main.Main;
import com.ope.presentation.slide.elements.E_Image;

public class Element 
{
	public static final String[] types = {"Image","Text","Shape","Graph"};
	public boolean editing,moving,colided;
	
	public int id,x,y,w,h;
	
	public String name,type;
	

	public Element() {}
	
	public void update(SlideCreator sc) {}
	
	public static Element getElementsByName(String s)
	{
		if (s.equals("Image")) return new E_Image(0,0,200,200);
		return null;
	}
	
	public void load(Tag element) {}
	
	public void save(XmlWriter xml)
	{
		xml.addTag("element", "name=" + name,"type=" + type);
	}
	
	public void frame()
	{
		editing = true;
	}
	
	public void destroy() {}

	public void render() {}
	
	public class Frame extends JDialog
	{
		private static final long serialVersionUID = 1L;
		
		Element element;
		
		public Frame(Element element,String title,int width,int height)
		{
			super();
			
			setSize(width,height);
			setTitle(title);
			setLocationRelativeTo(Main.frame);
			setAlwaysOnTop(true);
			setResizable(false);
			setIconImage(Util.loadIcon("/images/icon.png"));
			
			addWindowListener(new WindowAdapter()
			{
				@Override
				public void windowClosing(WindowEvent e)
				{
					element.editing = false;
				}
			});
		}
		
		public void whenOpen()
		{
			setVisible(true);
		}
	}
}