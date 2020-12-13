package org.jbpm.gpd.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.jbpm.gpd.ProcessDesigner;

import eflow.config.Configuration;

/**
 * Definie o look and feel do tipo do Sistema Operacional
 * @author Isac Velozo Aguiar
 *
 */
public class LookAndFeelSystem extends AbstractAction {
	
	/** The short descriptive name = "tilt graph" */
	public static final String NAME_VALUE = "Sistema Operacional";

	/** Short description for tool-tip */
	public static final String SHORT_DESCRIPTION_VALUE = "new graph";

	public LookAndFeelSystem(){
		super( NAME_VALUE );
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			Configuration config = new Configuration();
			String nomeArquivo = config.CONFIGURATION_FILE;
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JOptionPane.showMessageDialog(null,"Reinicie o sistema " +
										"para que as alterações sejam realizadas!");
			//UIManager.getCrossPlatformLookAndFeelClassName()
			boolean retorno = config.writeProperties(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,"Erro:" +ex);
		}
	}
	
	
	public static void main(String args[]) {
		try {
			
			System.out.println(Configuration.getIpServer());
            String caminho = System.getProperty("user.dir");
            caminho+=System.getProperty("file.separator");
            caminho+="resources";
            caminho+=System.getProperty("file.separator");
            caminho+="configuration.properties";
            System.out.println(caminho);
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
