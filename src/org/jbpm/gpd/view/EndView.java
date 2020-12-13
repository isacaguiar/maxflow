package org.jbpm.gpd.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

import org.jbpm.gpd.cell.EndCell;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;


public class EndView extends VertexView {
	
	public static final Color DEFAULT_BACKGROUND = new Color(190,0,0);
	
	public static EndRenderer renderer = new EndRenderer();

	public EndView(Object cell) {
		super(cell);
	}

	public CellViewRenderer getRenderer() {
		getAttributes().put("backColor", renderer.getBackColor());
		renderer.setBackColor(((EndCell) cell).getModel().getBackColor());
		//System.out.println(" START VIEW get renderer "+renderer.getBackColor());
		return renderer;
	}

	public static class EndRenderer extends VertexRenderer {
		
		private Color backColor = new Color(190,0,0);
		
		public void paint(Graphics g) {
			
			int b = borderWidth;
			Graphics2D g2 = (Graphics2D) g;
			
			Dimension d = getSize();
			
			int width = d.width + 10;
			int height = d.height + 10;
			
			boolean tmp = selected;
			
			
			//g2.setColor(getForeground());
			g2.setColor(getBackColor());
			g2.fillRect(b - 1, b - 1, d.width - b + 1, d.height - b + 1);
			g2.clearRect(borderWidth + 1, borderWidth + 1, d.width - b + 1 - 4, d.height - b + 1 - 4);
			g2.fillRect(b + 2, b + 2, d.width - b + 1 - 6, d.height - b + 1 - 6);
			
			
			try {
				setBorder(null);
				setOpaque(false);
				selected = false;
				super.paint(g);
			} finally {
				selected = tmp;
			}
		}

		public Color getBackColor() {
			return backColor;
		}

		public void setBackColor(Color backColor) {
			this.backColor = backColor;
		}
		
	}

}
