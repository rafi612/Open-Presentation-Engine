package com.ope.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
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

import com.ope.io.Config;
import com.ope.io.Util;
import com.ope.project.Project;

public class MenuBar extends JMenuBar implements ActionListener
{
	private static final long serialVersionUID = 1L;

	public JFrame parentframe;
	
	public JMenu file,edit,tools,run,settings,help;
	
	//file
	public JMenuItem newproject,loadproject,save,export,exitproject,exit;
	
	//edit
	public JMenuItem cut,paste,copy,selectAll;
	
	//tools
	public JMenuItem refresh;
	
	//run
	public JMenuItem runandbuild,run_;
	
	//settings
	public JMenu theme;
	public JRadioButtonMenuItem m_system,m_metal,m_nimbus;
	
	//help
	public JMenuItem about,license;
	
	public AboutDialog aboutdialog;
	
	public MenuBar(JFrame parentframe)
	{
		this.parentframe = parentframe;
		
		aboutdialog = new AboutDialog();
		
		//menu
		file = new JMenu("File");
		edit = new JMenu("Edit");
		tools = new JMenu("Tools");
		settings = new JMenu("Settings");
		run = new JMenu("Run");
		help = new JMenu("Help");
		
		//file
		newproject = new JMenuItem("New Project");
		newproject.addActionListener(this::actionPerformed);
		newproject.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/new.png")));
		newproject.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
		file.add(newproject);
		
		loadproject = new JMenuItem("Load Project");
		loadproject.addActionListener(this::actionPerformed);
		loadproject.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/open.png")));
		loadproject.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		file.add(loadproject);
		
		file.add(new JSeparator());
		
		save = new JMenuItem("Save");
		save.addActionListener(this::actionPerformed);
		save.setEnabled(false);
		save.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/save.png")));
		save.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		file.add(save);
		
		export = new JMenuItem("Export");
		export.setEnabled(false);
		export.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
		file.add(export);
		
		exitproject = new JMenuItem("Exit Project");
		exitproject.addActionListener(this::actionPerformed);
		exitproject.setEnabled(false);
		exitproject.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/exit.png")));
		file.add(exitproject);
		
		file.add(new JSeparator());
		
		exit = new JMenuItem("Exit");
		exit.addActionListener(this::actionPerformed);
		exit.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
		file.add(exit);
		
		//edit
		copy= new JMenuItem("Copy");
		copy.setEnabled(false);
		edit.add(copy);
		paste= new JMenuItem("Paste");   
		paste.setEnabled(false);
		edit.add(paste);
		cut= new JMenuItem("Cut");
		cut.setEnabled(false);
		edit.add(cut);
		selectAll= new JMenuItem("Select All"); 
		selectAll.setEnabled(false);
		edit.add(selectAll);
		
		
		//tools
		refresh = new JMenuItem("Refresh Project"); 
		refresh.addActionListener(this::actionPerformed);
		refresh.setEnabled(false);
		refresh.setAccelerator(KeyStroke.getKeyStroke("F5"));
		tools.add(refresh);
		
		runandbuild = new JMenuItem("Build And Run"); 
		runandbuild.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/runandbuild.png")));
		runandbuild.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
		runandbuild.addActionListener(this::actionPerformed);
		
		run_ = new JMenuItem("Run"); 
		run_.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/run.png")));
		run_.setAccelerator(KeyStroke.getKeyStroke("shift R"));;
		run.add(runandbuild);
		run.add(run_);
		
		//settings
		theme = new JMenu("Theme");
		
		ButtonGroup group = new ButtonGroup();
		
		String type = Platform.get() == Platform.WINDOWS ? "Win32" : 
			(Platform.get() == Platform.LINUX ? "GTK+" : "Aqua");
		
		m_system = new JRadioButtonMenuItem("System (" + type + ")");
		m_system.addActionListener(this::actionPerformed);
		group.add(m_system);
		theme.add(m_system);
		
		theme.add(new JSeparator());
		
		m_metal = new JRadioButtonMenuItem("Metal");
		m_metal.addActionListener(this::actionPerformed);
		group.add(m_metal);
		theme.add(m_metal);
		
		m_nimbus = new JRadioButtonMenuItem("Nimbus");
		m_nimbus.addActionListener(this::actionPerformed);
		group.add(m_nimbus);
		theme.add(m_nimbus);
	
		settings.add(theme);
		
		//help 
		about = new JMenuItem("About");
		about.addActionListener(this::actionPerformed);
		about.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/about.png")));
		help.add(about);
		license = new JMenuItem("View License");
		license.addActionListener(this::actionPerformed);
		license.setIcon(new ImageIcon(Util.loadIcon("/icons/menu/license.png")));
		help.add(license);
		
		add(file);
		add(edit);
		add(tools);
		add(run);
		add(settings);
		add(help);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		
		//menu===================================
		//new project
		if (source == newproject)
		{
			int yesno = 0;
			if (Project.projectIsLoaded) 
				yesno = Project.lost_save_dialog();
			
			if (yesno != 2) Project.newproject_dialog();
		}
		if (source == loadproject)
		{
			int yesno = 0;
			if (Project.projectIsLoaded) 
				yesno = Project.lost_save_dialog();
			
			if (yesno != 2) Project.load_dialog();
		}
		
		//save
		if (source == save)
		{
			Project.save_dialog();
		}
		
		//exit project
		if (source == exitproject)
		{
			int yesno = 0;
			if (Project.projectIsLoaded) 
				yesno = Project.lost_save_dialog();
			
			if (yesno != 2) Project.unloadProject();
		}
		
		//exit 
		if (source == exit)
			System.exit(0);
		
		//refresh
		if (source == refresh)
			Project.refreshProject();
		
		//settings
		if (source == m_system)
		{
			Config.lookandfeel = Config.getSystemTheme();
			
			Config.saveXML();
			Config.reloadTheme();
		}
		
		if (source == m_metal)
		{
			Config.lookandfeel = "javax.swing.plaf.metal.MetalLookAndFeel";
			Config.saveXML();
			Config.reloadTheme();
		}
		
		if (source == m_nimbus)
		{
			Config.lookandfeel = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
			Config.saveXML();
			Config.reloadTheme();
		}
		
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
		
		if (source == runandbuild)
		{
			Project.run();
		}
		
		if (source == about)
		{
			aboutdialog.setVisible(true);
		}
	}
}
