/* Copyright 2019-2020 by rafi612 */
package com.ope.main;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;

import com.ope.gui.ActionPanel;
import com.ope.gui.MenuBar;
import com.ope.gui.SlideCreator;
import com.ope.gui.SlideRack;
import com.ope.gui.Tree;
import com.ope.io.Config;
import com.ope.io.Util;
import com.ope.presentation.main.Presentation;
import com.ope.project.Project;

public class Main
{
	public static String[] args;
	public static JFrame frame;
	
    public static final String TITLE = "Open Presentation Engine";
    
    public static Tree tree;
    public static DefaultMutableTreeNode workspace;
    
    public static JTabbedPane tabs;
    
    public static MenuBar menubar;
    
    public static SlideCreator slidecreator;
    public static SlideRack sliderack;
    
    public static ActionPanel actionpanel;

    public static void initUI()
    {
    	tabs = new JTabbedPane();
        
        //slide creator
        slidecreator = new SlideCreator();
        
        //slide rack
        sliderack = new SlideRack();

        //tabs
        tabs.add("Slides",sliderack);
        tabs.add("Slide Creator",slidecreator);
        
        frame.add(tabs);
    	 
        //tree
        workspace = new DefaultMutableTreeNode("Workspace"); 
        tree = new Tree(workspace);
        frame.add(new JScrollPane(tree),BorderLayout.WEST);
        
        actionpanel = new ActionPanel();
        frame.add(actionpanel,BorderLayout.SOUTH);
        
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