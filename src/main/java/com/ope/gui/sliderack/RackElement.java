package com.ope.gui.sliderack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.w3c.dom.Element;

import com.ope.gui.SlideRack;
import com.ope.gui.sliderack.atributes.Attribute;
import com.ope.io.Util;
import com.ope.io.xml.XmlWriter;
import com.ope.main.Main;

public class RackElement extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private static final int HEIGHT = 150;
	
	JCheckBox selected;
	JLabel name;
	JPanel toppanel,centerpanel;
	JButton add;
	
	JPopupMenu menu;
	JMenuItem rename,color;
	
	SlideRack sliderack;
	
	AttributesChooser chooser;
	
	public ArrayList<Attribute> attributes; 
	
	int hexcolor = -1;

	public RackElement(String name_,SlideRack sliderack) 
	{
		this.sliderack = sliderack;
		
		attributes = new ArrayList<Attribute>();
		
		setBorder(BorderFactory.createRaisedBevelBorder());
		setLayout(new BorderLayout());
		
		setMinimumSize(new Dimension(0, HEIGHT));
		setPreferredSize(new Dimension(0,HEIGHT));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT));
		
		toppanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selected = new JCheckBox();
		selected.addActionListener(this);
		name = new JLabel(name_);
		
		toppanel.add(selected);
		toppanel.add(name);
		
		add(toppanel,BorderLayout.NORTH);
		
		add = new JButton();
		add.setIcon(new ImageIcon(Util.loadIcon("/icons/rack_add.png")));
		add.addActionListener(this);
		add(add,BorderLayout.EAST);
		
		//menu
		menu = new JPopupMenu();
		
		rename = new JMenuItem("Rename");
		rename.addActionListener(this);
		color = new JMenuItem("Change color");
		color.addActionListener(this);
		
		menu.add(rename);
		menu.add(color);
		setComponentPopupMenu(menu);
		
		//center
		centerpanel = new JPanel();
		centerpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		add(centerpanel,BorderLayout.CENTER);
		
		sliderack.selectAllEvent();
		chooser = new AttributesChooser(this);
		chooser.sync();
	}
	

	@Override
	public void actionPerformed(ActionEvent e)
	{
		var source = e.getSource();
		
		if(source == selected)
			sliderack.selectAllEvent();
		
		if(source == color)
		{
			JColorChooser colorchooser = new JColorChooser();
			
			int choose = JOptionPane.showConfirmDialog(Main.frame, colorchooser, "Select " + getRackName() + " color",JOptionPane.OK_CANCEL_OPTION);
			
			if (choose == 0)
				setColor(colorchooser.getColor());
		}
		
		if (source == rename)
		{
			String name = JOptionPane.showInputDialog(Main.frame, "Enter new name", "Rename",JOptionPane.QUESTION_MESSAGE);
			setRackName(name);
		}
		
		if (source == add)
		{
			chooser.setVisible(true);
		}
	}
	
	public void getSlideXmlTag(XmlWriter xml)
	{		
		xml.openTag("slide", "name=" + name.getText(),"color=" + hexcolor);
		
		for (Attribute a : attributes)
			a.getXmlTag(xml);
		
		xml.closeTag();
	}
	
	public void load(Element element)
	{	
		chooser.load(element);
		
		chooser.sync();
	}
	
	public void setColor(Color color)
	{
		toppanel.setBackground(color);
		hexcolor = color.getRGB() & 0xFFFFFF;
	}
	
	public Color getColor()
	{
		return getForeground();
	}
	
	public void setEnabled(boolean b)
	{
		name.setEnabled(b);
		selected.setEnabled(b);
		super.setEnabled(b);
	}
	
	public void setSelected(boolean b)
	{
		selected.setSelected(b);
	}
	
	public boolean getSelected()
	{
		return selected.isSelected();
	}
	
	
	public void setRackName(String name)
	{
		this.name.setText(name);
	}
	
	public String getRackName()
	{
		return name.getText();
	}
	
	public void addAtribute(Attribute a)
	{
		centerpanel.add(a);
	}

}
