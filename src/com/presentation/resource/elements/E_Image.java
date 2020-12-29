package com.presentation.resource.elements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import com.io.Stream;
import com.jogamp.opengl.GL2;
import com.main.Main;
import com.presentation.graphics.Screen;
import com.presentation.resource.Element;
import com.presentation.resource.ImageResource;
import com.presentation.resource.e_frames.E_ImageFrame;
import com.project.Project;

public class E_Image extends Element
{
	public String path;
	
	public int x;
	public int y;
	public int w;
	public int h;
	
	public ImageResource image;
	
	public E_Image()
	{
		frame();
	}

	public E_Image(String path,int x,int y,int w,int h)
	{
		super();
		this.path = path;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		if (!path.equals(""))
			this.image = new ImageResource(path);
	}
	
	public void render(GL2 gl)
	{
		if (path.equals(""))
			Screen.frectnofill(x,y,w,h,Color.RED);
		else
			Screen.drawImage(image,x,y,w,h);
	}
	
	
	public void frame()
	{
		new E_ImageFrame(this);
	}
}
