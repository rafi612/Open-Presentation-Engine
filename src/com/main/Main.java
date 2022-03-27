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
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;

import org.lwjgl.system.Platform;

import com.audio.Sound;
import com.gui.SlideCreator;
import com.gui.SlideRack;
import com.gui.Tree;
import com.input.Action;
import com.input.ColoredKeywords;
import com.input.Window;
import com.io.Config;
import com.presentation.main.Presentation;
import com.project.Project;

public class Main
{
	public static String[] args;
	public static JFrame frame,scripteditor;
    public static JMenuBar menubar;
    public static JMenu file,edit,tools,run,settings,help;
    
    public static String interpreterpath = "",interpretertype = "";
    
    public static final String TITLE = "Open Presentation Engine";
    
    //file
    public static JMenuItem newproject,loadproject,save,export,exitproject,exit;
    
    //edit
    public static JMenuItem cut,paste,copy,selectAll;
    
    //tools
    public static JMenuItem refresh,shell;
    
    //run
    public static JMenuItem runandbuild,run_;
    
    //settings
    public static JMenu python;
    
    public static JRadioButtonMenuItem winpy,winsystem,linux,macos;
    public static JRadioButtonMenuItem custom;
    
    //help
    public static JMenuItem about,license;
    
    //tree popup
    
    public static Tree tree;
    public static DefaultMutableTreeNode workspace;
    
    public static JScrollPane scrollpane;
	public static JScrollPane scrollpane2,scrollpane3;
    public static JTextPane textpane,textarea2;
    public static JTextArea areaconsole;
    
    public static ArrayList<JButton> actions = new ArrayList<JButton>();
    public static ArrayList<JButton> autoscripts = new ArrayList<JButton>();
    
    public static JTabbedPane tabs;
    
    public static SlideCreator slidecreator;
    public static SlideRack sliderack;

    public static void initUI()
    {
    	tabs = new JTabbedPane();
    	
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
        
        areaconsole = new JTextArea("No python output");
        areaconsole.setFont(new Font(areaconsole.getFont().getName(), Font.TRUETYPE_FONT, 16));
        areaconsole.setEditable(false);
        
        textpane.setEnabled(false);
        scrollpane = new JScrollPane(textpane);
        scrollpane2 = new JScrollPane(textarea2);
        
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
        tabs.add("Main.py",textpanel);
        tabs.add("Slides",sliderack);
        tabs.add("Config.xml",scrollpane2);
        //tabs.add("Template Chooser",tchooser);
        tabs.add("Console",new JScrollPane(areaconsole));
        tabs.add("Slide Creator",slidecreator);
        
        frame.add(tabs);
    	 
        //tree=======================================
        workspace = new DefaultMutableTreeNode("Workspace"); 
        tree = new Tree(workspace);
        
        scrollpane2 = new JScrollPane(tree);
        frame.add(scrollpane2,BorderLayout.WEST);
        
        JPanel buttons = new JPanel();
        
        buttons.setBorder(BorderFactory.createTitledBorder("Actions"));
        buttons.setToolTipText("Necessary actions");
        
        actions.add(new JButton("Build & Run"));
        actions.add(new JButton("Save"));
        actions.add(new JButton("Stop"));
//        actions.add(new JButton("Open XML editor"));
//        actions.add(new JButton("Open Python editor"));
        
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
		
		Presentation.init();
		
		if (args.length < 1)
		{
			frame = new JFrame(TITLE);
			frame.setSize(1280,720);
			frame.setIconImage(loadIcon("/images/icon.png"));
			frame.setLocationRelativeTo(null);
			frame.setLayout(new BorderLayout());
			frame.addWindowListener(new Window());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			menubar = new JMenuBar(); 
			menu();
		    frame.setJMenuBar(menubar);
			
			initUI();
			
			Config.loadinterpreterpath();
			
			theme();
			
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
        
        //plik
        newproject = new JMenuItem("New Project");
        newproject.addActionListener(new Action());
        newproject.setIcon(new ImageIcon(loadIcon("/icons/new.png")));
        newproject.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        file.add(newproject);
        
        loadproject = new JMenuItem("Load Project");
        loadproject.addActionListener(new Action());
        loadproject.setIcon(new ImageIcon(loadIcon("/icons/open.png")));
        loadproject.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        file.add(loadproject);
        
        file.add(new JSeparator());
        
        save = new JMenuItem("Save");
        save.addActionListener(new Action());
        save.setEnabled(false);
        save.setIcon(new ImageIcon(loadIcon("/icons/save.png")));
        save.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        file.add(save);
        
        export = new JMenuItem("Export");
        export.setEnabled(false);
        export.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
        file.add(export);
        
        exitproject = new JMenuItem("Exit Project");
        exitproject.addActionListener(new Action());
        exitproject.setEnabled(false);
        exitproject.setIcon(new ImageIcon(loadIcon("/icons/exit.png")));
        file.add(exitproject);
        
        file.add(new JSeparator());
        
        exit = new JMenuItem("Exit");
        exit.addActionListener(new Action());
        exit.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
        file.add(exit);
        
        //edytuj
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
        shell = new JMenuItem("OPE Python Shell"); 
        shell.addActionListener(new Action());
        tools.add(shell);
        refresh = new JMenuItem("Refresh Project"); 
        refresh.addActionListener(new Action());
        refresh.setEnabled(false);
        refresh.setAccelerator(KeyStroke.getKeyStroke("F5"));
        tools.add(refresh);
        
        runandbuild = new JMenuItem("Build And Run"); 
        runandbuild.setIcon(new ImageIcon(loadIcon("/icons/runandbuild.png")));
        runandbuild.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
        runandbuild.addActionListener(new Action());
        //shell.addActionListener(new Action());
        tools.add(shell);
        run_ = new JMenuItem("Run"); 
        run_.setIcon(new ImageIcon(loadIcon("/icons/run.png")));
        run_.setAccelerator(KeyStroke.getKeyStroke("shift R"));
        //refresh.addActionListener(new Action());
        run.add(runandbuild);
        run.add(run_);
        
        //settings
        python = new JMenu("Set Python interpreter");
        python.addActionListener(new Action()); 
        
        ButtonGroup group = new ButtonGroup();
        
        winpy = new JRadioButtonMenuItem("Windows - OPE build in (Python\\python.exe)");
        winpy.setEnabled(false);
        winpy.addActionListener(new Action());
        group.add(winpy);
        // Python/ folder exist
        if (new File("Python").exists() && Platform.get() == Platform.WINDOWS)
        	winpy.setEnabled(true);
        python.add(winpy);
        
        winsystem = new JRadioButtonMenuItem("Windows - System (python.exe)");
        winsystem.setEnabled(false);
        winsystem.addActionListener(new Action());
        group.add(winsystem);
        if (Platform.get() == Platform.WINDOWS)
        	winsystem.setEnabled(true);
        python.add(winsystem);
        
        linux = new JRadioButtonMenuItem("Linux (python3)");
        linux.setEnabled(false);
        linux.addActionListener(new Action());
        group.add(linux);
        if (Platform.get() == Platform.LINUX)
        	linux.setEnabled(true);
        python.add(linux);
        
        macos = new JRadioButtonMenuItem("MacOS (python)");
        macos.setEnabled(false);
        macos.addActionListener(new Action());
        group.add(macos);
        if (Platform.get() == Platform.MACOSX)
        	macos.setEnabled(true);
        python.add(macos);
        
        python.add(new JSeparator());
        
        custom = new JRadioButtonMenuItem("Custom");
        custom.addActionListener(new Action());
        group.add(custom);
        python.add(custom);
        
        settings.add(python);
        
        //pomoc 
        about = new JMenuItem("About");
        about.addActionListener(new Action());
        about.setIcon(new ImageIcon(loadIcon("/icons/about.png")));
        help.add(about);
        license = new JMenuItem("View License");
        license.addActionListener(new Action());
        license.setIcon(new ImageIcon(loadIcon("/icons/license.png")));
        help.add(license);
        
        //dodawanie
        menubar.add(file);
        menubar.add(edit);
        menubar.add(tools);
        menubar.add(run);
        menubar.add(settings);
        menubar.add(help);
	}
	

	
	public static void theme()
	{
	    try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        //UIManager.getDefaults().put("TextArea.font", UIManager.getFont("TextField.font"));
	        
	    } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	    } catch (InstantiationException e) {
	            e.printStackTrace();
	    } catch (IllegalAccessException e) {
	            e.printStackTrace();
	    } catch (UnsupportedLookAndFeelException e) {
	            e.printStackTrace();
	    }
	
	    SwingUtilities.updateComponentTreeUI(frame);
	}
	
	public static Image loadIcon(String path)
	{
		Image i = null;
		
		try
		{
			i = ImageIO.read(Main.class.getResourceAsStream(path));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return i;
	}

}