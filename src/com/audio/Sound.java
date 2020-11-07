package com.audio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Sound 
{	
	private boolean isPlay = false;
	
	private Player player;
	
	String path;
	
	public Sound(String path)
	{
		this.path = path;
	}
	
	public boolean getPlay()
	{
		return isPlay;
	}
	
	public void play()
	{
		try 
		{
			player = new Player(new FileInputStream(path));
			player.play();
		} 
		catch (JavaLayerException | FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public void playInBg()
	{
		isPlay = true;
		
		Thread thread = new Thread()
		{
			@SuppressWarnings("deprecation")
			public void run()
			{
				try 
				{
					player = new Player(new FileInputStream(path));
					player.play();
					
					player.close();
					stop();
				} 
				catch (JavaLayerException | FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
			
		};
		thread.start();

		isPlay = false;
	}
	
	public void stop()
	{
		isPlay = false;
		player.close();
	}

}
