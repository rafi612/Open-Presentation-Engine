package com.gui.sliderack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RackElement extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static final int HEIGHT = 150;
	
	public JCheckBox selected;
	public JLabel name;
	public JPanel toppanel;

	public RackElement(String name_) 
	{
		setBorder(BorderFactory.createRaisedBevelBorder());
		setLayout(new BorderLayout());
		
		setMinimumSize(new Dimension(0, HEIGHT));
		setPreferredSize(new Dimension(0,HEIGHT));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT));
		
		toppanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selected = new JCheckBox();
		name = new JLabel(name_);
		
		toppanel.add(selected);
		toppanel.add(name);
		
		add(toppanel,BorderLayout.NORTH);
	}
	
	public void setColor(Color color)
	{
		toppanel.setBackground(color);
		setBackground(color);
	}
	
	public void setEnabled(boolean b)
	{
		name.setEnabled(b);
		selected.setEnabled(b);
		super.setEnabled(b);
	}

}
