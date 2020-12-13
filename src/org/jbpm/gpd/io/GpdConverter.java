package org.jbpm.gpd.io;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.jbpm.gpd.GpdGraph;

public class GpdConverter {

	//
	// Image Converter
	//

	// Create a buffered image of the specified graph.
	public static BufferedImage toImage(GpdGraph graph) {
		Object[] cells = graph.getRoots();

		if (cells.length > 0) {
			Rectangle2D bounds = graph.getCellBounds(cells);
			graph.toScreen(bounds);

			// Create a Buffered Image
			Dimension d = bounds.getBounds().getSize();
			BufferedImage img =
				new BufferedImage(
					d.width + 10,
					d.height + 10,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = img.createGraphics();
			graphics.setColor(graph.getBackground());
			graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
			graphics.translate(-bounds.getX() + 5, -bounds.getY() + 5);

			Object[] selection = graph.getSelectionCells();
			boolean gridVisible = graph.isGridVisible();
			graph.setGridVisible(false);
			graph.clearSelection();

			graph.paint(graphics);

			graph.setSelectionCells(selection);
			graph.setGridVisible(gridVisible);

			return img;
		}
		return null;
	}

}