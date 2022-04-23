/* Copyright 2019-2020 by rafi612 */
package com.main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;

import org.lwjgl.system.Platform;

import com.audio.Sound;
import com.gui.AboutDialog;
import com.gui.SlideCreator;
import com.gui.SlideRack;
import com.gui.Tree;
import com.input.Action;
import com.input.ColoredKeywords;
import com.io.Config;
import com.io.Util;
import com.presentation.main.Presentation;
import com.project.Project;

public class Main
{
	public static String[] args;
	public static JFrame frame,scripteditor;
    public static JMenuBar menubar;
    public static JMenu file,edit,tools,run,settings,help;
    
    public static final String TITLE = "Open Presentation Engine";
    
    //file
    public static JMenuItem newproject,loadproject,save,export,exitproject,exit;
    
    //edit
    public static JMenuItem cut,paste,copy,selectAll;
    
    //tools
    public static JMenuItem refresh;
    
    //run
    public static JMenuItem runandbuild,run_;
    
    //settings
    public static JMenu theme;

    public static JRadioButtonMenuItem m_system,m_metal,m_nimbus;
    
    //help
    public static JMenuItem about,license;
    
    //tree popup
    
    public static Tree tree;
    public static DefaultMutableTreeNode workspace;
    
    public static JScrollPane scrollpane;
	public static JScrollPane scrollpane3;
    public static JTextPane textpane,textarea2;
    
    public static ArrayList<JButton> actions = new ArrayList<JButton>();
    public static ArrayList<JButton> autoscripts = new ArrayList<JButton>();
    
    public static JTabbedPane tabs;
    
    public static SlideCreator slidecreator;
    public static SlideRack sliderack;
    
    public static AboutDialog aboutdialog;

    public static void initUI()
    {
    	tabs = new JTabbedPane();
    	
    	aboutdialog = new AboutDialog();
    	
        JPopupMenu textareapopup = new JPopupMenu();
//        textareapopup.add(copy);
//        textareapopup.add(paste);
//        textareapopup.add(cut);
//        textareapopup.add(selectAll);
        
    	//text=============================
    	JPanel textpanel = new JPanel();
    	textpanel.setLayout(new BorderLayout());
        
    	ColoredKeywords keywords = new ColoredKeywords();
        textpane = new JTextPane(keywords);
        textpane.setFont(new Font(textpane.getFont().getName(), Font.TRUETYPE_FONT, 16));
        textpane.setEnabled(false);
        textpane.setComponentPopupMenu(textareapopup);
           
        textarea2 = new JTextPane();
        textarea2.setFont(new Font(textarea2.getFont().getName(), Font.TRUETYPE_FONT, 16));
        textarea2.setEditable(false);
        
        textpane.setEnabled(false);
        scrollpane = new JScrollPane(textpane);
        
        textpanel.add(scrollpane);

        //autoscripts==================================
        JPanel autoscript = new JPanel();
        autoscript.setLayout(new BoxLayout(autoscript, 1));
        autoscript.setBorder(BorderFactory.createTitledBorder("Auto Scripting"));
        autoscript.setToolTipText("Auto-Complete Python methods by clicking button");
        
        autoscripts.add(new JButton("Import Libs"));
        autoscripts.add(new JButton("Create Slide"));
        autoscripts.add(new JButton("Add Slide Background"));
        autoscripts.add(new JButton("Add Fullscreen"));
        autoscripts.add(new JButton("Add General Music"));
        autoscripts.add(new JButton("Add TTS"));
        autoscripts.add(new JButton("Set TTS Start Key"));
        autoscripts.add(new JButton("Add Slide Start Animation"));
        autoscripts.add(new JButton("Add Slide Exit Animation"));
        autoscripts.add(new JButton("End Script"));
        autoscripts.add(new JButton("More Auto Scripts"));
        
        for (int i = 0;i < autoscripts.size(); i++)
        	autoscripts.get(i).addActionListener(new Action());
        
        for (int i = 0;i < autoscripts.size(); i++)
        {
        	autoscripts.get(i).setBorderPainted(false);
        	autoscripts.get(i).setFocusPainted(false);
        }
        
        for (int i = 0;i < autoscripts.size(); i++)
        	autoscript.add(autoscripts.get(i));
        
        textpanel.add(autoscript,BorderLayout.EAST);
        
        //slide creator
        slidecreator = new SlideCreator();
        
        //slide rack
        sliderack = new SlideRack();

        //tabs====================================
        tabs.add("Slides",sliderack);
        tabs.add("Slide Creator",slidecreator);
        tabs.add("Main.py",textpanel);
        
        frame.add(tabs);
    	 
        //tree=======================================
        workspace = new DefaultMutableTreeNode("Workspace"); 
        tree = new Tree(workspace);
        frame.add(new JScrollPane(tree),BorderLayout.WEST);
        
        JPanel buttons = new JPanel();
        
        buttons.setBorder(BorderFactory.createTitledBorder("Actions"));
        buttons.setToolTipText("Necessary actions");
        
        actions.add(new JButton("Build & Run"));
        actions.add(new JButton("Save"));
        actions.add(new JButton("Stop"));
        
        for (int i = 0;i < actions.size(); i++)
        	actions.get(i).addActionListener(new Action());
        
        for (int i = 0;i < actions.size(); i++)
        	buttons.add(actions.get(i));
        
        frame.add(buttons,BorderLayout.SOUTH);
        
        //Project.CreateNewProject("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop", JOptionPane.showInputDialog(frame,"Project Name:","Project",JOptionPane.QUESTION_MESSAGE));
        Project.unloadProject();
    }

	public static void main(String[] args)
	{
		Main.args = args;
		
		Sound.init();
		
		if (args.length < 1)
		{
			frame = new JFrame(TITLE);
			frame.setSize(1280,720);
			frame.setIconImage(Util.loadIcon("/images/icon.png"));
			frame.setLocationRelativeTo(null);
			frame.setLayout(new BorderLayout());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			menubar = new JMenuBar(); 
			menu();
		    frame.setJMenuBar(menubar);
			
			initUI();
			
			Config.loadSettings();
			
			frame.setVisible(true);
			
	        slidecreator.canvasLoop();

		}
		else 
			Presentation.start();
		
		for (int i = 0;i < args.length;i++)
			System.out.println(args[i]);
	}
	
	public static void menu()
	{
        //menu
        file = new JMenu("File");
        edit = new JMenu("Edit");
        tools = new JMenu("Tools");
        settings = new JMenu("Settings");
        run = new JMenu("Run");
        help = new JMenu("Help");
        
        //file
        newproject = new JMenuItem("New Project");
        newproject.addActionListener(new Action());
        newproject.setIcon(new ImageIcon(Util.loadIcon("/icons/new.png")));
        newproject.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        file.add(newproject);
        
        loadproject = new JMenuItem("Load Project");
        loadproject.addActionListener(new Action());
        loadproject.setIcon(new ImageIcon(Util.loadIcon("/icons/open.png")));
        loadproject.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        file.add(loadproject);
        
        file.add(new JSeparator());
        
        save = new JMenuItem("Save");
        save.addActionListener(new Action());
        save.setEnabled(false);
        save.setIcon(new ImageIcon(Util.loadIcon("/icons/save.png")));
        save.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        file.add(save);
        
        export = new JMenuItem("Export");
        export.setEnabled(false);
        export.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
        file.add(export);
        
        exitproject = new JMenuItem("Exit Project");
        exitproject.addActionListener(new Action());
        exitproject.setEnabled(false);
        exitproject.setIcon(new ImageIcon(Util.loadIcon("/icons/exit.png")));
        file.add(exitproject);
        
        file.add(new JSeparator());
        
        exit = new JMenuItem("Exit");
        exit.addActionListener(new Action());
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
        
        
        //narzędzia
        refresh = new JMenuItem("Refresh Project"); 
        refresh.addActionListener(new Action());
        refresh.setEnabled(false);
        refresh.setAccelerator(KeyStroke.getKeyStroke("F5"));
        tools.add(refresh);
        
        runandbuild = new JMenuItem("Build And Run"); 
        runandbuild.setIcon(new ImageIcon(Util.loadIcon("/icons/runandbuild.png")));
        runandbuild.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
        runandbuild.addActionListener(new Action());
        //shell.addActionListener(new Action());
        
        run_ = new JMenuItem("Run"); 
        run_.setIcon(new ImageIcon(Util.loadIcon("/icons/run.png")));
        run_.setAccelerator(KeyStroke.getKeyStroke("shift R"));
        //refresh.addActionListener(new Action());
        run.add(runandbuild);
        run.add(run_);
        
        //settings
        theme = new JMenu("Theme");
        
        ButtonGroup group = new ButtonGroup();
        
        String type = Platform.get() == Platform.WINDOWS ? "Win32" : 
        	(Platform.get() == Platform.LINUX ? "GTK+" : "Aqua");
        
        m_system = new JRadioButtonMenuItem("System (" + type + ")");
        m_system.addActionListener(new Action());
        group.add(m_system);
        theme.add(m_system);
        
        theme.add(new JSeparator());
        
        m_metal = new JRadioButtonMenuItem("Metal");
        m_metal.addActionListener(new Action());
        group.add(m_metal);
        theme.add(m_metal);
        
        m_nimbus = new JRadioButtonMenuItem("Nimbus");
        m_nimbus.addActionListener(new Action());
        group.add(m_nimbus);
        theme.add(m_nimbus);
    
        settings.add(theme);
        
        //help 
        about = new JMenuItem("About");
        about.addActionListener(new Action());
        about.setIcon(new ImageIcon(Util.loadIcon("/icons/about.png")));
        help.add(about);
        license = new JMenuItem("View License");
        license.addActionListener(new Action());
        license.setIcon(new ImageIcon(Util.loadIcon("/icons/license.png")));
        help.add(license);
        
        menubar.add(file);
        menubar.add(edit);
        menubar.add(tools);
        menubar.add(run);
        menubar.add(settings);
        menubar.add(help);
	}

}