package com.ope.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.ope.presentation.slide.Element;

public class SlideList extends JPanel implements ActionListener,ListSelectionListener
{
	private static final long serialVersionUID = 1L;
	
	public SlideCreator sc;
	public ArrayList<Slide> slides = new ArrayList<>();
	
	private JList<String> list;
	private DefaultListModel<String> listModel;
	private JButton add,up,down;
	
	private int currentSlide;
	
	public static class Slide 
	{
		public ArrayList<Element> elements = new ArrayList<>();
		public DefaultListModel<String> listModel = new DefaultListModel<>();
	}

	public SlideList(SlideCreator sc)
	{
		this.sc = sc;
		sc.slideList = this;
		setPreferredSize(new Dimension(215,0));
		
		setLayout(new BorderLayout());
		
		list = new JList<>(listModel = new DefaultListModel<>()); 
		list.addListSelectionListener(this);
		add(new JScrollPane(list),BorderLayout.CENTER);
		
		add = new JButton("+");
		add.addActionListener(this);
		
		up = new JButton("\u25B2");
		up.addActionListener(this);
		
		down = new JButton("\u25BC");
		down.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(add);
		buttonPanel.add(up);
		buttonPanel.add(down);
		
		add(buttonPanel,BorderLayout.SOUTH);
	}
	
	public Slide getCurrentSlide()
	{
		return slides.get(currentSlide);
	}
	
	public void addSlide(Slide slide)
	{
		slides.add(slide);
		listModel.addElement("Slide " + (listModel.size() + 1));
	}
	
	public void build(String path) throws IOException
	{
		
	}
	
	public void clear()
	{
		for (Slide slide : slides)
			for (Element element : slide.elements)
				element.destroy();
		
		listModel.clear();
		
		slides.clear();
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		var source = e.getSource();
		
		if (source == add)
		{
			addSlide(new Slide());
		}
	}


	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		int index = list.getSelectedIndex();
		currentSlide = index;
		if (index == -1)
		{
			sc.slideloaded = false;
			sc.setEnabled(false);
		}
		else
		{
			sc.slideloaded = true;
			sc.setEnabled(true);
			sc.list.setModel(slides.get(index).listModel);
		}
	}
	
	public SlideCreator getSlideCreator()
	{
		return sc;
	}

}
