package com.slidecreator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import com.io.Stream;
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
import com.presentation.resource.ImageResource;
import com.project.Project;

public class SlideCreator extends JPanel implements ActionListener, GLEventListener
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
    
    int elements = 0;
    
    ImageResource canvasimage;
	
	public SlideCreator() 
	{
		setLayout(new BorderLayout());
		
		//actions
        JPanel buttons = new JPanel();
        
        buttons.setBorder(BorderFactory.createTitledBorder("Slide Creator Actions"));
        buttons.setToolTipText("Necessary actions");
        
        actions.add(new JButton("Create new slide"));
        actions.add(new JButton("Open slide"));
        actions.add(new JButton("Save slide"));
        
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
        //list.setPreferredSize(new Dimension(250,0));
        
        newelement = new JButton("New Element");
        newelement.addActionListener(this);
        edit = new JButton("Edit");
        
        JPanel bpanel = new JPanel();
        bpanel.add(newelement);
        bpanel.add(edit);
        
        listpanel.add(new JScrollPane(list));
        listpanel.add(bpanel,BorderLayout.SOUTH);
        listpanel.setPreferredSize(new Dimension(200,0));
		enableComponents(listpanel, false);
        
        add(listpanel,BorderLayout.WEST);
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
	    animator = new FPSAnimator(canvas,60);
	    animator.start();
	    cpanel.add(canvas);
	    
	    add(cpanel);
	}

	@Override
	public void display(GLAutoDrawable drawable)
	{
		Presentation.profile = profile;
		EventListener.gl = gl;
		
		gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		
		gl.glClearColor(1, 1, 1, 1);
		
		Screen.drawImage(canvasimage,0, 0, 1280, 720);
	}

	@Override
	public void dispose(GLAutoDrawable arg0) 
	{
		animator.stop();
	}
	
	@Override
	public void init(GLAutoDrawable drawable) 
	{
		System.out.println("init");
		gl = drawable.getGL().getGL2();
		
		gl.glClearColor(1,1,1, 1);
		
		gl.glEnable(GL2.GL_TEXTURE_2D);
		
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) 
	{
		System.out.println("reshape");
		gl = drawable.getGL().getGL2();
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glOrtho(0,1280,720,0, -1, 1);
		
		gl.glViewport(0,0,width + 200,height + 140);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	Object source;
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		source = e.getSource();
		
		if (source == newelement)
		{
			String[] s = {"Image","Text","Shape","Graph"};
			JComboBox<String> combo = new JComboBox<String>(s);
			JComponent[] c = {new JLabel("Choose Element:"),combo};
			
			JOptionPane.showConfirmDialog(Main.frame,c, "New Element", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
			
			elements++;
			listModel.addElement(combo.getSelectedItem().toString() + " " + elements);
		}
		//new slide
		if (source == actions.get(0))
		{
			if (canvas == null)
				initCanvas();
			
			canvasimage = new ImageResource(SlideCreator.class.getResourceAsStream("/images/canvas.png"));
			
			enableComponents(listpanel, true);
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

}
