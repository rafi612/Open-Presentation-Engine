/* Copyright 2019-2020 by rafi612 */
package com.main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.tree.DefaultMutableTreeNode;

import com.gui.MenuBar;
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
	public static JFrame frame;
	
    public static final String TITLE = "Open Presentation Engine";
    
    public static Tree tree;
    public static DefaultMutableTreeNode workspace;
    
    public static JScrollPane scrollpane;
	public static JScrollPane scrollpane3;
    public static JTextPane textpane,textarea2;
    
    public static ArrayList<JButton> actions = new ArrayList<JButton>();
    public static ArrayList<JButton> autoscripts = new ArrayList<JButton>();
    
    public static JTabbedPane tabs;
    
    public static MenuBar menubar;
    
    public static SlideCreator slidecreator;
    public static SlideRack sliderack;

    public static void initUI()
    {
    	tabs = new JTabbedPane();
    	
        JPopupMenu textareapopup = new JPopupMenu();
        
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
        
		menubar = new MenuBar(frame); 
	    frame.setJMenuBar(menubar);
      
	    
        Project.unloadProject();
    }

	public static void main(String[] args)
	{
		Main.args = args;
		
		if (args.length < 1)
		{
			frame = new JFrame(TITLE);
			frame.setSize(1280,720);
			frame.setIconImage(Util.loadIcon("/images/icon.png"));
			frame.setLocationRelativeTo(null);
			frame.setLayout(new BorderLayout());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			initUI();
			
			Config.loadSettings();
			
			frame.setVisible(true);
			
	        slidecreator.canvasLoop();

		}
		else 
			Presentation.start();
		
		//debug
		for (int i = 0;i < args.length;i++)
			System.out.println(args[i]);
	}
}