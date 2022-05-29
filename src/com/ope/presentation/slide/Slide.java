/* Copyright 2019-2020 by rafi612 */
package com.ope.presentation.slide;

import java.io.File;
import java.io.FileNotFoundException;

import com.ope.audio.Sound;
import com.ope.graphics.Renderer;
import com.ope.graphics.Texture;
import com.ope.io.Util;
import com.ope.main.Main;
import com.ope.presentation.animation.Animation;

public class Slide
{
	public String imagepath,bgpath;
	public String ent_animation,exit_animation;
	
	public Texture image,bgimage;
	
	public Animation startanimation,exitanimation;
	
	public String ttspath;
	public Sound tts;
	
	Element[] elements;
	
	int camerax,cameray,cameraw,camerah;
	
	public Slide() {} 

	public Slide(String path) 
	{
		this.imagepath = path;
		image = loadImage(path);
	}
	
	public void setSlideImage(String path)
	{
		this.imagepath = path;
		if (path.equals("null")) return;
		image = loadImage(path);
	}
	
	public void setBg(String path)
	{
		bgpath = path;
		if (path.equals("null")) return;
		bgimage = loadImage(path);
	}
	
	private static Texture loadImage(String path)
	{
		if (Main.args.length < 1)
			return new Texture(Util.projectPath(path));
		else 
			return new Texture(path);
	}
	
	public void setTTS(String path)
	{
		ttspath = path;
		if (path.equals("null")) return;
		File f = new File(Util.projectPath(path));
		File f1 = new File(path);
		if (f.exists() || f1.exists())
		{
			try {
				if (Main.args.length < 1)
					tts = new Sound(Util.projectPath(path));
				else 
					tts = new Sound(path);
				//tts.setPitch(2000f);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setStartAnimation(String path)
	{
		startanimation = Animation.getAnimation(path);
	}
	
	public void setExitAnimation(String path)
	{
		exitanimation = Animation.getAnimation(path);
	}
	
	public void render()
	{
		if (bgimage != null) Renderer.drawImage(bgimage, 0 - camerax, 0 - cameray, 1280 - cameraw,720 - camerah);
		if (image != null) Renderer.drawImage(image, 0 - camerax, 0 - cameray, 1280 - cameraw,720 - camerah);
	}
}
