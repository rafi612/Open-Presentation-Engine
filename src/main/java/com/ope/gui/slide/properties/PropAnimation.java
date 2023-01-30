package com.ope.gui.slide.properties;

import javax.swing.JComboBox;

import com.ope.core.animation.Animation;
import com.ope.core.slide.Slide;
import com.ope.gui.slide.SlideList;

public class PropAnimation extends SlideProperty
{
	private static final long serialVersionUID = 1L;
	
	private JComboBox<String> comboBox;

	public PropAnimation(SlideList slideList) 
	{
		super("Animation",slideList);
		comboBox = new JComboBox<>(Animation.getAnimationsList());
		
		comboBox.addItemListener(event -> {
			slideList.getCurrentSlide().animation = 
					Animation.getAnimation((String)comboBox.getSelectedItem());
		});
		add(comboBox);
	}
	
	@Override
	public void onSlideChange(Slide slide)
	{
		comboBox.setSelectedItem(slide.animation.getName());
	}

}
