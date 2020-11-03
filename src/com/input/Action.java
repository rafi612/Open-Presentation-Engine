/* Copyright 2019-2020 by rafi612 */
package com.input;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.io.Stream;
import com.main.Main;
import com.presentation.main.Presentation;
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
			insert("import ope.slide as slide\n", Main.textpane.getCaretPosition(),Main.textpane);
		}
		
		//create slide
		if (source == Main.autoscripts.get(1))
		{
			int slide = Integer.parseInt(JOptionPane.showInputDialog(Main.frame, "Enter slide number:", "slide", JOptionPane.QUESTION_MESSAGE));
			String file = JOptionPane.showInputDialog(Main.frame, "Enter file name", "slide", JOptionPane.QUESTION_MESSAGE);
			insert("slide.createSlide(" + slide + "," + "\"" + file + "\")\n", Main.textpane.getCaretPosition(),Main.textpane);
		}
		
		//bg
		if (source == Main.autoscripts.get(2))
		{
			int num = Integer.parseInt(JOptionPane.showInputDialog(Main.frame, "Enter slide number:", "background",JOptionPane.QUESTION_MESSAGE));
			String name = JOptionPane.showInputDialog(Main.frame, "Enter background file name:", "background", JOptionPane.QUESTION_MESSAGE);
			insert("slide.setSlideBg(" + num + ",\"" + name + "\")\n", Main.textpane.getCaretPosition(),Main.textpane);
		}
		
		//fullscreen
		if (source == Main.autoscripts.get(3))
			insert("slide.setFullscreen(True)", Main.textpane.getCaretPosition(),Main.textpane);
		
		//music
		if (source == Main.autoscripts.get(4))
		{
			String name = JOptionPane.showInputDialog(Main.frame, "Enter music file name:", "music", JOptionPane.QUESTION_MESSAGE);
			insert("slide.setGeneralMusic(\"" + name + "\")\n", Main.textpane.getCaretPosition(),Main.textpane);
		}
		
		//tts
		if (source == Main.autoscripts.get(5))
		{
			Speak.TTSFrame();
		}
		
		//tts key
		if (source == Main.autoscripts.get(6))
		{
			String key;
			do
			{
				key = JOptionPane.showInputDialog(Main.frame, "Enter TTS key char:", "tts key", JOptionPane.QUESTION_MESSAGE);
			} while (key.length() != 1);
			
			insert("slide.setTTSKey(\"" + key + "\")\n", Main.textpane.getCaretPosition(),Main.textpane);
		}
		
		//entrace animation
		if (source == Main.autoscripts.get(7))
		{
			String[] s = {"Appearing"};
			JComboBox<String> combo = new JComboBox<String>(s);
			JComponent[] c = {new JLabel("Choose Animation:"),combo};
			
			int num = Integer.parseInt(JOptionPane.showInputDialog(Main.frame, "Enter slide number:", "Animation",JOptionPane.QUESTION_MESSAGE));
			JOptionPane.showConfirmDialog(Main.frame,c , "Animation", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
			
			insert("slide.setEntranceAnimation(" + num + ",\"" + combo.getSelectedItem().toString().toLowerCase() + "\")\n", Main.textpane.getCaretPosition(),Main.textpane);
		}
		
		//exit animation
		if (source == Main.autoscripts.get(8))
		{
			String[] s = {"Disappearance"};
			JComboBox<String> combo = new JComboBox<String>(s);
			JComponent[] c = {new JLabel("Choose Animation:"),combo};
			
			int num = Integer.parseInt(JOptionPane.showInputDialog(Main.frame, "Enter slide number:", "Animation",JOptionPane.QUESTION_MESSAGE));
			JOptionPane.showConfirmDialog(Main.frame,c , "Animation", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
			
			insert("slide.setExitAnimation(" + num + ",\"" + combo.getSelectedItem().toString().toLowerCase() + "\")\n", Main.textpane.getCaretPosition(),Main.textpane);
		}
		
		
		//end
		if (source == Main.autoscripts.get(Main.autoscripts.size() - 2))
		{
			insert("\nslide.End()", Main.textpane.getCaretPosition(),Main.textpane);
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
					Process process = Runtime.getRuntime().exec("cmd /c start " + Main.interpreterpath);
					process.waitFor();
				}
				if (Stream.isLinux())
				{
					Process process = Runtime.getRuntime().exec("/usr/bin/x-terminal-emulator -e " + Main.interpreterpath);
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
		
		//python 
		if (source == Main.winpy)
		{
			Main.interpreterpath = "Python\\python.exe";
			Main.interpretertype = "winpy";
			Stream.saveOPExml(Main.interpretertype, Main.interpreterpath);
			Main.custom.setText("Custom");
		}
		if (source == Main.winsystem)
		{
			Main.interpreterpath = "python.exe";
			Main.interpretertype = "winsystem";
			Stream.saveOPExml(Main.interpretertype, Main.interpreterpath);
			Main.custom.setText("Custom");
		}
		if (source == Main.linux)
		{
			Main.interpreterpath = "python3.7";
			Main.interpretertype = "linux";
			Stream.saveOPExml(Main.interpretertype, Main.interpreterpath);
			Main.custom.setText("Custom");
		}
		if (source == Main.macos)
		{
			Main.interpreterpath = "python";
			Main.interpretertype = "macos";
			Stream.saveOPExml(Main.interpretertype, Main.interpreterpath);
			Main.custom.setText("Custom");
		}
		if (source == Main.custom)
		{
			String path = JOptionPane.showInputDialog(Main.frame, "Enter Python path:", "Python",JOptionPane.QUESTION_MESSAGE);
			if (!(path == null))
			{
				Main.interpreterpath = path;
				Main.custom.setText("Custom " + "(" + Main.interpreterpath + ")");
				Main.interpretertype = "custom";
				Stream.saveOPExml(Main.interpretertype, Main.interpreterpath);
			}
			else
			{
				Stream.selectPy();
			}
			
		}
		
		if (source == Main.license)
		{
			JDialog dialog = new JDialog(Main.frame,"OPE License");
			dialog.setSize(600,600);
			dialog.setLayout(new BorderLayout());
			dialog.setLocationRelativeTo(Main.frame);
			JTextArea text = new JTextArea();
			text.setEditable(false);
			text.setFont(new Font(text.getFont().getName(), Font.TRUETYPE_FONT, 12));
			try 
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(Action.class.getResourceAsStream("/LICENSE.txt")));
				String str;
				while ((str = in.readLine()) != null) 
				{
					text.append(str + "\n");
				}
			} 
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			text.setSelectionStart(0);
			text.setSelectionEnd(0);
			JScrollPane s = new JScrollPane(text);
			dialog.add(s,BorderLayout.CENTER);
			dialog.setVisible(true);
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
	
	public void insert(String s,int p,JTextPane t) 
	{
		   try 
		   {
		      Document doc = t.getDocument();
		      doc.insertString(p, s, null);
		   } 
		   catch(BadLocationException exc) 
		   {
		      exc.printStackTrace();
		   }
	}

}
