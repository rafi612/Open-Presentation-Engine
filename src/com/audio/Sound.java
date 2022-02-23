package com.audio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Sound 
{	
	private boolean isPlay = false;
	
	private Player player;
	
	String path;
	
	InputStream input;
	
	public Sound(String path) throws FileNotFoundException
	{
		this(new FileInputStream(path));
	}
	
	public Sound(InputStream input)
	{
		this.input = input;
	}
	
	public boolean getPlay()
	{
		return isPlay;
	}
	
	public static void init()
	{
		
	}
	
	public void play()
	{
		try 
		{
			player = new Player(input);
			player.play();
		} 
		catch (JavaLayerException e)
		{
			e.printStackTrace();
		}
	}
	
	public void playInBg()
	{
		isPlay = true;
		
		Thread thread = new Thread()
		{
			public void run()
			{
				play();
			}
			
		};
		thread.start();

		
	}
	
	public void stop()
	{
		isPlay = false;
		player.close();
	}

}
