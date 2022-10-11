package com.ope.presentation.slide.elements;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import com.ope.graphics.Renderer;
import com.ope.graphics.Texture;
import com.ope.gui.SlideCreator;
import com.ope.gui.TreeFileEvent;
import com.ope.io.Util;
import com.ope.io.xml.XmlParser;
import com.ope.io.xml.XmlWriter;
import com.ope.main.Main;

import org.joml.Vector4f;

public class E_Image extends SquareBasedElement
{
	public String path;
	public Texture image;
	
	protected Frame frame;
	
	public E_Image(int x,int y,int w,int h)
	{
		super();
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		type = "Image";
		frame = new Frame(this);
	}

	public E_Image(String path,int x,int y,int w,int h) throws IOException
	{
		this(x,y,w,h);
		this.path = path;
		
		if (!path.equals(""))
			this.image = new Texture(path);
	}
	
	@Override
	public void update(SlideCreator sc)
	{
		super.update(sc);
		
		if (editing) 
			frame.update();
	}
	
	@Override
	public void load(org.w3c.dom.Element element)
	{
		
		if (element.getAttribute("type").equals("Image"))
		{
			type = element.getAttribute("type");
			name = element.getAttribute("name");
			
			//[0] getting first element from array
			var data = XmlParser.getElementsFromElementByName(element,"data")[0];
			
			//loading image
			path = data.getAttribute("src");
			try
			{
				if (!path.isEmpty())
				{
					frame.buttonpath.setText(path);
					image = new Texture(Util.projectPath(path));
				}
			} 
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(Main.frame, "Error loading image: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
			}
			
			x = Integer.parseInt(data.getAttribute("x"));
			y = Integer.parseInt(data.getAttribute("y"));
			w = Integer.parseInt(data.getAttribute("width"));
			h = Integer.parseInt(data.getAttribute("height"));
		}
	}
	
	@Override
	public void destroy()
	{
		if (image != null)
			image.destroy();
		
		frame.dispose();
	}
	
	@Override
	public void save(XmlWriter xml)
	{
		xml.openTag("element", "name=" + name,"type=" + type);
		
		xml.addTag("data", "src=" + path.replaceAll(File.separator, "/"),"x=" + x,"y=" + y,"width=" + w,"height=" + h);
		
		xml.closeTag();
	}
	
	@Override
	public void render()
	{
		if (path.isEmpty() && image == null)
			Renderer.frectnofill(x,y,w,h,new Vector4f(0,0,0,1));
		else
		{
			Renderer.drawImage(image,x,y,w,h);
		}
		
		//rendering SquareBasedElement overlay
		super.render();
	}
	
	@Override
	public void frame()
	{
		frame.whenOpen();
		super.frame();
	}
	

	class Frame extends SquareBasedElement.Frame
	{
		private static final long serialVersionUID = 1L;
		
		JButton buttonpath;
		
		E_Image element;
		public Frame(E_Image element)
		{
			super(element,"Image",360,250);
			this.element = element;
			
			setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
			
			buttonpath = new JButton("Select Image");
			buttonpath.addActionListener((e) -> {
				
				TreeFileEvent event = new TreeFileEvent(buttonpath);
				
				event.open((path) -> {
					element.path = Util.getPathFromProject(new File(path));
					
					if (element.image != null)
						element.image.destroy();
					
					try
					{
						element.image = new Texture(Util.projectPath(element.path));
					} 
					catch (IOException ex)
					{
						JOptionPane.showMessageDialog(Main.frame, "Error loading image: " + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
					}
					element.w = element.image.width;
					element.h = element.image.height;
					sw.setValue(element.image.width);
					sh.setValue(element.image.height);
					
					event.setButtonResultText(element.path);
				});
			});
			
			String[] labels = {"","X:","Y:","Width:","Height"};
			JComponent[] components = {buttonpath,sx,sy,sw,sh};
			
			for (JComponent comp : components)
			{
				comp.setMinimumSize(new Dimension(0, 25));
				comp.setPreferredSize(new Dimension(300,25));
				comp.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
				if (comp instanceof JSpinner)
				{
					((JSpinner) comp).addChangeListener(this);
				}
			}
			
			for (int i = 0; i < labels.length;i++)
			{
				JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
				
				panel.add(new JLabel(labels[i]));
				panel.add(components[i]);
				
				this.add(panel);
			}
		}
		
	}
}