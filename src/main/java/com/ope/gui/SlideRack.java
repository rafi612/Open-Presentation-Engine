package com.ope.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.xml.sax.SAXException;

import com.ope.gui.sliderack.RackElement;
import com.ope.io.Util;
import com.ope.io.xml.Tag;
import com.ope.io.xml.XmlReader;
import com.ope.io.xml.XmlWriter;
import com.ope.main.Main;

public class SlideRack extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	public ArrayList<JButton> actionbuttons;
	
	public JCheckBox selectedAll;
	public JLabel slidecount;
	public JPanel rackpanel;
	
	public ArrayList<RackElement> elements;
	
	public MenuBar parentmenu;

	public SlideRack(MenuBar parentmenu) 
	{
		this.parentmenu = parentmenu;
		elements = new ArrayList<>();
		
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
		
	    for (JButton button : actionbuttons)
	    {
	    	button.addActionListener(this);
	    	button.setMaximumSize(new Dimension(200,40));
	    	
	    	actions.add(button);
	    }
	        
	    add(actions,BorderLayout.EAST);
		
		JPanel toppanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		toppanel.setBorder(BorderFactory.createTitledBorder(""));
		
		selectedAll = new JCheckBox("Select all");
		selectedAll.setEnabled(false);
		selectedAll.addActionListener(this);
		toppanel.add(selectedAll);
		
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
		var source = e.getSource();
		
		if (source == selectedAll)
		{
			for (RackElement rack : elements)
				rack.setSelected(selectedAll.isSelected());
		}
		
		//add slide
		if (source == actionbuttons.get(0))
		{
			RackElement element = new RackElement("Slide " + elements.size(),this);
			addElement(element);
			
			selectedAll.setEnabled(true);
		}
		
		//delete slide
		if (source == actionbuttons.get(2))
		{
			if (!check()) return;
			
			if (JOptionPane.showConfirmDialog(Main.frame,"Are you sure to delete this slides?", "Delete", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == 0) 
			{
				for (int i : getSelectedIndexes())
				{
					rackpanel.remove(elements.get(i));
					elements.remove(i);
				} 
			}
			
		}
		
		//update
		slidecount.setText("Slides: " + elements.size());
		selectAllEvent();
		validate();
		repaint();
	}
	
	public void addElement(RackElement element)
	{
		slidecount.setText("Slides: " + (elements.size() + 1));
		
		elements.add(element);
		rackpanel.add(element);
		
		selectedAll.setEnabled(true);
	}
	
	public void clear()
	{
		slidecount.setText("Slides: 0");
		
		rackpanel.removeAll();
		
		//repaint
		rackpanel.validate();
		rackpanel.repaint();
		
		elements.clear();
		
		selectedAll.setEnabled(false);
		
		validate();
		repaint();
	}
	
	//select all event
	public void selectAllEvent()
	{
		if (elements.size() == 0)
		{
			selectedAll.setSelected(false);
			selectedAll.setEnabled(false);
			return;
		}
		
		if (getSelectedIndexes().size() == elements.size())
		{
			selectedAll.setSelected(true);
		}
		else selectedAll.setSelected(false);
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
				list.add(i);
		}
		
		return list;
	}
	
	public void build(String path)
	{		
		XmlWriter xml = new XmlWriter();
		xml.openTag("project");
		
		for (RackElement element : elements)
			element.getSlideXmlTag(xml);
		
		xml.addTag("global", "fullscreen=" + parentmenu.fullscreen.isSelected());
		
		xml.closeTag();
		
		Util.saveFile(path, xml.get());
	}
	
	public void load(String path)
	{
		try 
		{
			XmlReader xml = new XmlReader(path);
			
			Tag[] slides = xml.getTags("slide");
			
			for (Tag slidetag : slides)
			{
				RackElement elem = new RackElement(slidetag.getAttribute("name"), this);
				int color = Integer.parseInt(slidetag.getAttribute("color"));

				if (color != -1)
					elem.setColor(new Color(color));
				
				
				elem.load(slidetag);
				
				addElement(elem);
			}
			
			Tag global = xml.getTags("global")[0];
			
			parentmenu.fullscreen.setSelected(Boolean.parseBoolean(global.getAttribute("fullscreen")));
		} 
		catch (IOException | SAXException e) 
		{
			JOptionPane.showMessageDialog(Main.frame, "Error loading slides: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}

	}
	
	
	public void setEnabled(boolean b)
	{
		enableComponents(this,b);
	}
	
    public void enableComponents(Container container, boolean enable)
    {
        Component[] components = container.getComponents();
        for (Component component : components) 
        {
            component.setEnabled(enable);
            if (component instanceof Container) 
            {
                enableComponents((Container)component, enable);
            }
        }
    }

}
