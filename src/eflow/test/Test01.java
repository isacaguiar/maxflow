/*
 * Created on 11/03/2005
 *
 */
package eflow.test;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Victor
 *
 */
public class Test01 extends JFrame {

    

    public Test01()
    {
        setTitle( "RTF Text Application" );
        setSize( 400, 240 );
        setBackground( Color.gray );
        getContentPane().setLayout( new BorderLayout() );

        JPanel topPanel = new JPanel();
        topPanel.setLayout( new BorderLayout() );
        getContentPane().add( topPanel, BorderLayout.CENTER );
        JEditorPane jep = new JEditorPane("text/rtf","the text inside");
        
        jep.show();
    }

    public static void main(String[] args){
        new Test01();
    }
}
