package com.gui.sliderack.atributes;

public class A_Start_Animation extends Atribute 
{
	private static final long serialVersionUID = 1L;

	public A_Start_Animation()
	{
		super(Atribute.Type.START_ANIMATION.getFullName() + ": None");
		canBeMultiple = false;
		isAlways = false;
	}

}
