package com.ope.gui;

import java.io.File;

import javax.swing.JButton;
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
	
	public JButton button;
	
	public String resulttext = "";
	
	public TreeFileEvent(JButton button)
	{
		this.button = button;
	}
	
	public void open(Target target) 
	{
		String text = button.getText();
		
		button.setText(TreeFileEvent.DEFAULT_TEXT);
		button.setEnabled(false);
		
		listener = (event) -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)Main.frame.tree.getLastSelectedPathComponent();
			File file = new File(node.toString());
			
			target.fileSelected(file.getPath());
			
			Main.frame.tree.removeTreeSelectionListener(listener);
			
			button.setText(resulttext.equals("") ? text : resulttext);
			button.setEnabled(true);
		};
		
		Main.frame.tree.clearSelection();
		
		Main.frame.tree.addTreeSelectionListener(listener);
	}

	
	public void setButtonResultText(String text)
	{
		this.resulttext = text;
	}
}
