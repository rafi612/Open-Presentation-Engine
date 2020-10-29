package com.opengl.resource;

import com.io.Stream;
import com.jogamp.opengl.GL2;
import com.main.Main;
import com.opengl.graphics.Screen;
import com.opengl.main.Presentation;
import com.project.Project;

public class SlideResource
{
	public static int slides;
	public String imagepath;
	public ImageResource image;
	

	public SlideResource(String path) 
	{
		this.imagepath = path;
		if (Main.args.length < 1) image = new ImageResource(Project.projectlocation + Stream.slash() + path);
		else image = new ImageResource(Project.projectlocation + path);
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
			Presentation.slide.add(new SlideResource(Stream.readXml(path, "slide" + (i + 1), "path")));
		for (int i = 0;i < slides;i++)
			System.out.println(Presentation.slide.get(i).imagepath);
	}
	
	public void render(GL2 gl)
	{
		Screen.drawImage(image, 0, 0, 1280,720);
	}
}
