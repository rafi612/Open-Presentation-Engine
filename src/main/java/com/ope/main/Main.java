/* Copyright 2019-2020 by rafi612 */
package com.ope.main;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;

import com.ope.gui.ActionPanel;
import com.ope.gui.MenuBar;
import com.ope.gui.Tree;
import com.ope.gui.slide.SlideList;
import com.ope.io.Config;
import com.ope.io.Util;
import com.ope.project.Project;
import com.ope.viewer.Presentation;

public class Main
{
	public static JFrame frame;
	
	public static final String TITLE = "Open Presentation Engine";

	public static Tree tree;

	public static JTabbedPane tabs2;

	public static MenuBar menubar;

	public static ActionPanel actionpanel;
	
	public static SlideList slideList;

	public static void initUI()
	{
		tabs2 = new JTabbedPane();
		
		menubar = new MenuBar(frame); 
		 
		//tree
		tree = new Tree(new DefaultMutableTreeNode("Workspace"));
		slideList = new SlideList(menubar);
		
		tabs2.add(slideList,"Slides");
		tabs2.add(tree,"Project Explorer");
		frame.add(tabs2,BorderLayout.WEST);
		
		frame.add(slideList.getSlideCreator());
		
		actionpanel = new ActionPanel();
		frame.add(actionpanel,BorderLayout.SOUTH);
		
		frame.setJMenuBar(menubar);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event)
			{
				if (Project.projectIsLoaded)
				{
					int choose = Project.lostSaveDialog();
				
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
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("jdk.gtk.version", "2");
		
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
			
			slideList.getSlideCreator().canvasLoop();
		}
		else 
			Presentation.start(args[0]);
	}
}