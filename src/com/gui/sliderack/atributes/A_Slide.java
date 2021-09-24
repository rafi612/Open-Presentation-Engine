package com.gui.sliderack.atributes;

public class A_Slide extends Atribute
{
	private static final long serialVersionUID = 1L;
	
	public A_Slide()
	{
		super(Atribute.Type.SLIDE.getFullName() + ": None");
		canBeMultiple = false;
		isAlways = true;
	}

}
