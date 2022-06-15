package com.ope.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.ope.main.Main;

public class TreeFileChooser extends JDialog
{
	private static final long serialVersionUID = 1L;

	public interface Target
	{
		public void fileSelected(String path);
	}
	
	private Target target;
	
	private JButton ok,cancel;
	private JTextField pathtextfield;
	
	private TreeSelectionListener treelistener;
	
	private String selectedpath;
	
	private Window parent;
	boolean parentalwaysontop;

	public TreeFileChooser(Window parent) 
	{
		super(Main.frame,"Select file");
		this.parent = parent;
		
		setSize(300,125);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		
		pathtextfield = new JTextField();
		pathtextfield.setEditable(false);
		
		ok = new JButton("OK");
		ok.setPreferredSize(new Dimension(50,25));
		ok.addActionListener((event) -> {
			
			if (selectedpath == null)
			{
				setAlwaysOnTop(false);
				JOptionPane.showMessageDialog(Main.frame, "Element not selected", "Error", JOptionPane.ERROR_MESSAGE);
				
				setAlwaysOnTop(true);
				return;
			}
			
			dispose();
			target.fileSelected(selectedpath);
			
			if (parent != null)
				parent.setAlwaysOnTop(parentalwaysontop);
		});
		
		cancel = new JButton("Cancel");
		cancel.setPreferredSize(new Dimension(60,25));
		cancel.addActionListener((event) -> dispose());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JLabel label = new JLabel("Select file in Project Explorer");
		label.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		
		panel.add(leftJustify(label));
		panel.add(pathtextfield);
		
		JPanel flowpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		flowpanel.add(cancel);
		flowpanel.add(ok);
		panel.add(flowpanel);
		
		treelistener = (event) -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)Main.tree.getLastSelectedPathComponent();				
			
			File file = new File(node.toString());
			
			selectedpath = file.getPath();
			
			pathtextfield.setText(file.getName());
		};
		
		add(panel);
	}
	
	public void open(Target target)
	{
		this.target = target;
		Main.tree.addTreeSelectionListener(treelistener);
		
		Main.tree.clearSelection();
		
		if (parent != null)
		{
			parentalwaysontop = parent.isAlwaysOnTop();
			parent.setAlwaysOnTop(false);
		}
		
		setVisible(true);
	}
	
	public void dispose()
	{
		Main.tree.removeTreeSelectionListener(treelistener);
		super.dispose();
	}
	
	private JComponent leftJustify(JComponent component)
	{
		Box b = Box.createHorizontalBox();
		b.add(component);
		b.add(Box.createHorizontalGlue());
		return b;
	}

}
