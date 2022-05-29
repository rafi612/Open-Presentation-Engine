/* Copyright 2019-2020 by rafi612 */
package com.tts;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.io.Util;
import com.main.Main;
import com.project.Project;

public class Speak
{
	//"https://translate.google.com/translate_tts?ie=UTF-8&tl=pl-PL&client=tw-ob&q=hello+world"
	
	//TODO: Optimalize code
	public static void TTSFrame()
	{
		JDialog f = new JDialog(Main.frame,"Text-to-Speech Creator");
		f.setSize(640, 480);
		f.setLocationRelativeTo(Main.frame);
		f.setIconImage(Util.loadIcon("/images/icon.png"));
		f.setLayout(new BorderLayout());
		f.setResizable(false);
		
		JTextArea area = new JTextArea();
		area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Arial", Font.TRUETYPE_FONT, 12));
		
		JButton ok = new JButton("Ok");
		JPanel pok = new JPanel();
		pok.add(ok);
		
		JComboBox<Language> lang = new JComboBox<Language>(Language.values());
		JPanel plang = new JPanel();
		plang.add(new JLabel("Language:"));
		plang.add(lang);
		
		f.add(new JScrollPane(area),BorderLayout.CENTER);
		f.add(plang,BorderLayout.NORTH);
		f.add(pok,BorderLayout.SOUTH);
		
		f.setVisible(true);
		
		ok.addActionListener(new ActionListener() 
		{
            public void actionPerformed(ActionEvent evt)
            {
            	f.dispose();
            	String name = JOptionPane.showInputDialog(Main.frame,"Enter file name: .mp3","TTS Creator",JOptionPane.QUESTION_MESSAGE);
            	String text = area.getText().replace("\n", " ");
   
            	if (text.length() <= 200)
            	{
					try 
					{
						download((Language) lang.getSelectedItem(),text,Util.projectPath(name + ".mp3"));
						
					} catch (UnknownHostException e) 
					{
						JOptionPane.showMessageDialog(Main.frame, "No internet connection, please connect to internet to use TTS","TTS Error",JOptionPane.ERROR_MESSAGE);
						
					} catch (MalformedURLException e) 
					{
						e.printStackTrace();
					} catch (IOException e) 
					{
						e.printStackTrace();
					}
					//Util.insert("slide.setSlideTTS(\"" + name + ".mp3\")\n", Main.textpane.getCaretPosition(),Main.textpane);
	            	Project.refreshProject();
            	}
            	else
            		JOptionPane.showMessageDialog(Main.frame,"Text is too long (" + text.length() + " characters). Text must have 200 characters or less!","TTS Creator",JOptionPane.ERROR_MESSAGE);
            }
        });
	}
	
	
	//preview
	public static void download(Language lang,String text,String savepath) throws MalformedURLException, IOException,UnknownHostException
	{
		URLConnection openConnection;
		openConnection = new URL(createURL(lang, text)).openConnection();
		openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
		InputStream in = openConnection.getInputStream();

		try
		{
			FileOutputStream fileOutputStream = new FileOutputStream(savepath);
			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) 
			{
				fileOutputStream.write(dataBuffer, 0, bytesRead);
			}
			fileOutputStream.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	//preview
	public static String createURL(Language lang,String text)
	{
		return "https://translate.google.com/translate_tts?ie=UTF-8&tl=" + lang.getLang() + "&client=tw-ob&q=" + text.replace(' ','+');
	}

}
