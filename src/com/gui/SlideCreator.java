package com.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.io.Stream;
import com.io.XmlParser;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.main.Main;
import com.presentation.graphics.Screen;
import com.presentation.main.EventListener;
import com.presentation.main.Presentation;
import com.presentation.resource.Element;
import com.presentation.resource.ImageResource;
import com.project.Project;

public class SlideCreator extends JPanel implements ActionListener, GLEventListener,MouseWheelListener,MouseMotionListener,MouseListener
{
	private static final long serialVersionUID = 1L;
	
	public GL2 gl = null;
	public GLCanvas canvas;
	public GLProfile profile;
	public FPSAnimator animator;
	GLCapabilities cabs;
	
    public ArrayList<JButton> actions = new ArrayList<JButton>();
    
    public static final int WIDTH = 1280,HEIGHT = 720;
    
    DefaultListModel<String> listModel;
    public JList<String> list;
    public JPanel listpanel;
    
    JButton newelement,edit,up,down,rename,delete;
    JLabel position;
    JCheckBox movecheck;
    
    boolean canOnlyMoveCheck;
    
    public int elementsint = 0;
    public int xPixel,yPixel;
    
    ImageResource canvasimage;
    
    public boolean slideloaded = false;
    public boolean elementopen = false;
    public boolean dragged = false;
    
    public ArrayList<Element> elements;
	
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
        
        up = new JButton("▲");
        up.addActionListener(this);
        
        down = new JButton("▼");
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
		GLProfile.initSingleton();
	    profile = GLProfile.get(GLProfile.GL2);
	    cabs = new GLCapabilities(profile);
	    
	    Presentation.profile = profile;
	    EventListener.gl = gl;
	    
	    JPanel cpanel = new JPanel();
	    cpanel.setLayout(new BorderLayout());
	    canvas = new GLCanvas(cabs);
	    canvas.addGLEventListener(this);
	    canvas.addMouseWheelListener(this);
	    canvas.addMouseMotionListener(this);
	    canvas.addMouseListener(this);
	    animator = new FPSAnimator(canvas,60);
	    animator.start();
	    
	    movecheck = new JCheckBox("Move only checked");
	    
	    JPanel ppanel = new JPanel();
	    ppanel.add(position);
	    ppanel.add(movecheck);
	    cpanel.add(ppanel,BorderLayout.SOUTH);
	    
	    cpanel.add(canvas);
	    
		canvasimage = new ImageResource(SlideCreator.class.getResourceAsStream("/images/canvas.png"));
	    
	    add(cpanel);
	}

	@Override
	public void display(GLAutoDrawable drawable)
	{
		Presentation.profile = profile;
		EventListener.gl = gl;

		gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		
		gl.glClearColor(1,1,1,1);
		if (slideloaded)
		{
			Screen.frect(0, 0, 1280, 720, new Color(0xFFFFFF));
		
			if (elements.size() == 0)
				Screen.drawImage(canvasimage,0, 0, 1280, 720);
		}
		
//		TextRenderer textRenderer = new TextRenderer(new Font("Sans", Font.BOLD, 40));
//		textRenderer.beginRendering(1280,720);
//		textRenderer.setColor(Color.BLACK);
//		textRenderer.setSmoothing(true);
//
//		textRenderer.draw("Hello world!!\nlololo",0,600);
//		textRenderer.endRendering();
		
		//update
		if (movecheck.isSelected() && list.getSelectedIndex() != -1)
		{
			elements.get(list.getSelectedIndex()).update(this);
		}
		else
			for (int i = 0;i < elements.size();i++)
				elements.get(i).update(this);
		
		//render
		for (int i = 0;i < elements.size();i++)
			elements.get(i).render(gl);

		//selected border
		if (list.getSelectedIndex() > -1)
		{
			Element e = elements.get(list.getSelectedIndex());
			Screen.frectnofill(e.x, e.y, e.w, e.h, new Color(0xFFA200));
		}
	}

	@Override
	public void dispose(GLAutoDrawable arg0) 
	{
		animator.stop();
	}
	
	@Override
	public void init(GLAutoDrawable drawable) 
	{
		System.out.println("Canvas init");
		gl = drawable.getGL().getGL2();
		
		gl.glClearColor(1,1,1,1);
		
		gl.glEnable(GL2.GL_TEXTURE_2D);
		
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) 
	{
		//System.out.println("Canvas reshape");
		gl = drawable.getGL().getGL2();
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glViewport(0,0,(int)(width * getScale()),(int)(height * getScale()));
		
		gl.glOrtho(0,WIDTH,HEIGHT,0,1,-1);
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	Object source;
	
	public static double getScale()
	{
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	    return device.getDisplayMode().getWidth() / (double) device.getDefaultConfiguration().getBounds().width;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		source = e.getSource();
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
			
			//get and add element
		    elements.add(Element.getElementsByName(combo.getSelectedItem().toString()));
		    listModel.addElement(elementname);
		    
		    //setting name
		    elements.get(elements.size() - 1).name = elementname;
		    
		    //set id
		    //elements.get(elements.size() - 1).id = elements.size() - 1;
		    
		    //frame
		    elements.get(elements.size() - 1).frame();
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
			}
		}
		if (source == delete)
		{
			if (list.getSelectedIndex() == -1)
				JOptionPane.showMessageDialog(Main.frame, "No selected element", "Error", JOptionPane.ERROR_MESSAGE);
			else
			{
				int choose = JOptionPane.showConfirmDialog(Main.frame,"Are you sure to delete " + listModel.get(list.getSelectedIndex()) + "?", "Delete", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
				if (choose == 0)
				{
					elements.remove(list.getSelectedIndex());
					listModel.remove(list.getSelectedIndex());
				}
			}
		}
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
			String path = JOptionPane.showInputDialog(Main.frame, "Enter slide save name:", "Save",JOptionPane.QUESTION_MESSAGE);
			
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					+ "<slide>\n";
			
			//saving elements to xml string
			for (int i = 0;i < elements.size();i++)
			{
				xml = xml + elements.get(i).save();
			}
			xml = xml + "<all>" + elements.size() + "</all>\n";
			xml = xml + "</slide>";
			
			//save file
			Stream.saveFile(Project.projectlocation + Stream.slash() + path, xml);
			
			Project.refreshProject();
		}
		//open slide
		if (source == actions.get(1))
		{
			opendialog();
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
	
	private void opendialog()
	{
		//check discard
		if (discarddialog() != 0) return;
		
		JDialog dialog = new JDialog(Main.frame,"Open");
		dialog.setSize(300, 110);
		dialog.setLayout(new BorderLayout());
		dialog.setLocationRelativeTo(null);
		dialog.setResizable(false);
		
		JTextField text = new JTextField();
		text.setDropTarget(null);
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				dialog.dispose();
				
				XmlParser xml = new XmlParser(Project.projectlocation + Stream.slash() + text.getText());
				
				org.w3c.dom.Element[] elements_ = xml.getElements(xml.getElementsByTagName("element"));
				
				for (int i = 0;i < elements_.length;i++)
				{
					Element elem = Element.getElementsByName(elements_[i].getAttribute("type"));
					elem.load(xml, i);
					
					elements.add(elem);
					listModel.addElement(elements_[i].getAttribute("name"));
					slideloaded = true;
				}
				
				//enable
				enableComponents(listpanel, true);
				savediscardenable(true);
			}
		});
		
		JPanel okpanel = new JPanel();
		
		dialog.add(new JLabel("  Enter path or drop file here:"),BorderLayout.NORTH);
		dialog.add(text,BorderLayout.CENTER);
		
		okpanel.add(ok);
		dialog.add(okpanel,BorderLayout.SOUTH);
		
		//drag and drop
		dialog.setDropTarget(new DropTarget()
		{
			private static final long serialVersionUID = 1L;
			public synchronized void drop (DropTargetDropEvent evt)
        	{
				try 
				{
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					File file = new File((String) evt.getTransferable().getTransferData(DataFlavor.stringFlavor));
					
					String path = new File(Project.projectlocation).toURI().relativize(file.toURI()).getPath();
					
					text.setText(path);
				} 
				catch (Exception ex)
				{
					ex.printStackTrace();
					JOptionPane.showMessageDialog(Main.frame,ex.getStackTrace(), "Unsupported file type", JOptionPane.ERROR_MESSAGE);
				}
        	}
	
		});
		dialog.setVisible(true);
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
    
	public GLProfile getProfile()
	{
		return profile;
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
	public void mouseWheelMoved(MouseWheelEvent e) 
	{

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
