package com.gui.sliderack.atributes;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.w3c.dom.Element;

import com.main.Main;

public class A_Start_Animation extends Attribute 
{
	private static final long serialVersionUID = 1L;
	
	String animation;

	public A_Start_Animation()
	{
		super(Attribute.Type.START_ANIMATION.getFullName() + ": None");
		type = Attribute.Type.START_ANIMATION;
		
		canBeMultiple = false;
		isAlways = false;
	}

	public String getXmlTag()
	{
		return "\t<start_animation>" + animation + "</start_animation>";
	}
	
	public void load(Element element)
	{
		animation = element.getTextContent();
		setText(animation);
	}
	
	public void onActivate()
	{
		String[] s = {"Appearing","None"};
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
