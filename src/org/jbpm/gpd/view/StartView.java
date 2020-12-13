package org.jbpm.gpd.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import org.jbpm.gpd.cell.StartCell;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;

import eflow.web.dwr.DWRService;

public class StartView extends VertexView {

	public static final Color DEFAULT_BACKGROUND = Color.black;

	public static StartRenderer renderer = new StartRenderer();

	public StartView(Object cell) {
		super(cell);
	}

	public CellViewRenderer getRenderer() {
		getAttributes().put("backColor", renderer.getBackColor());
		renderer.setBackColor(((StartCell) cell).getModel().getBackColor());
		 //System.out.println(" START VIEW get renderer"
				 			//+renderer.getBackColor());
		return renderer;
	}

	public static class StartRenderer extends VertexRenderer implements
			CellViewRenderer {
		private Color backColor = Color.black;

		public void paint(Graphics g) {
			int b = borderWidth;
			Dimension d = getSize();
			
			Graphics2D g2 = (Graphics2D) g;
			
			g.setColor(Color.WHITE);
			int width = d.width - 1;
			int height = d.height - 1;
			g.fillRect(borderWidth - 1, borderWidth - 1, width+1, height+1);

			boolean tmp = selected;

			g.setColor(getBackColor());
			g.fillOval(b - 1, b - 1, d.width - b + 1, d.height - b + 1);
			
			//g.clearRect(10, 10, 30, 30);
			g.setColor(Color.WHITE);
			Polygon p = new Polygon();
			
			p.addPoint(d.width/4, d.height/6);
			p.addPoint(d.width/4, d.height/6*5);
			p.addPoint(d.width/6*5, d.height/2);
			g.fillPolygon(p);
			
			try {
				setBorder(null);
				setOpaque(false);
				selected = false;
				super.paint(g);
			} finally {
				selected = tmp;
			}
		}

		/**
		 * Getter for property backgroundColor.
		 * 
		 * @return Value of property backgroundColor.
		 */
		public java.awt.Color getBackColor() {
			return backColor;
		}

		/**
		 * Setter for property backgroundColor.
		 * 
		 * @param backgroundColor
		 *            New value of property backgroundColor.
		 */
		public void setBackColor(java.awt.Color backColor) {
			this.backColor = backColor;
		}

	}

	/*
	 * public static class StartRenderer1 extends VertexRenderer {
	 * 
	 * public void paint(Graphics g) { int b = borderWidth; Graphics2D g2 =
	 * (Graphics2D) g; Dimension d = getSize(); boolean tmp = selected; if
	 * (super.isOpaque()) { g.setColor(super.getBackground()); g.fillOval(b - 1,
	 * b - 1, d.width - b, d.height - b); } try { setBorder(null);
	 * setOpaque(false); selected = false; super.paint(g); } finally { selected =
	 * tmp; } if (bordercolor != null) { g.setColor(bordercolor);
	 * g2.setStroke(new BasicStroke(b)); g.fillOval(b - 1, b - 1, d.width - b,
	 * d.height - b); } if (selected) {
	 * g2.setStroke(GraphConstants.SELECTION_STROKE);
	 * g.setColor(graph.getHighlightColor()); g.drawOval(b - 1, b - 1, d.width -
	 * b, d.height - b); } } }
	 */
}
