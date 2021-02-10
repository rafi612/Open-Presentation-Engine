/* Copyright 2019-2020 by rafi612 */
package com.input;


import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class TreeListener implements TreeSelectionListener 
{

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		//System.out.println((DefaultMutableTreeNode)Main.tree.getLastSelectedPathComponent());
	}

}
