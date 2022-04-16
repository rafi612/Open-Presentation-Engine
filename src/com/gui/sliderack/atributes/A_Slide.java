package com.gui.sliderack.atributes;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.gui.TreeFileChooser;
import com.io.Util;
import com.main.Main;

public class A_Slide extends Attribute implements TreeFileChooser.Target
{
	private static final long serialVersionUID = 1L;
	
	public static String path = "";
	
	public A_Slide()
	{
		super(Attribute.Type.SLIDE.getFullName() + ": None");
		canBeMultiple = false;
		isAlways = true;
	}

	@Override
	public void fileSelected(String path) 
	{
		if (name.contains(".layout"))
		{
			path = Util.getPathFromProject(new File(path));
			
			setText(Attribute.Type.SLIDE.getFullName() + ": " + path);
		}
		else
			JOptionPane.showMessageDialog(Main.frame, "This file is not layout file","Error",JOptionPane.ERROR_MESSAGE);
	}
	
	public String getXmlTag()
	{
		return "\t<layout>" + path + "</layout>";
	}

}
