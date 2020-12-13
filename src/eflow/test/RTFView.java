/*
 * Created on 11/03/2005
 *
 */
package eflow.test;

import java.awt.*;
import java.io.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.rtf.*;

class RTFView
        extends     JFrame
{
    public RTFView() throws IOException
    {
        setTitle( "RTF Text Application" );
        setSize( 400, 240 );
        setBackground( Color.gray );
        getContentPane().setLayout( new BorderLayout() );

        JPanel topPanel = new JPanel();
        topPanel.setLayout( new BorderLayout() );
        getContentPane().add( topPanel, BorderLayout.CENTER );

        // Create an RTF editor window
        RTFEditorKit rtf = new RTFEditorKit();
        
        
        JEditorPane editor = new JEditorPane("text/rtf", "");
        
        editor.setEditorKit( rtf );
        editor.setBackground( Color.white );
        
        // This text could be big so add a scroll pane
        JScrollPane scroller = new JScrollPane();
        scroller.getViewport().add( editor );
        topPanel.add( scroller, BorderLayout.CENTER );

        // Load an RTF file into the editor
        try {
            FileInputStream fi = new FileInputStream( "c:\\test.rtf" );
            rtf.read( fi, editor.getDocument(), 0 );
        }
        catch( FileNotFoundException e )
        {
            System.out.println( "File not found" );
        }
        catch( IOException e )
        {
            System.out.println( "I/O error" );
        }
        catch( BadLocationException e )
        {
        }
    }

    public static void main( String args[] )
    {
        // Create an instance of the test application
        RTFView mainFrame;
        try {
            mainFrame = new RTFView();
            mainFrame.setVisible( true );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}