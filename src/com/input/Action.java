/* Copyright 2019-2020 by rafi612 */
package com.input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.main.Main;
import com.tts.Speak;

//TODO: Remove
public class Action implements ActionListener 
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		
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
