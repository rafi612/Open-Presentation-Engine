package com.ope.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import org.lwjgl.system.Platform;

import com.ope.io.Util;
import com.ope.project.Project;

public class MenuBar extends JMenuBar implements ActionListener
{
	private static final long serialVersionUID = 1L;

	public JFrame parentframe;
	
	public JMenu file,tools,run,project,settings,help;
	
	//file
	public JMenuItem newproject,loadproject,save,export,exitproject,exit;
	
	//tools
	public JMenuItem refresh;
	
	//project
	public JCheckBoxMenuItem fullscreen;
	
	//run
	public JMenuItem runproject;
	
	//settings
	public JMenu theme;
	public JRadioButtonMenuItem m_system,m_metal,m_nimbus,m_flatlaf_light,m_flatlaf_dark;
	
	//help
	public JMenuItem about,license;
	
	public AboutDialog aboutdialog;
	
	public MenuBar(JFrame parentframe)
	{
		this.parentframe = parentframe;
		
		aboutdialog = new AboutDialog();
		
		//menu
		file = new JMenu("File");
		tools = new JMenu("Tools");
		project = new JMenu("Project");
		run = new JMenu("Run");
		settings = new JMenu("Settings");
		help = new JMenu("Help");
		
		//file
		newproject = new JMenuItem("New Project");
		newproject.addActionListener(this);
		newproject.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/new.png")));
		newproject.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
		file.add(newproject);
		
		loadproject = new JMenuItem("Load Project");
		loadproject.addActionListener(this);
		loadproject.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/open.png")));
		loadproject.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		file.add(loadproject);
		
		file.add(new JSeparator());
		
		save = new JMenuItem("Save");
		save.addActionListener(this);
		save.setEnabled(false);
		save.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/save.png")));
		save.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		file.add(save);
		
		export = new JMenuItem("Export");
		export.setEnabled(false);
		export.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
		file.add(export);
		
		exitproject = new JMenuItem("Exit Project");
		exitproject.addActionListener(this);
		exitproject.setEnabled(false);
		exitproject.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/exit.png")));
		file.add(exitproject);
		
		file.add(new JSeparator());
		
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		exit.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
		file.add(exit);
		
		//tools
		refresh = new JMenuItem("Refresh Project"); 
		refresh.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/refresh.png")));
		refresh.addActionListener(this);
		refresh.setEnabled(false);
		refresh.setAccelerator(KeyStroke.getKeyStroke("F5"));
		tools.add(refresh);
		
		runproject = new JMenuItem("Run"); 
		runproject.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/run.png")));
		runproject.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
		runproject.addActionListener(this);
		run.add(runproject);
		
		//project
		fullscreen = new JCheckBoxMenuItem("Fullscreen");
		project.add(fullscreen);
		
		//settings
		theme = new JMenu("Theme");
		theme.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/theme.png")));
		
		ButtonGroup group = new ButtonGroup();
		
		String type = Platform.get() == Platform.WINDOWS ? "Win32" : 
			(Platform.get() == Platform.LINUX ? "GTK+" : "Aqua");
		
		m_system = new JRadioButtonMenuItem("System (" + type + ")");
		group.add(m_system);
		theme.add(m_system);
		
		theme.add(new JSeparator());
		
		m_metal = new JRadioButtonMenuItem("Metal");
		group.add(m_metal);
		theme.add(m_metal);
		
		m_nimbus = new JRadioButtonMenuItem("Nimbus");
		group.add(m_nimbus);
		theme.add(m_nimbus);
		
		theme.add(new JSeparator());
		
		m_flatlaf_light = new JRadioButtonMenuItem("FlatLaf Light");
		group.add(m_flatlaf_light);
		theme.add(m_flatlaf_light);
		
		m_flatlaf_dark = new JRadioButtonMenuItem("FlatLaf Dark");
		group.add(m_flatlaf_dark);
		theme.add(m_flatlaf_dark);
	
		settings.add(theme);
		
		//help 
		about = new JMenuItem("About");
		about.addActionListener(this);
		about.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/about.png")));
		help.add(about);
		license = new JMenuItem("View License");
		license.addActionListener(this);
		license.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/license.png")));
		help.add(license);
		
		add(file);
		add(tools);
		add(project);
		add(run);
		add(settings);
		add(help);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		var source = event.getSource();
		
		//new project
		if (source == newproject)
		{
			int yesno = 0;
			if (Project.projectIsLoaded) 
				yesno = Project.lostSaveDialog();
			
			if (yesno != 2) 
				Project.newProjectDialog();
		}
		if (source == loadproject)
		{
			int yesno = 0;
			if (Project.projectIsLoaded) 
				yesno = Project.lostSaveDialog();
			
			if (yesno != 2) 
				Project.loadDialog();
		}
		
		//save
		if (source == save)
			Project.saveDialog();
		
		//exit project
		if (source == exitproject)
		{
			int yesno = 0;
			if (Project.projectIsLoaded) 
				yesno = Project.lostSaveDialog();
			
			if (yesno != 2)
				Project.unloadProject();
		}
		
		//exit 
		if (source == exit)
			System.exit(0);
		
		//refresh
		if (source == refresh)
			Project.refreshProject();
		
		//license
		if (source == license)
		{
			JDialog dialog = new JDialog(parentframe,"OPE License");
			dialog.setSize(600,600);
			dialog.setLayout(new BorderLayout());
			dialog.setLocationRelativeTo(parentframe);
			
			JTextArea text = new JTextArea();
			text.setText(Util.getLicense());
			text.setEditable(false);
			text.setSelectionStart(0);
			text.setSelectionEnd(0);
			
			text.setFont(new Font(text.getFont().getName(), Font.TRUETYPE_FONT, 12));
			
			JScrollPane s = new JScrollPane(text);
			dialog.add(s,BorderLayout.CENTER);
			dialog.setVisible(true);
		}
		
		if (source == runproject)
			Project.run();
		
		if (source == about)
			aboutdialog.setVisible(true);
	}
}