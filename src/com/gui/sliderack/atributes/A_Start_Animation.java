package com.gui.sliderack.atributes;

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
}
