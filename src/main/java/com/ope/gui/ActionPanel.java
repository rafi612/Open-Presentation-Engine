package com.ope.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.ope.io.Util;
import com.ope.project.Project;

public class ActionPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	public ArrayList<JButton> actions = new ArrayList<JButton>();

	public ActionPanel() 
	{
		setBorder(BorderFactory.createTitledBorder("Actions"));
		setToolTipText("Necessary actions");
		
		actions.add(new JButton("Run",new ImageIcon(Util.loadIcon("/icons/menu/run.png"))));
		actions.add(new JButton("Save",new ImageIcon(Util.loadIcon("/icons/menu/save.png"))));
		
		for (JButton button : actions)
		{
			button.addActionListener(this);
			add(button);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		
		//run
		if (source == actions.get(0))
			Project.run();
		
		//action save
		if (source == actions.get(1))		
			Project.saveDialog();
	}
	
	public void setEnabled(boolean enable)
	{
		for (JButton button : actions)
			button.setEnabled(enable);
	}
}