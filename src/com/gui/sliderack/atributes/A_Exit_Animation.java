package com.gui.sliderack.atributes;

public class A_Exit_Animation extends Attribute 
{
	private static final long serialVersionUID = 1L;

	public A_Exit_Animation()
	{
		super(Attribute.Type.EXIT_ANIMATION.getFullName() + ": None");
		canBeMultiple = false;
		isAlways = false;
	}

	public String getXmlTag()
	{
		return "";
	}
}
