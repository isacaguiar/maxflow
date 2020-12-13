/*
 * Created on 04/04/2005
 *
 */
package eflow.remote.session;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.jbpm.JpdlException;
import org.jbpm.gpd.GpdGraph;
import org.jbpm.gpd.action.ExportJBPMFile;

import eflow.config.Configuration;
import eflow.ejb.DefinitionSessionRemote;
import eflow.ejb.ServiceLocator;
import eflow.model.user.User;
import eflow.remote.Remote;

/**
 * @author Victor
 *
 */
public class SessionRemote implements Remote {
    
    
    private static final SessionRemote sessionRemote = new SessionRemote();
    
    private SessionRemote() {
        
    }
    
    public static SessionRemote getInstance() {
        return sessionRemote;
    }
    
    
    
    public ServiceLocator getServiceLocator() throws IOException {
        String ipServer = Configuration.getIpServer();
        System.out.println("Server at: "+ipServer);
        ServiceLocator j2eeServiceLocator = new ServiceLocator("org.jnp.interfaces.NamingContextFactory", 
                "jnp://"+ipServer+":1099/",
        "org.jboss.naming:org.jnp.interfaces");
        
        return j2eeServiceLocator;
    }
    
    
    
    public void deployProcessArchive(GpdGraph graph) throws IOException, JpdlException {
        DefinitionSessionRemote definitionSession = (DefinitionSessionRemote) this.getServiceLocator().getService(DefinitionSessionRemote.class);
        String temp = System.getProperty("java.io.tmpdir") + File.separator + "process.par";
        int bufferSize = 1024*512; 
        //tirando as ports da imagem
        graph.setPortsVisible(false);
        File file = new File(temp);
        ExportJBPMFile jbpm = new ExportJBPMFile(graph);
        jbpm.doExport(file);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(file);
        
        byte b[] = new byte[bufferSize];
        int i;
        
        while ((i = fis.read(b)) != -1)
        {
            baos.write(b, 0, i);
        }
        fis.close();
        baos.close();
        definitionSession.deployProcessArchive( baos.toByteArray() );
    }
    
    
    public List getAllRole(User user) throws IOException {
        DefinitionSessionRemote definitionSession = (DefinitionSessionRemote) this.getServiceLocator().getService(DefinitionSessionRemote.class);
        return definitionSession.getAllRole();        
    }
    
    public List getAllProcessType() throws IOException {
        DefinitionSessionRemote definitionSession = (DefinitionSessionRemote) this.getServiceLocator().getService(DefinitionSessionRemote.class);
        return definitionSession.getAllProcessType();        
    }

	public boolean login(User user) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
    
}
