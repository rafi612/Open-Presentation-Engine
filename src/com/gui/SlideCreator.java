package com.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

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

public class SlideCreator extends JPanel implements ActionListener, GLEventListener,MouseWheelListener,MouseMotionListener,MouseListener
{
	private static final long serialVersionUID = 1L;
	
	public static GL2 gl = null;
	public static GLCanvas canvas;
	public static GLProfile profile;
	public static FPSAnimator animator;
	GLCapabilities cabs;
	
    public ArrayList<JButton> actions = new ArrayList<JButton>();
    
    DefaultListModel<String> listModel;
    public JList<String> list;
    public JPanel listpanel;
    
    JButton newelement,edit;
    
    int elementsint = 0;
    int cwidth,cheight,x,y;
    double zoom = 1;
    
    ImageResource canvasimage;
    
    public ArrayList<Element> elements;
	
	public SlideCreator() 
	{
		setLayout(new BorderLayout());
		
		elements = new ArrayList<Element>();
		
		//actions
        JPanel buttons = new JPanel();
        
        buttons.setBorder(BorderFactory.createTitledBorder("Slide Creator Actions"));
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
        
        newelement = new JButton("New");
        newelement.addActionListener(this);
        
        edit = new JButton("Edit");
        edit.addActionListener(this);
        
        JPanel bpanel = new JPanel();
        bpanel.add(newelement);
        bpanel.add(edit);
        
        listpanel.add(new JScrollPane(list));
        listpanel.add(bpanel,BorderLayout.SOUTH);
        listpanel.setPreferredSize(new Dimension(200,0));
		enableComponents(listpanel, false);
        
        add(listpanel,BorderLayout.WEST);
        
        //initCanvas();
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
	    cpanel.add(canvas);
	    
		canvasimage = new ImageResource(SlideCreator.class.getResourceAsStream("/images/canvas.png"));
	    
	    add(cpanel);
	}
	
	private double getScaleFactor() 
	{
        double trueHorizontalLines = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
        double scaledHorizontalLines = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        double dpiScaleFactor = trueHorizontalLines / scaledHorizontalLines;
        return dpiScaleFactor;
    }

	@Override
	public void display(GLAutoDrawable drawable)
	{
		Presentation.profile = profile;
		EventListener.gl = gl;
		
		gl.glViewport(x,y,(int)(cwidth * zoom),(int)(cheight * zoom));
		
		gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		
		gl.glClearColor(0,0,0, 1);
		
		if (elements.size() == 0)
			Screen.drawImage(canvasimage,0, 0, 1280, 720);
		
//		TextRenderer textRenderer = new TextRenderer(new Font("Sans", Font.BOLD, 40));
//		textRenderer.beginRendering(1280,720);
//		textRenderer.setColor(Color.BLACK);
//		textRenderer.setSmoothing(true);
//
//		textRenderer.draw("Hello world!!\nlololo",0,600);
//		textRenderer.endRendering();
		
		for (int i = 0;i < elements.size();i++)
			elements.get(i).render(gl);
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
		System.out.println("Canvas reshape");
		gl = drawable.getGL().getGL2();
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		cwidth = width;
		cheight = height;
//		
		//NOTE: this set viewport to canvas resolution multiply by system scale factor 
		//not working on linux yet
		gl.glViewport(this.x,this.y,(int)(width * zoom),(int)(height * zoom));
		
		gl.glOrtho(0,1280,720,0,1,-1);
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	Object source;
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		source = e.getSource();
		//new element
		if (source == newelement)
		{
			String[] s = {"Image","Text","Shape","Graph"};
			JComboBox<String> combo = new JComboBox<String>(s);
			JTextField textfield = new JTextField();
			JComponent[] c = {new JLabel("Choose Element:"),combo,new JLabel("Name:"),textfield};
			
			JOptionPane.showConfirmDialog(Main.frame,c, "New Element", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
			
			elementsint++;
			if (textfield.getText().equals(""))
				listModel.addElement(combo.getSelectedItem().toString());
			else
				listModel.addElement(textfield.getText());
			
		    elements.add(Element.getElementsByName(combo.getSelectedItem().toString()));
		    elements.get(elements.size() - 1).frame();
		}
		//new slide
		if (source == actions.get(0))
		{
			listModel.clear();
			elements.clear();
			if (canvas == null)
				initCanvas();
			
			enableComponents(listpanel, true);
		}
		//edit
		if (source == edit)
		{
			if (list.getSelectedIndex() == -1)
				JOptionPane.showMessageDialog(Main.frame, "No selected element", "Error", JOptionPane.ERROR_MESSAGE);
			else
				elements.get(list.getSelectedIndex()).frame();
		}
	}
    
    public void initenable()
    {
		enableComponents(this, true);
		enableComponents(listpanel, false);
    }
    
    public void disable()
    {
		enableComponents(this, false);
		enableComponents(listpanel, false);
    }
    
	public static GLProfile getProfile()
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
		if (e.getWheelRotation() == 1) zoom += 0.05;
		else zoom -= 0.05;
	}
	
	Point initialClick;

	@Override
	public void mouseDragged(MouseEvent e) 
	{
        int thisX = x;
        int thisY = y;

        // Determine how much the mouse moved since the initial click
        int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
        int yMoved = (thisY + -e.getY()) - (thisY + -initialClick.y);

        // Move picture to this position
        x = thisX + xMoved;
        y = thisY + yMoved;
        
		initialClick = e.getPoint();

	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		initialClick = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
