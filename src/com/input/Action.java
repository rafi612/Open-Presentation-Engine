/* Copyright 2019-2020 by rafi612 */
package com.input;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.lwjgl.system.Platform;

import com.io.Config;
import com.io.IoUtil;
import com.main.Main;
import com.presentation.main.Presentation;
import com.project.Project;
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
			String name = JOptionPane.showInputDialog(Main.frame, "Enter background file name:", "background", JOptionPane.QUESTION_MESSAGE);
			insert("slide.setSlideBg(\"" + name + "\")\n", Main.textpane.getCaretPosition(),Main.textpane);
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
			
			JOptionPane.showConfirmDialog(Main.frame,c , "Animation", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
			
			insert("slide.setEntranceAnimation(slide.Animation." + combo.getSelectedItem().toString().toUpperCase() + ")\n", Main.textpane.getCaretPosition(),Main.textpane);
		}
		
		//exit animation
		if (source == Main.autoscripts.get(8))
		{
			String[] s = {"Disappearance"};
			JComboBox<String> combo = new JComboBox<String>(s);
			JComponent[] c = {new JLabel("Choose Animation:"),combo};
			
			JOptionPane.showConfirmDialog(Main.frame,c , "Animation", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
			
			insert("slide.setExitAnimation(slide.Animation." + combo.getSelectedItem().toString().toUpperCase() + ")\n", Main.textpane.getCaretPosition(),Main.textpane);
		}
		
		
		//end
		if (source == Main.autoscripts.get(Main.autoscripts.size() - 2))
		{
			insert("\nslide.End()", Main.textpane.getCaretPosition(),Main.textpane);
		}
		
		//run
		if (source == Main.actions.get(0) || source == Main.runandbuild)
		{
			Project.run();
		}
		
		//stop
		if (source == Main.actions.get(2))
		{
			Presentation.stop();
		}
		
		//action save
		if (source == Main.actions.get(1))
		{			
			Project.save_dialog();
			
		}
		
		
		//menu===================================
		//new project
		if (source == Main.newproject)
		{
			int yesno = 0;
			if (Project.projectIsLoaded) 
				yesno = Project.lost_save_dialog();
			
			if (yesno != 2) Project.newproject_dialog();
		}
		if (source == Main.loadproject)
		{
			int yesno = 0;
			if (Project.projectIsLoaded) 
				yesno = Project.lost_save_dialog();
			
			if (yesno != 2) Project.load_dialog();
		}
		
		//save
		if (source == Main.save)
		{
			Project.save_dialog();
		}
		
		//exit project
		if (source == Main.exitproject)
		{
			int yesno = 0;
			if (Project.projectIsLoaded) 
				yesno = Project.lost_save_dialog();
			
			if (yesno != 2) Project.unloadProject();
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
				if (Platform.get() == Platform.WINDOWS)
				{
					Process process = Runtime.getRuntime().exec("cmd /c start " + Main.interpreterpath);
					process.waitFor();
				}
				if (Platform.get() == Platform.LINUX)
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
			Config.saveOPExml(Main.interpretertype, Main.interpreterpath);
			Main.custom.setText("Custom");
		}
		if (source == Main.winsystem)
		{
			Main.interpreterpath = "python.exe";
			Main.interpretertype = "winsystem";
			Config.saveOPExml(Main.interpretertype, Main.interpreterpath);
			Main.custom.setText("Custom");
		}
		if (source == Main.linux)
		{
			Main.interpreterpath = "python3.7";
			Main.interpretertype = "linux";
			Config.saveOPExml(Main.interpretertype, Main.interpreterpath);
			Main.custom.setText("Custom");
		}
		if (source == Main.macos)
		{
			Main.interpreterpath = "python";
			Main.interpretertype = "macos";
			Config.saveOPExml(Main.interpretertype, Main.interpreterpath);
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
				Config.saveOPExml(Main.interpretertype, Main.interpreterpath);
			}
			else
			{
				Config.selectPy();
			}
			
		}
		//license
		if (source == Main.license)
		{
			JDialog dialog = new JDialog(Main.frame,"OPE License");
			dialog.setSize(600,600);
			dialog.setLayout(new BorderLayout());
			dialog.setLocationRelativeTo(Main.frame);
			JTextArea text = getLicense();
			text.setFont(new Font(text.getFont().getName(), Font.TRUETYPE_FONT, 12));
			
			JScrollPane s = new JScrollPane(text);
			dialog.add(s,BorderLayout.CENTER);
			dialog.setVisible(true);
		}
		
		if (source == Main.about)
		{
			JDialog dialog = new JDialog(Main.frame,"About");	
			dialog.setSize(720,520);
			dialog.setLayout(new BorderLayout());
			dialog.setLocationRelativeTo(Main.frame);
			dialog.setLayout(new BorderLayout());
			
			//logo
			JPanel logo = new JPanel();
			logo.setLayout(new BorderLayout());
			
			JLabel l = new JLabel(new ImageIcon(Main.loadIcon("/images/iconsmall.png")));
			
			logo.add(new JLabel("        "),BorderLayout.WEST);
			logo.add(l,BorderLayout.CENTER);
			logo.add(new JLabel("        "),BorderLayout.EAST);
			//logo.setBorder(BorderFactory.createTitledBorder(""));
			dialog.add(logo,BorderLayout.WEST);
			
			//botton panel
			JPanel bottom = new JPanel();
			bottom.add(new JButton("Autor Github Profile"));
			bottom.add(new JButton("Project Web Site"));
			bottom.setBackground(Color.LIGHT_GRAY);
			dialog.add(bottom,BorderLayout.SOUTH);
			
			//Main
			JLabel title = new JLabel("Open Presentation Engine");
			title.setFont(new Font(title.getFont().getName(), Font.TRUETYPE_FONT, 30));
			JLabel v = new JLabel("   v.1.0");
			v.setFont(new Font(title.getFont().getName(), Font.TRUETYPE_FONT, 20));
			
			JLabel author = new JLabel("     Copyright (C) 2019-2021 by rafi612");
			author.setFont(new Font(title.getFont().getName(), Font.TRUETYPE_FONT, 16));
			
			JLabel text1 = new JLabel("Open Presentation Engine is free and open source software");
			JLabel text2 = new JLabel("to making presentations and slide shows using Python. OPE can be");
			JLabel text3 = new JLabel("distributed with Python. OPE is distributed under LGPL license.");
			text1.setFont(new Font(title.getFont().getName(), Font.TRUETYPE_FONT, 13));
			text2.setFont(new Font(title.getFont().getName(), Font.TRUETYPE_FONT, 13));
			text3.setFont(new Font(title.getFont().getName(), Font.TRUETYPE_FONT, 13));
			
			JPanel main = new JPanel();
			main.setLayout(new BoxLayout(main,1));
			main.add(title);
			main.add(v);
			main.add(author);
			main.add(new JLabel("            "));
			main.add(text1);
			main.add(text2);
			main.add(text3);
			main.add(new JLabel("            "));
			//main.add(new JScrollPane(getLicense()));
			main.add(new JLabel("            "));
			
			dialog.add(main);
			dialog.setVisible(true);
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
	
	private JTextArea getLicense()
	{
		JTextArea text = new JTextArea();
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
		text.setEditable(false);
		text.setSelectionStart(0);
		text.setSelectionEnd(0);
		return text;
	}

}
