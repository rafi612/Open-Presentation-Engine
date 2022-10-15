package com.ope.io.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class XmlReader
{
    Document doc;

    public XmlReader(String path) throws IOException, SAXException
    {
        this(new FileInputStream(path));
    }

    public XmlReader(InputStream input) throws IOException, SAXException
    {
        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(input);
        }
        catch (ParserConfigurationException e)
        {
            throw new RuntimeException(e);
        }
    }

    static Element[] getElements(NodeList nList)
    {
        ArrayList<Element> elements = new ArrayList<>();

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

    public Tag[] getTags(String name)
    {
        Element[] elements = getElements(doc.getElementsByTagName(name));
        Tag[] tags = new Tag[elements.length];

        for (int i = 0;i < elements.length;i++)
            tags[i] = Tag.fromElement(elements[i]);

        return tags;
    }

    public boolean exists(String name)
    {
        return XmlReader.getElements(doc.getElementsByTagName(name)).length > 0;
    }

}
