package com.presentation.resource.e_frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.io.Stream;
import com.main.Main;
import com.presentation.resource.ImageResource;
import com.presentation.resource.elements.E_Image;
import com.project.Project;

public class E_ImageFrame extends JDialog implements ChangeListener,ActionListener
{
	private static final long serialVersionUID = 1L;
	JSpinner sx;
	JSpinner sy;
	JSpinner sw;
	JSpinner sh;
	JButton select;
	JTextField textfieldpath;
	
	E_Image element;
	public E_ImageFrame(E_Image element)
	{
		super();
		this.element = element;
		setTitle("Image");
		setSize(300,200);
		setLocationRelativeTo(Main.frame);
		setIconImage(Main.loadIcon("/images/icon.png"));
		setVisible(true);
		setResizable(false);
		setAlwaysOnTop(true);
		setLayout(null);
		
		SpinnerModel modelx = new SpinnerNumberModel(element.x,0,1280,1);       
		sx = new JSpinner(modelx);
		sx.addChangeListener(this);
		
		SpinnerModel modely = new SpinnerNumberModel(element.y,0,720,1);       
		sy = new JSpinner(modely);
		sy.addChangeListener(this);
		
		SpinnerModel modelw = new SpinnerNumberModel(element.w,0,Integer.MAX_VALUE,1);       
		sw = new JSpinner(modelw);
		sw.addChangeListener(this);
		
		SpinnerModel modelh = new SpinnerNumberModel(element.h,0,Integer.MAX_VALUE,1);       
		sh = new JSpinner(modelh);
		sh.addChangeListener(this);
		
		textfieldpath = new JTextField(element.path);
		select = new JButton(new ImageIcon(Main.loadIcon("/icons/open.png")));
		select.setVisible(true);
		select.addActionListener(this);
		
		JLabel lp = new JLabel("Path:");
		lp.setBounds(10, 10, 30,10);
		add(lp);
		textfieldpath.setBounds(40, 5, 200,20);
		textfieldpath.setEditable(false);
		add(textfieldpath);
		select.setBounds(250,5, 20,20);
		add(select);
		
		JLabel lx = new JLabel("X:");
		lx.setBounds(10, 35, 10,10);
		add(lx);
		sx.setBounds(40, 30, 230,20);
		add(sx);
		
		JLabel ly = new JLabel("Y:");
		ly.setBounds(10, 65, 10,10);
		add(ly);
		sy.setBounds(40, 60, 230,20);
		add(sy);
		
		JLabel lw = new JLabel("Width:");
		lw.setBounds(10, 95, 30,10);
		add(lw);
		sw.setBounds(50, 90, 220,20);
		add(sw);
		
		JLabel lh = new JLabel("Height:");
		lh.setBounds(10, 125, 40,10);
		add(lh);
		sh.setBounds(60, 120, 210,20);
		add(sh);
	}
	
	@Override
	public void stateChanged(ChangeEvent e) 
	{
		element.x = (int) sx.getValue();
		element.y = (int) sy.getValue();
		element.w = (int) sw.getValue();
		element.h = (int) sh.getValue();
		
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
				element.path = new File(Project.projectlocation).toURI().relativize(new File(jfc.getSelectedFile().getPath()).toURI()).getPath();
				
				element.image = new ImageResource(Project.projectlocation + Stream.slash() + element.path);
				element.w = element.image.image.getWidth();
				element.h = element.image.image.getHeight();
				sw.setValue(element.image.image.getWidth());
				sh.setValue(element.image.image.getHeight());
				textfieldpath.setText(element.path);
			}
		}
		
	}


}
