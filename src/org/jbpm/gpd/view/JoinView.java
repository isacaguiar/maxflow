
package org.jbpm.gpd.view;


import org.jbpm.gpd.cell.JoinCell;
import org.jbpm.gpd.renderer.JoinRenderer;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexView;

public class JoinView extends VertexView {

	public static JoinRenderer renderer = new JoinRenderer();

	public JoinView(Object cell) {
		super(cell);
	}

	public CellViewRenderer getRenderer() {
		return renderer;
	}

	
}
