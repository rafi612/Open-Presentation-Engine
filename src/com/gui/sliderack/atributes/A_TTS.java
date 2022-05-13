package com.gui.sliderack.atributes;

public class A_TTS extends Attribute 
{
	private static final long serialVersionUID = 1L;

	public A_TTS()
	{
		super(Attribute.Type.TTS.getFullName());
		type = Attribute.Type.TTS;
		
		canBeMultiple = false;
		isAlways = false;
	}
	
	public String getXmlTag()
	{
		return "<tts></tts>";
	}

}
