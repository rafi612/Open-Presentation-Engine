package com.gui.sliderack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.gui.SlideRack;

public class RackElement extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static final int HEIGHT = 150;
	
	JCheckBox selected;
	JLabel name;
	JPanel toppanel;

	public RackElement(String name_,SlideRack sliderack) 
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
		
		selected.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				sliderack.selectAllEvent();
			}
		});
		
		add(toppanel,BorderLayout.NORTH);
		
		sliderack.selectAllEvent();
	}
	
	public void setColor(Color color)
	{
		toppanel.setBackground(color);
		setBackground(color);
	}
	
	public Color getColor()
	{
		return getForeground();
	}
	
	public void setEnabled(boolean b)
	{
		name.setEnabled(b);
		selected.setEnabled(b);
		super.setEnabled(b);
	}
	
	public void setSelected(boolean b)
	{
		selected.setSelected(b);
	}
	
	public boolean getSelected()
	{
		return selected.isSelected();
	}
	
	
	public void setRackName(String name)
	{
		this.name.setText(name);
	}
	
	public String getRackName()
	{
		return name.getText();
	}

}
