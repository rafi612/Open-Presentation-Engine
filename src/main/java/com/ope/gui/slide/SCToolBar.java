package com.ope.gui.slide;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import com.ope.core.slide.Element;
import com.ope.io.Util;

public class SCToolBar extends JToolBar implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private SlideCreator sc;
	
	public JButton image,text;
	
	public SCToolBar(SlideCreator sc)
	{
		this.sc = sc;
		
		setFloatable(false);
		
		image = addButton("Add image","/icons/toolbar/image.png");
		text = addButton("Add text","/icons/toolbar/text.png");	
		addSeparator();
	}
	
	private String getNameWithNum(String name)
	{
		int repeat = 0;
		for (Element element : sc.getCurrentSlide().elements)
			if (element.name.startsWith(name))
				repeat++;
		
		return repeat == 0 ? name : name + " #" + repeat;
	}
	
	private JButton addButton(String text,String iconpath)
	{
		JButton item = new JButton(new ImageIcon(Util.loadIcon(iconpath)));
		item.setToolTipText(text);
		item.addActionListener(this);
		add(item);
		return item;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		var source = e.getSource();
		
		if (source == image)
			sc.addElement(Element.getElementsByName("Image"),getNameWithNum("Image"));
	}
}
