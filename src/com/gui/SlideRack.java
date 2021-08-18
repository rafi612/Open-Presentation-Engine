package com.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import com.gui.sliderack.RackElement;
import com.main.Main;

public class SlideRack extends JPanel implements ActionListener
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
	    	actionbuttons.get(i).addActionListener(this);
	    	actionbuttons.get(i).setMaximumSize(new Dimension(200,40));
	    	actions.add(actionbuttons.get(i));
	    }
	        
	    add(actions,BorderLayout.EAST);
		
		elements = new ArrayList<RackElement>();
//		
		JPanel toppanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		toppanel.setBorder(BorderFactory.createTitledBorder(""));
		
		selected = new JCheckBox("Select all");
		selected.setEnabled(false);
		selected.addActionListener(this);
		toppanel.add(selected);
		
		slidecount = new JLabel("Slides: " + elements.size());
		toppanel.add(slidecount);
		add(toppanel,BorderLayout.NORTH);
		
		rackpanel = new JPanel();
		rackpanel.setLayout(new BoxLayout(rackpanel, BoxLayout.Y_AXIS));
		
		
		JScrollPane rackscroll = new JScrollPane(rackpanel);
		rackscroll.getVerticalScrollBar().setUnitIncrement(20);
		add(rackscroll,BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		
		if (source == selected)
		{
			for (RackElement r : elements)
			{
				r.setSelected(selected.isSelected());
			}
		}
		
		//add slide
		if (source == actionbuttons.get(0))
		{
			RackElement element = new RackElement("Slide " + elements.size(),this);
			//r.setColor(new Color(new Random().nextInt()));
			elements.add(element);
			rackpanel.add(element);
			
			selected.setEnabled(true);
		}
		
		//delete slide
		if (source == actionbuttons.get(2))
		{
			if (!check()) return;
			for (int i : getSelectedIndexes())
			{
				rackpanel.remove(elements.get(i));
				elements.remove(i);
			}
			
		}
		
		//delete slide
		if (source == actionbuttons.get(5))
		{
			if (!check()) return;
			for (int i : getSelectedIndexes())
			{
				RackElement element = elements.get(i);
				
				JColorChooser colorchooser = new JColorChooser();
				colorchooser.setBorder(BorderFactory.createTitledBorder("Set color for: " + element.getRackName()));
				
				int choose = JOptionPane.showConfirmDialog(this, colorchooser, "Select " + element.getRackName() + " color",JOptionPane.OK_CANCEL_OPTION);
				
				if (choose == 0)
					element.setColor(colorchooser.getColor());
			}
			
		}
		
		//update
		slidecount.setText("Slides: " + elements.size());
		selectAllEvent();
		validate();
		repaint();
	}
	
	//select all event
	public void selectAllEvent()
	{
		if (elements.size() == 0)
		{
			selected.setSelected(false);
			selected.setEnabled(false);
			return;
		}
		
		if (getSelectedIndexes().size() == elements.size())
		{
			selected.setSelected(true);
		}
		else selected.setSelected(false);
	}
	
	private boolean check()
	{
		if (getSelectedIndexes().size() == 0)
		{
			JOptionPane.showMessageDialog(Main.frame, "Slide not selected", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else return true;
	}
	
	private ArrayList<Integer> getSelectedIndexes()
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		// searching from biggest to smallest for repair index bug
		for (int i = elements.size() - 1; i >= 0 ;i--)
		{
			if (elements.get(i).getSelected() == true)
			{
				list.add(i);
			}
		}
		
		return list;
	}
	
	
	public void setEnabled(boolean b)
	{
		enableComponents(this,b);
	}
	
    public void enableComponents(Container container, boolean enable)
    {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableComponents((Container)component, enable);
            }
        }
    }

}
