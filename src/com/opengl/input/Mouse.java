package com.opengl.input;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.main.Main;
import com.opengl.main.Presentation;

public class Mouse implements MouseListener
{

	public static int X,Y,Xpixel,Ypixel;
	public static boolean button_left,button_right,button_clicked_left,button_clicked_right;
	private static boolean b1 = false,b2 = false;
	
	public static void update() 
	{
		//Klikanie lewym
		if(!b1 && button_left)
		{
			b1 = true;
			button_clicked_left = true;
		}
		else
		{
			button_clicked_left = false;
		}
		if(!button_left) 
		{
			b1 = false;
		}
		
		//Klikanie prawym
		if(!b2 && button_right)
		{
			b2 = true;
			button_clicked_right = true;
		}
		else
		{
			button_clicked_right = false;
		}
		if(!button_right)
		{
			b2 = false;
		}
	
	}
	
	@Override
	public void mouseDragged(MouseEvent e) 
	{
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		X = e.getX();
		Y = e.getY();
		Xpixel = (int)((float)X*(1280 / ((float)Presentation.window.getWidth())));
		Ypixel = (int)((float)Y*(720 / ((float)Presentation.window.getHeight())));
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{

		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1) 
		{//Lewy
			button_left = true;
		}
		
		if(e.getButton() == MouseEvent.BUTTON3) 
		{//Prawy
			button_right = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1) 
		{//Lewy
			button_left = false;
		}
		
		if(e.getButton() == MouseEvent.BUTTON3) 
		{//Prawy
			button_right = false;
		}
	}


	@Override
	public void mouseWheelMoved(MouseEvent arg0)
	{

		
	}

}
