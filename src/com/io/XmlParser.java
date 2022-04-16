package com.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlParser 
{
	String path;
	
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dBuilder;
	Document doc;
	
	public XmlParser(String path)
	{
		this.path = path;
		try 
		{
			File inputFile = new File(path);
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
		} 
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			e.printStackTrace();
		}

	}
	
	public NodeList getElementsByTagName(String ElementsByTagName)
	{
		return doc.getElementsByTagName(ElementsByTagName);
	}
	
	public static Element[] getElements(NodeList nList)
	{
		ArrayList<Element> elements = new ArrayList<Element>();
		
		for (int i = 0; i < nList.getLength(); i++)
		{
			Node nNode = nList.item(i);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;
			
				elements.add(eElement);
			}
		}
		
		Element[] earray = new Element[elements.size()];
		
		for (int i = 0;i < earray.length;i++)
			earray[i] = elements.get(i);
		
		return earray;
	}

	public static Element[] getElementsFromElement(Element element)
	{
		return getElements(element.getChildNodes());
		
	}
	
	public static Element[] getElementsFromElementByName(Element element,String name)
	{
		return getElements(element.getElementsByTagName(name));
		
	}
}