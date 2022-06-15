package com.ope.gui;

import java.io.File;

import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.ope.main.Main;

public class TreeFileEvent 
{
	public interface Target
	{
		public void fileSelected(String path);
	}
	
	private TreeSelectionListener listener;
	
	public static final String DEFAULT_TEXT = "Select file in Project Explorer";
	
	public void open(Target target) 
	{
		listener = (event) -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)Main.tree.getLastSelectedPathComponent();
			File file = new File(node.toString());
			
			target.fileSelected(file.getPath());
			
			Main.tree.removeTreeSelectionListener(listener);
		};
		
		Main.tree.clearSelection();
		
		Main.tree.addTreeSelectionListener(listener);
	}

}
