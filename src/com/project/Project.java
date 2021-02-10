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

import com.io.Stream;
import com.main.Main;
import com.presentation.main.Presentation;

public class Project 
{
	public static boolean projectIsLoaded = false;
	
	public static String projectlocation = "";
	
	//public static int editor = 0;
	
	public static void CreateNewProject(String location,String name)
	{
		if (projectIsLoaded) unloadProject();
		projectIsLoaded = true;
		projectlocation = location + slash() + name;
		new File(projectlocation).mkdir();
		Main.textpane.setEnabled(true);
		Main.frame.setTitle(Main.TITLE + " - " + location + slash() + name);
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
		Main.slidecreator.initenable();
		
        for (int i = 0;i < Main.actions.size(); i++)
        	Main.actions.get(i).setEnabled(true);
        
        for (int i = 0;i < Main.autoscripts.size(); i++)
        	Main.autoscripts.get(i).setEnabled(true);
		
		Stream.copyFile("/script/main.py",projectlocation + slash() +"main.py");
		Stream.copyFile("/script/config.xml",projectlocation + slash() + "config.xml");
		
		new File(Project.projectlocation + slash() + "ope").mkdir();
		Stream.copyFile("/script/ope/slide.py", Project.projectlocation + slash() + "ope" + slash() + "slide.py");

		loadTextFromFileToTextArea(projectlocation + slash() + "main.py");
		loadTextFromFileToTextArea(projectlocation + slash() + "config.xml",Main.textarea2);
		
		Main.workspace.add(new DefaultMutableTreeNode(new File(projectlocation + slash() + "main.py")));
    	DefaultTreeModel model=(DefaultTreeModel)Main.tree.getModel();
    	model.reload(Main.workspace);
    	    	
    	Project.refreshProject();
	}
	
	public static void LoadNewProject(String name)
	{
		if (projectIsLoaded) unloadProject();
		projectIsLoaded = true;
		projectlocation = name;
		Main.textpane.setEnabled(true);
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
		Main.slidecreator.initenable();
		
        for (int i = 0;i < Main.actions.size(); i++)
        	Main.actions.get(i).setEnabled(true);
        
        for (int i = 0;i < Main.autoscripts.size(); i++)
        	Main.autoscripts.get(i).setEnabled(true);

		loadTextFromFileToTextArea(projectlocation + slash() + "main.py");
		loadTextFromFileToTextArea(projectlocation + slash() + "config.xml",Main.textarea2);
		
		Main.workspace.add(new DefaultMutableTreeNode(new File(projectlocation + slash() + "main.py")));
    	DefaultTreeModel model=(DefaultTreeModel)Main.tree.getModel();
    	model.reload(Main.workspace);
    	
    	Project.refreshProject();
	}
	public static void unloadProject()
	{
		projectIsLoaded = false;
		projectlocation = "";
		Main.workspace.removeAllChildren();
		Main.textpane.setText("");
		Main.textpane.setEnabled(false);
		Main.frame.setTitle(Main.TITLE + " - Project not loaded");
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
		Main.slidecreator.disable();
		
		Main.textpane.setText("Project is not loaded. Load project or create a new one.");
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
	
	public static int save()
	{
		int yesno = JOptionPane.showConfirmDialog(Main.frame, "Do you want to save?", "Save",JOptionPane.YES_NO_OPTION ,JOptionPane.QUESTION_MESSAGE);
				
		if (yesno == 0)
		{
			SaveTextFromTextArea(Project.projectlocation + slash() + "main.py");
			SaveTextFromTextArea(Project.projectlocation + slash() + "config.xml",Main.textarea2);
		}
		
		return yesno;
	}
	
	public static void run()
	{
		try //(PythonInterpreter pyInterp = new PythonInterpreter()) 
		{
			Project.SaveTextFromTextArea(Project.projectlocation + slash() + "main.py");
			Process process;
			
			ArrayList<String> pyout = new ArrayList<String>();
			//String pyout = "Python output:";
			
			ProcessBuilder builder = new ProcessBuilder(Main.interpreterpath, Project.projectlocation + slash() + "main.py");
			builder.directory(new File(projectlocation + slash()));
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
				//System.out.println("!config.exists()");
			}
			else Main.areaconsole.setText("No python output");
			
			Project.loadTextFromFileToTextArea(Project.projectlocation + slash() + "config.xml", Main.textarea2);
			Project.refreshProject();
			
			if (run) Presentation.init();
			
			//System.out.println("boolean run = " + run);
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
				
				if (new File(jfc.getSelectedFile().toString() + slash() + "main.py").exists())
					LoadNewProject(jfc.getSelectedFile().toString());
				else
					JOptionPane.showMessageDialog(Main.frame, "This folder is not a project", "Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		if (jfc.getSelectedFile() != null) return jfc.getSelectedFile().toString();
		return null;
	}
	
	private static String slash()
	{
		return File.separator;
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
}
