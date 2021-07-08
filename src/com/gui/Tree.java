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
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.io.Stream;
import com.main.Main;
import com.project.Project;

public class Tree extends JTree implements ActionListener,TreeSelectionListener
{
	private static final long serialVersionUID = 1L;
    public JMenuItem newfile, newfolder, deletefile,renamefile,movefile;
	DefaultMutableTreeNode selected = null;

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
        movefile = new JMenuItem("Move");
        movefile.addActionListener(this);
		
        JPopupMenu treepopup = new JPopupMenu();
        treepopup.add(newfile);
        treepopup.add(newfolder);
        treepopup.add(renamefile);
        treepopup.add(deletefile);
        treepopup.add(movefile);
        
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
					for (File file : droppedFiles)
					{
						Stream.copyFileondrive(file.getPath(), Project.projectlocation + Stream.slash() + file.getName());
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
		if (source == newfile)
		{
			String name = JOptionPane.showInputDialog(Main.frame, "Enter file name:", "Create new file", JOptionPane.QUESTION_MESSAGE);
			Stream.saveFile(Project.projectlocation + Stream.slash() + name, "");
				
			Project.refreshProject();
		}
		if (source == newfolder)
		{
			String folder = JOptionPane.showInputDialog(Main.frame, "Enter folder name:", "Create new folder", JOptionPane.QUESTION_MESSAGE);
			new File(Project.projectlocation + Stream.slash() + folder).mkdir();
				
			Project.refreshProject();
		}
		if (source == deletefile)
		{
			File select = new File(selected.toString());
			if (check(select)) return;
			
			int choose = JOptionPane.showConfirmDialog(Main.frame,"Do you want to delete " + select.getName() + "?", "Delete", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if (choose == 0) select.delete();
			
			Project.refreshProject();
		}
		if (source == renamefile)
		{
			File select = new File(selected.toString());
			if (check(select)) return;
			
			String name = JOptionPane.showInputDialog(Main.frame, "Enter new file/folder name:", "Rename", JOptionPane.QUESTION_MESSAGE);
			//rename file
			select.renameTo(new File(select.getParent() + Stream.slash() + name));
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
		Main.tree.newfile.setEnabled(b);
		Main.tree.newfolder.setEnabled(b);
		Main.tree.deletefile.setEnabled(b);
	}

}
 
class TreeCellRenderer extends DefaultTreeCellRenderer 
{
	private static final long serialVersionUID = 1L;
	private FileSystemView fsv = FileSystemView.getFileSystemView();
        
	public String extension(File file)
	{
		// convert the file name into string
		String fileName = file.toString();

		int index = fileName.lastIndexOf('.');
		if(index > 0) 
		{
			String extension = fileName.substring(index + 1);
			return extension;
		}
		return "";
	}
 
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) 
	{
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (value instanceof DefaultMutableTreeNode) {
			value = ((DefaultMutableTreeNode)value).getUserObject();
			if (value instanceof File) 
			{
				File file = (File) value;
				if (file.isFile()) 
				{
//					if (extension(file).equals("xml"))
//						setIcon(new ImageIcon("res\\icons\\xml.png"));
//					else 
					setIcon(fsv.getSystemIcon(file));
					setText(file.getName());
				} else {
					setIcon(fsv.getSystemIcon(file));
					setText(file.getName());
				}
			}
		}
		return this;
	}
}