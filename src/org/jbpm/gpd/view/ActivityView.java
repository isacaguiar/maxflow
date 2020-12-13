package org.jbpm.gpd.view;

import java.awt.Color;

import org.jbpm.gpd.cell.ActivityCell;
import org.jbpm.gpd.renderer.ActivityRenderer;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexView;


/**
 * 
 * como estava implementado, existia apenas um activity renderer para todas
 * caixinhas de atividade, por causa do static
 * tirei o static
 * 
 **/

public class ActivityView extends VertexView {

	public ActivityRenderer renderer = new ActivityRenderer();

	public ActivityView(Object cell) {
		super(cell);
		ActivityCell sub = (ActivityCell) cell;
		this.setBackColor(sub.getModel().getBackColor());
	}

	public CellViewRenderer getRenderer() {
		if (renderer==null)
			renderer= new ActivityRenderer();
		return renderer;
	}
	
	public void setBackColor(Color color) {
		this.renderer.setBackColor(color);
	}
}
