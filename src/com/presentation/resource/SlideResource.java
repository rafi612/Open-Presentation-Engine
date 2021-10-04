/* Copyright 2019-2020 by rafi612 */
package com.presentation.resource;

import java.io.File;

import com.audio.Sound;
import com.io.Stream;
import com.jogamp.opengl.GL2;
import com.main.Main;
import com.presentation.animation.Animation;
import com.presentation.graphics.Renderer;
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
		if (Main.args.length < 1) 
			image = new ImageResource(Project.projectlocation + Stream.slash() + path);
		else 
			image = new ImageResource(Project.projectlocation + path);
	}
	
	public void setSlideImage(String path)
	{
		this.imagepath = path;
		if (path.equals("null")) return;
		if (Main.args.length < 1) 
			image = new ImageResource(Project.projectlocation + Stream.slash() + path);
		else 
			image = new ImageResource(Project.projectlocation + path);
	}
	
	public void setBg(String path)
	{
		bgpath = path;
		if (path.equals("null")) return;
		if (Main.args.length < 1)
			bgimage = new ImageResource(Project.projectlocation + Stream.slash() + path);
		else 
			bgimage = new ImageResource(path);
	}
	
	public void setTTS(String path)
	{
		ttspath = path;
		if (path.equals("null")) return;
		File f = new File(Project.projectlocation + Stream.slash() + path);
		File f1 = new File(path);
		if (f.exists() || f1.exists())
		{
			if (Main.args.length < 1)
				tts = new Sound(Project.projectlocation + Stream.slash() + path);
			else 
				tts = new Sound(path);
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
	
	public void render(GL2 gl)
	{
		if (bgimage != null) Renderer.drawImage(bgimage, 0 - camerax, 0 - cameray, 1280 - cameraw,720 - camerah);
		if (image != null) Renderer.drawImage(image, 0 - camerax, 0 - cameray, 1280 - cameraw,720 - camerah);
	}
}
