package maxflow.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import eflow.config.Configuration;

/**
 * Definie o look and feel do tipo do Sistema Operacional
 * @author Isac Velozo Aguiar
 *
 */
public class LookAndFeelSistem extends AbstractAction {
	
	/** The short descriptive name = "tilt graph" */
	public static final String NAME_VALUE = "Estilo Windows";

	/** Short description for tool-tip */
	public static final String SHORT_DESCRIPTION_VALUE = "new graph";

	public LookAndFeelSistem(){
		super( NAME_VALUE );
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			Configuration config = new Configuration();
			String nomeArquivo = config.CONFIGURATION_FILE;
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JOptionPane.showMessageDialog(null,"Reinicie o sistema " +
										"para utilizar a apresetação Estilo Windows!");
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
