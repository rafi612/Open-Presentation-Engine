package com.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.presentation.main.Presentation;
import com.project.Project;

public class ActionPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
    public ArrayList<JButton> actions = new ArrayList<JButton>();

	public ActionPanel() 
	{
        setBorder(BorderFactory.createTitledBorder("Actions"));
        setToolTipText("Necessary actions");
        
        actions.add(new JButton("Build & Run"));
        actions.add(new JButton("Save"));
        actions.add(new JButton("Stop"));
        
        for (JButton button : actions)
        {
        	button.addActionListener(this::actionPerformed);
        	add(button);
        }
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		
		//run
		if (source == actions.get(0))
		{
			Project.run();
		}
		
		//action save
		if (source == actions.get(1))
		{			
			Project.save_dialog();
			
		}
		
		//stop
		if (source == actions.get(2))
		{
			Presentation.stop();
		}
	}
	
	public void setEnabled(boolean enable)
	{
		for (JButton button : actions)
			button.setEnabled(enable);
	}

}
