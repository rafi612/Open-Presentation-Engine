package com.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.input.Action;
import com.input.TreeListener;
import com.io.Stream;
import com.main.Main;
import com.project.Project;
import com.tree.TreeCellRenderer;

public class Tree extends JTree implements ActionListener
{
	private static final long serialVersionUID = 1L;
    public JMenuItem newfile,editfile, newfolder, openfile, newpython,newxml;

	public Tree(DefaultMutableTreeNode workspace) 
	{       
		super(workspace);
		
        //popup drzewka
        newfile = new JMenuItem("New file");
        newfile.addActionListener(this);
        editfile = new JMenuItem("Edit File");
        editfile.addActionListener(this);
        newfolder = new JMenuItem("New Folder");
        newfolder.addActionListener(this);
        openfile = new JMenuItem("Open File/Folder");
        newxml = new JMenuItem("New XML File");
        newxml.addActionListener(this);
		
        JPopupMenu treepopup = new JPopupMenu();
        treepopup.add(newfile);
        treepopup.add(editfile);
        treepopup.add(newfolder);
        treepopup.add(openfile);
        treepopup.add(newxml);
        
        setBorder(BorderFactory.createTitledBorder("Project Explorer"));
        setShowsRootHandles(true);
        setToolTipText("Drag and Drop file to copy into project.");
        setCellRenderer(new TreeCellRenderer());
        addTreeSelectionListener(new TreeListener());
        setComponentPopupMenu(treepopup);
        setEnabled(false);
        setDragEnabled(true);
        //add drop target to tree
        setDropTarget(new DropTarget() {
			private static final long serialVersionUID = 1L;
			public synchronized void drop (DropTargetDropEvent evt)
        	{
				try 
				{
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					@SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles)
					{
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
        });
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		if (source == newfile)
		{
			String name = JOptionPane.showInputDialog(Main.frame, "Enter file name:", "Create new file", JOptionPane.QUESTION_MESSAGE);
			Stream.saveFile(Project.projectlocation + Stream.slash() + name, "");
				
			Project.refreshProject();
		}
		if (source == editfile)
		{
			try 
			{
				if (Main.tree.getLastSelectedPathComponent() != null 
						&& !new File(Main.tree.getLastSelectedPathComponent().toString()).isDirectory() 
						&& !Main.tree.getLastSelectedPathComponent().toString().equals("Workspace                                  "))
				{
					if (Stream.isWindows())
						Runtime.getRuntime().exec("notepad " + "\"" + Main.tree.getLastSelectedPathComponent() + "\"");
				else if (Stream.isLinux())
						Runtime.getRuntime().exec("/usr/bin/x-terminal-emulator -e nano" /* + "\"" */+ Main.tree.getLastSelectedPathComponent() /*+ "\""*/);
				}
				else
					JOptionPane.showMessageDialog(Main.frame, "No selected file", "File", JOptionPane.ERROR_MESSAGE);
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
					
//			JTextArea t = new JTextArea();
//			t.setFont(new Font(t.getFont().getName(), Font.TRUETYPE_FONT, 16));
//				
//			Main.tabs.add(Main.tree.getLastSelectedPathComponent().toString(),new JScrollPane(t));
//					
//			Project.loadTextFromFileToTextArea(Main.tree.getLastSelectedPathComponent().toString(), t);
		}
		if (source == newfolder)
		{
			String folder = JOptionPane.showInputDialog(Main.frame, "Enter folder name:", "Create new folder", JOptionPane.QUESTION_MESSAGE);
			new File(Project.projectlocation + Stream.slash() + folder).mkdir();
				
			Project.refreshProject();
		}
		if (source == openfile)
		{
			
		}
		if (source == newxml)
		{
			String name = JOptionPane.showInputDialog(Main.frame, "Enter XML name:", "Create new XML", JOptionPane.QUESTION_MESSAGE);
			Stream.copyFile("/script/config.xml", Project.projectlocation + Stream.slash() + name + ".xml");
				
			Project.refreshProject();
		}
		
	}

}
