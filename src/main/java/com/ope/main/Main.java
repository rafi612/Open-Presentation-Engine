/* Copyright 2019-2020 by rafi612 */
package com.ope.main;

import com.ope.gui.MainWindow;
import com.ope.io.Config;
import com.ope.project.Project;
import com.ope.viewer.Presentation;

public class Main
{
	public static MainWindow frame;
	
	public static void main(String[] args)
	{
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("jdk.gtk.version", "2");
		
		if (args.length < 1)
		{
			frame = new MainWindow();
			
			Config.loadSettings();
			Project.unloadProject();
			
			frame.setVisible(true);
			frame.slideList.getSlideCreator().canvasLoop();
		}
		else 
			Presentation.start(args[0]);
	}
}