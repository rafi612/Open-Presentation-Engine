/* Copyright 2019-2020 by rafi612 */
package com.presentation.resource;

import java.io.File;
import java.io.FileNotFoundException;

import com.audio.Sound;
import com.graphics.Renderer;
import com.io.Util;
import com.main.Main;
import com.presentation.animation.Animation;
import com.project.Project;

public class SlideResource
{
	public String imagepath,bgpath;
	public String ent_animation,exit_animation;
	
	public ImageResource image,bgimage;
	
	public Animation startanimation,exitanimation;
	
	public String ttspath;
	public Sound tts;
	
	Element[] elements;
	
	int camerax,cameray,cameraw,camerah;
	
	public SlideResource() {} 

	public SlideResource(String path) 
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
	
	private static ImageResource loadImage(String path)
	{
		if (Main.args.length < 1)
			return new ImageResource(Util.projectPath(path));
		else 
			return new ImageResource(path);
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
