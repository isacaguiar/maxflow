package org.jbpm.gpd.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import org.jbpm.gpd.Configuration;
import org.jbpm.gpd.ExceptionHandler;
import org.jbpm.gpd.GpdGraph;
import org.jbpm.gpd.io.GraphModelFileFormat;
import org.jbpm.gpd.io.JBPMFileFormatXML;
import org.jbpm.gpd.io.SimpleFileFilter;

import eflow.gpd.io.EflowFileFormatXML;

public class ExportJBPMAction extends AbstractAction{
    /** The short descriptive name = "Print Preview  ..." */
    public static final String NAME_VALUE = "export processdefinition ...";
    
    /** The mnemonic keycode = KeyEvent.VK_W */
    public static final int MNEMONIC_CODE = KeyEvent.VK_X;
    
    /** The long description text of this action */
    public static final String LONG_DESCRIPTION_VALUE = 
        "Export to JBPM processdefinition.xml";
    
    /** The icon for the toolbar */
    public final Icon SMALL_ICON_VALUE = 
        new ImageIcon(getClass().getClassLoader().getResource
                ( "gif/export.gif" ));
    
    /** Short description for tool-tip */
    public static final String SHORT_DESCRIPTION_VALUE = "Export to JBPM processdefinition.xml";
    
    private GpdGraph graph;
    
    public ExportJBPMAction( GpdGraph graph )
    {
        super( NAME_VALUE );
        
        // Setup action parameters
        putValue( MNEMONIC_KEY, new Integer( MNEMONIC_CODE ));
        putValue( LONG_DESCRIPTION, LONG_DESCRIPTION_VALUE );
        putValue( SMALL_ICON, SMALL_ICON_VALUE );
        putValue( SHORT_DESCRIPTION, SHORT_DESCRIPTION_VALUE );
        
        this.graph = graph;
        setEnabled( true );
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        JFileChooser jc = new JFileChooser();
        jc.setDialogTitle("Export File");
        jc.addChoosableFileFilter(new SimpleFileFilter("xml","\" *.xml \" Process Definition File"));
        String lastFile=(String)Configuration.getInstance().getProperty(Configuration.LAST_SELECTED_PROCESSEXPORT_FILE);
        if (lastFile!=null){
            jc.setSelectedFile(new File(lastFile));
        }
        int result = jc.showSaveDialog(graph);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = jc.getSelectedFile();
            if ( file.getName().indexOf('.') < 0 )
                file = new File(file+".xml");
            export(file);
            Configuration.getInstance().setProperty(Configuration.LAST_SELECTED_PROCESSEXPORT_FILE,file.toString());
        }
    }
    
    protected void export(File file)
    {
        try {
            GraphModelFileFormat graphModelFileFormat=new EflowFileFormatXML();
            Hashtable props = new Hashtable();
            graphModelFileFormat.write(file.toURL(), props, graph, graph.getModel());
        } catch (Exception e2) {
            ExceptionHandler.getInstance().handleException(e2);
        }
    }
    
    protected List getClasses(GpdGraph gpGraph)
    {
        try {
            return new EflowFileFormatXML().getClasses(gpGraph);
        } catch (Exception e2) {
            ExceptionHandler.getInstance().handleException(e2);
        }
        return null;
    }
}
