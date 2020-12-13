package org.jbpm.gpd.view;

import java.awt.Color;

import org.jbpm.gpd.cell.SubProcessCell;
import org.jbpm.gpd.renderer.SubProcessRenderer;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexView;

public class SubProcessView extends VertexView {

	public SubProcessRenderer renderer = new SubProcessRenderer();

	public SubProcessView(Object cell) {
		super(cell);
		SubProcessCell sub = (SubProcessCell) cell;
		this.setBackColor(sub.getModel().getBackColor());
	}

	public CellViewRenderer getRenderer() {
		if (renderer==null)
			renderer= new SubProcessRenderer();
		return renderer;
	}
	
	public void setBackColor(Color color) {
		this.renderer.setBackColor(color);
	}
}
