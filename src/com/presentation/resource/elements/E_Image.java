package com.presentation.resource.elements;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.gui.SlideCreator;
import com.io.IoUtil;
import com.io.XmlParser;
import com.jogamp.opengl.GL2;
import com.main.Main;
import com.presentation.graphics.Renderer;
import com.presentation.resource.Element;
import com.presentation.resource.ImageResource;
import com.project.Project;

public class E_Image extends Element
{
	public String path;
	
	public int id;
	
	public ImageResource image;
	
	public boolean editing,moving,colided;
	public boolean dragged;
	ImageFrame frame;
	
    public Point clickpoint = new Point(0,0);
    
    ArrayList<Resizer> resizers;
	
	public E_Image()
	{
		type = "Image";
		frame();
	}

	public E_Image(String path,int x,int y,int w,int h)
	{
		super();
		this.path = path;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		resizers = new ArrayList<Resizer>();
		resizers.add(new Resizer(Resizer.Type.DOWN_RIGHT));
		resizers.add(new Resizer(Resizer.Type.DOWN_LEFT));
		resizers.add(new Resizer(Resizer.Type.UP_LEFT));
		resizers.add(new Resizer(Resizer.Type.UP_RIGHT));
		
		type = "Image";
		
		if (!path.equals(""))
			this.image = new ImageResource(path);
		
		frame = new ImageFrame(this);
	}
	
	public void update(SlideCreator sc)
	{
		//grobal dragged value
		dragged = sc.dragged;
		
		//check tracking on all elements
		boolean canTracked = true;
		//resizer update and track
		for (int i = 0; i < resizers.size(); i++)
		{
			resizers.get(i).track(this);
			resizers.get(i).update(sc);
			
			//if one is false, then all is false
			canTracked = canTracked && resizers.get(i).canTrakedElementMove;
		}
		
		//checking AABB colision on click
		if (x < sc.xPixel && x + w > sc.xPixel && y < sc.yPixel && y + h > sc.yPixel)
			colided = true;
		else
			colided = false;
		
		//executing on dragging
		if (sc.dragged && canTracked)
		{
			//if colided
			if (colided)
			{
				colided = true;
				if (!moving)
				{
					moving = true;
					//sc.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					clickpoint.setLocation(sc.xPixel, sc.yPixel);
				}
			}
			//executing on moving
			if (moving)
			{
		        int xMoved = (x + sc.xPixel) - (x + clickpoint.x);
		        int yMoved = (y + sc.yPixel) - (y + clickpoint.y);
		        x += xMoved;
		        y += yMoved;
			}
	        
	        clickpoint = new Point(sc.xPixel,sc.yPixel);
		}
		
		// reseting "moving" after release mouse
		if (!sc.dragged)
		{
			moving = false;
			//sc.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		
		if (editing) frame.update();
	}
	
	public void load(XmlParser xml,int id)
	{
		//getting tag element from id
		org.w3c.dom.Element element = xml.getElements(xml.getElementsByTagName("element"))[id];
		
		if (element.getAttribute("type").equals("Image"))
		{
			type = element.getAttribute("type");
			name = element.getAttribute("name");
			
			//[0] getting first element from array
			org.w3c.dom.Element data = xml.getElementsFromElementByName(element,"data")[0];
			
			//loading image
			path = data.getAttribute("src");
			image = new ImageResource(Project.projectlocation + IoUtil.slash() + path);
			
			x = Integer.parseInt(data.getAttribute("x"));
			y = Integer.parseInt(data.getAttribute("y"));
			w = Integer.parseInt(data.getAttribute("width"));
			h = Integer.parseInt(data.getAttribute("height"));
		}
	}
	
	public String save()
	{
		//creating xml tag with element data
		return "	<element name=\"" +  name + "\" type=\"" + type + "\">\n"
				+ "		<data src=\"" + path.replaceAll(File.separator, "/") +"\" x=\"" + x + "\" y=\"" + y + "\" width=\"" + w + "\" height=\"" + h + "\"></data>\n"
				+ "	</element>\n";
	}
	
	public void render(GL2 gl)
	{
		if (path.equals(""))
			Renderer.frectnofill(x,y,w,h,Color.BLACK);
		else
			Renderer.drawImage(image,x,y,w,h);
		
		if (colided)
			Renderer.frectnofill(x,y,w,h,Color.BLUE);
		
		if (dragged && colided)
			Renderer.frectnofill(x,y,w,h,Color.RED);
		
		for (int i = 0; i < resizers.size(); i++)
			resizers.get(i).render(gl,colided);
	}
	
	
	public void frame()
	{
		editing = true;
		frame.setVisible(true);
	}
}

class ImageFrame extends JDialog implements ChangeListener
{
	private static final long serialVersionUID = 1L;
	public JSpinner sx;
	public JSpinner sy;
	JSpinner sw;
	JSpinner sh;
	JTextField textfieldpath;
	
	E_Image element;
	public ImageFrame(E_Image element)
	{
		super();
		this.element = element;
		setSize(360,250);
		setTitle("Image");
		setLocationRelativeTo(Main.frame);
		setIconImage(Main.loadIcon("/images/icon.png"));
		setAlwaysOnTop(true);
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				element.editing = false;
			}
		});
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		setDropTarget(new DragAndDrop());
		
		textfieldpath = new JTextField(element.path);
		
		SpinnerModel modelx = new SpinnerNumberModel(element.x,-Integer.MAX_VALUE,Integer.MAX_VALUE,1);       
		sx = new JSpinner(modelx);
		
		SpinnerModel modely = new SpinnerNumberModel(element.y,-Integer.MAX_VALUE,Integer.MAX_VALUE,1);       
		sy = new JSpinner(modely);
		
		SpinnerModel modelw = new SpinnerNumberModel(element.w,0,Integer.MAX_VALUE,1);       
		sw = new JSpinner(modelw);
		
		SpinnerModel modelh = new SpinnerNumberModel(element.h,0,Integer.MAX_VALUE,1);       
		sh = new JSpinner(modelh);
		
		String[] labels = {"Path:","X:","Y:","Width:","Height"};
		JComponent[] components = {textfieldpath,sx,sy,sw,sh};
		
		for (JComponent comp : components)
		{
			comp.setMinimumSize(new Dimension(0, 25));
			comp.setPreferredSize(new Dimension(300,25));
			comp.setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
			if (comp instanceof JSpinner)
			{
				((JSpinner) comp).addChangeListener(this);
			}
		}
		
		for (int i = 0; i < labels.length;i++)
		{
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			panel.add(new JLabel(labels[i]));
			panel.add(components[i]);
			
			this.add(panel);
		}
	}
	
	// setting JSpinner value form elements
	public void update()
	{
		sx.setValue(element.x);
		sy.setValue(element.y);
		sw.setValue(element.w);
		sh.setValue(element.h);
	}
	
	// setting elements value form JSpinner if it's dragged
	@Override
	public void stateChanged(ChangeEvent e) 
	{
		if (!element.dragged)
		{
			element.x = (int) sx.getValue();
			element.y = (int) sy.getValue();
			element.w = (int) sw.getValue();
			element.h = (int) sh.getValue();
		}
	}
	
	class DragAndDrop extends DropTarget
	{
		private static final long serialVersionUID = 1L;
		public synchronized void drop (DropTargetDropEvent evt)
    	{
			try 
			{
				evt.acceptDrop(DnDConstants.ACTION_COPY);
				File file = new File((String) evt.getTransferable().getTransferData(DataFlavor.stringFlavor));
				if (!file.getPath().startsWith(Project.projectlocation))
				{
					System.out.println("Error");
					return;
				}
				else
				{
					element.path = IoUtil.getPathFromProject(file);
					
					element.image = new ImageResource(Project.projectlocation + IoUtil.slash() + element.path);
					element.w = element.image.image.getWidth();
					element.h = element.image.image.getHeight();
					sw.setValue(element.image.image.getWidth());
					sh.setValue(element.image.image.getHeight());
					textfieldpath.setText(element.path);
				}
				     
			} 
			catch (Exception ex)
			{
				ex.printStackTrace();
				JOptionPane.showMessageDialog(Main.frame,ex.getStackTrace(), "Unsupported file type", JOptionPane.ERROR_MESSAGE);
			}
    	}

	}
	
}
