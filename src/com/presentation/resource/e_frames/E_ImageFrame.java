package com.presentation.resource.e_frames;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.io.Stream;
import com.main.Main;
import com.presentation.resource.ImageResource;
import com.presentation.resource.elements.E_Image;
import com.project.Project;

public class E_ImageFrame extends JDialog implements ChangeListener,WindowListener
{
	private static final long serialVersionUID = 1L;
	public JSpinner sx;
	public JSpinner sy;
	JSpinner sw;
	JSpinner sh;
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
		setResizable(false);
		setAlwaysOnTop(true);
		addWindowListener(this);
		setLayout(null);
		setDropTarget(new DropTarget()
		{
			private static final long serialVersionUID = 1L;
			public synchronized void drop (DropTargetDropEvent evt)
        	{
				try 
				{
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					File file = new File((String) evt.getTransferable().getTransferData(DataFlavor.stringFlavor));
					if (!file.getPath().startsWith(Project.projectlocation))
					{
						System.out.println("Error");
						return;
					}
					else
					{
						element.path = new File(Project.projectlocation).toURI().relativize(file.toURI()).getPath();
						
						element.image = new ImageResource(Project.projectlocation + Stream.slash() + element.path);
						element.w = element.image.image.getWidth();
						element.h = element.image.image.getHeight();
						sw.setValue(element.image.image.getWidth());
						sh.setValue(element.image.image.getHeight());
						textfieldpath.setText(element.path);
					}
					     
				} 
				catch (Exception ex)
				{
					ex.printStackTrace();
					JOptionPane.showMessageDialog(Main.frame,ex.getStackTrace(), "Unsupported file type", JOptionPane.ERROR_MESSAGE);
				}
        	}
	
		});
		SpinnerModel modelx = new SpinnerNumberModel(element.x,-Integer.MAX_VALUE,Integer.MAX_VALUE,1);       
		sx = new JSpinner(modelx);
		sx.addChangeListener(this);
		
		SpinnerModel modely = new SpinnerNumberModel(element.y,-Integer.MAX_VALUE,Integer.MAX_VALUE,1);       
		sy = new JSpinner(modely);
		sy.addChangeListener(this);
		
		SpinnerModel modelw = new SpinnerNumberModel(element.w,0,Integer.MAX_VALUE,1);       
		sw = new JSpinner(modelw);
		sw.addChangeListener(this);
		
		SpinnerModel modelh = new SpinnerNumberModel(element.h,0,Integer.MAX_VALUE,1);       
		sh = new JSpinner(modelh);
		sh.addChangeListener(this);
		
		textfieldpath = new JTextField(element.path);
		
		JLabel lp = new JLabel("Path:");
		lp.setBounds(10, 10, 30,10);
		add(lp);
		textfieldpath.setBounds(40, 5, 200,20);
		textfieldpath.setEditable(false);
		add(textfieldpath);
		
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
	
	// setting JSpinner value form elements
	public void update()
	{
		sx.setValue(element.x);
		sy.setValue(element.y);
		sw.setValue(element.w);
		sh.setValue(element.h);
	}
	
	// setting elements value form JSpinner if it's dragged
	@Override
	public void stateChanged(ChangeEvent e) 
	{
		if (!element.dragged)
		{
			element.x = (int) sx.getValue();
			element.y = (int) sy.getValue();
			element.w = (int) sw.getValue();
			element.h = (int) sh.getValue();
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		element.editing = false;
	}

	@Override
	public void windowClosed(WindowEvent e) 
	{
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


}
