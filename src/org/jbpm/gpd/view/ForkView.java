
package org.jbpm.gpd.view;


import org.jbpm.gpd.cell.ForkCell;
import org.jbpm.gpd.renderer.ForkRenderer;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexView;

public class ForkView extends VertexView {

	public static ForkRenderer renderer = new ForkRenderer();

	public ForkView(Object cell) {
		super(cell);
	}

	public CellViewRenderer getRenderer() {
		return renderer;
	}

	
    
    
}
