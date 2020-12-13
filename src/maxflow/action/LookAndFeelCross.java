package maxflow.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import eflow.config.Configuration;

/**
 * Classe que define o look and feel do tipo Metal
 * @author Isac Velozo Aguiar
 *
 */
public class LookAndFeelCross extends AbstractAction {
	
	/** The short descriptive name = "tilt graph" */
	public static final String NAME_VALUE = "Estilo Aqua";

	/** Short description for tool-tip */
	public static final String SHORT_DESCRIPTION_VALUE = "new graph";

	public LookAndFeelCross(){
		super( NAME_VALUE );
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			Configuration config = new Configuration();
			String nomeArquivo = config.CONFIGURATION_FILE;
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JOptionPane.showMessageDialog(null,"Reinicie o sistema " +
										"para utilizar a apresetação Estilo Aqua!");
			//UIManager.getCrossPlatformLookAndFeelClassName()
			boolean retorno = config.writeProperties(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,"Erro:" +ex);
		}
	}
}

