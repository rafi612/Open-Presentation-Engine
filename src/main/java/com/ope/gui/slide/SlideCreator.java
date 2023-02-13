package com.ope.gui.slide;

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
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

import static org.lwjgl.opengl.GL11.*;

import com.ope.core.slide.Element;
import com.ope.core.slide.Slide;
import com.ope.graphics.Renderer;
import com.ope.graphics.Texture;
import com.ope.io.ResourceLoader;
import com.ope.main.Main;

public class SlideCreator extends JPanel implements ActionListener,MouseMotionListener,MouseListener
{
	private static final long serialVersionUID = 1L;
	
	public static int WIDTH = 1280,HEIGHT = 720;
	
	public int xPixel,yPixel;
	
	public int currentColidedID = -1,currentMovedID = -1;
	
	private int viewportW,viewportH;
	
	public boolean slideloaded = false;
	public boolean dragged = false;
	
	public SlideList slideList;
	
	public SCProperties scProperties;
	private SCToolBar toolbar;
	
	private JTabbedPane leftTabs;
	
	public JList<String> list;
	private JPanel listpanel;
	
	private JButton edit,up,down,rename,delete;
	private JLabel position;
	private AWTGLCanvas canvas;
	
	private Texture canvasimage;
	
	public SlideCreator(SlideList slideList) 
	{
		setLayout(new BorderLayout());
		
		this.slideList = slideList;
		scProperties = new SCProperties(slideList);
		
		//toolbar
		toolbar = new SCToolBar(this);
		
		leftTabs = new JTabbedPane();
		
		//list
		listpanel = new JPanel();
		listpanel.setLayout(new BorderLayout());
		
		list = new JList<String>();
		
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
		
		leftTabs.add(listpanel,"Elements");
		leftTabs.add(new JScrollPane(scProperties),"Properties");
		add(leftTabs,BorderLayout.WEST);
		
		position = new JLabel("X: " + xPixel + " Y:" + yPixel);
		
		//canvas
		JPanel cpanel = new JPanel();
		cpanel.setLayout(new BorderLayout());
		
		//panel in canvas
		JPanel ppanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		ppanel.add(position);
		cpanel.add(ppanel,BorderLayout.SOUTH);
		cpanel.add(toolbar,BorderLayout.NORTH);

		GLData data = new GLData();
		data.swapInterval = 1;
		
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
		glClearColor(0,0,0,1);
		
		reshape(canvas.getWidth(),canvas.getHeight());
		Renderer.frect(0, 0, WIDTH, HEIGHT, new Vector4f(1,1,1,1));
		
		if (slideloaded)
		{		
			if (getCurrentSlide().elements.size() == 0)
				Renderer.drawImage(canvasimage,0, 0,WIDTH, HEIGHT);
			
			for (Element element : getCurrentSlide().elements)
				element.update(this);
			
			//setting moved element as selected on list
			list.setSelectedIndex(currentMovedID);
			
			//render
			for (Element element : getCurrentSlide().elements)
				element.render();
		}
	}
	
	private float sx,sy;
	
	public void init() 
	{
		System.out.println("Canvas init");
		
		Renderer.init(WIDTH,HEIGHT);
		
		AffineTransform transform = canvas.getGraphicsConfiguration().getDefaultTransform();
		sx = (float) transform.getScaleX();
		sy = (float) transform.getScaleY();
		
		canvasimage = new Texture(ResourceLoader.load("/images/canvas.png"));
		
		glClearColor(0,0,0,1);
		
		glEnable(GL_TEXTURE_2D);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public void reshape(int width, int height) 
	{
		if (Renderer.isFallback())
			Renderer.fallbackResize();
		
		float aspectRatio = (float)width / (float)height;
		float targetAspect = (float)WIDTH / (float)HEIGHT;
		
		if (aspectRatio >= targetAspect)
		{
			viewportW = (int)((targetAspect / aspectRatio) * width * sx);
			viewportH = (int)(height * sy);
			glViewport((int)((width / 2) - (viewportW / 2)),0,viewportW,viewportH);
		}
		else
		{
			viewportH = (int)((aspectRatio / targetAspect) * height * sy);
			viewportW = (int)(width * sx);
			glViewport(0,(int)((height / 2) - (viewportH / 2)),viewportW,viewportH);
		}
		
		//glViewport(0,0,(int)(width * sx),(int)(height * sy));
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		var source = e.getSource();
		
		//edit
		if (source == edit)
		{
			if (list.getSelectedIndex() == -1)
				JOptionPane.showMessageDialog(Main.frame, "No selected element", "Error", JOptionPane.ERROR_MESSAGE);
			else
				getCurrentSlide().elements.get(list.getSelectedIndex()).frame();
		}
		//up & down
		if (source == up || source == down)
		{
			if (list.getSelectedIndex() == -1)
				JOptionPane.showMessageDialog(Main.frame, "No selected element", "Error", JOptionPane.ERROR_MESSAGE);
			else
			{
				int new_ = list.getSelectedIndex() + (source == down ? 1 : -1);
				int old = list.getSelectedIndex();
				//swap func
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
				int choose = JOptionPane.showConfirmDialog(Main.frame,"Are you sure to delete " + getCurrentSlide().listModel.get(list.getSelectedIndex()) + "?", "Delete", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if (choose == 0)
				{
					int index = list.getSelectedIndex();
					getCurrentSlide().elements.get(index).destroy();
					
					getCurrentSlide().elements.remove(index);
					getCurrentSlide().listModel.remove(index);
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
					getCurrentSlide().listModel.set(list.getSelectedIndex(), choose);
					getCurrentSlide().elements.get(list.getSelectedIndex()).name = choose;
				}
			}
		}
	}
	
	public void addElement(Element element,String name)
	{		
		element.id = getCurrentSlide().elements.size();
		element.name = name;
		element.frame();
		
		//get and add element
		getCurrentSlide().elements.add(element);
		getCurrentSlide().listModel.addElement(name);
	}
	
	private void refreshElementsID()
	{
		//refresh id
		for (int i = 0;i < getCurrentSlide().elements.size();i++)
			getCurrentSlide().elements.get(i).id = i;
	}
	
	private void swaplist(int old,int new_)
	{
		//check out of bounds
		if (new_ < 0 || new_ > getCurrentSlide().elements.size()-1) return;
		
		//swap in elements arraylist
		Collections.swap(getCurrentSlide().elements, old, new_);
		
		//swap in JList
		String aObject = getCurrentSlide().listModel.getElementAt(old);
		String bObject = getCurrentSlide().listModel.getElementAt(new_);
		getCurrentSlide().listModel.set(old, bObject);
		getCurrentSlide().listModel.set(new_, aObject);
	}
	
	public Slide getCurrentSlide()
	{
		return slideList.getCurrentSlide();
	}
   
	public void setEnabled(boolean enable)
	{
		enableComponents(this, enable);
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

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		dragged = true;		
		//redirecting to move event
		mouseMoved(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		dragged = false;
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		dragged = true;
		//redirecting to move event
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		int X = e.getX();
		int Y = e.getY();
		
		float aspectRatio = (float)canvas.getWidth() / (float)canvas.getHeight();
		float targetAspect = (float)WIDTH / (float)HEIGHT;
		
		if (aspectRatio >= targetAspect)
		{
			float calculatedW = (targetAspect / aspectRatio ) * canvas.getWidth();
			float x = (int)((canvas.getWidth() / 2) - (calculatedW / 2));
			
			xPixel = (int)(((float)(X-x)*((WIDTH) / calculatedW)));
			yPixel = (int)((float)Y*((HEIGHT) / ((float)canvas.getHeight())));
		}
		else
		{
			float calculatedH = (aspectRatio / targetAspect) * canvas.getHeight();
			float y = (int)((canvas.getHeight() / 2) - (calculatedH / 2));
			
			xPixel = (int)((float)X*((WIDTH) / ((float)canvas.getWidth())));
			yPixel = (int)((float)(Y-y)*((HEIGHT) / calculatedH));
		}
		
		position.setText("X: " + xPixel + " Y:" + yPixel);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
