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
import com.ope.io.XmlParser;

import org.joml.Vector4f;

public class E_Image extends SquareBasedElement
{
	public String path;
	public Texture image;
	
	public boolean editing;
	Frame frame;
	

	public E_Image(String path,int x,int y,int w,int h)
	{
		super();
		
		this.path = path;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		type = "Image";
		
		if (!path.equals(""))
			try 
			{
				this.image = new Texture(path);
			} 
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "File" + path +" not found");
			}
		
		frame = new Frame(this);
	}
	
	public void update(SlideCreator sc)
	{
		super.update(sc);
		
		if (editing) frame.update();
	}
	
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
				image = new Texture(Util.projectPath(path));
			} 
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "File" + path +" not found");
			}
			
			x = Integer.parseInt(data.getAttribute("x"));
			y = Integer.parseInt(data.getAttribute("y"));
			w = Integer.parseInt(data.getAttribute("width"));
			h = Integer.parseInt(data.getAttribute("height"));
		}
	}
	
	public void destroy()
	{
		if (image != null)
			image.destroy();
		
		frame.dispose();
	}
	
	public String save()
	{
		//creating xml tag with element data
		return "	<element name=\"" +  name + "\" type=\"" + type + "\">\n"
				+ "		<data src=\"" + path.replaceAll(File.separator, "/") +"\" x=\"" + x + "\" y=\"" + y + "\" width=\"" + w + "\" height=\"" + h + "\"></data>\n"
				+ "	</element>\n";
	}
	
	public void render()
	{
		if (path.equals(""))
			Renderer.frectnofill(x,y,w,h,new Vector4f(0,0,0,1));
		else
		{
			Renderer.drawImage(image,x,y,w,h);
		}
		
		//rendering SquareBasedElement overlay
		super.render();
	}
	
	
	public void frame()
	{
		editing = true;
		frame.setVisible(true);
	}
	
	protected void finalize()
	{
		frame.dispose();
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
						JOptionPane.showMessageDialog(null, "File" + path + " not found");
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