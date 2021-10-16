package com.gui.sliderack.atributes;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.io.IoUtil;
import com.main.Main;

public class A_Slide extends Atribute
{
	private static final long serialVersionUID = 1L;
	
	public static String path = "";
	
	public A_Slide()
	{
		super(Atribute.Type.SLIDE.getFullName() + ": None");
		canBeMultiple = false;
		isAlways = true;
		setDropTarget(new DragAndDrop());
	}
	
	class DragAndDrop extends DropTarget
	{
		private static final long serialVersionUID = 1L;
		public synchronized void drop (DropTargetDropEvent evt)
    	{
			evt.acceptDrop(DnDConstants.ACTION_COPY);
			try 
			{
				File file = new File((String) evt.getTransferable().getTransferData(DataFlavor.stringFlavor));
				
				if (file.getName().contains(".layout"))
				{
					path = IoUtil.getPathFromProject(file);
					
					setText(Atribute.Type.SLIDE.getFullName() + ": " + file.getName());
				}
				else
					JOptionPane.showMessageDialog(Main.frame, "This file is not layout file","Error",JOptionPane.ERROR_MESSAGE);
			}
			catch (UnsupportedFlavorException | IOException e) 
			{
				e.printStackTrace();
			}
    	}
	}

}
