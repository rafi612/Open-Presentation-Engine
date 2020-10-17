package com.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.io.Stream;
import com.main.Main;
import com.opengl.main.Presentation;

public class Project 
{
	public static boolean projectIsLoaded = false;
	
	public static String projectlocation = "";
	
	public static int editor = 0;
	
	public static void CreateNewProject(String location,String name)
	{
		if (projectIsLoaded) unloadProject();
		projectIsLoaded = true;
		projectlocation = location + "\\" + name;
		new File(projectlocation).mkdir();
		Main.textarea.setEnabled(true);
		Main.frame.setTitle(Main.TITLE + " - " + location + "\\" + name);
		Main.tree.setEnabled(true);
		Main.save.setEnabled(true);
		Main.copy.setEnabled(true);
		Main.paste.setEnabled(true);
		Main.cut.setEnabled(true);
		Main.selectAll.setEnabled(true);
		Main.newfile.setEnabled(true);
		Main.newfolder.setEnabled(true);
		Main.openfile.setEnabled(true);
		Main.newxml.setEnabled(true);
		Main.run.setEnabled(true);
		Main.refresh.setEnabled(true);
		Main.export.setEnabled(true);
		Main.exitproject.setEnabled(true);
		
        for (int i = 0;i < Main.actions.size(); i++)
        	Main.actions.get(i).setEnabled(true);
        
        for (int i = 0;i < Main.autoscripts.size(); i++)
        	Main.autoscripts.get(i).setEnabled(true);
		
		Stream.copyFile("/script/main.py",projectlocation + "\\main.py");
		Stream.copyFile("/script/config.xml",projectlocation + "\\config.xml");
		
		new File(Project.projectlocation + "\\ope").mkdir();
		Stream.copyFile("/script/ope/slide.py", Project.projectlocation + "\\ope\\slide.py");

		loadTextFromFileToTextArea(projectlocation + "\\main.py");
		loadTextFromFileToTextArea(projectlocation + "\\config.xml",Main.textarea2);
		
		Main.workspace.add(new DefaultMutableTreeNode(new File(projectlocation + "\\main.py")));
    	DefaultTreeModel model=(DefaultTreeModel)Main.tree.getModel();
    	model.reload(Main.workspace);
    	
    	Project.refreshProject();
	}
	
	public static void LoadNewProject(String name)
	{
		if (projectIsLoaded) unloadProject();
		projectIsLoaded = true;
		projectlocation = name;
		Main.textarea.setEnabled(true);
		Main.frame.setTitle(Main.TITLE + " - " + name);
		Main.tree.setEnabled(true);
		Main.save.setEnabled(true);
		Main.copy.setEnabled(true);
		Main.paste.setEnabled(true);
		Main.cut.setEnabled(true);
		Main.selectAll.setEnabled(true);
		Main.newfile.setEnabled(true);
		Main.newfolder.setEnabled(true);
		Main.openfile.setEnabled(true);
		Main.newxml.setEnabled(true);
		Main.run.setEnabled(true);
		Main.refresh.setEnabled(true);
		Main.export.setEnabled(true);
		Main.exitproject.setEnabled(true);
		
        for (int i = 0;i < Main.actions.size(); i++)
        	Main.actions.get(i).setEnabled(true);
        
        for (int i = 0;i < Main.autoscripts.size(); i++)
        	Main.autoscripts.get(i).setEnabled(true);

		loadTextFromFileToTextArea(projectlocation + "\\main.py");
		loadTextFromFileToTextArea(projectlocation + "\\config.xml",Main.textarea2);
		
		Main.workspace.add(new DefaultMutableTreeNode(new File(projectlocation + "\\main.py")));
    	DefaultTreeModel model=(DefaultTreeModel)Main.tree.getModel();
    	model.reload(Main.workspace);
    	
    	Project.refreshProject();
	}
	public static void unloadProject()
	{
		projectIsLoaded = false;
		projectlocation = "";
		Main.workspace.removeAllChildren();
		Main.textarea.setText("");
		Main.textarea.setEnabled(false);
		Main.frame.setTitle(Main.TITLE);
		Main.tree.setEnabled(false);
		Main.save.setEnabled(false);
		Main.copy.setEnabled(false);
		Main.paste.setEnabled(false);
		Main.cut.setEnabled(false);
		Main.newfile.setEnabled(false);
		Main.newfolder.setEnabled(false);
		Main.openfile.setEnabled(false);
		Main.newxml.setEnabled(false);
		Main.selectAll.setEnabled(false);
		Main.run.setEnabled(false);
		Main.refresh.setEnabled(false);
		Main.export.setEnabled(false);
		Main.exitproject.setEnabled(false);
		
		Main.textarea.setText("Project is not loaded. Load project or create a new one.");
		Main.textarea2.setText("Project is not loaded. Load project or create a new one.");
		
        for (int i = 0;i < Main.actions.size(); i++)
        	Main.actions.get(i).setEnabled(false);
        
        for (int i = 0;i < Main.autoscripts.size(); i++)
        	Main.autoscripts.get(i).setEnabled(false);
		
    	DefaultTreeModel model=(DefaultTreeModel)Main.tree.getModel();
    	model.reload(Main.workspace);
	}
	
	public static void loadTextFromFileToTextArea(String path)
	{
		loadTextFromFileToTextArea(path,Main.textarea);
	}
	
	public static void loadTextFromFileToTextArea(String path,JTextArea t)
	{
		t.setText("");
		BufferedReader in = null;
		try 
		{
			in = new BufferedReader(new FileReader(path));
			String str;
			while ((str = in.readLine()) != null) 
			{
				t.append(str + "\n");
			}
		} 
		catch (IOException e)
		{
			
		}
	}
	

    private static void createChildren(File fileRoot, DefaultMutableTreeNode node) 
    {
        File[] files = fileRoot.listFiles();
        if (files == null) return;

        for (File file : files) 
        {
        	DefaultMutableTreeNode childNode = null;
        	
        	//System.out.println(file.getName());
        	
        	if (file.isDirectory() && file.getName().equals("ope"))
        	{
        		//nic nie rob
        	}
        	else
        	{
        		childNode = new DefaultMutableTreeNode(file);
                node.add(childNode);
                if (file.isDirectory()) 
                {
                    createChildren(file, childNode);
                }
        	}
        
        }
    }

	public static void refreshProject()
	{
		Main.workspace.removeAllChildren();
//	    for (final File file : new File(projectlocation).listFiles()) 
//	    {
//	        Main.workspace.add(new DefaultMutableTreeNode(file));
//	    }
		
		createChildren(new File(projectlocation), Main.workspace);
	    
    	DefaultTreeModel model=(DefaultTreeModel)Main.tree.getModel();
    	model.reload(Main.workspace);
	}
	
	public static void SaveTextFromTextArea(String path) 
	{
		SaveTextFromTextArea(path,Main.textarea);
    }

	public static void SaveTextFromTextArea(String path,JTextArea t) 
	{
		try 
		{
			PrintWriter pw = new PrintWriter(new File(path));
			Scanner s = new Scanner(t.getText());
			while (s.hasNext())
				pw.println(s.nextLine());
			pw.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
    }
	
	public static int save()
	{
		int yesno = JOptionPane.showConfirmDialog(Main.frame, "Do you want to save?", "Save",JOptionPane.YES_NO_OPTION ,JOptionPane.QUESTION_MESSAGE);
				
		if (yesno == 0)
		{
			SaveTextFromTextArea(Project.projectlocation + "\\" + "main.py");
			SaveTextFromTextArea(Project.projectlocation + "\\" + "config.xml",Main.textarea2);
		}
		
		return yesno;
	}
	
	public static void run()
	{
		try //(PythonInterpreter pyInterp = new PythonInterpreter()) 
		{
			if (Project.editor == 0)Project.SaveTextFromTextArea(Project.projectlocation + "\\main.py");
			Process process;
			//process = Runtime.getRuntime().exec("Python\\python.exe \"" + Project.projectlocation + "\\main.py\" " + "\"" + Project.projectlocation + "\"");
			process = Runtime.getRuntime().exec("Python\\python.exe \"" + Project.projectlocation + "\\main.py\"");
			process.waitFor();
			
		    //pyInterp.execfile(projectlocation + "\\main.py");
			//System.out.println("Python\\python.exe \"" + Project.projectlocation + "\\main.py\" " + "\"" + Project.projectlocation + "\"");
								
			boolean run = true;
			File config = new File("config.xml");
			if (!config.exists())
			{
				int yesno = JOptionPane.showConfirmDialog(Main.frame, "Python interpreter found errors.\nNew XML not generated. Do you want to run?", "Program errors found", JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE);
				if (yesno == 1) run = false;
			}
				
			if (config.exists()) 
			{
				Stream.copyFileondrive("config.xml",Project.projectlocation + "\\" + "config.xml");
				config.delete();
			}
			
			Project.refreshProject();
			
			if (run) Presentation.init();
			
			Project.loadTextFromFileToTextArea(Project.projectlocation + "\\" + "config.xml", Main.textarea2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static String load()
	{
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setDialogTitle("Choose a directory to load project:");
		
		int returnValue = jfc.showOpenDialog(Main.frame);
		if (returnValue == JFileChooser.APPROVE_OPTION) 
		{
			if (jfc.getSelectedFile().isDirectory()) 
			{
				System.out.println("You selected the directory: " + jfc.getSelectedFile());
				
				if (new File(jfc.getSelectedFile().toString() + "\\main.py").exists())
					LoadNewProject(jfc.getSelectedFile().toString());
				else
					JOptionPane.showMessageDialog(Main.frame, "This folder is not a project", "Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		return jfc.getSelectedFile().toString();
	}
}
