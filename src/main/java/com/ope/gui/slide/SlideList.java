package com.ope.gui.slide;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.xml.sax.SAXException;

import com.ope.core.slide.Slide;
import com.ope.gui.MenuBar;
import com.ope.io.Util;
import com.ope.io.xml.Tag;
import com.ope.io.xml.XmlReader;
import com.ope.io.xml.XmlWriter;
import com.ope.main.Main;

public class SlideList extends JPanel implements ActionListener,ListSelectionListener
{
	private static final long serialVersionUID = 1L;
	
	public SlideCreator sc;
	private MenuBar parentmenu;
	
	public ArrayList<Slide> slides = new ArrayList<>();
	
	private JList<String> list;
	private DefaultListModel<String> listModel;
	private JButton add,delete,up,down;
	
	private JMenuItem add_menu,delete_menu,rename_menu;
	
	private int currentSlide;
	
	public SlideList(MenuBar parentmenu)
	{
		this.parentmenu = parentmenu;
		
		sc = new SlideCreator(this);
		
		setPreferredSize(new Dimension(215,0));
		
		setLayout(new BorderLayout());
		
		list = new JList<>(listModel = new DefaultListModel<>()); 
		list.addListSelectionListener(this);
		add(new JScrollPane(list),BorderLayout.CENTER);
		
		add = new JButton("+");
		add.addActionListener(this);
		
		delete = new JButton("-");
		delete.addActionListener(this);
		
		up = new JButton("\u25B2");
		up.addActionListener(this);
		
		down = new JButton("\u25BC");
		down.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(add);
		buttonPanel.add(delete);
		buttonPanel.add(up);
		buttonPanel.add(down);
		
		add_menu = new JMenuItem("Add slide");
		add_menu.addActionListener(this);
		delete_menu = new JMenuItem("Delete slide");
		delete_menu.addActionListener(this);
		rename_menu = new JMenuItem("Rename");
		rename_menu.addActionListener(this);
		
		JPopupMenu popup = new JPopupMenu();
		popup.add(add_menu);
		popup.add(delete_menu);
		popup.add(rename_menu);
		list.setComponentPopupMenu(popup);
		
		add(buttonPanel,BorderLayout.SOUTH);
	}
	
	public Slide getCurrentSlide()
	{
		return slides.get(currentSlide);
	}
	
	public void addSlide(Slide slide)
	{
		slides.add(slide);
		listModel.addElement(slide.name);
	}
	
	public void createNewSlide()
	{
		addSlide(new Slide("Slide " + (listModel.size() + 1)));
	}
	
	public void deleteSlide(int id)
	{
		Slide slide = slides.get(id);
		slide.destroy();
		
		listModel.remove(id);
		slides.remove(id);
	}
	
	public void load(String path)
	{
		try 
		{
			XmlReader xml = new XmlReader(path);
			
			Tag[] slides = xml.getTags("slide");
			
			for (Tag slidetag : slides)
			{				
				Slide slide = Slide.load(slidetag);
				
				addSlide(slide);
			}
			
			Tag global = xml.getTags("global")[0];
			
			parentmenu.fullscreen.setSelected(Boolean.parseBoolean(global.getAttribute("fullscreen")));
		} 
		catch (IOException | SAXException e) 
		{
			JOptionPane.showMessageDialog(Main.frame, "Error loading slides: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}

	}
	
	public void build(String path) throws IOException
	{
		XmlWriter xml = new XmlWriter();
		xml.openTag("project");
		
		for (Slide slide : slides)
			slide.saveXml(xml);
		
		xml.addTag("global", "fullscreen=" + parentmenu.fullscreen.isSelected());
		xml.closeTag();
		
		Util.saveFile(path, xml.get());
	}
	
	public void clear()
	{			
		for (int i = slides.size()-1; i >= 0;i--)
			deleteSlide(i);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		var source = e.getSource();
		
		if (source == add || source == add_menu)
		{
			createNewSlide();
			list.setSelectedIndex(listModel.size() - 1);
		}
		
		if (source == delete || source == delete_menu)
		{
			if (check()) return;
			
			deleteSlide(list.getSelectedIndex());
		}
		
		if (source == up || source == down)
		{
			if (check()) return;
			
			int new_ = list.getSelectedIndex() + (source == down ? 1 : -1);
			int old = list.getSelectedIndex();
			//swap func
			swaplist(old,new_);
			//set selection
			list.setSelectedIndex(new_);
		}
		
		if (source == rename_menu)
		{
			if (check()) return;
			
			String choose = JOptionPane.showInputDialog(Main.frame,"Enter new element name:","Rename",JOptionPane.QUESTION_MESSAGE);
			if (choose != null)
			{
				int index = list.getSelectedIndex();
				Slide slide = slides.get(index);
				
				slide.name = choose;
				listModel.set(index, choose);
			}
		}
	}
	
	private boolean check()
	{
		if (list.getSelectedIndex() == -1)
		{
			JOptionPane.showMessageDialog(Main.frame, "No selected element", "Error", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		return false;
	}
	
	private void swaplist(int old,int new_)
	{
		//check out of bounds
		if (new_ < 0 || new_ > slides.size()-1) return;
		
		//swap in elements arraylist
		Collections.swap(slides, old, new_);
		
		//swap in JList
		String aObject = listModel.getElementAt(old);
		String bObject = listModel.getElementAt(new_);
		listModel.set(old, bObject);
		listModel.set(new_, aObject);
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
			Slide slide = slides.get(index);
			sc.slideloaded = true;
			sc.setEnabled(true);
			sc.list.setModel(slide.listModel);
			
			sc.scProperties.onSlideChange(slide);
		}
	}
	
	public SlideCreator getSlideCreator()
	{
		return sc;
	}
	
	@Override
	public void setEnabled(boolean enabled)
	{
		enableComponents(this, enabled);
		enableComponents(list.getComponentPopupMenu(), enabled);
	}
	
	private void enableComponents(Container container, boolean enable)
	{
		Component[] components = container.getComponents();
		for (Component component : components) 
		{
			component.setEnabled(enable);
			if (component instanceof Container) 
				enableComponents((Container)component, enable);
		}
	}

}
