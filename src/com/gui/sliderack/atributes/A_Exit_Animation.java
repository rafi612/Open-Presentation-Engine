package com.gui.sliderack.atributes;

public class A_Exit_Animation extends Atribute 
{
	private static final long serialVersionUID = 1L;

	public A_Exit_Animation()
	{
		super(Atribute.Type.EXIT_ANIMATION.getFullName() + ": None");
		canBeMultiple = false;
		isAlways = false;
	}

}
