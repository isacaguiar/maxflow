package eflow.gpd.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.jbpm.gpd.GpdGraph;

import eflow.remote.session.SessionRemote;

public class DeployAction extends AbstractAction{
    /** The short descriptive name = "Print Preview  ..." */
    public static final String NAME_VALUE = "Exportar processo para o E-Flow...";
    
    /** The mnemonic keycode = KeyEvent.VK_W */
    public static final int MNEMONIC_CODE = KeyEvent.VK_X;
    
    /** The long description text of this action */
    public static final String LONG_DESCRIPTION_VALUE = 
        "Deploy process to server.";
    
    /** The icon for the toolbar */
    public final Icon SMALL_ICON_VALUE = 
        new ImageIcon(getClass().getClassLoader().getResource
                ( "gif/export.gif" ));
    
    /** Short description for tool-tip */
    public static final String SHORT_DESCRIPTION_VALUE = "Deploy process to server.";
    
    private GpdGraph graph;
    
    public DeployAction( GpdGraph graph )
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
        //local
        /*
        JbpmConfiguration jbpmConfiguration = new JbpmConfiguration("jbpm.properties");
        JbpmServiceFactory serviceLocator = new JbpmServiceFactory( jbpmConfiguration );
        DefinitionService definitionService = serviceLocator.openDefinitionService();
        
        
        String temp = System.getProperty("java.io.tmpdir") + File.separator + "process.par";
        try {
            //tirando as ports da imagem
            graph.setPortsVisible(false);
            File file = new File(temp);
            ExportJBPMFile jbpm = new ExportJBPMFile(graph);
            jbpm.doExport(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream finstr = new FileInputStream( file );
            JarInputStream jarInputStream = new JarInputStream( finstr );
            definitionService.deployProcessArchive( jarInputStream );
            
            
            JOptionPane.showMessageDialog(null, "Deploy realizado com sucesso.");
            
        }catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro no deploy do processo: " + ex.getMessage());
        }finally {
            definitionService.close();
            graph.setPortsVisible(true);
        }
        */
        
        try {
            SessionRemote.getInstance().deployProcessArchive(graph);
            JOptionPane.showMessageDialog(graph, "Deploy realizado com sucesso.");
        }catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(graph, "Erro no deploy do processo: " + ex.getMessage());
        }finally {
            graph.setPortsVisible(true);
        }
        //*/
    }
    
    
    
}
