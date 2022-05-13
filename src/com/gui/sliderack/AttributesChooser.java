package com.gui.sliderack;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.w3c.dom.Element;

import javax.swing.JCheckBox;

import com.gui.sliderack.atributes.Attribute;
import com.io.Util;
import com.io.XmlParser;

public class AttributesChooser extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	RackElement rackelement;
	
	JPanel panel;
	
	public ArrayList<JCheckBox> checkbox = new ArrayList<JCheckBox>();
	public ArrayList<Attribute> atributes = new ArrayList<Attribute>();
	
	public AttributesChooser(RackElement rackelement)
	{
		this.rackelement = rackelement;
		
		setSize(600,400);
		setTitle("Atributes");
		setLocationRelativeTo(null);
		setIconImage(Util.loadIcon("/images/icon.png"));
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		init();
		
		add(new JScrollPane(panel));
	}
	
	public void sync()
	{
		rackelement.centerpanel.removeAll();
		rackelement.attributes.clear();
		
		for (int i = 0; i < atributes.size();i++)
			if (checkbox.get(i).isSelected())
			{
				rackelement.centerpanel.add(atributes.get(i));
				rackelement.attributes.add(atributes.get(i));
			}
		rackelement.centerpanel.validate();
		rackelement.centerpanel.repaint();
	}
	
	private void init()
	{
		for (Attribute.Type type : Attribute.Type.values())
		{
			JPanel elementpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			Attribute atrib = Attribute.getAtributeByName(type.getName());
			
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
	
	public void load(Element element)
	{
		rackelement.centerpanel.removeAll();
		rackelement.attributes.clear();
		
		Element[] elements = XmlParser.getElementsFromElement(element);
		
		for (Element e : elements)
		{
			Attribute attribute = getAttributeByName(e.getTagName());
			
			attribute.load(e);
			
			JCheckBox cbox = checkbox.get(atributes.lastIndexOf(attribute));
			cbox.setSelected(true);
		}
		
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
	
	public Attribute getAttributeByName(String name)
	{
		for (Attribute a : atributes)
		{
			if (a.type.getName().equals(name))
				return a;
		}
		return null;
	}
}
