package com.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.io.Util;
import com.main.Main;
import com.project.Project;

public class Tree extends JTree implements ActionListener,TreeSelectionListener
{
	private static final long serialVersionUID = 1L;
    public JMenuItem newfile, newfolder, deletefile,renamefile,cutfile,copyfile,pastefile;
	DefaultMutableTreeNode selected = null;
	
	File copy = null;
	
	// 1 = copy 0 = cut -1 = none operation
	int operation = -1;

	public Tree(DefaultMutableTreeNode workspace) 
	{       
		super(workspace);
		setPreferredSize(new Dimension(215,0));
		
        //popup drzewka
        newfile = new JMenuItem("New file");
        newfile.addActionListener(this);
        newfolder = new JMenuItem("New Folder");
        newfolder.addActionListener(this);
        deletefile = new JMenuItem("Delete");
        deletefile.addActionListener(this);
        renamefile = new JMenuItem("Rename");
        renamefile.addActionListener(this);
        cutfile = new JMenuItem("Cut");
        cutfile.addActionListener(this);
        copyfile = new JMenuItem("Copy");
        copyfile.addActionListener(this);
        pastefile = new JMenuItem("Paste");
        pastefile.addActionListener(this);
		
        JPopupMenu treepopup = new JPopupMenu();
        treepopup.add(newfile);
        treepopup.add(newfolder);
        treepopup.add(cutfile);
        treepopup.add(copyfile);
        treepopup.add(pastefile);
        treepopup.add(renamefile);
        treepopup.add(deletefile);
        
        setBorder(BorderFactory.createTitledBorder("Project Explorer"));
        setShowsRootHandles(true);
        setToolTipText("Drag and Drop file to copy into project.");
        setCellRenderer(new TreeCellRenderer());
        setComponentPopupMenu(treepopup);
        super.setEnabled(false);
        setDragEnabled(true);
        
        //add drop target to tree
        setDropTarget(new DropTarget() {
			private static final long serialVersionUID = 1L;
			public synchronized void drop (DropTargetDropEvent evt)
        	{
				try 
				{
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					@SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					
					//procesing file copy path in project
					String copypath = "";
					if (selected != null)
					{
						if (selected.toString().equals("Workspace"))
							copypath = Project.projectlocation;
						else copypath = selected.toString();
					}
					else copypath = Project.projectlocation;
					
					
					//copying files
					for (File file : droppedFiles)
					{
						Util.copyFile(file.getPath(), Util.path(copypath,file.getName()));
						Project.refreshProject();
					}        
				} 
				catch (Exception ex)
				{
					ex.printStackTrace();
					JOptionPane.showMessageDialog(Main.frame,ex.getStackTrace(), "Unsupported file type", JOptionPane.ERROR_MESSAGE);
				}
        	}
        });
        addTreeSelectionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		
		File select = null;
		if (selected != null) select = new File(selected.toString());
		
		if (source == newfile)
		{
			String name = JOptionPane.showInputDialog(Main.frame, "Enter file name:", "Create new file", JOptionPane.QUESTION_MESSAGE);
			Util.saveFile(Util.projectPath(name), "");
				
			Project.refreshProject();
		}
		if (source == newfolder)
		{
			String folder = JOptionPane.showInputDialog(Main.frame, "Enter folder name:", "Create new folder", JOptionPane.QUESTION_MESSAGE);
			new File(Util.projectPath(folder)).mkdir();
				
			Project.refreshProject();
		}
		if (source == deletefile)
		{
			if (check(select)) return;
			
			int choose = JOptionPane.showConfirmDialog(Main.frame,"Do you want to delete " + select.getName() + "?", "Delete", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if (choose == 0) select.delete();
			
			Project.refreshProject();
		}
		if (source == renamefile)
		{
			if (check(select)) return;
			
			String name = JOptionPane.showInputDialog(Main.frame, "Enter new file/folder name:", "Rename", JOptionPane.QUESTION_MESSAGE);
			//rename file
			select.renameTo(new File(Util.projectPath(select.getParent() + File.separator + name)));
			Project.refreshProject();
		}
		if (source == cutfile)
		{
			if (check(select)) return;
			
			copy = select;
			//cut
			operation = 0;
		}
		
		if (source == copyfile)
		{
			if (check(select)) return;
			
			copy = select;
			//cut
			operation = 1;
		}
		
		if (source == pastefile)
		{
			if (operation == -1) 
			{
				JOptionPane.showMessageDialog(Main.frame, "No file to paste", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//destination
			File destdir = select;
			
			//check if select is workspace
			if (select.getPath().equals("Workspace")) destdir = new File(Project.projectlocation);
			
			//check
			if (!destdir.isDirectory())
			{
				JOptionPane.showMessageDialog(Main.frame, "This is not a directory", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (operation == 0)
			{
				//cut
				copy.renameTo(new File(Util.path(destdir.getPath(),copy.getName())));
			}
			else if (operation == 1)
			{
				//copy
				Util.copyFile(copy.getPath(),destdir.getPath());
			}
			
			Project.refreshProject();
		}
	}
	
	private boolean check(File select)
	{
		if (selected == null)
		{
			JOptionPane.showMessageDialog(Main.frame, "Element not selected", "Error", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if (!(select.isFile() || select.isDirectory()))
		{
			JOptionPane.showMessageDialog(Main.frame, "This element is not a file or directory", "Error", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		return false;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) 
	{
		selected = (DefaultMutableTreeNode)getLastSelectedPathComponent();
	}
	
	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		newfile.setEnabled(b);
		newfolder.setEnabled(b);
		cutfile.setEnabled(b);
		copyfile.setEnabled(b);
		pastefile.setEnabled(b);
		renamefile.setEnabled(b);
		deletefile.setEnabled(b);
	}

}
 
class TreeCellRenderer extends DefaultTreeCellRenderer 
{
	private static final long serialVersionUID = 1L;
	
	private ImageIcon image = new ImageIcon(Util.loadIcon("/icons/files/image.png"));
	private ImageIcon layout = new ImageIcon(Util.loadIcon("/icons/files/layout.png"));
	private ImageIcon xml = new ImageIcon(Util.loadIcon("/icons/files/xml.png"));
	private ImageIcon file = new ImageIcon(Util.loadIcon("/icons/files/file.png"));
	private ImageIcon directory_open = new ImageIcon(Util.loadIcon("/icons/files/directory_open.png"));
	private ImageIcon directory_closed = new ImageIcon(Util.loadIcon("/icons/files/directory_closed.png"));
	
	private Border border = BorderFactory.createEmptyBorder(2,2,2,2);
 
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) 
	{
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (value instanceof DefaultMutableTreeNode) 
		{
			value = ((DefaultMutableTreeNode)value).getUserObject();
			if (value instanceof File) 
			{
				File file = (File) value;
				setText(file.getName());
				//setIcon(fsv.getSystemIcon(file));
				
				if (Util.FileExtension(file).equals("png") || Util.FileExtension(file).equals("jpg"))
					setIcon(image);
				else if (Util.FileExtension(file).equals("layout"))
					setIcon(layout);
				else if (Util.FileExtension(file).equals("xml"))
					setIcon(xml);
				else
				{
					if (file.isFile())
						setIcon(this.file);
					else if (file.isDirectory())
					{
						if (expanded)
							setIcon(directory_open);
						else
							setIcon(directory_closed);
					}
				}
			}
			//setting border
			this.setBorder(border);
		}
		return this;
	}
}