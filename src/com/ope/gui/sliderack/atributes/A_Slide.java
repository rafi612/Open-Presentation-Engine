package com.ope.gui.sliderack.atributes;

import java.io.File;

import javax.swing.JOptionPane;

import org.w3c.dom.Element;

import com.ope.gui.TreeFileChooser;
import com.ope.io.Util;
import com.ope.main.Main;

public class A_Slide extends Attribute 
{
	private static final long serialVersionUID = 1L;
	
	public String path;
	
	public A_Slide()
	{
		super(Attribute.Type.SLIDE.getFullName() + ": None");
		type = Attribute.Type.SLIDE;
		
		canBeMultiple = false;
		isAlways = true;
	}
	
	public String getXmlTag()
	{
		return "\t<layout>" + path + "</layout>";
	}
	
	public void load(Element element)
	{
		path = element.getTextContent();
		setText(path.equals("") ? "None" : new File(path).getName());
	}
	
	public void onActivate()
	{
		TreeFileChooser chooser = new TreeFileChooser(null);
		
		chooser.open((path) -> 
		{
			//executing when file selected
			File file = new File(path);
			if (file.getName().contains(".layout"))
			{
				this.path = Util.getPathFromProject(file);
				
				setText(file.getName());
			}
			else
				JOptionPane.showMessageDialog(Main.frame, "This file is not layout file","Error",JOptionPane.ERROR_MESSAGE);
		});
	}
	
	public void setText(String text)
	{
		super.setText(type.getFullName() + ": " + text);
	}

}
