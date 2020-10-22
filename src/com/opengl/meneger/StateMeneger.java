package com.opengl.meneger;

import com.jogamp.opengl.GL2;
import com.opengl.states.S_Slide;
import com.opengl.states.State;

public class StateMeneger
{
	static State s;
	
	//stany
	public static final int INTRO_STATE = -1;
	public static final int MENU_STATE = 0;
	//public static final int SETTINGS_STATE = 1;
	
	//slaidy
	public static final int SLIDE = 2;
	
	public StateMeneger()
	{
		changeState(SLIDE);
	}
	
	public static void changeState(int ID)
	{
//		if (ID==INTRO_STATE) s = new S_Intro();
//		if (ID==MENU_STATE) s = new S_Menu();
//		if (ID==SETTINGS_STATE) s = new S_Settings();
//		
		if (ID==SLIDE) s = new S_Slide();
	}
	
	public void update()
	{
		s.update();
	}
	
	public void render(GL2 gl)
	{
		s.render(gl);
	}

}