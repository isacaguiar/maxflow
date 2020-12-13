package org.jbpm.gpd.view;

import java.awt.Color;

import org.jbpm.gpd.cell.ActivityCell;
import org.jbpm.gpd.cell.ActivityDecisionCell;
import org.jbpm.gpd.renderer.DecisionRenderer;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexView;

public class ActivityDecisionView extends VertexView {

	public DecisionRenderer renderer = new DecisionRenderer();

	public ActivityDecisionView(Object cell) {
		super(cell);
		ActivityDecisionCell sub = (ActivityDecisionCell) cell;
		this.setBackColor(sub.getModel().getBackColor());
	}

	public CellViewRenderer getRenderer() {
		renderer.setBackColor(((ActivityCell) cell).getModel().getBackColor());
		return renderer;
	}

    public void setBackColor(Color color) {
        this.renderer.setBackColor(color);
    }

}
