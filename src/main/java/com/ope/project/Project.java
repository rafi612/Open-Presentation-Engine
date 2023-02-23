/* Copyright 2019-2020 by rafi612 */
package com.ope.project;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;
import org.lwjgl.util.nfd.NativeFileDialog;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import com.ope.gui.MainWindow;
import com.ope.io.Util;
import com.ope.io.xml.XmlWriter;
import com.ope.main.Main;

public class Project 
{
	public static boolean projectIsLoaded = false;
	
	public static String projectLocation;
	public final static String PROJECT_XML_NAME = "project.xml";
	
	public static void createNewProject(String location,String name) throws Exception
	{
		if (projectIsLoaded) 
			unloadProject();
		
		String path = Util.path(location,name);
		
		if (new File(path).mkdir())
		{
			createProjectXml(Util.path(path,PROJECT_XML_NAME));
			
			loadProject(path);
		}
		else throw new Exception("Project directory cannot be created");
	}
	
	public static void loadProject(String path) throws Exception
	{
		projectLocation = path;
			
		if (!new File(Util.projectPath(PROJECT_XML_NAME)).exists())
			throw new Exception("This is not project folder (project.xml missing)");
			
		if (projectIsLoaded) 
			unloadProject();
			
		projectIsLoaded = true;
			
		Main.frame.setTitle(MainWindow.TITLE + " - " + path);

		interfaceEnable(true);
		
		//catch error when project is invalid
		try
		{
			Main.frame.slideList.load(Util.projectPath(PROJECT_XML_NAME));
		} 
		catch (Exception e) 
		{
			unloadProject();
			throw new Exception("Can't load project (Project corrupted)");
		}
			
		refreshProject();
	}
	public static void unloadProject()
	{
		projectIsLoaded = false;
		projectLocation = "";
		
		Main.frame.setTitle(MainWindow.TITLE + " - Project not loaded");
		
		Main.frame.slideList.clear();
		
		interfaceEnable(false);
		
		refreshProject();
	}
	
	private static void createProjectXml(String path) throws IOException
	{
		XmlWriter xml = new XmlWriter();
		
		xml.openTag("project");
		xml.addTag("global", "fullscreen=true");
		xml.closeTag();
		
		Util.saveFile(path, xml.get());
	}
	
	public static void interfaceEnable(boolean enabled)
	{
		Main.frame.menubar.save.setEnabled(enabled);
		Main.frame.menubar.project.setEnabled(enabled);
		Main.frame.menubar.run.setEnabled(enabled);
		Main.frame.menubar.refresh.setEnabled(enabled);
		Main.frame.menubar.export.setEnabled(enabled);
		Main.frame.menubar.exitproject.setEnabled(enabled);
		
		Main.frame.tree.setEnabled(enabled);
		Main.frame.actionpanel.setEnabled(enabled);
		
		Main.frame.slideList.getSlideCreator().setEnabled(false);
		Main.frame.slideList.setEnabled(enabled);
		
	}

	public static void refreshProject()
	{		
		Main.frame.tree.getRootNode().removeAllChildren();
		
		createChildren(new File(projectLocation), Main.frame.tree.getRootNode());
		
		((DefaultTreeModel) Main.frame.tree.getModel()).reload(Main.frame.tree.getRootNode());
	}
	
	
	public static void save()
	{
		try 
		{
			Main.frame.slideList.build(Util.projectPath(PROJECT_XML_NAME));
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(Main.frame, "Error:" + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void run()
	{
		try 
		{
			save();
				
			refreshProject();
				
			String javaexe = Util.path(System.getProperty("java.home"),"bin","java");
					
			ProcessBuilder pb = new ProcessBuilder(javaexe,"-cp",System.getProperty("java.class.path"),Main.class.getName(),projectLocation);
					
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
			try 
			{
				Project.createNewProject(project.getParent(),project.getName());
			}
			catch (Exception e) 
			{
				JOptionPane.showMessageDialog(Main.frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		return path;
	}
	
	public static String loadDialog()
	{
		String path;
		
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
		
		if (path != null)
		{
			try 
			{
				loadProject(path);
			} 
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(Main.frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
		
		if (files == null) 
			return;
		
		for (File file : files) 
		{
			var childNode = new DefaultMutableTreeNode(file);
			
			node.add(childNode);
			if (file.isDirectory()) 
				createChildren(file, childNode); 
		}
	}
}
