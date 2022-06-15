package com.ope.gui.sliderack.atributes;

import javax.swing.JButton;

import org.w3c.dom.Element;

public class Attribute extends JButton
{
	private static final long serialVersionUID = 1L;
	
	public String name;
	public boolean canBeMultiple,isAlways;
	
	public Type type;
	
	public enum Type
	{
		SLIDE("layout","Slide Layout"),
		START_ANIMATION("start_animation","Start Animation"),
		EXIT_ANIMATION("exit_animation","Exit Animation");
		
		String name,full_name;
		Type(String name,String full_name) 
		{
			this.name = name;
			this.full_name = full_name;
		}
		
		public String getName()
		{
			return name;
		}
		public String getFullName()
		{
			return full_name;
		}
	}

	public Attribute(String name)
	{
		this.name = name;
		super.setText(name);
		
		addActionListener((actionEvent) -> this.onActivate());
	}
	
	public String getXmlTag()
	{
		return "";
	}
	
	public void load(Element element)
	{
		
	}
	
	public void onActivate()
	{
		
	}
	
	public static Attribute getAtributeByName(String n)
	{
		if (n.equals("layout")) return new A_Slide();
		else if (n.equals("start_animation")) return new A_Start_Animation();
		else if (n.equals("exit_animation")) return new A_Exit_Animation();
		return new Attribute(n);
	}
}
