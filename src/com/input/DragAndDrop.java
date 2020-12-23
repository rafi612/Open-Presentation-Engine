package com.input;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;

import com.io.Stream;
import com.main.Main;
import com.project.Project;

public class DragAndDrop extends DropTarget
{
	private static final long serialVersionUID = 1L;
	public synchronized void drop(DropTargetDropEvent evt) 
	{
		try 
		{
			evt.acceptDrop(DnDConstants.ACTION_COPY);
			@SuppressWarnings("unchecked")
			List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
			for (File file : droppedFiles)
			{
				// process files
				//System.out.println(file.getName());
				Stream.copyFileondrive(file.getPath(), Project.projectlocation + Stream.slash() + file.getName());
				Project.refreshProject();
			}
                   
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(Main.frame,ex.getStackTrace(), "Unsupported file type", JOptionPane.ERROR_MESSAGE);
		}	
	}

}
