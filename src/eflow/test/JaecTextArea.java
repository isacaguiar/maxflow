/*
 * Created on 11/03/2005
 *
 */
package eflow.test;
/*
 * JaecTextArea.java
 *
 * Copyright (c) 2004, Joel Espinosa All rights reserved.
 *
 * This is free software; you can redistribute and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software 
 * Foundation
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on 7 de julio de 2004, 04:38 PM
 *
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;

/**
 * @author  Joel Espinosa
 */
public class JaecTextArea extends JTextPane {
    private SimpleAttributeSet style = null;
    private Image backgroundImage = null;
    private final JaecTextArea textarea = this;
    
    /** Crea una nueva instancia de JaecTextArea */
    public JaecTextArea() {
        super(new JaecDocument());
        initStyle();
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        addMouseListener(new JaecPopupListener() {
            public JPopupMenu createPopup() {
                JPopupMenu popup = new JPopupMenu();
                JMenuItem copyMenuItem = new JMenuItem("Copiar");
                JMenuItem cutMenuItem = new JMenuItem("Cortar");
                JMenuItem pasteMenuItem = new JMenuItem("Pegar");
                JMenuItem deleteMenuItem = new JMenuItem("Eliminar");     
                JMenuItem selectAllMenuItem = new JMenuItem("Seleccionar todo");
                
                popup.add(copyMenuItem);
                popup.add(cutMenuItem);
                popup.add(pasteMenuItem);
                popup.add(deleteMenuItem);
                popup.add(selectAllMenuItem);
                cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 
                                           ActionEvent.CTRL_MASK));
                cutMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        textarea.cut();
                    }
                });
                copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, 
                                            ActionEvent.CTRL_MASK));
                copyMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        textarea.copy();
                    }
                });
                pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, 
                                             ActionEvent.CTRL_MASK));
                pasteMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        textarea.paste();
                    }
                });
                deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,
                                              0));
                deleteMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if(textarea.getSelectedText() != null) {
                            try {
                                getDocument().remove(textarea.getSelectionStart(), 
                                textarea.getSelectedText().length());
                            } catch(BadLocationException ex) { ex.printStackTrace(); }
                        }
                    }
                });
                selectAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, 
                                                 ActionEvent.CTRL_MASK));
                selectAllMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        textarea.selectAll();
                    }
                });
                
                return popup;
            }
        });
    }
    
    private void initStyle() {
        style = new SimpleAttributeSet();
        
        StyleConstants.setFontFamily(style, "Arial");
        StyleConstants.setFontSize(style, 12);
        StyleConstants.setForeground(style, Color.BLACK);
        StyleConstants.setUnderline(style, false);
        StyleConstants.setBold(style, false);
        StyleConstants.setItalic(style, false);
    }
    
    /**
     * Método que agrega texto con formato al final del texto existente en el 
     * JaecTextArea
     *
     * @param text La cadena a insertar
     * @param font El tipo de fuente, el tamaño, negrita, cursiva, etc.
     * @param underline Especifica si el texto será subrrayado
     * @param color El color del texto a insertar
     */
    public void append(String text, Font font, boolean underline, Color color) {
        StyleConstants.setFontFamily(style, font.getFamily());
        StyleConstants.setFontSize(style, font.getSize());
        StyleConstants.setForeground(style, color);
        StyleConstants.setUnderline(style, underline);
        switch(font.getStyle()) {
            case Font.BOLD:
                StyleConstants.setBold(style, true);
                break;
            case Font.ITALIC:
                StyleConstants.setItalic(style, true);
                break;
            case Font.BOLD + Font.ITALIC:
                StyleConstants.setBold(style, true);
                StyleConstants.setItalic(style, true);
                break;
            case Font.PLAIN:
            default:
                StyleConstants.setBold(style, false);
                StyleConstants.setItalic(style, false);
                StyleConstants.setUnderline(style, false);
        }
        try {
            getDocument().insertString(getDocument().getLength(), text, style);
            setCaretPosition(getDocument().getLength());
        } catch(BadLocationException e) { e.printStackTrace(); }
        initStyle();
    }
    
    /**
     * Método que agrega texto con formato al final del texto existente en el
     * JaecTextArea
     *
     * @param text La cadena a insertar
     * @param _style Los atributos del formato del texto a insertar
     */
    public void append(String text, SimpleAttributeSet _style) {
        try {
            getDocument().insertString(getDocument().getLength(), text, _style);
            setCaretPosition(getDocument().getLength());
        } catch(BadLocationException e) { e.printStackTrace(); }
    }
    
    /**
     * Método que agrega texto con el formato por default al final del texto 
     * existente en el JaecTextArea
     *
     * @param text La cadena a insertar
     */
    public void append(String text) {
        try {
            getDocument().insertString(getDocument().getLength(), text, style);
            setCaretPosition(getDocument().getLength());
        } catch(BadLocationException e) { e.printStackTrace(); }
    }
    
    /**
     * Método que agrega una imagen al final del texto actual en el JaecTextArea
     *
     * @param file La ruta del archivo de imagen a insertar, es relativa a la
     * ruta donde se encuentra ésta clase
     */
    public void insertImage(String file) {
        try {
            insertIcon(new ImageIcon(JaecTextArea.class.getResource(file)));
        }catch(Exception e) {}
    }
    
    /** 
     * Clase abstracta usada para mostrar el menú emergente (popup) con el clic
     * del botón derecho del mouse 
     */
    abstract class JaecPopupListener extends MouseAdapter {
    
        public void mousePressed(MouseEvent e) {
            showPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            showPopup(e);
        }

        public abstract JPopupMenu createPopup();

        protected void showPopup(MouseEvent e) {
            if(e.isPopupTrigger()) {
                createPopup().show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
    
    /** Un ejemplo de cómo usar el JaecTextArea... */
    public static void main(String args[]) {
        JFrame frame = new JFrame("Test del JaecTextArea");
        JaecTextArea textarea = new JaecTextArea();
        
        textarea.append("Hello World!\n", new Font("Times New Roman", 
                        Font.BOLD + Font.ITALIC, 14), true, new Color(255, 0, 0));
        
        textarea.append("Hola Mundo!\n", new Font("Arial", Font.PLAIN, 10), 
                        false, new Color(0, 0, 200));
        
        textarea.insertImage("jc.jpg");
        
        textarea.append("fin\n");
        
        JPanel noWrapPanel = new JPanel();
        noWrapPanel.setLayout(new BorderLayout());
        noWrapPanel.add(textarea);
        JScrollPane pane = new JScrollPane(noWrapPanel);
        frame.getContentPane().add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setVisible(true);
    }

}

/**
 * Clase que define un documento personalizado para el JaecTextArea, en el cual
 * es posible modificar la forma en cómo se inserta y elimina texto del 
 * JaecTextArea
 */
class JaecDocument extends DefaultStyledDocument {
    private int counter = 1;

    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException  {
        System.out.println(counter++ + " Insertando en el JaecTextArea...");
        super.insertString(offs, str, a);
    }
    
    public void remove(int offs, int len) throws BadLocationException {
        System.out.println(counter++ + " Removiendo del JaecTextArea...");
        super.remove(offs, len);
    }
        
}
