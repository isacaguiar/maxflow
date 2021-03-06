package eflow.rtf;

/*
 * Copyright (c) 2003 Sun Microsystems, Inc. All  Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * -Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduct the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT
 * BE LIABLE FOR ANY DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT
 * OF OR RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN
 * IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that Software is not designed, licensed or intended for
 * use in the design, construction, operation or maintenance of any nuclear
 * facility.
 */

/*
 * @(#)Stylepad.java	1.17 03/01/23
 */


import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.text.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.*;

import org.jbpm.gpd.dialog.controller.MasterDetailCellController;

import java.io.*;

/**
 * Sample application using JTextPane.
 *
 * @author Timothy Prinzing
 * @version 1.17 01/23/03
 */
public class Stylepad extends Notepad {
	
	private MasterDetailCellController controller;
    
    private static ResourceBundle resources;
    JTextArea textArea = null;
	private String title = "";
    
    static {
        try {
            resources = ResourceBundle.getBundle("eflow.rtf.resources.Stylepad");
        } catch (MissingResourceException mre) {
            System.err.println("Stylepad.properties not found");
            System.exit(0);
        }
    }

    
    public Stylepad() {
        super();
    }
    
    public Stylepad(String title, JTextArea textArea, MasterDetailCellController controller) {
        this();
        this.controller = controller;
        this.textArea = textArea;
        this.title = title;
        getEditor().setText(this.textArea.getText());
    }
	
	public void open() {
		String vers = System.getProperty("java.version");
        if (vers.compareTo("1.1.2") < 0) {
            System.out.println("!!!WARNING: Swing must be run with a " +
            "1.1.2 or higher version VM!!!");
        }
        JFrame frame = new JFrame();
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
				new SaveAction(textArea).actionPerformed(null);
				//textArea.setText("teste");
            }
        });
		
        frame.setTitle(title);
        frame.setBackground(Color.lightGray);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add("Center", this);
        //frame.addWindowListener(new AppCloser());
        frame.pack();
        frame.setSize(600, 480);
        //frame.show();
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);		
	}
    
    public static void main(String[] args) {
        new Stylepad().open();
    }
    
    /**
     * Fetch the list of actions supported by this
     * editor.  It is implemented to return the list
     * of actions supported by the superclass
     * augmented with the actions defined locally.
     */
    public Action[] getActions() {
        Action[] defaultActions = {
                new NewAction(),
                new OpenAction(),
                new SaveAction(this.textArea),
                new StyledEditorKit.FontFamilyAction("font-family-LucidaSans", 
                "Lucida Sans"),
                new StyledEditorKit.FontFamilyAction("font-family-Arial", 
                "Arial"),
        };
        Action[] a = TextAction.augmentList(super.getActions(), defaultActions);
        return a;
    }
    
    /**
     * Try and resolve the resource name in the local
     * resource file, and if not found fall back to
     * the superclass resource file.
     */
    protected String getResourceString(String nm) {
        String str;
        try {
            str = this.resources.getString(nm);
        } catch (MissingResourceException mre) {
            str = super.getResourceString(nm);
        }
        return str;
    }
    
    /**
     * Create an editor to represent the given document.  
     */
    protected JTextComponent createEditor() {
        StyleContext sc = new StyleContext();
        DefaultStyledDocument doc = new DefaultStyledDocument(sc);
        //initDocument(doc, sc);
        //JTextPane p = new JTextPane(doc);
        JTextPane p = new JTextPane();
		//setando a fonte padr�o do editor rtf
		p.setFont(new Font("Arial", Font.ITALIC, 10));
        RTFEditorKit rtf = new RTFEditorKit();
        p.setEditorKit(rtf);
        p.setDragEnabled(true);
        
        //p.getCaret().setBlinkRate(0);
        
        return p;
    }
    
    /**
     * Create a menu for the app.  This is redefined to trap 
     * a couple of special entries for now.
     */
    protected JMenu createMenu(String key) {
        if (key.equals("color")) {
            return createColorMenu();
        } 
        return super.createMenu(key);
    }
    
    
    // this will soon be replaced
    JMenu createColorMenu() {
        ActionListener a;
        JMenuItem mi;
        JMenu menu = new JMenu(getResourceString("color" + labelSuffix));
		
		//mi = new JMenuItem(resources.getString("Black"));
		mi = new JMenuItem("Black");
        mi.setHorizontalTextPosition(JButton.RIGHT);
        mi.setIcon(new ColoredSquare(Color.black));
        a = new StyledEditorKit.ForegroundAction("set-foreground-black", Color.black);
        //a = new ColorAction(se, Color.red);
        mi.addActionListener(a);
		menu.add(mi);
		
        mi = new JMenuItem(resources.getString("Red"));
        mi.setHorizontalTextPosition(JButton.RIGHT);
        mi.setIcon(new ColoredSquare(Color.red));
        a = new StyledEditorKit.ForegroundAction("set-foreground-red", Color.red);
        //a = new ColorAction(se, Color.red);
        mi.addActionListener(a);
        menu.add(mi);
        mi = new JMenuItem(resources.getString("Green"));
        mi.setHorizontalTextPosition(JButton.RIGHT);
        mi.setIcon(new ColoredSquare(Color.green));
        a = new StyledEditorKit.ForegroundAction("set-foreground-green", Color.green);
        //a = new ColorAction(se, Color.green);
        mi.addActionListener(a);
        menu.add(mi);
        mi = new JMenuItem(resources.getString("Blue"));
        mi.setHorizontalTextPosition(JButton.RIGHT);
        mi.setIcon(new ColoredSquare(Color.blue));
        a = new StyledEditorKit.ForegroundAction("set-foreground-blue", Color.blue);
        //a = new ColorAction(se, Color.blue);
        mi.addActionListener(a);
        menu.add(mi);
        
        return menu;
    }
    
    void initDocument(DefaultStyledDocument doc, StyleContext sc) {
        Wonderland w = new Wonderland(doc, sc);
        //HelloWorld h = new HelloWorld(doc, sc);
        Icon alice = new ImageIcon(resources.getString("aliceGif"));
        w.loadDocument();
    }
    
	

	JComboBox createFamilyChoices() {
        JComboBox b = new JComboBox();
        String[] fonts = getToolkit().getFontList();
        for (int i = 0; i < fonts.length; i++) {
            b.addItem(fonts[i]);
        }
        return b;
    }
    
    /**
     * Trys to read a file which is assumed to be a 
     * serialization of a document.
     */
    class OpenAction extends AbstractAction {
        
        OpenAction() {
            super(openAction);
        }
        
        public void actionPerformed(ActionEvent e) {
            Frame frame = getFrame();
            if (fileDialog == null) {
                fileDialog = new FileDialog(frame);
            }
            fileDialog.setMode(FileDialog.LOAD);
            fileDialog.show();
            
            String file = fileDialog.getFile();
            if (file == null) {
                return;
            }
            String directory = fileDialog.getDirectory();
            File f = new File(directory, file);
            if (f.exists()) {
                try {
                    FileInputStream fin = new FileInputStream(f);
                    ObjectInputStream istrm = new ObjectInputStream(fin);
                    Document doc = (Document) istrm.readObject();
                    if(getEditor().getDocument() != null)
                        getEditor().getDocument().removeUndoableEditListener
                        (undoHandler);
                    getEditor().setDocument(doc);
                    doc.addUndoableEditListener(undoHandler);
                    resetUndoManager();
                    frame.setTitle(file);
                    validate();
                } catch (IOException io) {
                    // should put in status panel
                    System.err.println("IOException: " + io.getMessage());
                } catch (ClassNotFoundException cnf) {
                    // should put in status panel
                    System.err.println("Class not found: " + cnf.getMessage());
                }
            } else {
                // should put in status panel
                System.err.println("No such file: " + f);
            }
        }
    }
    
    /**
     * Trys to write the document as a serialization.
     */
    class SaveAction extends AbstractAction {
        
        private JTextArea textArea = null;
        
        SaveAction(JTextArea textArea) {
            super(saveAction);
            this.textArea = textArea;
        }
        /*
        SaveAction(JTextArea textArea) {
            super(saveAction);
            this.textArea = textArea;
        }
        */
        public void actionPerformed(ActionEvent e) {
            Frame frame = getFrame();
            try {
                String file = System.getProperty("java.io.tmpdir") + File.separator + "objective.rtf";//   "c:\\objective.rtf";
                
                FileOutputStream fstrm = new FileOutputStream(file);
                //ObjectOutput ostrm = new ObjectOutputStream(fstrm);
                
                RTFEditorKit rtf = new RTFEditorKit();
                
                try {
                    rtf.write(fstrm, getEditor().getDocument(),0,0);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
                finally {
                    fstrm.close();
                }
                
                try {
                    FileInputStream fileInput = new FileInputStream(file);
                    DataInputStream in = new DataInputStream(fileInput);
                    StringBuffer str = new StringBuffer();
                    BufferedReader buff = new BufferedReader(new InputStreamReader(in));
                    
                    while (buff.ready()) {
                        try {
                            char ch[] = new char[1];
                            buff.read(ch);
                            str.append(ch);
                        }catch(Exception ex) {
                            break;
                        }
                    }
                    in.close();
                    fileInput.close();                    
                    
//                  String txt = RtfToHtml.convertRtfToHtmlWithLineBreak(str.toString());
                    
                    this.textArea.setText(str.toString());
                    controller.saveCurrent();
                    //Stylepad.textArea.setText(RtfToHtml.convertRtfToHtml(str.toString()));
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            } catch (IOException io) {
                // should put in status panel
                System.err.println("IOException: " + io.getMessage());
                io.printStackTrace();
            }
        }
    }
    
    /**
     * Creates an empty document.
     */
    class NewAction extends AbstractAction {
        
        NewAction() {
            super(newAction);
        }
        
        public void actionPerformed(ActionEvent e) {
            if(getEditor().getDocument() != null)
                getEditor().getDocument().removeUndoableEditListener
                (undoHandler);
            getEditor().setDocument(new DefaultStyledDocument());
            getEditor().getDocument().addUndoableEditListener(undoHandler);
            resetUndoManager();
            validate();
        }
    }
    
    class ColoredSquare implements Icon {
        Color color;
        public ColoredSquare(Color c) {
            this.color = c;
        }
        
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color oldColor = g.getColor();
            g.setColor(color);
            g.fill3DRect(x,y,getIconWidth(), getIconHeight(), true);
            g.setColor(oldColor);
        }
        public int getIconWidth() { return 12; }
        public int getIconHeight() { return 12; }
        
    }

	
}
