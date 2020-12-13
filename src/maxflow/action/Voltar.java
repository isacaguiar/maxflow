package maxflow.action;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.TransferHandler;

import org.jbpm.gpd.GpdGraph;
import org.jgraph.graph.GraphUndoManager;

public class Voltar extends AbstractAction {
	
	protected GraphUndoManager undoManager;
	protected GpdGraph graph;
	protected Action undo;
	
	public static final String NAME_VALUE = "Voltar";
	
	public static final int MNEMONIC_CODE = KeyEvent.VK_CONTROL;

	public static final String LONG_DESCRIPTION_VALUE = "Voltar";

	public final Icon SMALL_ICON_VALUE = 
	new ImageIcon(getClass().getClassLoader().getResource( "gif/undo.gif" ));

	public static final String SHORT_DESCRIPTION_VALUE = "Voltar";

	public final static Rectangle defaultBounds = new Rectangle(10, 10, 20, 20);
	
	public Voltar(GpdGraph graph,GraphUndoManager undoManager) {
		
		super( NAME_VALUE );
		
		putValue( LONG_DESCRIPTION, LONG_DESCRIPTION_VALUE );
		putValue( SMALL_ICON, SMALL_ICON_VALUE );
		putValue( SHORT_DESCRIPTION, SHORT_DESCRIPTION_VALUE );
		
		this.graph = graph; 
		this.undoManager = undoManager;
		//this.undo = undo;
	}
	
	public void actionPerformed(ActionEvent e) {
		undo();
	}
	
	public void undo() {
		try {
			undoManager.undo(graph.getGraphLayoutCache());
		} catch (Exception ex) {
			System.err.println(ex);
		} finally {
			updateHistoryButtons();
		}
	}
	
	protected void updateHistoryButtons() {
		this.setEnabled(undoManager.canUndo(graph.getGraphLayoutCache()));
	}
}
