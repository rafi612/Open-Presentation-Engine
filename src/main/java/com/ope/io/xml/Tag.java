package com.ope.io.xml;

import org.w3c.dom.Element;

public class Tag
{
    Element element;

    private Tag(Element element)
    {
        this.element = element;
    }

    public static Tag fromElement(Element element)
    {
        return new Tag(element);
    }

    public Tag[] getTags()
    {
        Element[] elements = XmlReader.getElements(element.getChildNodes());
        Tag[] tags = new Tag[elements.length];

        for (int i = 0;i < elements.length;i++)
            tags[i] = Tag.fromElement(elements[i]);

        return tags;
    }

    public Tag[] getTags(String name)
    {
        Element[] elements = XmlReader.getElements(element.getElementsByTagName(name));
        Tag[] tags = new Tag[elements.length];

        for (int i = 0;i < elements.length;i++)
            tags[i] = Tag.fromElement(elements[i]);

        return tags;
    }

    public boolean exists(String name)
    {
        return XmlReader.getElements(element.getElementsByTagName(name)).length > 0;
    }

    public String getText()
    {
        return element.getTextContent();
    }

    public String getAttribute(String name)
    {
        return element.getAttribute(name);
    }
    
    public String getTagName()
    {
    	return element.getTagName();
    }

}
