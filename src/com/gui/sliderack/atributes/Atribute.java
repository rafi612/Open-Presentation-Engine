package com.gui.sliderack.atributes;

import javax.swing.JButton;

public class Atribute extends JButton
{
	private static final long serialVersionUID = 1L;
	
	public String name;
	public boolean canBeMultiple,isAlways;
	
	public enum Type
	{
		SLIDE("slide","Slide Layout"),
		TTS("tts","Text To Speech (TTS)"),
		START_ANIMATION("start animation","Start Animation"),
		EXIT_ANIMATION("exit animation","Exit Animation");
		
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

	public Atribute(String name)
	{
		this.name = name;
		this.setText(name);
	}
	
	public static Atribute getAtributeByName(String n)
	{
		if (n.equals("slide")) return new A_Slide();
		else if (n.equals("tts")) return new A_TTS();
		else if (n.equals("start animation")) return new A_Start_Animation();
		else if (n.equals("exit animation")) return new A_Exit_Animation();
		return new Atribute(n);
	}
}
