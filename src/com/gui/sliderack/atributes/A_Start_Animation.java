package com.gui.sliderack.atributes;

public class A_Start_Animation extends Attribute 
{
	private static final long serialVersionUID = 1L;

	public A_Start_Animation()
	{
		super(Attribute.Type.START_ANIMATION.getFullName() + ": None");
		canBeMultiple = false;
		isAlways = false;
	}

	public String getXmlTag()
	{
		return "";
	}
}
