package com.presentation.slide.e_frames;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.main.Main;
import com.presentation.slide.elements.E_Shape;

public class E_ShapeFrame extends JDialog implements ChangeListener
{
	private static final long serialVersionUID = 1L;
	
	JSpinner sx;
	JSpinner sy;
	JSpinner sw;
	JSpinner sh;
	E_Shape element;
	
	public E_ShapeFrame(E_Shape element)
	{
		super();
		this.element = element;
		setTitle("Image");
		setSize(300,200);
		setLocationRelativeTo(Main.frame);
		setIconImage(Main.loadIcon("/images/icon.png"));
		setVisible(true);
		setResizable(false);
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
		
		JLabel lp = new JLabel("Path:");
		lp.setBounds(10, 10, 30,10);
		add(lp);
		
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


}
