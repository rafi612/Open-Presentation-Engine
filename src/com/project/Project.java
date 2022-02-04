/* Copyright 2019-2020 by rafi612 */
package com.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import com.io.IoUtil;
import com.main.Main;
import com.presentation.main.Presentation;

public class Project 
{
	public static boolean projectIsLoaded = false;
	
	public static String projectlocation = "";
	
	public static void CreateNewProject(String location,String name)
	{
		if (projectIsLoaded) unloadProject();
		
		projectIsLoaded = true;
		projectlocation = location + File.separator + name;
		
		new File(projectlocation).mkdir();
		Main.frame.setTitle(Main.TITLE + " - " + location + File.separator + name);
		
		interfaceEnable(true);
		
        for (int i = 0;i < Main.actions.size(); i++)
        	Main.actions.get(i).setEnabled(true);
        
        for (int i = 0;i < Main.autoscripts.size(); i++)
        	Main.autoscripts.get(i).setEnabled(true);
		
		IoUtil.copyFile("/script/main.py",projectlocation + File.separator +"main.py");
		IoUtil.copyFile("/script/config.xml",projectlocation + File.separator + "config.xml");
		
		new File(Project.projectlocation + File.separator + "ope").mkdir();
		IoUtil.copyFile("/script/ope/slide.py", Project.projectlocation + File.separator + "ope" + File.separator + "slide.py");

		loadTextFromFileToTextArea(projectlocation + File.separator + "main.py");
		loadTextFromFileToTextArea(projectlocation + File.separator + "config.xml",Main.textarea2);
    	    	
    	refreshProject();
	}
	
	public static void LoadNewProject(String name)
	{
		if (projectIsLoaded) unloadProject();
		projectIsLoaded = true;
		projectlocation = name;
		Main.frame.setTitle(Main.TITLE + " - " + name);

		interfaceEnable(true);
		
        for (int i = 0;i < Main.actions.size(); i++)
        	Main.actions.get(i).setEnabled(true);
        
        for (int i = 0;i < Main.autoscripts.size(); i++)
        	Main.autoscripts.get(i).setEnabled(true);

		loadTextFromFileToTextArea(projectlocation + File.separator + "main.py");
		loadTextFromFileToTextArea(projectlocation + File.separator + "config.xml",Main.textarea2);
    	
    	refreshProject();
	}
	public static void unloadProject()
	{
		projectIsLoaded = false;
		projectlocation = "";
		Main.workspace.removeAllChildren();
		Main.textpane.setText("");
		Main.frame.setTitle(Main.TITLE + " - Project not loaded");

		interfaceEnable(false);
		
		Main.textpane.setText("Project is not loaded. Load project or create a new one.");
		Main.textarea2.setText("Project is not loaded. Load project or create a new one.");
		
        for (int i = 0;i < Main.actions.size(); i++)
        	Main.actions.get(i).setEnabled(false);
        
        for (int i = 0;i < Main.autoscripts.size(); i++)
        	Main.autoscripts.get(i).setEnabled(false);
		
    	DefaultTreeModel model=(DefaultTreeModel)Main.tree.getModel();
    	model.reload(Main.workspace);
    	
    	refreshProject();
	}
	
	public static void interfaceEnable(boolean b)
	{
		Main.textpane.setEnabled(b);
		Main.tree.setEnabled(b);
		Main.save.setEnabled(b);
		Main.copy.setEnabled(b);
		Main.paste.setEnabled(b);
		Main.cut.setEnabled(b);
		Main.selectAll.setEnabled(b);
		Main.run.setEnabled(b);
		Main.refresh.setEnabled(b);
		Main.export.setEnabled(b);
		Main.exitproject.setEnabled(b);
		Main.sliderack.setEnabled(b);
		
		if(b)
			Main.slidecreator.initenable();
		else
			Main.slidecreator.disable();
		
	}

	public static void refreshProject()
	{
		Main.workspace.removeAllChildren();
		
		createChildren(new File(projectlocation), Main.workspace);
	    
    	DefaultTreeModel model=(DefaultTreeModel)Main.tree.getModel();
    	model.reload(Main.workspace);
	}
	
	
	public static void save()
	{
		SaveTextFromTextArea(Project.projectlocation + File.separator + "main.py");
		SaveTextFromTextArea(Project.projectlocation + File.separator + "config.xml",Main.textarea2);
	}
	
	public static void run()
	{
		try //(PythonInterpreter pyInterp = new PythonInterpreter()) 
		{
			Project.SaveTextFromTextArea(Project.projectlocation + File.separator + "main.py");
			Process process;
			
			ArrayList<String> pyout = new ArrayList<String>();
			//String pyout = "Python output:";
			
			ProcessBuilder builder = new ProcessBuilder(Main.interpreterpath, Project.projectlocation + File.separator + "main.py");
			builder.directory(new File(projectlocation + File.separator));
			process = builder.start();
				
	        BufferedReader subProcessInputReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	        String line = null;
	        while ((line = subProcessInputReader.readLine()) != null)
	        {
	        	pyout.add(line);
	        	
	        }
				
			process.waitFor();
			//System.out.println("Python exit code: " + process.exitValue());
								
			boolean run = true;
//			
			if (process.exitValue() != 0)
			{
				String lines = "Python Output:\n";
				Main.areaconsole.setText("");
				for (int i = 0;i < pyout.size();i++)
				{
					lines = lines + "<html><font color=#FF0000>"+ pyout.get(i) + "</font></html>" + "\n";
					Main.areaconsole.append(pyout.get(i) + "\n");
				}
				int yesno = JOptionPane.showConfirmDialog(Main.frame, "Python return exit code " + process.exitValue() + ". Do you want to run?\n" + lines, "Error", JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE);
				if (yesno == 1) run = false;
			}
			else Main.areaconsole.setText("No python output");
			
			Project.loadTextFromFileToTextArea(Project.projectlocation + File.separator + "config.xml", Main.textarea2);
			Project.refreshProject();
			
			if (run) Presentation.init();
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
		String path = TinyFileDialogs.tinyfd_selectFolderDialog("Select project folder",System.getProperty("user.home") + File.separator);
		
		if(path != null)
		{
			if (new File(path).exists())
				LoadNewProject(path);
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
        	
        	//System.out.println(file.getName());
        	
        	if (file.isDirectory() && file.getName().equals("ope"))
        	{}
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
	
	public static void append(String s,JTextPane t) 
	{
		   try {
		      Document doc = t.getDocument();
		      doc.insertString(doc.getLength(), s, null);
		   } catch(BadLocationException exc) {
		      exc.printStackTrace();
		   }
	}
	
	public static void loadTextFromFileToTextArea(String path)
	{
		loadTextFromFileToTextArea(path,Main.textpane);
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
	
	public static void loadTextFromFileToTextArea(String path,JTextPane t)
	{
		t.setText("");
		BufferedReader in = null;
		try 
		{
			in = new BufferedReader(new FileReader(path));
			String str;
			while ((str = in.readLine()) != null) 
			{
				append(str + "\n",t);
			}
		} 
		catch (IOException e)
		{
			
		}
	}
	
	public static void SaveTextFromTextArea(String path) 
	{
		SaveTextFromTextArea(path,Main.textpane);
    }

	public static void SaveTextFromTextArea(String path,JTextComponent t) 
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
}
