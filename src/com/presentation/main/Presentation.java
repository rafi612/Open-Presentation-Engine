/* Copyright 2019-2020 by rafi612 */
package com.presentation.main;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.io.Stream;
import com.jogamp.nativewindow.WindowClosingProtocol.WindowClosingMode;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;
import com.main.Main;
import com.presentation.input.Keyboard;
import com.presentation.input.Mouse;
import com.presentation.meneger.StateMeneger;
import com.presentation.resource.SlideResource;
import com.project.Project;

public class Presentation 
{
	public static ArrayList<SlideResource> slide = new ArrayList<SlideResource>();
	
	public static String generalMusic = "null";
	public static boolean fullscreen = false;
	public static int TTSKeyCode = -1;
	
	//info
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final String TITLE = "OPE Presentation";
	
	//okno
	public static GLWindow window;
	public static GLProfile profile;
	public static FPSAnimator animator;
	
	//tech
	public static StateMeneger sm;
	//public static int clock;
	
	//wejscie
	//public static Keyboard keyboard = new Keyboard();
	
	public static void init()
	{
		if (window != null)
			if (window.isVisible())
				stop();
		load();
	    sm = new StateMeneger();
	    
		GLProfile.initSingleton();
	    profile = GLProfile.get(GLProfile.GL2);
	    GLCapabilities cabs = new GLCapabilities(profile);
	       
	    window = GLWindow.create(cabs);
	    window.setSize(WIDTH,HEIGHT);
	    window.setTitle(TITLE);
	    window.addGLEventListener(new EventListener());
	    window.addKeyListener(new Keyboard());
	    window.addMouseListener(new Mouse());
	    window.setDefaultCloseOperation(WindowClosingMode.DISPOSE_ON_CLOSE);
//	    if (Main.args.length < 1)
//	    	window.setFullscreen(Boolean.parseBoolean(Stream.readXml(Project.projectlocation + Stream.slash() + "config.xml", "summary", "fullscreen")));
//	    else
//	    	window.setFullscreen(Boolean.parseBoolean(Stream.readXml("config.xml", "summary", "fullscreen")));
	    window.setFullscreen(fullscreen);
	    window.setVisible(true);
		
	    animator = new FPSAnimator(window,60);
	    animator.start();
	    
	    //System.out.println(window.getWidth() + ","+ window.getHeight());
		
	}
	
	private static void load()
	{
		String path;
		if (Main.args.length < 1)
			path = Project.projectlocation + Stream.slash();
		else path = "";
		
		String config = path + "config.xml";
		
		SlideResource.slides = Integer.parseInt(Stream.readXml(config, "summary", "slides"));
		generalMusic = Stream.readXml(config, "summary", "general_music");
		fullscreen = Boolean.parseBoolean(Stream.readXml(config, "summary", "fullscreen"));
		
		String key = Stream.readXml(config, "summary", "ttskey");
		if (!key.equals("auto"))
			TTSKeyCode = KeyEvent.getExtendedKeyCodeForChar(key.charAt(0));
		else TTSKeyCode = -1;
		
		
	    SlideResource.load();
	}
	
	public static GLProfile getProfile()
	{
		return profile;
	}
	
	public static void stop()
	{
		Presentation.animator.stop();
		Presentation.window.destroy();
	}

}
