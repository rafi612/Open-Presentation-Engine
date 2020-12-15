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
import com.project.Project;

public class E_Image extends Element implements ChangeListener,ActionListener
{
	String path;
	
	int x;
	int y;
	int w;
	int h;
	
	ImageResource image;
	
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
	
	
	JSpinner sx;
	JSpinner sy;
	JSpinner sw;
	JSpinner sh;
	JButton select;
	JTextField textfieldpath;
	public void frame()
	{
		JDialog dialog = new JDialog();
		dialog.setTitle("Image");
		dialog.setSize(300,200);
		dialog.setLocationRelativeTo(Main.frame);
		dialog.setIconImage(Main.loadIcon("/images/icon.png"));
		dialog.setVisible(true);
		dialog.setResizable(false);
		dialog.setLayout(null);
		
		SpinnerModel modelx = new SpinnerNumberModel(x,0,1280,1);       
		sx = new JSpinner(modelx);
		sx.addChangeListener(this);
		
		SpinnerModel modely = new SpinnerNumberModel(y,0,720,1);       
		sy = new JSpinner(modely);
		sy.addChangeListener(this);
		
		SpinnerModel modelw = new SpinnerNumberModel(w,0,Integer.MAX_VALUE,1);       
		sw = new JSpinner(modelw);
		sw.addChangeListener(this);
		
		SpinnerModel modelh = new SpinnerNumberModel(h,0,Integer.MAX_VALUE,1);       
		sh = new JSpinner(modelh);
		sh.addChangeListener(this);
		
		textfieldpath = new JTextField(path);
		select = new JButton(new ImageIcon(Main.loadIcon("/icons/open.png")));
		select.setVisible(true);
		select.addActionListener(this);
		
		JLabel lp = new JLabel("Path:");
		lp.setBounds(10, 10, 30,10);
		dialog.add(lp);
		textfieldpath.setBounds(40, 5, 200,20);
		textfieldpath.setEditable(false);
		dialog.add(textfieldpath);
		select.setBounds(250,5, 20,20);
		dialog.add(select);
		
		JLabel lx = new JLabel("X:");
		lx.setBounds(10, 35, 10,10);
		dialog.add(lx);
		sx.setBounds(40, 30, 230,20);
		dialog.add(sx);
		
		JLabel ly = new JLabel("Y:");
		ly.setBounds(10, 65, 10,10);
		dialog.add(ly);
		sy.setBounds(40, 60, 230,20);
		dialog.add(sy);
		
		JLabel lw = new JLabel("Width:");
		lw.setBounds(10, 95, 30,10);
		dialog.add(lw);
		sw.setBounds(50, 90, 220,20);
		dialog.add(sw);
		
		JLabel lh = new JLabel("Height:");
		lh.setBounds(10, 125, 40,10);
		dialog.add(lh);
		sh.setBounds(60, 120, 210,20);
		dialog.add(sh);
		
	}

	@Override
	public void stateChanged(ChangeEvent e) 
	{
		x = (int) sx.getValue();
		y = (int) sy.getValue();
		w = (int) sw.getValue();
		h = (int) sh.getValue();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		
		if (source == select)
		{
			JFileChooser jfc = new JFileChooser(Project.projectlocation);
			jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			jfc.setDialogTitle("Choose a image to load:");
			jfc.setFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
			
			int returnValue = jfc.showOpenDialog(Main.frame);
			if (returnValue == JFileChooser.APPROVE_OPTION)
			{
				path = new File(Project.projectlocation).toURI().relativize(new File(jfc.getSelectedFile().getPath()).toURI()).getPath();
				
				image = new ImageResource(Project.projectlocation + Stream.slash() + path);
				w = image.image.getWidth();
				h = image.image.getHeight();
				sw.setValue(image.image.getWidth());
				sh.setValue(image.image.getHeight());
				textfieldpath.setText(path);
			}
		}
		
	}
}
