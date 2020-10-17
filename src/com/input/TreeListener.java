package com.input;

import java.io.File;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.main.Main;

public class TreeListener implements TreeSelectionListener 
{

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		//System.out.println((DefaultMutableTreeNode)Main.tree.getLastSelectedPathComponent());
	}

}
