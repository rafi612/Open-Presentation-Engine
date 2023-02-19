package com.ope.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;

import com.ope.gui.slide.SlideList;
import com.ope.io.Util;
import com.ope.project.Project;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	public static final String TITLE = "Open Presentation Engine";
	
	public Tree tree;
	public JTabbedPane tabs;
	public MenuBar menubar;
	public ActionPanel actionpanel;
	public SlideList slideList;

	public MainWindow()
	{
		super(TITLE);
		setSize(1280,720);
		setIconImage(Util.loadIcon("/images/icon.png"));
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		tabs = new JTabbedPane();
		menubar = new MenuBar(this); 
		 
		//tree
		tree = new Tree(new DefaultMutableTreeNode("Workspace"));
		slideList = new SlideList(menubar);
		
		tabs.add(new JScrollPane(slideList),"Slides");
		tabs.add(tree,"Project Explorer");
		add(tabs,BorderLayout.WEST);
		
		add(slideList.getSlideCreator());
		
		actionpanel = new ActionPanel();
		add(actionpanel,BorderLayout.SOUTH);
		
		setJMenuBar(menubar);
		
		addWindowListener(new WindowAdapter() {
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
	}
}
