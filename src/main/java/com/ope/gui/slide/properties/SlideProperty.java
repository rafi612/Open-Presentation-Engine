package com.ope.gui.slide.properties;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.ope.core.slide.Slide;
import com.ope.gui.slide.SlideList;

public class SlideProperty extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	protected SlideList slideList;

	public SlideProperty(String name,SlideList slideList)
	{
		this.slideList = slideList;
        setBorder(BorderFactory.createTitledBorder(name));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void onSlideChange(Slide slide) {}
	
	@Override
	public Dimension getMaximumSize()
	{
		return new Dimension(Integer.MAX_VALUE,getPreferredSize().height);
	}

}
