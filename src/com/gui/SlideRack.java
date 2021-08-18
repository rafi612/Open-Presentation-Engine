package com.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import com.gui.sliderack.RackElement;

public class SlideRack extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	public ArrayList<JButton> actionbuttons;
	
	public JCheckBox selected;
	public JLabel slidecount;
	public JPanel rackpanel;
	
	public ArrayList<RackElement> elements;

	public SlideRack() 
	{
		setLayout(new BorderLayout());
		
        JPanel actions = new JPanel();
        actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
        actions.setBorder(BorderFactory.createTitledBorder("Actions"));
        actions.setToolTipText("Actions in Slide Rack");
        actions.setPreferredSize(new Dimension(200, 0));
        
		actionbuttons = new ArrayList<JButton>();
		actionbuttons.add(new JButton("Add Slide"));
		actionbuttons.add(new JButton("Copy Slide"));
		actionbuttons.add(new JButton("Delete Slide"));
		actionbuttons.add(new JButton("Move Up"));
		actionbuttons.add(new JButton("Move Down"));
		actionbuttons.add(new JButton("Change Color"));
		//actionbuttons.add(new JButton("Select All Slides"));
		
	    for (int i = 0;i < actionbuttons.size(); i++)
	    {
	    	actionbuttons.get(i).setMaximumSize(new Dimension(200,40));
	    	actions.add(actionbuttons.get(i));
	    }
	        
	    add(actions,BorderLayout.EAST);
		
		elements = new ArrayList<RackElement>();
//		
		JPanel toppanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		toppanel.setBorder(BorderFactory.createTitledBorder(""));
		
		selected = new JCheckBox("Select all");
		toppanel.add(selected);
		
		slidecount = new JLabel("Slides: " + elements.size());
		toppanel.add(slidecount);
		add(toppanel,BorderLayout.NORTH);
		
		rackpanel = new JPanel();
		rackpanel.setLayout(new BoxLayout(rackpanel, BoxLayout.Y_AXIS));
		
		for (int i =0;i < 10;i++)
		{
			RackElement r = new RackElement("test " + i);
			r.setEnabled(false);
			//r.setColor(new Color(new Random().nextInt()));
			rackpanel.add(r);
		}
		
		JScrollPane rackscroll = new JScrollPane(rackpanel);
		rackscroll.getVerticalScrollBar().setUnitIncrement(20);
		add(rackscroll,BorderLayout.CENTER);
	}

}
