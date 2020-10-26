package com.main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.undo.UndoManager;

import com.input.Action;
import com.input.TreeListener;
import com.io.Stream;
import com.opengl.main.Presentation;
import com.project.Project;
import com.tree.TreeCellRenderer;


public class Main
{
	public static String[] args;
	public static JFrame frame,scripteditor;
    public static JMenuBar menubar;
    public static JMenu file,edit,tools,run,settings,help;
    
   // static UndoManager manager = new UndoManager();
    
    public static final String TITLE = "Open Presentation Engine";
    
    //file
    public static JMenuItem newproject,loadproject,save,export,exitproject,exit;
    
    //edit
    public static JMenuItem cut,paste,copy,selectAll;
    
    //tools
    public static JMenuItem refresh,shell;
    
    //run
    public static JMenuItem runandbuild,run_;
    
    //help
    public static JMenuItem about,license;
    
    //tree popup
    public static JMenuItem newfile,editfile, newfolder, openfile, newpython,newxml;
    
    public static JTree tree;
    public static DefaultMutableTreeNode workspace;
    
    public static JScrollPane scrollpane;
	public static JScrollPane scrollpane2,scrollpane3;
    public static JTextArea textarea,textarea2;
    
    public static ArrayList<JButton> actions = new ArrayList<JButton>();
    public static ArrayList<JButton> autoscripts = new ArrayList<JButton>();
    
    public static JTabbedPane tabs;

    public Main()
    {
    	tabs = new JTabbedPane();
    	
        JPopupMenu textareapopup = new JPopupMenu();
//        textareapopup.add(copy);
//        textareapopup.add(paste);
//        textareapopup.add(cut);
//        textareapopup.add(selectAll);
    	
    	//JPanel textpanel = new JPanel();
    	//textpanel.setLayout(new BorderLayout());
        
        textarea = new JTextArea();
        textarea.setFont(new Font(textarea.getFont().getName(), Font.TRUETYPE_FONT, 16));
        textarea.setEnabled(false);
        textarea.setComponentPopupMenu(textareapopup);
        
        textarea2 = new JTextArea();
        textarea2.setFont(new Font(textarea.getFont().getName(), Font.TRUETYPE_FONT, 16));
        textarea2.setEnabled(false);
        
        textarea.setEnabled(false);
        scrollpane = new JScrollPane(textarea);
        scrollpane2 = new JScrollPane(textarea2);
        
        tabs.add("Main.py",scrollpane);
        tabs.add("Config.xml",scrollpane2);
        
        frame.add(tabs);
    	
        JPopupMenu treepopup = new JPopupMenu();
        treepopup.add(newfile);
        treepopup.add(editfile);
        treepopup.add(newfolder);
        treepopup.add(openfile);
        treepopup.add(newxml);
        
        workspace = new DefaultMutableTreeNode("Workspace                                  "); 
        tree = new JTree(workspace);
        tree.setBorder(BorderFactory.createTitledBorder("Project Explorer"));
        tree.setShowsRootHandles(true);
        tree.setComponentPopupMenu(treepopup);
        tree.setCellRenderer(new TreeCellRenderer());
        tree.addTreeSelectionListener(new TreeListener());
        tree.setEnabled(false);
        
        scrollpane2 = new JScrollPane(tree);
        frame.add(scrollpane2,BorderLayout.WEST);
        
        frame.setDropTarget(new DropTarget()
        {
                private static final long serialVersionUID = 1L;
                public synchronized void drop(DropTargetDropEvent evt) 
                {
                try 
                {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                               
                            @SuppressWarnings("unchecked")
							List<File> droppedFiles = (List<File>)
                                        evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                            for (File file : droppedFiles)
                            {
                                // process files
                                
                                //System.out.println(file.getName());
                            	
                            	Stream.copyFileondrive(file.getPath(), Project.projectlocation + Stream.slash() + file.getName());
                            	
                                Project.refreshProject();
                            }
                               
                } 
                catch (Exception ex) 
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame,ex.getStackTrace(), "Unsupported file type", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JPanel autoscript = new JPanel();
        autoscript.setLayout(new BoxLayout(autoscript, 1));
        autoscript.setBorder(BorderFactory.createTitledBorder("Auto Scripting"));
        
        autoscripts.add(new JButton("Import Libs"));
        autoscripts.add(new JButton("Create Slide"));
        autoscripts.add(new JButton("Add Slide Background"));
        autoscripts.add(new JButton("Add fullscreen"));
        autoscripts.add(new JButton("Add General Music"));
        autoscripts.add(new JButton("Add TTS"));
        autoscripts.add(new JButton("End Script"));
        autoscripts.add(new JButton("More Auto Scripts"));
        
        for (int i = 0;i < autoscripts.size(); i++)
        	autoscript.add(autoscripts.get(i));
        
        for (int i = 0;i < autoscripts.size(); i++)
        	autoscripts.get(i).addActionListener(new Action());
        
        frame.add(autoscript,BorderLayout.EAST);
        
        JPanel buttons = new JPanel();
        
        buttons.setBorder(BorderFactory.createTitledBorder("Actions"));
        actions.add(new JButton("Build & Run"));
        actions.add(new JButton("Save"));
        actions.add(new JButton("Stop"));
        actions.add(new JButton("Open Slide Creator"));
//        actions.add(new JButton("Open XML editor"));
//        actions.add(new JButton("Open Python editor"));
        
        for (int i = 0;i < actions.size(); i++)
        	buttons.add(actions.get(i));
        
        for (int i = 0;i < actions.size(); i++)
        	actions.get(i).addActionListener(new Action());
        
        frame.add(buttons,BorderLayout.SOUTH);
        
        //Project.CreateNewProject("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop", JOptionPane.showInputDialog(frame,"Project Name:","Project",JOptionPane.QUESTION_MESSAGE));
        Project.unloadProject();
    }

	public static void main(String[] args)
	{
		Main.args = args;
		if (args.length < 1)
		{
			
			frame = new JFrame(TITLE);
			frame.setSize(1280,720);
			frame.setIconImage(loadIcon("/images/icon.png"));
			frame.setLocationRelativeTo(null);
			frame.setLayout(new BorderLayout());
			
			UI();
			
			menubar = new JMenuBar();
		       
			menu();
			
			new Main();
			
		    frame.setJMenuBar(menubar);
		      
			frame.setVisible(true);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		}
		else 
			Presentation.init();
		
		for (int i = 0;i < args.length;i++)
			System.out.println(args[i]);
	}
	
	public static void menu()
	{
        //menu
        file = new JMenu("File");
        edit = new JMenu("Edit");
        tools = new JMenu("Tools");
        settings = new JMenu("Settings");
        run = new JMenu("Run");
        help = new JMenu("Help");
        
        //plik
        newproject = new JMenuItem("New Project");
        newproject.addActionListener(new Action());
        file.add(newproject);
        
        loadproject = new JMenuItem("Load Project");
        loadproject.addActionListener(new Action());
        file.add(loadproject);
        
        file.add(new JSeparator());
        
        save = new JMenuItem("Save");
        save.addActionListener(new Action());
        save.setEnabled(false);
        file.add(save);
        
        export = new JMenuItem("Export");
        export.setEnabled(false);
        file.add(export);
        
        exitproject = new JMenuItem("Exit Project");
        exitproject.addActionListener(new Action());
        exitproject.setEnabled(false);
        file.add(exitproject);
        
        file.add(new JSeparator());
        
        exit = new JMenuItem("Exit");
        exit.addActionListener(new Action());
        file.add(exit);
        
        //edytuj
        copy= new JMenuItem("Copy");
        copy.setEnabled(false);
        edit.add(copy);
        paste= new JMenuItem("Paste");   
        paste.setEnabled(false);
        edit.add(paste);
        cut= new JMenuItem("Cut");
        cut.setEnabled(false);
        edit.add(cut);
        selectAll= new JMenuItem("Select All"); 
        selectAll.setEnabled(false);
        edit.add(selectAll);
        
        
        //narzędzia
        shell = new JMenuItem("OPE Python Shell"); 
        shell.addActionListener(new Action());
        tools.add(shell);
        refresh = new JMenuItem("Refresh Project"); 
        refresh.addActionListener(new Action());
        refresh.setEnabled(false);
        tools.add(refresh);
        
        runandbuild = new JMenuItem("Build And Run"); 
        //shell.addActionListener(new Action());
        tools.add(shell);
        run_ = new JMenuItem("Run"); 
        //refresh.addActionListener(new Action());
        run.add(runandbuild);
        run.add(run_);
        
        //settings
        
        //pomoc 
        about = new JMenuItem("About");
        about.addActionListener(new Action());
        help.add(about);
        license = new JMenuItem("View License");
        //license.addActionListener(new Action());
        help.add(license);
        
        //popup drzewka
        newfile = new JMenuItem("New file");
        newfile.addActionListener(new Action());
        editfile = new JMenuItem("Edit File");
        editfile.addActionListener(new Action());
        newfolder = new JMenuItem("New Folder");
        newfolder.addActionListener(new Action());
        openfile = new JMenuItem("Open File/Folder");
        newxml = new JMenuItem("New XML File");
        newxml.addActionListener(new Action());
        
        //dodawanie
        menubar.add(file);
        menubar.add(edit);
        menubar.add(tools);
        menubar.add(run);
        menubar.add(settings);
        menubar.add(help);
	}
	
	public static void UI()
	{
	    try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        //UIManager.getDefaults().put("TextArea.font", UIManager.getFont("TextField.font"));
	    } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	    } catch (InstantiationException e) {
	            e.printStackTrace();
	    } catch (IllegalAccessException e) {
	            e.printStackTrace();
	    } catch (UnsupportedLookAndFeelException e) {
	            e.printStackTrace();
	    }
	
	    SwingUtilities.updateComponentTreeUI(frame);
	}
	
	public static Image loadIcon(String path)
	{
		Image i = null;
		
		try
		{
			i = ImageIO.read(Main.class.getResourceAsStream(path));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return i;
	}
	
//	public static void initUndoRedo()
//	{
//        textarea.getDocument().addUndoableEditListener(new UndoableEditListener() {
//            @Override
//            public void undoableEditHappened(UndoableEditEvent e) {
//                manager.addEdit(e.getEdit());
//            }
//        });
//        
//        KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(
//                KeyEvent.VK_Z, Event.CTRL_MASK);
//        KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(
//                KeyEvent.VK_Y, Event.CTRL_MASK);
//        
//        textarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(undoKeyStroke, "undoKeyStroke");
//        textarea.getActionMap().put("undoKeyStroke", new AbstractAction() 
//        {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//        	public void actionPerformed(ActionEvent e) {
//        		try {
//        			manager.undo();
//        		} catch (CannotUndoException cue) {}
//        	}
//        });
//        
//        // Map redo action
//        textarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
//                .put(redoKeyStroke, "redoKeyStroke");
//        textarea.getActionMap().put("redoKeyStroke", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    manager.redo();
//                 } catch (CannotRedoException cre) {}
//            }
//        });
//	}

}