/* Copyright 2019-2020 by rafi612 */
package com.presentation.resource;

import com.io.Stream;
import com.jogamp.opengl.GL2;
import com.main.Main;
import com.presentation.animation.Animation;
import com.presentation.graphics.Screen;
import com.presentation.main.Presentation;
import com.project.Project;

public class SlideResource
{
	public static int slides;
	public String imagepath,bgpath;
	public String ent_animation,exit_animation;
	public ImageResource image,bgimage;
	public Animation startanimation,exitanimation;
	
	public String ttspath;
	
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
	}
	
	public void setStartAnimation(String path)
	{
		startanimation = Animation.getAnimation(path);
	}
	
	public void setExitAnimation(String path)
	{
		exitanimation = Animation.getAnimation(path);
	}


	public static void load()
	{
		String path = "";
		if (Main.args.length < 1)
		{
			path = Project.projectlocation + Stream.slash() + "config.xml";
		}
		else
			path = Main.args[0];
		
		for (int i = 0;i < slides;i++)
		{
			Presentation.slide.add(new SlideResource());
			Presentation.slide.get(i).setSlideImage(Stream.readXml(path, "slide" + (i + 1), "path"));
			Presentation.slide.get(i).setBg(Stream.readXml(path, "slide" + (i + 1), "bg"));
			Presentation.slide.get(i).setTTS(Stream.readXml(path, "slide" + (i + 1), "tts"));
			Presentation.slide.get(i).setStartAnimation(Stream.readXml(path, "slide" + (i + 1), "ent_ani"));
			Presentation.slide.get(i).setExitAnimation(Stream.readXml(path, "slide" + (i + 1), "exi_ani"));
		}
	}
	
	public void render(GL2 gl)
	{
		if (bgimage != null) Screen.drawImage(bgimage, 0, 0, 1280,720);
		if (image != null) Screen.drawImage(image, 0, 0, 1280,720);
	}
}
