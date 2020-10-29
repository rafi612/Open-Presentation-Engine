package com.main;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;

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
    
    public static String interpreterpath = "",interpretertype = "";
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
    
    //settings
    public static JMenu python;
    
    public static JRadioButtonMenuItem winpy,winsystem,linux,macos;
    public static JRadioButtonMenuItem custom;
    
    //help
    public static JMenuItem about,license;
    
    //tree popup
    public static JMenuItem newfile,editfile, newfolder, openfile, newpython,newxml;
    
    public static JTree tree;
    public static DefaultMutableTreeNode workspace;
    
    public static JScrollPane scrollpane;
	public static JScrollPane scrollpane2,scrollpane3;
    public static JTextPane textpane;
    public static JTextPane textarea2;
    
    public static ArrayList<JButton> actions = new ArrayList<JButton>();
    public static ArrayList<JButton> autoscripts = new ArrayList<JButton>();
    
    public static JTabbedPane tabs;
	public static Object lineNumberingTextArea;

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
        
        textpane = new JTextPane(initDocument());
        textpane.setFont(new Font(textpane.getFont().getName(), Font.TRUETYPE_FONT, 16));
        textpane.setEnabled(false);
        textpane.setComponentPopupMenu(textareapopup);;
           
        textarea2 = new JTextPane();
        textarea2.setFont(new Font(textpane.getFont().getName(), Font.TRUETYPE_FONT, 16));
        textarea2.setEnabled(false);
        
        textpane.setEnabled(false);
        scrollpane = new JScrollPane(textpane);
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
			
			Stream.loadinterpreterpath();
			
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
        python = new JMenu("Set Python interpreter");
        python.addActionListener(new Action()); 
        
        ButtonGroup group = new ButtonGroup();
        
        winpy = new JRadioButtonMenuItem("Windows - OPE build in (Python\\python.exe)");
        winpy.setEnabled(false);
        winpy.addActionListener(new Action());
        group.add(winpy);
        // Python/ folder exist
        if (new File("Python").exists() && Stream.isWindows())
        	winpy.setEnabled(true);
        python.add(winpy);
        
        winsystem = new JRadioButtonMenuItem("Windows - System (python.exe)");
        winsystem.setEnabled(false);
        winsystem.addActionListener(new Action());
        group.add(winsystem);
        if (Stream.isWindows())
        	winsystem.setEnabled(true);
        python.add(winsystem);
        
        linux = new JRadioButtonMenuItem("Linux (python3.7)");
        linux.setEnabled(false);
        linux.addActionListener(new Action());
        group.add(linux);
        if (Stream.isLinux())
        	linux.setEnabled(true);
        python.add(linux);
        
        macos = new JRadioButtonMenuItem("MacOS (python)");
        macos.setEnabled(false);
        macos.addActionListener(new Action());
        group.add(macos);
        if (Stream.isMac())
        	macos.setEnabled(true);
        python.add(macos);
        
        python.add(new JSeparator());
        
        custom = new JRadioButtonMenuItem("Custom");
        custom.addActionListener(new Action());
        group.add(custom);
        python.add(custom);
        
        settings.add(python);
        
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
	
	private DefaultStyledDocument initDocument()
	{
        final StyleContext cont = StyleContext.getDefaultStyleContext();
        final AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
        final AttributeSet attrgreen = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.GREEN);
        final AttributeSet attrgray = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.GRAY);
        final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        DefaultStyledDocument doc = new DefaultStyledDocument()
        {
			private static final long serialVersionUID = 1L;

			public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                        if (text.substring(wordL, wordR).matches("(\\W)*(and|as|break|class|continue|def|del|elif|else|except|False|finally|for|from|global|if|import|in|is|lambda|None|nonlocal|not|or|pass|raise|return|True|try|while|with|yield)"))
                            setCharacterAttributes(wordL, wordR - wordL, attr, false);
                        else if (text.substring(wordL, wordR).matches("(\\W)*(#)"))
                            setCharacterAttributes(wordL, wordR - findNonWordChar("\n",text, str.length()), attrgreen, false);
                        else if (text.substring(wordL, wordR).matches("(\\W)*(ope|slide)"))
                            setCharacterAttributes(wordL,wordR - wordL, attrgray, false);
                        else
                            setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                        wordL = wordR;
                    }
                    wordR++;
                }
            }

            public void remove (int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offs);

                if (text.substring(before, after).matches("(\\W)*(and|as|break|class|continue|def|del|elif|else|except|False|finally|for|from|global|if|import|in|is|lambda|None|nonlocal|not|or|pass|raise|return|True|try|while|with|yield)")) {
                    setCharacterAttributes(before, after - before, attr, false);
                } else {
                    setCharacterAttributes(before, after - before, attrBlack, false);
                }
            }
        };
        
        return doc;
	}
	
    private int findLastNonWordChar (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }
    
    private int findNonWordChar (String c,String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches(c)) {
                break;
            }
            index++;
        }
        return index;
    }
	
//	public static void initUn
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