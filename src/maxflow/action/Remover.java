package maxflow.action;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.TransferHandler;

import org.jbpm.gpd.GpdGraph;

public class Remover extends AbstractAction {
	
	
	public static final String NAME_VALUE = "Excluir";
	
	public static final int MNEMONIC_CODE = KeyEvent.VK_CONTROL;

	public static final String LONG_DESCRIPTION_VALUE = "Excluir";

	public final Icon SMALL_ICON_VALUE = 
	new ImageIcon(getClass().getClassLoader().getResource( "gif/delete.gif" ));

	public static final String SHORT_DESCRIPTION_VALUE = "Excluir";

	public final static Rectangle defaultBounds = new Rectangle(10, 10, 20, 20);
	
	private GpdGraph graphpad;
	
	public Remover(GpdGraph graph) {
		//putValue( MNEMONIC_KEY, new Integer( MNEMONIC_CODE ));
		super( NAME_VALUE );
		
		TransferHandler.getCutAction();
		putValue( LONG_DESCRIPTION, LONG_DESCRIPTION_VALUE );
		putValue( SMALL_ICON, SMALL_ICON_VALUE );
		putValue( SHORT_DESCRIPTION, SHORT_DESCRIPTION_VALUE );
		this.graphpad = graph;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (!graphpad.isSelectionEmpty()) {
			Object[] cells = graphpad.getSelectionCells();
			cells = graphpad.getDescendants(cells);
			graphpad.getModel().remove(cells);
		}
	}

}
