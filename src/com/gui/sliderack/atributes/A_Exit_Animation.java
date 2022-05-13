package com.gui.sliderack.atributes;

public class A_Exit_Animation extends Attribute 
{
	private static final long serialVersionUID = 1L;
	
	String animation;

	public A_Exit_Animation()
	{
		super(Attribute.Type.EXIT_ANIMATION.getFullName() + ": None");
		type = Attribute.Type.EXIT_ANIMATION;
		
		canBeMultiple = false;
		isAlways = false;
	}
	
	public String getXmlTag()
	{
		return "\t<exit_animation>" + animation + "</exit_animation>";
	}
}
