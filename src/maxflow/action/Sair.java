package maxflow.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.jbpm.gpd.GpdGraph;
import org.jbpm.gpd.ProcessDesigner;
import org.jbpm.gpd.action.SaveAction;

import eflow.splash.EflowFrame;

public class Sair extends AbstractAction {
	
	public static final String NAME_VALUE = "Sair";
	
	private ProcessDesigner process;
	
	public final Icon SMALL_ICON_VALUE = new ImageIcon(getClass()
			.getClassLoader().getResource("gif/logout.gif"));
	
	public Sair(ProcessDesigner p) {
		//putValue( MNEMONIC_KEY, new Integer( MNEMONIC_CODE ));
		super( NAME_VALUE );
		putValue(SMALL_ICON, SMALL_ICON_VALUE);
		this.process = p;
	}
	
	public void actionPerformed(ActionEvent e) {
		int result = JOptionPane.showConfirmDialog(process, "Salvar alterações?","E-flow Editor", JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.YES_OPTION){
			//salvar
			new SaveAction(process.getGraph()).actionPerformed(null);
		}
		System.exit(0);
	}
}
