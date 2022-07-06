/* Copyright 2019-2020 by rafi612 */
package com.ope.main;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
	public static JFrame frame;
	
	public static final String TITLE = "Open Presentation Engine";

	public static Tree tree;

	public static JTabbedPane tabs;

	public static MenuBar menubar;

	public static SlideCreator slidecreator;
	public static SlideRack sliderack;

	public static ActionPanel actionpanel;

	public static void initUI()
	{
		tabs = new JTabbedPane();
		
		menubar = new MenuBar(frame); 

		//slide creator
		slidecreator = new SlideCreator();
		
		//slide rack
		sliderack = new SlideRack(menubar);

		//tabs
		tabs.add("Slides",sliderack);
		tabs.add("Slide Creator",slidecreator);
		
		frame.add(tabs);
		 
		//tree
		DefaultMutableTreeNode treerootnode = new DefaultMutableTreeNode("Workspace"); 
		tree = new Tree(treerootnode);
		frame.add(new JScrollPane(tree),BorderLayout.WEST);
		
		actionpanel = new ActionPanel();
		frame.add(actionpanel,BorderLayout.SOUTH);
		
		frame.setJMenuBar(menubar);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event)
			{
				if (Project.projectIsLoaded)
				{
					int choose = Project.lost_save_dialog();
				
					if (choose != 2)
					{
						if (choose == 0)
							Project.save();
					}
					else return;
				}
				System.exit(0);
			}
		});
		
		Project.unloadProject();
	}

	public static void main(String[] args)
	{
		if (args.length < 1)
		{
			frame = new JFrame(TITLE);
			frame.setSize(1280,720);
			frame.setIconImage(Util.loadIcon("/images/icon.png"));
			frame.setLocationRelativeTo(null);
			frame.setLayout(new BorderLayout());
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			
			initUI();
			
			Config.loadSettings();
			
			frame.setVisible(true);
			
			slidecreator.canvasLoop();

		}
		else 
			Presentation.start(args[0]);
	}
}