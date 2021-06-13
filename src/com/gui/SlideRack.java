package com.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class SlideRack extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	public ArrayList<JButton> actionbuttons;
	
	public JCheckBox selected;
	
	public ArrayList<?> elements;

	public SlideRack() 
	{
		setLayout(new BorderLayout());
		
        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, 1));
        actions.setBorder(BorderFactory.createTitledBorder("Actions"));
        actions.setToolTipText("Actions in Slide Rack");
        actions.setPreferredSize(new Dimension(200, 0));
        
		actionbuttons = new ArrayList<JButton>();
		actionbuttons.add(new JButton("Add Slide"));
		actionbuttons.add(new JButton("Delete Slide"));
		actionbuttons.add(new JButton("Move Up"));
		actionbuttons.add(new JButton("Move Down"));
		//actionbuttons.add(new JButton("Select All Slides"));
		
		JPanel toppanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		toppanel.setBorder(BorderFactory.createTitledBorder(""));
		
		selected = new JCheckBox("Select all");
		toppanel.add(selected);
		
		add(toppanel,BorderLayout.NORTH);
		
	    for (int i = 0;i < actionbuttons.size(); i++)
	    	actions.add(actionbuttons.get(i));
	        
	    add(actions,BorderLayout.EAST);
	}

}
