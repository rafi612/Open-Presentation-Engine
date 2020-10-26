package com.input;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;

import com.io.Stream;
import com.main.Main;
import com.opengl.main.Presentation;
import com.project.Project;
import com.tts.Language;
import com.tts.Speak;

public class Action implements ActionListener 
{
	Object source;

	@Override
	public void actionPerformed(ActionEvent e)
	{
		source = e.getSource();
		
		//import
		if (source == Main.autoscripts.get(0))
		{
			Main.textarea.insert("import ope.slide as slide\n", Main.textarea.getCaretPosition());
		}
		
		//create slide
		if (source == Main.autoscripts.get(1))
		{
			int slide = Integer.parseInt(JOptionPane.showInputDialog(Main.frame, "Enter slide number:", "slide", JOptionPane.QUESTION_MESSAGE));
			String file = JOptionPane.showInputDialog(Main.frame, "Enter file name", "slide", JOptionPane.QUESTION_MESSAGE);
			Main.textarea.insert("slide.createSlide(" + slide + "," + "\"" + file + "\")\n", Main.textarea.getCaretPosition());
		}
		
		//bg
		if (source == Main.autoscripts.get(2))
		{
			int num = Integer.parseInt(JOptionPane.showInputDialog(Main.frame, "Enter slide number:", "background",JOptionPane.QUESTION_MESSAGE));
			String name = JOptionPane.showInputDialog(Main.frame, "Enter background file name:", "background", JOptionPane.QUESTION_MESSAGE);
			Main.textarea.insert("slide.setSlideBg(" + num + ",\"" + name + "\")\n", Main.textarea.getCaretPosition());
		}
		
		//fullscreen
		if (source == Main.autoscripts.get(3))
			Main.textarea.insert("slide.setFullscreen(True)", Main.textarea.getCaretPosition());
		
		//music
		if (source == Main.autoscripts.get(4))
		{
			String name = JOptionPane.showInputDialog(Main.frame, "Enter music file name:", "music", JOptionPane.QUESTION_MESSAGE);
			Main.textarea.insert("slide.setGeneralMusic(\"" + name + "\")\n", Main.textarea.getCaretPosition());
		}
		
		if (source == Main.autoscripts.get(5))
		{
			Speak.TTSFrame();
		}
		
		
		//end
		if (source == Main.autoscripts.get(Main.autoscripts.size() - 2))
		{
			Main.textarea.insert("\nslide.End()", Main.textarea.getCaretPosition());
		}
		
		//run
		if (source == Main.actions.get(0))
		{
			Project.run();
		}
		
		//stop
		if (source == Main.actions.get(2))
		{
			Presentation.stop();
		}
		
//		//xml
//		if (source == Main.actions.get(3))
//		{
//			if (Project.editor == 0)
//			{
//				Project.SaveTextFromTextArea(Project.projectlocation + "\\" + "main.py");
//				Main.scrollpane.setBorder(BorderFactory.createTitledBorder("XML - config.xml"));
//				Project.editor = 1;
//				Main.textarea.setText("");
//				Project.loadTextFromFileToTextArea(Project.projectlocation + "\\" + "config.xml");
//			}
//		}
//		//python
//		if (source == Main.actions.get(4))
//		{
//			if (Project.editor == 1)
//			{
//				Project.SaveTextFromTextArea(Project.projectlocation + "\\" + "config.xml");
//				Main.scrollpane.setBorder(BorderFactory.createTitledBorder("Script - main.py"));
//				Project.editor = 0;
//				Main.textarea.setText("");
//				Project.loadTextFromFileToTextArea(Project.projectlocation + "\\" + "main.py");
//			}
//		}
		
		//action save
		if (source == Main.actions.get(1))
		{
//			int yesno = 1;
//			if (Project.editor == 0)
//				yesno = JOptionPane.showConfirmDialog(Main.frame, "Do you want to save main.py?", "Save",JOptionPane.YES_NO_OPTION ,JOptionPane.QUESTION_MESSAGE);
//			else
//				yesno = JOptionPane.showConfirmDialog(Main.frame, "Do you want to save config.xml?", "Save",JOptionPane.YES_NO_OPTION ,JOptionPane.QUESTION_MESSAGE);
//			
//			if (yesno == 0)
//			{
//				if (Project.editor == 0)
//					Project.SaveTextFromTextArea(Project.projectlocation + "\\" + "main.py");
//				else
//					Project.SaveTextFromTextArea(Project.projectlocation + "\\" + "config.xml");
//			}
			
			Project.save();
			
		}
		
		
		//menu===================================
		//new project
		if (source == Main.newproject)
		{
			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			jfc.setDialogTitle("Choose project directory:");
			
			int returnValue = jfc.showDialog(Main.frame,"Select this location");
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				if (jfc.getSelectedFile().isDirectory()) {
					System.out.println("You selected the directory: " + jfc.getSelectedFile());
				}
			}
			if (Project.projectIsLoaded)
			{
				int yesno = 1;
				yesno = JOptionPane.showConfirmDialog(Main.frame, "All changes are lost. Do you want to save?", "Save",JOptionPane.YES_NO_CANCEL_OPTION ,JOptionPane.QUESTION_MESSAGE);
			
				
				if (yesno == 0)
				{
					Project.SaveTextFromTextArea(Project.projectlocation + Stream.slash() + "main.py");
				}
				
				Project.unloadProject();
				
				if (yesno != 2)
				{
					if (returnValue == JFileChooser.APPROVE_OPTION) 
					{
						if (jfc.getSelectedFile().isDirectory()) 
						{
							String path = JOptionPane.showInputDialog(Main.frame,"Project Name:","Project",JOptionPane.QUESTION_MESSAGE);
							if (!path.equals(null)) 
								Project.CreateNewProject(jfc.getSelectedFile().toString(),path);
						}
					}
				}
			}
			else 
			{
				if (returnValue == JFileChooser.APPROVE_OPTION) 
				{
					if (jfc.getSelectedFile().isDirectory()) 
					{
						String path = JOptionPane.showInputDialog(Main.frame,"Project Name:","Project",JOptionPane.QUESTION_MESSAGE);
						if (!(path == null)) 
							Project.CreateNewProject(jfc.getSelectedFile().toString(),path);
					}
				}
			
			}
		}
		if (source == Main.loadproject)
		{
			if (Project.projectIsLoaded) Project.save();
			Project.load();
		}
		
		//save
		if (source == Main.save)
		{
//			int yesno = 1;
//			if (Project.editor == 0)
//				yesno = JOptionPane.showConfirmDialog(Main.frame, "Do you want to save main.py?", "Save",JOptionPane.YES_NO_OPTION ,JOptionPane.QUESTION_MESSAGE);
//			else
//				yesno = JOptionPane.showConfirmDialog(Main.frame, "Do you want to save config.xml?", "Save",JOptionPane.YES_NO_OPTION ,JOptionPane.QUESTION_MESSAGE);
//			
//			if (yesno == 0)
//			{
//				if (Project.editor == 0)
//					Project.SaveTextFromTextArea(Project.projectlocation + "\\" + "main.py");
//				else
//					Project.SaveTextFromTextArea(Project.projectlocation + "\\" + "config.xml");
//			}
			Project.save();
		}
		
		//exit project
		if (source == Main.exitproject)
		{
			int yesno = JOptionPane.showConfirmDialog(Main.frame, "All changes are lost. Do you want to save?", "Save",JOptionPane.YES_NO_CANCEL_OPTION ,JOptionPane.QUESTION_MESSAGE);
			
			if (yesno == 0)
			{
				Project.SaveTextFromTextArea(Project.projectlocation + Stream.slash() + "main.py");
			}
			
			if (yesno != 2)
			{
				Project.unloadProject();
			}
		}
		
		//exit 
		if (source == Main.exit)
			System.exit(0);
		
		//refresh
		if (source == Main.refresh)
			Project.refreshProject();
		
		//shell
		if (source == Main.shell)
		{
			try 
			{
				if (Stream.isWindows())
				{
					Process process = Runtime.getRuntime().exec("cmd /c start Python\\python.exe");
					process.waitFor();
				}
				else if (Stream.isLinux())
				{
					Process process = Runtime.getRuntime().exec("/usr/bin/x-terminal-emulator -e python3.7");
					process.waitFor();
				}
			} 
			catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		//popup
		//========================================================
		if (source == Main.newfile)
		{
			String name = JOptionPane.showInputDialog(Main.frame, "Enter file name:", "Create new file", JOptionPane.QUESTION_MESSAGE);
			Stream.saveFile(Project.projectlocation + Stream.slash() + name, "");
			
			Project.refreshProject();
		}
		if (source == Main.editfile)
		{
			try {
				if (Main.tree.getLastSelectedPathComponent() != null 
						&& !new File(Main.tree.getLastSelectedPathComponent().toString()).isDirectory() 
						&& !Main.tree.getLastSelectedPathComponent().toString().equals("Workspace                                  "))
					Runtime.getRuntime().exec("notepad " + "\"" + Main.tree.getLastSelectedPathComponent() + "\"");
				else
					JOptionPane.showMessageDialog(Main.frame, "No selected file", "File", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
//			JTextArea t = new JTextArea();
//			t.setFont(new Font(t.getFont().getName(), Font.TRUETYPE_FONT, 16));
//			
//			Main.tabs.add(Main.tree.getLastSelectedPathComponent().toString(),new JScrollPane(t));
//			
//			Project.loadTextFromFileToTextArea(Main.tree.getLastSelectedPathComponent().toString(), t);
		}
		if (source == Main.newfolder)
		{
			String folder = JOptionPane.showInputDialog(Main.frame, "Enter folder name:", "Create new folder", JOptionPane.QUESTION_MESSAGE);
			new File(Project.projectlocation + Stream.slash() + folder).mkdir();
			
			Project.refreshProject();
		}
		if (source == Main.openfile)
		{
			
		}
		if (source == Main.newxml)
		{
			String name = JOptionPane.showInputDialog(Main.frame, "Enter XML name:", "Create new XML", JOptionPane.QUESTION_MESSAGE);
			Stream.copyFile("/script/config.xml", Project.projectlocation + Stream.slash() + name + ".xml");
			
			Project.refreshProject();
		}
	}

}
