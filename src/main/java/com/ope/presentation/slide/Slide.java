/* Copyright 2019-2020 by rafi612 */
package com.ope.presentation.slide;

import java.util.ArrayList;

import org.joml.Vector4f;

import com.ope.graphics.Renderer;
import com.ope.io.Util;
import com.ope.io.xml.XmlParser;
import com.ope.presentation.animation.Animation;

public class Slide
{
	public Animation animation;
	
	public ArrayList<Element> elements = new ArrayList<Element>();

	public void load(org.w3c.dom.Element element)
	{
		var layout = XmlParser.getElementsFromElementByName(element,"layout")[0];
		
		//layout
		XmlParser layoutxml = new XmlParser(Util.projectPath(layout.getTextContent()));
		var elements_ = XmlParser.getElements(layoutxml.getElementsByTagName("element"));
		
		for (int i = 0;i < elements_.length;i++)
		{
			Element elem = Element.getElementsByName(elements_[i].getAttribute("type"));
			elem.id = i;
			elem.load(elements_[i]);
			
			elements.add(elem);
		}
		if (XmlParser.existsFromElementByName(element, "animation"))
			animation = Animation.getAnimation(XmlParser.getElementsFromElementByName(element,"animation")[0].getAttribute("type"));
		else animation = Animation.getAnimation("none");
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
