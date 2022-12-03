/* Copyright 2019-2020 by rafi612 */
package com.ope.core.slide;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

import org.joml.Vector4f;
import org.xml.sax.SAXException;

import com.ope.core.animation.Animation;
import com.ope.graphics.Renderer;
import com.ope.io.xml.Tag;
import com.ope.io.xml.XmlWriter;

public class Slide
{
	public String name;
	public DefaultListModel<String> listModel = new DefaultListModel<>();
	
	public Animation animation;
	public ArrayList<Element> elements = new ArrayList<Element>();
	
	public Slide() {}
	
	public Slide(String name)
	{
		this.name = name;
	}

	public void load(Tag tag) throws IOException, SAXException
	{
		name = tag.getAttribute("name");
		
		Tag layout = tag.getTags("layout")[0];
		
		Tag[] elements_ = layout.getTags("element");
		
		for (int i = 0;i < elements_.length;i++)
		{
			Element elem = Element.getElementsByName(elements_[i].getAttribute("type"));
			elem.id = i;
			elem.load(elements_[i]);
			
			elements.add(elem);
			listModel.addElement(elem.name);
		}
		if (tag.exists("animation"))
			animation = Animation.getAnimation(tag.getTags("animation")[0].getAttribute("type"));
		else 
			animation = Animation.getAnimation("none");
	}
	
	public void saveXml(XmlWriter xml)
	{
		xml.openTag("slide", "name=" + name);
		
		xml.openTag("layout");
		for (Element element : elements)
			element.save(xml);
		xml.closeTag();
		
		xml.closeTag();
	}
	
	public void destroy()
	{
		for (Element element : elements)
			element.destroy();
		
		if (animation != null)
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
