package eflow.gpd.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
 
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.jbpm.gpd.GpdGraph;

import eflow.model.user.User;
import eflow.remote.RemoteFactory;

public class ImportEflowAction extends AbstractAction {
    
	public static boolean CONECTADO = false;
    public static List roleList = null;
    public static List processTypeList = null;
    public static User user = null;

    //JbpmConfiguration jbpmConfiguration = new JbpmConfiguration("jbpm.properties");
    //JbpmServiceFactory serviceLocator = new JbpmServiceFactory( jbpmConfiguration );
    
    /** The short descriptive name = "Print Preview  ..." */
    public static final String NAME_VALUE = "Importar dados do E-Flow...";
    
    /** The mnemonic keycode = KeyEvent.VK_W */
    public static final int MNEMONIC_CODE = KeyEvent.VK_X;
    
    /** The long description text of this action */
    public static final String LONG_DESCRIPTION_VALUE = 
        "Importar dados do E-Flow.";
    
    /** The icon for the toolbar */
    public final Icon SMALL_ICON_VALUE = 
        new ImageIcon(getClass().getClassLoader().getResource
                ( "gif/import.gif" ));
    
    /** Short description for tool-tip */
    public static final String SHORT_DESCRIPTION_VALUE = "Importar dados do E-Flow.";
    
    private GpdGraph graph;
    
    public ImportEflowAction( GpdGraph graph )
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
        try {
            importRoleList();
            importProcessTypeList();
        	CONECTADO = true;
        }catch(Exception ex) {
        	CONECTADO = false;
            ex.printStackTrace();
            JOptionPane.showMessageDialog(graph, "Erro na importação dos dados: " + ex.getMessage());
        }finally {
            graph.setPortsVisible(true);
        }
        
    }
    
    
    public List importRoleList() throws Exception {
        roleList = RemoteFactory.getRemote().getAllRole(user);
        return roleList;
    }
    
    public List importCachedRoleList() throws Exception {
        if(roleList == null) {
            return importRoleList();
        }
        return roleList;
    }

    
    public List importProcessTypeList() throws Exception {
        processTypeList = RemoteFactory.getRemote().getAllProcessType();
        return processTypeList; 
    }
    
    
    public List importCachedProcessTypeList() throws Exception {
        if(processTypeList == null) {
            return importProcessTypeList();
        }
        return processTypeList;
    }
    
    
    
    
    
    
    /**
     * thread que carrega os dados do servidor do eflow para o editor
     * @author Victor
     *
     */
    public class ImportThread implements Runnable {
        public void run() {
            try {
                importProcessTypeList();
                importRoleList();
            	CONECTADO = true;
            } catch (Exception e) {
            	CONECTADO = false;
                e.printStackTrace();
                JOptionPane.showMessageDialog(graph, "Não foi possível importar os dados do servidor!");
            }
        }
    }
   
}
