package com.gui.sliderack.atributes;

public class A_TTS extends Atribute 
{
	private static final long serialVersionUID = 1L;

	public A_TTS()
	{
		super(Atribute.Type.TTS.getFullName());
		canBeMultiple = false;
		isAlways = false;
	}

}
