package com.ope.gui.slide;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.ope.core.slide.Slide;
import com.ope.gui.slide.properties.PropAnimation;
import com.ope.gui.slide.properties.SlideProperty;

public class SCProperties extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<SlideProperty> properties = new ArrayList<>();
	
	public SCProperties(SlideList slideList) 
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		addProperty(new PropAnimation(slideList));
	}
	
	private void addProperty(SlideProperty property)
	{
		properties.add(property);
		add(property);
	}
	
	public void onSlideChange(Slide slide)
	{
		for (SlideProperty property : properties)
			property.onSlideChange(slide);
	}

}
