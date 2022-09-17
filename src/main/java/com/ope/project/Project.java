/* Copyright 2019-2020 by rafi612 */
package com.ope.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;
import org.lwjgl.util.nfd.NativeFileDialog;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import com.ope.io.Util;
import com.ope.main.Main;

public class Project 
{
	public static boolean projectIsLoaded = false;
	
	public static String projectlocation;
	public final static String PROJECT_XML_NAME = "project.xml";
	
	public static void createNewProject(String location,String name)
	{
		if (projectIsLoaded) 
			unloadProject();
		
		String path = Util.path(location,name);
		new File(path).mkdir();
		createProjectXml(Util.path(path,PROJECT_XML_NAME));
		
		try 
		{
			loadProject(path);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void loadProject(String path) throws Exception
	{
		projectlocation = path;
			
		if (!new File(Util.projectPath(PROJECT_XML_NAME)).exists())
			throw new FileNotFoundException("This is not project folder (project.xml missing)");
			
		if (projectIsLoaded) 
			unloadProject();
			
		projectIsLoaded = true;
			
		Main.frame.setTitle(Main.TITLE + " - " + path);

		interfaceEnable(true);
			
		try
		{
			Main.sliderack.load(Util.projectPath(PROJECT_XML_NAME));
		} 
		catch (Exception e) 
		{
			throw new IllegalStateException("Can't load project (Project corrupted)");
		}
			
		refreshProject();
	}
	public static void unloadProject()
	{
		projectIsLoaded = false;
		projectlocation = "";
		
		Main.frame.setTitle(Main.TITLE + " - Project not loaded");

		interfaceEnable(false);
		
		Main.sliderack.clear();
		
		refreshProject();
	}
	
	private static void createProjectXml(String path)
	{
		String lines = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<project>\n"
				+ "<global fullscreen=\"true\"/>\n"
				+ "</project>";
		Util.saveFile(path, lines);
	}
	
	public static void interfaceEnable(boolean enabled)
	{
		Main.menubar.save.setEnabled(enabled);
		Main.menubar.project.setEnabled(enabled);
		Main.menubar.run.setEnabled(enabled);
		Main.menubar.refresh.setEnabled(enabled);
		Main.menubar.export.setEnabled(enabled);
		Main.menubar.exitproject.setEnabled(enabled);
		
		Main.tree.setEnabled(enabled);
		Main.sliderack.setEnabled(enabled);
		Main.actionpanel.setEnabled(enabled);
		
		if(enabled)
			Main.slidecreator.initEnable();
		else
			Main.slidecreator.setEnabled(enabled);
		
	}

	public static void refreshProject()
	{		
		Main.tree.getRootNode().removeAllChildren();
		
		createChildren(new File(projectlocation), Main.tree.getRootNode());
		
		((DefaultTreeModel) Main.tree.getModel()).reload(Main.tree.getRootNode());
	}
	
	
	public static void save()
	{
		Main.sliderack.build(Util.projectPath(PROJECT_XML_NAME));
	}
	
	public static void run()
	{
		try 
		{
			save();
				
			refreshProject();
				
			String javaexe = Util.path(System.getProperty("java.home"),"bin","java");
					
			ProcessBuilder pb = new ProcessBuilder(javaexe,"-cp",System.getProperty("java.class.path"),Main.class.getName(),projectlocation);
			pb.directory(new File(projectlocation));
					
			//redirect output to terminal
			pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
			pb.redirectError(ProcessBuilder.Redirect.INHERIT);
			pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
					
			pb.start();
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(Main.frame, "Can't run project: " + e.getMessage(), "Can't run project", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static String newProjectDialog()
	{
		String path = TinyFileDialogs.tinyfd_saveFileDialog("Create Project", System.getProperty("user.home") + File.separator, null, "Folder");
		
		if (path != null) 
		{
			File project = new File(path);
			Project.createNewProject(project.getParent(),project.getName());
		}
		
		return path;
	}
	
	public static String loadDialog()
	{
		String path = null;
		//NFD have better file dialog on windows
		if (Platform.get() == Platform.WINDOWS)
		{
			try (MemoryStack stack = MemoryStack.stackPush())
			{
				PointerBuffer p = stack.mallocPointer(1);
			
				NativeFileDialog.NFD_PickFolder(System.getProperty("user.home") + File.separator, p);
			
				path = p.getStringUTF8();
			}
		}
		else 
			path = TinyFileDialogs.tinyfd_selectFolderDialog("Select project folder",System.getProperty("user.home") + File.separator);
		
		if(path != null)
		{
			try 
			{
				loadProject(path);
			} 
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(Main.frame, e.getMessage(), "Can't load project", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		return path;
	}
	
	public static int lostSaveDialog()
	{
		int yesno = JOptionPane.showConfirmDialog(Main.frame, "All unsaved changes are lost. Do you want to save?", "Save",JOptionPane.YES_NO_CANCEL_OPTION ,JOptionPane.QUESTION_MESSAGE);
				
		if (yesno == 0)
			save();
		
		return yesno;
	}
	
	public static int saveDialog()
	{
		int yesno = JOptionPane.showConfirmDialog(Main.frame, "Do you want to save?", "Save",JOptionPane.YES_NO_OPTION ,JOptionPane.QUESTION_MESSAGE);
				
		if (yesno == 0)
			save();
		
		return yesno;
	}
	
	private static void createChildren(File fileRoot, DefaultMutableTreeNode node) 
	{
		File[] files = fileRoot.listFiles();
		if (files == null) return;
		for (File file : files) 
		{
			var childNode = new DefaultMutableTreeNode(file);
			
			node.add(childNode);
			if (file.isDirectory()) 
				createChildren(file, childNode); 
		}
	}
}
