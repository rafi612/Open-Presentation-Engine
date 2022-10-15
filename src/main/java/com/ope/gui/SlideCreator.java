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
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;
import org.lwjgl.system.MemoryUtil;
import org.xml.sax.SAXException;

import static org.lwjgl.opengl.GL11.*;

import com.ope.graphics.Renderer;
import com.ope.graphics.Texture;
import com.ope.io.Util;
import com.ope.io.xml.Tag;
import com.ope.io.xml.XmlReader;
import com.ope.io.xml.XmlWriter;
import com.ope.main.Main;
import com.ope.presentation.slide.Element;
import com.ope.project.Project;

public class SlideCreator extends JPanel implements ActionListener,MouseMotionListener,MouseListener
{
	private static final long serialVersionUID = 1L;
	
	public static int WIDTH = 1280,HEIGHT = 720;
	
	public int elementsint = 0;
	public int xPixel,yPixel;
	
	public int currentColidedID = -1,currentMovedID = -1;
	
	private int viewportX,viewportY,viewportW,viewportH;
	
	public boolean slideloaded = false;
	public boolean dragged = false;
	
	private DefaultListModel<String> listModel;
	public JList<String> list;
	public JPanel listpanel;
	
	private JButton edit,up,down,rename,delete;
	public ArrayList<JButton> actions = new ArrayList<>();
	private JLabel position;
	private AWTGLCanvas canvas;
	
	public ArrayList<Element> elements = new ArrayList<>();
	
	private Texture canvasimage;
	
	private File openedfile;
	
	class ToolBar extends JToolBar implements ActionListener
	{
		private static final long serialVersionUID = 1L;
		
		private SlideCreator sc;
		
		public JLabel name;
		public JButton image,text;
		
		public ToolBar(SlideCreator sc)
		{
			this.sc = sc;
			
			setFloatable(false);
			
			name = new JLabel("No file",SwingConstants.CENTER);
			name.setBorder(BorderFactory.createEmptyBorder(2,4,2,2));
			name.setPreferredSize(new Dimension(75,32));
			add(name);
			
			addSeparator();
			
			image = new JButton(new ImageIcon(Util.loadIcon("/icons/toolbar/image.png")));
			image.setToolTipText("Add image");
			image.addActionListener(this);
			add(image);
			
			text = new JButton(new ImageIcon(Util.loadIcon("/icons/toolbar/text.png")));
			text.setToolTipText("Add Text");
			text.addActionListener(this);
			add(text);
			
			addSeparator();
		}
		
		private String getNameWithNum(String name)
		{
			int repeat = 0;
			for (Element element : sc.elements)
				if (element.name.startsWith(name))
					repeat++;
			
			return repeat == 0 ? name : name + " #" + repeat;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			var source = e.getSource();
			
			if (source == image)
				sc.addElement(Element.getElementsByName("Image"),getNameWithNum("Image"));
		}
	}
	
	private ToolBar toolbar;
	
	public SlideCreator() 
	{
		setLayout(new BorderLayout());
		
		//toolbar
		toolbar = new ToolBar(this);
		add(toolbar,BorderLayout.NORTH);
		
		//actions
		JPanel buttons = new JPanel();
		
		buttons.setBorder(BorderFactory.createTitledBorder(""));
		buttons.setToolTipText("Necessary actions");
		
		actions.add(new JButton("Create new slide"));
		actions.add(new JButton("Open slide"));
		actions.add(new JButton("Save slide"));
		actions.add(new JButton("Discard slide"));
		
		for (JButton button : actions)
		{
			button.addActionListener(this);
			buttons.add(button);
		}
			
		add(buttons,BorderLayout.SOUTH);
		
		//list
		listpanel = new JPanel();
		listpanel.setLayout(new BorderLayout());

		listModel = new DefaultListModel<String>();
		list = new JList<String>(listModel);
		
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
		
		add(listpanel,BorderLayout.WEST);
		
		position = new JLabel("X: " + xPixel + " Y:" + yPixel);
		
		//canvas
		JPanel cpanel = new JPanel();
		cpanel.setLayout(new BorderLayout());
		
		//panel in canvas
		JPanel ppanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ppanel.add(position);
		cpanel.add(ppanel,BorderLayout.SOUTH);

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
	
	//private boolean tumbnailSaved = false;

	public void display()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0,0,0,1);
		
		reshape(canvas.getWidth(),canvas.getHeight());
		Renderer.frect(0, 0, WIDTH, HEIGHT, new Vector4f(1,1,1,1));
		
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
		for (Element element : elements)
			element.render();
		
//		if (dragged)
//			tumbnailSaved = true;
//		
//		if (!dragged && tumbnailSaved)
//		{
//			createTumbnail("");
//			tumbnailSaved = false;
//		}
	}
	
	private float sx,sy;
	
	public void init() 
	{
		System.out.println("Canvas init");
		
		Renderer.init(WIDTH,HEIGHT);
		
		AffineTransform transform = canvas.getGraphicsConfiguration().getDefaultTransform();
		sx = (float) transform.getScaleX();
		sy = (float) transform.getScaleY();
		
		canvasimage = new Texture(SlideCreator.class.getResourceAsStream("/images/canvas.png"));
		
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
			viewportW = (int)((targetAspect / aspectRatio ) * width * sx);
	        glViewport(viewportX = (int)((width / 2) - (viewportW / 2)),
	        		viewportY = 0,
	        		viewportW,
	        		viewportH = (int)(height * sy));
		}
		else
		{
	        viewportH = (int)((aspectRatio / targetAspect) * height * sy);
	        glViewport(viewportX = 0,
	        		viewportY = (int)((height / 2) - (viewportH / 2)),
	        		viewportW = (int)(width * sx),
	        		viewportH);
		}
		
		//glViewport(0,0,(int)(width * sx),(int)(height * sy));
	}
	
	public void createTumbnail(String path)
	{
		glReadBuffer(GL_FRONT);
		ByteBuffer buffer = MemoryUtil.memAlloc(viewportW * viewportH * 4);
		glReadPixels(viewportX, viewportY, viewportW, viewportH, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			
		//flip to revert projection matrix invertion
		STBImageWrite.stbi_flip_vertically_on_write(true);
		//save image
		STBImageWrite.stbi_write_png(path, viewportW, viewportH, STBImage.STBI_rgb_alpha, buffer, 0);
		
		STBImageWrite.stbi_flip_vertically_on_write(false);
		
		MemoryUtil.memFree(buffer);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		var source = e.getSource();

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
				
				toolbar.name.setText("Untitled");
				
				setEnabled(true);
				
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
	
	public void addElement(Element element,String name)
	{
		elementsint++;
		
		element.id = elements.size();
		element.name = name;
		element.frame();
		
		//get and add element
		elements.add(element);
		listModel.addElement(name);
	}
	
	private void refreshElementsID()
	{
		//refresh id
		for (int i = 0;i < elements.size();i++)
			elements.get(i).id = i;
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
	
	private void openslide(String path)
	{
		try 
		{
			XmlReader xml = new XmlReader(path);
			
			Tag[] tags = xml.getTags("element");
			
			for (int i = 0;i < tags.length;i++)
			{
				Element elem = Element.getElementsByName(tags[i].getAttribute("type"));
				elem.id = i;
				elem.load(tags[i]);
				
				elements.add(elem);
				listModel.addElement(tags[i].getAttribute("name"));
			}
			
			slideloaded = true;
			
			openedfile = new File(path);
			toolbar.name.setText(openedfile.getName());
			
			//enable
			setEnabled(true);
		} 
		catch (IOException | SAXException e) 
		{
			JOptionPane.showMessageDialog(Main.frame, "Error:" + e.getMessage());
			e.printStackTrace();
		}
	
	}
	
	public void closeSlide()
	{
		openedfile = null;
		toolbar.name.setText("No file");
		
		listModel.clear();
		elements.clear();
		initEnable();
		slideloaded = false;
	}
	
	private void savedialog()
	{
		String path;
		if (openedfile == null)
		{
			path = Util.projectPath(JOptionPane.showInputDialog(Main.frame, "Enter slide save name:", "Save",JOptionPane.QUESTION_MESSAGE)  
					+ ".layout");
			openedfile = new File(path);
			toolbar.name.setText(openedfile.getName());
		}
		else
			path = openedfile.getPath();
		
		XmlWriter xml = new XmlWriter();
		
		xml.openTag("slide");
		for (Element element : elements)
		{
			element.save(xml);
		}
		
		xml.addTagText("all", String.valueOf(elements.size()));
		xml.closeTag();
		
		//save file
		Util.saveFile(path, xml.get());
		
		Project.refreshProject();
	}
	
	private int discarddialog()
	{
		if (!slideloaded) return 0;
		
		int choose = JOptionPane.showConfirmDialog(Main.frame,"Are you sure to discard this slide?", "Discard", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		// "yes" option
		if (choose == 0)
			closeSlide();
		
		return choose;
	}
	
	public void initEnable()
	{
		enableComponents(this, true);
		enableComponents(listpanel, false);
		enableComponents(toolbar, false);
		
		//save
		actions.get(2).setEnabled(false);
		//discard
		actions.get(3).setEnabled(false);
	}
   
	public void setEnabled(boolean enable)
	{
		enableComponents(this, enable);
	}
	
	public void enableComponents(Container container, boolean enable)
	{
		Component[] components = container.getComponents();
		for (Component component : components) 
		{
			component.setEnabled(enable);
			if (component instanceof Container) 
			{
				enableComponents((Container)component, enable);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		dragged = true;
//		int X = e.getX();
//		int Y = e.getY();
//		xPixel = (int)((float)X*((WIDTH) / ((float)canvas.getWidth())));
//		yPixel = (int)((float)Y*((HEIGHT) / ((float)canvas.getHeight())));
//		
//		position.setText("X: " + xPixel + " Y:" + yPixel);
		
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
