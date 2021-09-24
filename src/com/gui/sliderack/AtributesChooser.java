package com.gui.sliderack;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;

import com.gui.sliderack.atributes.Atribute;
import com.main.Main;

public class AtributesChooser extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	RackElement rackelement;
	
	JPanel panel;
	
	public ArrayList<JCheckBox> checkbox = new ArrayList<JCheckBox>();
	public ArrayList<Atribute> atributes = new ArrayList<Atribute>();
	
	public AtributesChooser(RackElement rackelement)
	{
		this.rackelement = rackelement;
		
		setSize(600,400);
		setTitle("Atributes");
		setLocationRelativeTo(null);
		setIconImage(Main.loadIcon("/images/icon.png"));
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		init();
		
		add(new JScrollPane(panel));
	}
	
	public void sync()
	{
		rackelement.centerpanel.removeAll();
		for (int i = 0; i < atributes.size();i++)
			if (checkbox.get(i).isSelected())
				rackelement.centerpanel.add(atributes.get(i));
		rackelement.centerpanel.validate();
		rackelement.centerpanel.repaint();
	}
	
	private void init()
	{
		for (Atribute.Type type : Atribute.Type.values())
		{
			JPanel elementpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			Atribute atrib = Atribute.getAtributeByName(type.getName());
			
			JCheckBox cbox = new JCheckBox(type.getFullName());
			cbox.addActionListener(this);
			if (atrib.isAlways)
			{
				cbox.setSelected(true);
				cbox.setEnabled(false);
			}
			atributes.add(atrib);
			checkbox.add(cbox);
			
			elementpanel.add(cbox);
			panel.add(elementpanel);
		}
		
		validate();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		
		for (int i = 0; i < checkbox.size();i++)
		{
			if (source == checkbox.get(i))
			{
				sync();
			}

		}
	}
}
