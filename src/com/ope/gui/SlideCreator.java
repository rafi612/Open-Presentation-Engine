package com.ope.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

import static org.lwjgl.opengl.GL11.*;

import com.ope.graphics.Renderer;
import com.ope.graphics.Texture;
import com.ope.io.Util;
import com.ope.io.XmlParser;
import com.ope.main.Main;
import com.ope.presentation.slide.Element;
import com.ope.project.Project;

public class SlideCreator extends JPanel implements ActionListener,MouseMotionListener,MouseListener
{
	private static final long serialVersionUID = 1L;
	
    public ArrayList<JButton> actions = new ArrayList<JButton>();
    
    public static final int WIDTH = 1280,HEIGHT = 720;
    
    private DefaultListModel<String> listModel;
    public JList<String> list;
    public JPanel listpanel;
    
    private JButton newelement,edit,up,down,rename,delete;
    private JLabel position;
    
    public int elementsint = 0;
    public int xPixel,yPixel;
    
    private Texture canvasimage;
    
    public boolean slideloaded = false;
    public boolean dragged = false;
    
    public int currentColidedID = -1,currentMovedID = -1;
    
    public ArrayList<Element> elements;
    
    private AWTGLCanvas canvas;
	
	public SlideCreator() 
	{
		setLayout(new BorderLayout());
		
		elements = new ArrayList<Element>();
		
		//actions
        JPanel buttons = new JPanel();
        
        buttons.setBorder(BorderFactory.createTitledBorder(""));
        buttons.setToolTipText("Necessary actions");
        
        actions.add(new JButton("Create new slide"));
        actions.add(new JButton("Open slide"));
        actions.add(new JButton("Save slide"));
        actions.add(new JButton("Discard slide"));
        
        for (int i = 0;i < actions.size(); i++)
        	actions.get(i).addActionListener(this);
        
        for (int i = 0;i < actions.size(); i++)
        	buttons.add(actions.get(i));
        
        add(buttons,BorderLayout.SOUTH);
        
        //list
        listpanel = new JPanel();
        listpanel.setLayout(new BorderLayout());

        listModel = new DefaultListModel<String>();
        list = new JList<String>(listModel);
        
        newelement = new JButton("+");
        newelement.addActionListener(this);
        
        edit = new JButton("Edit");
        edit.addActionListener(this);
        
        up = new JButton("\u25B2");
        up.addActionListener(this);
        
        down = new JButton("\u25BC");
        down.addActionListener(this);
        
        rename = new JButton("Rename");
        rename.addActionListener(this);
        
        delete = new JButton("Delete");
        delete.addActionListener(this);
        
        JPanel panel1 = new JPanel();
        panel1.add(newelement);
        panel1.add(edit);
        panel1.add(up);
        panel1.add(down);
        
        JPanel panel2 = new JPanel();
        panel2.add(rename);
        panel2.add(delete);
        
        JPanel bpanel = new JPanel();
        bpanel.setLayout(new BorderLayout());
        bpanel.add(panel1,BorderLayout.NORTH);
        bpanel.add(panel2,BorderLayout.SOUTH);
        
        listpanel.add(new JScrollPane(list));
        listpanel.add(bpanel,BorderLayout.SOUTH);
        listpanel.setPreferredSize(new Dimension(200,0));
		enableComponents(listpanel, false);
        
        add(listpanel,BorderLayout.WEST);
        
        position = new JLabel("X: " + xPixel + " Y:" + yPixel);
        
        initCanvas();
	}
	
	public void initCanvas()
	{
	    JPanel cpanel = new JPanel();
	    cpanel.setLayout(new BorderLayout());
	    
	    JPanel ppanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    ppanel.add(position);
	    cpanel.add(ppanel,BorderLayout.SOUTH);

        GLData data = new GLData();
        data.swapInterval = 1;
        data.profile = GLData.Profile.CORE;
        
        cpanel.add(canvas = new AWTGLCanvas(data) 
        {
            private static final long serialVersionUID = 1L;
            public void initGL() 
            {
                System.out.println("OpenGL version: " + effective.majorVersion + "." + effective.minorVersion + " (Profile: " + effective.profile + ")");
                GL.createCapabilities();
                
        		if (Renderer.isFallback())
        			System.out.println("Using fallback renderer");
                
                init();
            }
            public void paintGL()
            {
            	display();
                swapBuffers();
            }
        });
        
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        
        add(cpanel);
        
	}
	
	public void canvasLoop()
	{ 
        Runnable renderLoop = new Runnable()
        {
			public void run() 
			{
				if (!canvas.isValid())
					return;
				canvas.render();
				SwingUtilities.invokeLater(this);
			}
		};
		SwingUtilities.invokeLater(renderLoop);
	}

	public void display()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(1,1,1,1);
		
		reshape(canvas.getWidth(),canvas.getHeight());
		
		if (slideloaded)
		{		
			if (elements.size() == 0)
				Renderer.drawImage(canvasimage,0, 0,WIDTH, HEIGHT);
		}
		
		for (Element element : elements)
			element.update(this);
		
		//setting moved element as selected on list
		list.setSelectedIndex(currentMovedID);
		
		//render
		for (int i = 0;i < elements.size();i++)
			elements.get(i).render();
	}
	
	private float sx,sy;
	
	public void init() 
	{
		System.out.println("Canvas init");
		
		Renderer.init();
		
		AffineTransform transform = canvas.getGraphicsConfiguration().getDefaultTransform();
		sx = (float) transform.getScaleX();
		sy = (float) transform.getScaleY();
		
		canvasimage = new Texture(SlideCreator.class.getResourceAsStream("/images/canvas.png"));
		
		glClearColor(1,1,1,1);
		
		glEnable(GL_TEXTURE_2D);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	

	public void reshape(int width, int height) 
	{
		if (Renderer.isFallback())
			Renderer.fallbackResize();
		
		glViewport(0,0,(int)(width * sx),(int)(height * sy));
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		//new element
		if (source == newelement)
		{
			JComboBox<String> combo = new JComboBox<String>(Element.types);
			JTextField textfield = new JTextField();
			JComponent[] c = {new JLabel("Choose Element:"),combo,new JLabel("Name:"),textfield};
			
			JOptionPane.showConfirmDialog(Main.frame,c, "New Element", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
			
			elementsint++;
			
			String elementname;
			
			//getting name
			if (textfield.getText().equals(""))
				elementname = combo.getSelectedItem().toString();
			else
				elementname = textfield.getText();
			
			Element elem = Element.getElementsByName(combo.getSelectedItem().toString());
			elem.id = elements.size();
		    elem.name = elementname;
		    elem.frame();
		    
			//get and add element
		    elements.add(elem);
		    listModel.addElement(elementname);
		}
		//new slide
		if (source == actions.get(0))
		{
			int choose = 0;
			if (slideloaded)
				 choose = JOptionPane.showConfirmDialog(Main.frame,"Are you sure to discard this slide?", "Discard", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if (choose == 0)
			{
				listModel.clear();
				elements.clear();
				if (canvas == null)
					initCanvas();
				
				enableComponents(listpanel, true);
				
				savediscardenable(true);
				
				slideloaded = true;
			}
		}
		//discard slide
		if (source == actions.get(3))
		{
			discarddialog();
		}
		//edit
		if (source == edit)
		{
			if (list.getSelectedIndex() == -1)
				JOptionPane.showMessageDialog(Main.frame, "No selected element", "Error", JOptionPane.ERROR_MESSAGE);
			else
				elements.get(list.getSelectedIndex()).frame();
		}
		//up
		if (source == up)
		{
			if (list.getSelectedIndex() == -1)
				JOptionPane.showMessageDialog(Main.frame, "No selected element", "Error", JOptionPane.ERROR_MESSAGE);
			else
			{
				int new_ = list.getSelectedIndex() - 1;
				int old = list.getSelectedIndex();
				//swap func
				swaplist(old,new_);
				//set selection
				list.setSelectedIndex(new_);
				
				refreshElementsID();
			}
		}
		//down
		if (source == down)
		{
			if (list.getSelectedIndex() == -1)
				JOptionPane.showMessageDialog(Main.frame, "No selected element", "Error", JOptionPane.ERROR_MESSAGE);
			else
			{
				int new_ = list.getSelectedIndex() + 1;
				int old = list.getSelectedIndex();
				//swapfunc
				swaplist(old,new_);
				//set selection
				list.setSelectedIndex(new_);
				
				refreshElementsID();
			}
		}
		//delete
		if (source == delete)
		{
			if (list.getSelectedIndex() == -1)
				JOptionPane.showMessageDialog(Main.frame, "No selected element", "Error", JOptionPane.ERROR_MESSAGE);
			else
			{
				int choose = JOptionPane.showConfirmDialog(Main.frame,"Are you sure to delete " + listModel.get(list.getSelectedIndex()) + "?", "Delete", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if (choose == 0)
				{
					int index = list.getSelectedIndex();
					elements.get(index).destroy();
					
					elements.remove(index);
					listModel.remove(index);
				}
				
				refreshElementsID();
			}
		}
		//rename
		if (source == rename)
		{
			if (list.getSelectedIndex() == -1)
				JOptionPane.showMessageDialog(Main.frame, "No selected element", "Error", JOptionPane.ERROR_MESSAGE);
			else
			{
				String choose = JOptionPane.showInputDialog(Main.frame,"Enter new element name:","Rename",JOptionPane.QUESTION_MESSAGE);
				if (choose != null)
				{
					listModel.set(list.getSelectedIndex(), choose);
					elements.get(list.getSelectedIndex()).name = choose;
				}
			}
		}
		//save slide
		if (source == actions.get(2))
		{
			savedialog();
		}
		//open slide
		if (source == actions.get(1))
		{
			//check discard
			if (discarddialog() != 0) return;
			
			TreeFileEvent event = new TreeFileEvent(actions.get(1));
			
			event.open(this::openslide);
		}
	}
	
	private void refreshElementsID()
	{
		//refresh id
		for (int i = 0;i < elements.size();i++)
		{
			elements.get(i).id = i;
		}
	}
	
	private void swaplist(int old,int new_)
	{
		//check out of bounds
		if (new_ < 0 || new_ > elements.size()-1) return;
		//swap in elements arraylist
		Collections.swap(elements, old, new_);
		//swap in JList
        String aObject = listModel.getElementAt(old);
        String bObject = listModel.getElementAt(new_);
        listModel.set(old, bObject);
        listModel.set(new_, aObject);
	}
	
	//setting save & discard action button enable
	private void savediscardenable(boolean b)
	{
		//save
		actions.get(2).setEnabled(b);
		//discard
		actions.get(3).setEnabled(b);
	}
	
	private void openslide(String path)
	{
		XmlParser xml = new XmlParser(path);
		
		org.w3c.dom.Element[] elements_ = XmlParser.getElements(xml.getElementsByTagName("element"));
		
		for (int i = 0;i < elements_.length;i++)
		{
			Element elem = Element.getElementsByName(elements_[i].getAttribute("type"));
			elem.id = i;
			elem.load(elements_[i]);
			
			elements.add(elem);
			listModel.addElement(elements_[i].getAttribute("name"));
		}
		
		slideloaded = true;
		
		//enable
		enableComponents(listpanel, true);
		savediscardenable(true);
	}
	
	private void savedialog()
	{
		String path = JOptionPane.showInputDialog(Main.frame, "Enter slide save name:", "Save",JOptionPane.QUESTION_MESSAGE);
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<slide>\n";
		
		//saving elements to xml string
		for (int i = 0;i < elements.size();i++)
		{
			xml += elements.get(i).save();
		}
		xml += "<all>" + elements.size() + "</all>\n";
		xml += "</slide>";
		
		//save file
		Util.saveFile(Util.projectPath(path + ".layout"), xml);
		
		Project.refreshProject();
	}
	
	private int discarddialog()
	{
		if (!slideloaded) return 0;
		int choose = JOptionPane.showConfirmDialog(Main.frame,"Are you sure to discard this slide?", "Discard", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		// "yes" option
		if (choose == 0)
		{
			listModel.clear();
			elements.clear();
			initenable();
			slideloaded = false;
		}
		
		return choose;
	}
    
    public void initenable()
    {
		enableComponents(this, true);
		enableComponents(listpanel, false);
		savediscardenable(false);
    }
   
    public void disable()
    {
		enableComponents(this, false);
		enableComponents(listpanel, false);
    }
	
    public void enableComponents(Container container, boolean enable)
    {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableComponents((Container)component, enable);
            }
        }
    }

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		dragged = true;
		int X = e.getX();
		int Y = e.getY();
		xPixel = (int)((float)X*((1280) / ((float)canvas.getWidth())));
		yPixel = (int)((float)Y*((720) / ((float)canvas.getHeight())));
		
		position.setText("X: " + xPixel + " Y:" + yPixel);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		int X = e.getX();
		int Y = e.getY();
		xPixel = (int)((float)X*((1280) / ((float)canvas.getWidth())));
		yPixel = (int)((float)Y*((720) / ((float)canvas.getHeight())));
		
		position.setText("X: " + xPixel + " Y:" + yPixel);
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		dragged = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {

		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}
