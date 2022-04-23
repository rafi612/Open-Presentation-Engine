package com.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.io.Util;
import com.main.Main;

public class AboutDialog extends JDialog
{

	private static final long serialVersionUID = 1L;

	public AboutDialog()
	{
		super(Main.frame,"About");	
		setSize(720,520);
		setLayout(new BorderLayout());
		setLocationRelativeTo(Main.frame);
		setLayout(new BorderLayout());
		
		//logo
		JPanel logo = new JPanel();
		logo.setLayout(new BorderLayout());
		
		JLabel l = new JLabel(new ImageIcon(Util.loadIcon("/images/iconsmall.png")));
		
		logo.add(new JLabel("        "),BorderLayout.WEST);
		logo.add(l,BorderLayout.CENTER);
		logo.add(new JLabel("        "),BorderLayout.EAST);
		//logo.setBorder(BorderFactory.createTitledBorder(""));
		add(logo,BorderLayout.WEST);
		
		//botton panel
		JPanel bottom = new JPanel();
		bottom.add(new JButton("Autor Github Profile"));
		bottom.add(new JButton("Project Web Site"));
		add(bottom,BorderLayout.SOUTH);
		
		//Main
		JLabel title = new JLabel("Open Presentation Engine");
		title.setFont(new Font(title.getFont().getName(), Font.TRUETYPE_FONT, 30));
		JLabel v = new JLabel("   v.1.0");
		v.setFont(new Font(title.getFont().getName(), Font.TRUETYPE_FONT, 20));
		
		JLabel author = new JLabel("     Copyright (C) 2019-2022 by rafi612");
		author.setFont(new Font(title.getFont().getName(), Font.TRUETYPE_FONT, 16));
		
		JLabel text1 = new JLabel("Open Presentation Engine is free and open source software");
		JLabel text2 = new JLabel("to making presentations and slide shows.");
		JLabel text3 = new JLabel("OPE is distributed under LGPL license.");
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
		
		add(main);
	}

}
