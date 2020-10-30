/* Copyright 2019-2020 by rafi612 */
package com.tree;
 
import java.awt.Component;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
 
public class TreeCellRenderer extends DefaultTreeCellRenderer 
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
//                        if (extension(file).equals("xml"))
//                        	setIcon(new ImageIcon("res\\icons\\xml.png"));
//                        else 
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