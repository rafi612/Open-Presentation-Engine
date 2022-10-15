/* Copyright 2019-2020 by rafi612 */
package com.ope.presentation.slide;

import java.io.IOException;
import java.util.ArrayList;

import org.joml.Vector4f;
import org.xml.sax.SAXException;

import com.ope.graphics.Renderer;
import com.ope.io.Util;
import com.ope.io.xml.Tag;
import com.ope.io.xml.XmlReader;
import com.ope.presentation.animation.Animation;

public class Slide
{
	public Animation animation;
	
	public ArrayList<Element> elements = new ArrayList<Element>();

	public void load(Tag tag) throws IOException, SAXException
	{
		Tag layout = tag.getTags("layout")[0];
		
		//layout
		XmlReader layoutxml = new XmlReader(Util.projectPath(layout.getText()));
		Tag[] elements_ = layoutxml.getTags("element");
		
		for (int i = 0;i < elements_.length;i++)
		{
			Element elem = Element.getElementsByName(elements_[i].getAttribute("type"));
			elem.id = i;
			elem.load(elements_[i]);
			
			elements.add(elem);
		}
		if (tag.exists("animation"))
			animation = Animation.getAnimation(tag.getTags("animation")[0].getAttribute("type"));
		else 
			animation = Animation.getAnimation("none");
	}
	
	public void destroy()
	{
		for (Element element : elements)
			element.destroy();
		
		animation.destroy();
	}
	
	private static final Vector4f bgcolor = new Vector4f(1,1,1,1);
	
	public void render()
	{
		Renderer.frect(0, 0, Renderer.getSize().x,Renderer.getSize().y,bgcolor);
		
		for (Element element : elements)
			element.render();
	}
}
