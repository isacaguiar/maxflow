package org.jbpm.gpd.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexRenderer;

public class JoinRenderer extends VertexRenderer {

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Dimension d = getSize();
		g2.setColor(Color.WHITE);
		int width = d.width - 1;
		int height = d.height - 1;
		//retangulo que retira as bordas quando cria o objeto
		g.fillRect(borderWidth - 1, borderWidth - 1, width+1, height+1);
		boolean tmp = selected;
		if (super.isOpaque()) {
			g.setColor(super.getBackground());
			g2.draw(new Line2D.Double((d.width), 0, d.width, d.height ));
		}
		try {
			setBorder(null);
			setOpaque(false);
			selected = false;
			super.paint(g);
		} finally {
			selected = tmp;
		}
		//if (bordercolor != null) {
			g.setColor(bordercolor);
			g2.setStroke(new BasicStroke(4));
			g2.draw(new Line2D.Double(0, d.height, d.width, d.height ));
		//}
		if (selected) {
			g2.setStroke(GraphConstants.SELECTION_STROKE);
			g.setColor(graph.getHighlightColor());
			g2.draw(new Line2D.Double((d.width), 0, 0, d.height ));
		}
	}
}
