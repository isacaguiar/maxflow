package org.jbpm.gpd.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Stroke;
import java.net.URL;

import javax.swing.ImageIcon;

import org.jbpm.gpd.cell.ActivityCell;
import org.jbpm.gpd.renderer.util.TextBreakRenderer;
import org.jbpm.gpd.view.ActivityDecisionView;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexRenderer;

public class DecisionRenderer extends VertexRenderer implements
		CellViewRenderer {
	private ActivityCell activityCell;
	
	private static final int TITLE_PADDING = 3;
	private static final int ROW_SPACING = 3;

	private Color backColor = Color.white;
	
	

	public java.awt.Component getRendererComponent(JGraph graph,
			CellView cellview, boolean sel, boolean focus, boolean preview) {
		if (cellview instanceof ActivityDecisionView) {
			ActivityDecisionView view = (ActivityDecisionView) cellview;
			activityCell = (ActivityCell) view.getCell();
			this.graph = graph;
			this.hasFocus = focus;
			this.selected = sel;
			this.preview = preview;
			setComponentOrientation(graph.getComponentOrientation());

			if (view.isLeaf()) {
				installAttributes(view);
			} else {
				setBorder(null);
				setOpaque(false);
			}
			return this;
		}
		return null;
	}

	public void paint2(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		Dimension dim = getSize();
		int width = dim.width - 1;
		int height = dim.height - 1;

		g.setColor(backColor);
		// g.fillRoundRect(borderWidth-1, borderWidth-1, dim.width,
		// dim.height,10, 10);
		g.fillRect(borderWidth - 1, borderWidth - 1, width, height);
		g.setColor(getForeground());

		Font titleFont = new Font("Arial", Font.PLAIN, 10);
		g.setFont(titleFont);
		FontMetrics fm = g.getFontMetrics();
		// Font titleFont = normalFont.deriveFont(Font.PLAIN);
		// fonte do nome da atividade
		int colTextHeight = fm.getMaxAscent() - fm.getMaxDescent()
				+ ROW_SPACING;
		int yCursor = colTextHeight + TITLE_PADDING;

		// escrevendo o papel funcional na atividade
		String role = activityCell.getModel().getRoleName();
		int xCursor = (width - fm.stringWidth(role)) / 2;
		// yCursor += (borderWidth);
		g.setFont(titleFont);
		g.drawString(role, xCursor, yCursor);

		Stroke lineStroke = new BasicStroke((float) borderWidth);
		// desenha a linha
		g2.setStroke(lineStroke);
		yCursor += (TITLE_PADDING + borderWidth);
		//g.drawLine(0, yCursor + 2, 25, yCursor + 2);
		g.drawLine(0, yCursor + 2, 20, yCursor + 2);
		
		g.drawLine(20, 0, 20, 20);

		String title = activityCell.getModel().getName();
		xCursor = (width - fm.stringWidth(title)) / 2;
		yCursor += (TITLE_PADDING + borderWidth + 7);
		Stroke textStroke = new BasicStroke(1.0f);
		
		TextBreakRenderer.draw(g2, width - 15, title, height, width, 20);
		

		if (selected || hasFocus) {
			// Draw selection/highlight border
			((Graphics2D) g).setStroke(GraphConstants.SELECTION_STROKE);

			if (hasFocus) {
				g.setColor(graph.getGridColor());
			} else if (selected) {
				g.setColor(graph.getHighlightColor());
			}

			g.drawRect(0, 0, width, height);
		}

		g2.setStroke(new BasicStroke(borderWidth));
		g.drawRect(borderWidth - 1, borderWidth - 1, width, height);
		
		
		/**
		 * Trecho que adiciona a imagem no elemento.
		 * @author Isac Velozo Aguiar - www.isacvelozo.com
		 */
		URL url = getClass().getClassLoader().getResource("gif/decision_peq.gif");
		ImageIcon activityIcon = new ImageIcon(url);
		Image img = activityIcon.getImage();
		
		g.drawImage(img, 2, 2, 16, 16, graph);

	}
	
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Dimension d = getSize();
		int height = d.height;
		int width = d.width;
		boolean tmp = selected;

		g.setColor(Color.WHITE);
		g.fillRect(borderWidth-1, borderWidth-1, width, height);

		Polygon pol = new Polygon(new int[]{0, width/2, width, width/2}, new int[]{height/2, 0, height/2, height}, 4);
		g.setColor(getBackColor());
		g2.fillPolygon(pol);
		g.setColor(getForeground());
		g2.drawPolygon(pol);
		
		//escrevendo nome da atividade
		Font titleFont = new Font("Arial", Font.PLAIN, 10);
		g.setFont(titleFont);
		FontMetrics fm = g.getFontMetrics();
		String title = activityCell.getModel().getName();
		int xCursor = (width - fm.stringWidth(title)) / 2;
		int yCursor = (height+8) / 2;
		Stroke textStroke = new BasicStroke(1.0f);
		
		/*
		// Draw the title box with name centered
		g2.setStroke(textStroke);
		g.drawString(title, xCursor, yCursor);
		*/
				
/*
		AttributedString attribString = new AttributedString(title);
		attribString.addAttribute(TextAttribute.FOREGROUND, Color.BLACK, 0,
				title.length()); // Start and end indexes.
		Font font = new Font("Verdana", Font.PLAIN, 10);
		attribString.addAttribute(TextAttribute.FONT, font, 0, title.length());
		AttributedCharacterIterator attribCharIterator = attribString
				.getIterator();

		FontRenderContext frc = new FontRenderContext(null, false, false);
		LineBreakMeasurer lbm = new LineBreakMeasurer(attribCharIterator, frc);
		double x = 5;
		double y = (height-12) / 2;
		//TODO: centralizar na vertical
		
		float wrappingWidth = width - 15;
		int linhas = 0;
		float space = 0;
		while (lbm.getPosition() < title.length()) {
			TextLayout layout = lbm.nextLayout(wrappingWidth);
			linhas++;
			space = layout.getAscent();
		}
		//o y começa de..
		y = (height - linhas*space)/2;
		
		lbm.setPosition(0);
		while (lbm.getPosition() < title.length()) {
			TextLayout layout = lbm.nextLayout(wrappingWidth);
			y += layout.getAscent();
			float center = (float) x;
			center = (width - layout.getVisibleAdvance()) / 2;
			layout.draw(g2, (float) center, (float) y);
			//height+=y;
			y += layout.getDescent() + layout.getLeading();			
		}
*/
		TextBreakRenderer.draw(g2, width - 15, title, height, width, 0);
		
		
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
	 * Getter for property backColor.
	 * 
	 * @return Value of property backColor.
	 */
	public java.awt.Color getBackColor() {
		return backColor;
	}

	/**
	 * Setter for property backColor.
	 * 
	 * @param backColor
	 *            New value of property backColor.
	 */
	public void setBackColor(java.awt.Color backColor) {
		this.backColor = backColor;
	}

}