package com.opengl.states;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;
import com.opengl.graphics.Screen;
import com.opengl.input.Keyboard;
import com.opengl.main.Presentation;
import com.opengl.resource.ImageResource;
import com.opengl.resource.SlideResource;
import com.opengl.states.State;

public class S_Slide extends State 
{
	public int choose;
	ImageResource empty;

	public S_Slide()
	{
		choose = 0;
		empty = new ImageResource(S_Slide.class.getResourceAsStream("/empty.png"));
	}
	
	public void update()
	{
		if (!(SlideResource.slides == 0))
		{
			if (Keyboard.getKeyOnce(KeyEvent.VK_UP))
			{
				if (choose < Presentation.slide.size() - 1) choose++;
				System.out.println(choose);
			}
			if (Keyboard.getKeyOnce(KeyEvent.VK_DOWN))
			{
				if (choose > 0)choose--;
				System.out.println(choose);
			}
			
		}
	}
	
	public void render(GL2 gl)
	{
		if (SlideResource.slides == 0) Screen.drawImage(empty, 0, 0, 1280,720);
		else
		{
			Presentation.slide.get(choose).render(gl);
		}
	}
	

}
