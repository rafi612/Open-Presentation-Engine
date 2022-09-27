package com.ope.gui.sliderack.atributes;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.w3c.dom.Element;

import com.ope.io.xml.XmlWriter;
import com.ope.main.Main;

public class A_Animation extends Attribute 
{
	private static final long serialVersionUID = 1L;
	
	String animation = "None";

	public A_Animation()
	{
		super(Attribute.Type.ANIMATION.getFullName() + ": None");
		type = Attribute.Type.ANIMATION;
		
		canBeMultiple = false;
		isAlways = false;
	}

	public void getXmlTag(XmlWriter xml)
	{
		xml.addTag("animation", "type=" + animation);
	}
	
	public void load(Element element)
	{
		animation = element.getAttribute("type");
		setText(animation);
	}
	
	public void onActivate()
	{
		String[] s = {"Appearing","Departure","Smooth","Rotating","None"};
		JComboBox<String> combo = new JComboBox<String>(s);
		JComponent[] c = {new JLabel("Choose Animation:"),combo};
		
		JOptionPane.showConfirmDialog(Main.frame,c , "Animation", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
		
		animation = combo.getSelectedItem().toString();
		
		setText(animation);
	}
	
	public void setText(String text)
	{
		super.setText(type.getFullName() + ": " + text);
	}
}
