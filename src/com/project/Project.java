/* Copyright 2019-2020 by rafi612 */
package com.project;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Platform;
import org.lwjgl.util.nfd.NativeFileDialog;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import com.io.Util;
import com.main.Main;

public class Project 
{
	public static boolean projectIsLoaded = false;
	
	public static String projectlocation;
	public static String projectXmlName = "project.xml";
	
	public static void CreateNewProject(String location,String name)
	{
		if (projectIsLoaded) 
			unloadProject();
		
		projectIsLoaded = true;
		projectlocation = Util.path(location,name);
		
		new File(projectlocation).mkdir();
		Main.frame.setTitle(Main.TITLE + " - " + location + File.separator + name);
		
		interfaceEnable(true);
		
		createProjectXml(Util.projectPath(projectXmlName));
    	    	
    	refreshProject();
	}
	
	//TODO: old config.xml based code will be removed
	private static void createProjectXml(String path)
	{
		String lines = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<project>\n"
				+ "</project>";
		Util.saveFile(path, lines);
	}
	
	public static void LoadProject(String path)
	{
		if (projectIsLoaded) 
			unloadProject();
		
		projectIsLoaded = true;
		projectlocation = path;
		
		if (!new File(Util.projectPath(projectXmlName)).exists())
		{
			JOptionPane.showMessageDialog(Main.frame, "This is not project folder (project.xml missing)", "Can't load project", JOptionPane.ERROR_MESSAGE);
			unloadProject();
			return;
		}
		
		Main.frame.setTitle(Main.TITLE + " - " + path);

		interfaceEnable(true);
    	
		Main.sliderack.load(Util.projectPath(projectXmlName));
		
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
	
	public static void interfaceEnable(boolean enabled)
	{
		Main.tree.setEnabled(enabled);
		Main.menubar.save.setEnabled(enabled);
		Main.menubar.copy.setEnabled(enabled);
		Main.menubar.paste.setEnabled(enabled);
		Main.menubar.cut.setEnabled(enabled);
		Main.menubar.selectAll.setEnabled(enabled);
		Main.menubar.run.setEnabled(enabled);
		Main.menubar.refresh.setEnabled(enabled);
		Main.menubar.export.setEnabled(enabled);
		Main.menubar.exitproject.setEnabled(enabled);
		Main.sliderack.setEnabled(enabled);
		
        Main.actionpanel.setEnabled(enabled);
		
		if(enabled)
			Main.slidecreator.initenable();
		else
			Main.slidecreator.disable();
		
	}

	public static void refreshProject()
	{		
		Main.workspace.removeAllChildren();
		
		createChildren(new File(projectlocation), Main.workspace);
	    
    	DefaultTreeModel model = (DefaultTreeModel) Main.tree.getModel();
    	model.reload(Main.workspace);
	}
	
	
	public static void save()
	{
		Main.sliderack.build(Util.projectPath(projectXmlName));
	}
	
	public static void run()
	{
		try
		{
			Main.sliderack.build(Util.projectPath(projectXmlName));
			
			Project.refreshProject();
			
			String javaexe = Util.path(System.getProperty("java.home"),"bin","java");
				
			ProcessBuilder pb = new ProcessBuilder(javaexe,"-cp",System.getProperty("java.class.path"),Main.class.getName(),Util.projectPath("config.xml"));
			pb.directory(new File(projectlocation));
				
			//redirect output to terminal
			pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
			pb.redirectError(ProcessBuilder.Redirect.INHERIT);
			pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
				
			pb.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static String newproject_dialog()
	{
		String path = TinyFileDialogs.tinyfd_saveFileDialog("Create Project", System.getProperty("user.home") + File.separator, null, "Folder");
		
		if (path != null) 
		{
			File project = new File(path);
			Project.CreateNewProject(project.getParent(),project.getName());
		}
		
		return path;
	}
	
	public static String load_dialog()
	{
		String path = null;
		//NFD have better file dialog on windows
		if (Platform.get() == Platform.WINDOWS)
		{
			PointerBuffer p = MemoryUtil.memAllocPointer(1);
			
			NativeFileDialog.NFD_PickFolder(System.getProperty("user.home") + File.separator, p);
			
			path = p.getStringUTF8();
			
			MemoryUtil.memFree(p);
		}
		else 
			path = TinyFileDialogs.tinyfd_selectFolderDialog("Select project folder",System.getProperty("user.home") + File.separator);
		
		if(path != null)
		{
			if (new File(path).exists())
				LoadProject(path);
			else
				JOptionPane.showMessageDialog(Main.frame, "This folder is not a project", "Error",JOptionPane.ERROR_MESSAGE);
		}
		
		return path;
	}
	
	public static int lost_save_dialog()
	{
		int yesno = JOptionPane.showConfirmDialog(Main.frame, "All unsaved changes are lost. Do you want to save?", "Save",JOptionPane.YES_NO_CANCEL_OPTION ,JOptionPane.QUESTION_MESSAGE);
				
		if (yesno == 0)
			save();
		
		return yesno;
	}
	
	public static int save_dialog()
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
        	DefaultMutableTreeNode childNode = null;
        	
        	childNode = new DefaultMutableTreeNode(file);
            node.add(childNode);
            if (file.isDirectory()) 
            {
            	createChildren(file, childNode);
            }    
        }
    }
}
