package org.jbpm.gpd.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.Map;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import org.jbpm.gpd.cell.ActivityCell;
import org.jbpm.gpd.renderer.util.TextBreakRenderer;
import org.jbpm.gpd.view.ActivityView;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;


/**
 * A JGraph Vertex renderer for Tables
 * 
 * @author Brian Sidharta
 * @version $Revision: 1.1 $
 */
public class ActivityRenderer extends JPanel implements CellViewRenderer {

	private int resize = 0;

	private ActivityCell activityCell;

	/** The default foreground color = Color.black */
	public static final Color DEFAULT_FOREGROUND = Color.black;

	public static final Color DEFAULT_BACKGROUND = Color.white;

	private Color backColor = DEFAULT_BACKGROUND;

	// gif const
	private static final String NOT_ACCESSIBLE_FILENAME = "gif/field/notaccessible.gif";

	private final Icon NOT_ACCESSIBLE = new ImageIcon(getClass()
			.getClassLoader().getResource(NOT_ACCESSIBLE_FILENAME));

	private static final String READ_ONLY_FILENAME = "gif/field/readonly.gif";

	private final Icon READ_ONLY = new ImageIcon(getClass().getClassLoader()
			.getResource(READ_ONLY_FILENAME));

	private static final String WRITE_ONLY_FILENAME = "gif/field/writeonly.gif";

	private final Icon WRITE_ONLY = new ImageIcon(getClass().getClassLoader()
			.getResource(WRITE_ONLY_FILENAME));

	private static final String WRITE_ONLY_REQUIRED_FILENAME = "gif/field/writeonlyrequired.gif";

	private final Icon WRITE_ONLY_REQUIRED = new ImageIcon(getClass()
			.getClassLoader().getResource(WRITE_ONLY_REQUIRED_FILENAME));

	private static final String READ_WRITE_FILENAME = "gif/field/readwrite.gif";

	private final Icon READ_WRITE = new ImageIcon(getClass().getClassLoader()
			.getResource(READ_WRITE_FILENAME));

	private static final String READ_WRITE_REQUIRED_FILENAME = "gif/field/readwriterequired.gif";

	private final Icon READ_WRITE_REQUIRED = new ImageIcon(getClass()
			.getClassLoader().getResource(READ_WRITE_REQUIRED_FILENAME));

	/** Vertical padding around title */
	private static final int TITLE_PADDING = 3;

	/** Left margin for column icon */
	private static final int COLUMN_ICON_LEFT = 2;

	/** Left margin for column name text */
	private final int COLUMN_LEFT = NOT_ACCESSIBLE.getIconWidth() + 2
			+ COLUMN_ICON_LEFT;

	/** The padding between text rows in pixels */
	private static final int ROW_SPACING = 3;

	/** The padding around the more-content indicator arrow */
	private static final int MORE_ARROW_PADDING = 4;

	/** The width of the more-content indicator arrow */
	private static final int MORE_ARROW_WIDTH = 8;

	/** The height of the more-content indicator arrow */
	private static final int MORE_ARROW_HEIGHT = 8;

	/** Cache the current graph for drawing. */
	transient private JGraph graph;

	/** Cached hasFocus and selected value. */
	transient private boolean hasFocus;

	/** Cached hasFocus and selected value. */
	transient private boolean selected;

	/** Cached hasFocus and selected value. */
	transient private boolean preview;

	/** Cached default foreground and default background. */
	transient private Color defaultForeground;

	/** Cached default foreground and default background. */
	transient private Color defaultBackground;

	/** Cached default foreground and default background. */
	transient private Color bordercolor;

	/** Cached borderwidth. */
	transient private int borderWidth;

	/** Default constructor */
	public ActivityRenderer() {
		defaultForeground = DEFAULT_FOREGROUND;
		defaultBackground = DEFAULT_BACKGROUND;
	}

	/**
	 * Configure and return the renderer based on the passed in components. The
	 * value is typically set from messaging the graph with
	 * <code>convertValueToString</code>. We recommend you check the value's
	 * class and throw an illegal argument exception if it's not correct.
	 * 
	 * @param graph
	 *            the graph that that defines the rendering context.
	 * @param value
	 *            the object that should be rendered.
	 * @param selected
	 *            whether the object is selected.
	 * @param hasFocus
	 *            whether the object has the focus.
	 * @param isPreview
	 *            whether we are drawing a preview.
	 * @return the component used to render the value.
	 */
	public Component getRendererComponent(JGraph graph, CellView cellview,
			boolean sel, boolean focus, boolean preview) {
		if (cellview instanceof ActivityView) {
			ActivityView view = (ActivityView) cellview;

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

	/**
	 * Install the attributes of specified cell in this renderer instance. This
	 * means, retrieve every published key from the cells hashtable and set
	 * global variables or superclass properties accordingly.
	 * 
	 * @param cell
	 *            to retrieve the attribute values from.
	 */
	protected void installAttributes(CellView view) {
		Map map = view.getAttributes();
		setOpaque(GraphConstants.isOpaque(map));
		setBorder(GraphConstants.getBorder(map));
		bordercolor = GraphConstants.getBorderColor(map);
		borderWidth = Math.max(1, Math.round(GraphConstants.getLineWidth(map)));

		if ((getBorder() == null) && (bordercolor != null)) {
			setBorder(BorderFactory.createLineBorder(bordercolor, borderWidth));
		}

		Color foreground = GraphConstants.getForeground(map);
		setForeground((foreground != null) ? foreground : defaultForeground);

		Color background = GraphConstants.getBackground(map);
		setBackground((background != null) ? background : defaultBackground);
		setFont(GraphConstants.getFont(map));

		setToolTipText("Atividade");
	}

	/**
	 * Paint the renderer. Overrides superclass paint to add specific painting.
	 */
	public void paint(Graphics g) {
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
		//g.drawLine(0, yCursor + 2, 20, yCursor + 2);
		g.drawLine(0, yCursor + 2, width, yCursor + 2);
		
		g.drawLine(20, 0, 20, 20);

		String title = activityCell.getModel().getName();
		xCursor = (width - fm.stringWidth(title)) / 2;
		yCursor += (TITLE_PADDING + borderWidth + 7);
		
		Stroke textStroke = new BasicStroke(1.0f);
		//TextBreakRenderer.draw(g2, width - 15, title, height, width, 20);
		/**
		 * Trecho inserido por Isac Velozo
		 * Este trecho define a impressão do texto no elemento visual
		 * se ele for menor ou igual a 20 ele chega o texto um pouco para a direita,
		 * pois o a imagem sobrescreve o texto.
		 */
		if ( height <= 20 ) {
			TextBreakRenderer.draw(g2, width -15, title, height, width + 15, 0);
		} else {
			TextBreakRenderer.draw(g2, width -15, title, height, width, 0);
		}
		 
		
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
		URL url = getClass().getClassLoader().getResource("gif/activity_peq.gif");
		ImageIcon activityIcon = new ImageIcon(url);
		Image img = activityIcon.getImage();
		
		g.drawImage(img, 2, 2, 16, 16, graph);

	}
	
	public void paintOld(Graphics g) {
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
		g.drawLine(0, yCursor + 2, width, yCursor + 2);

		String title = activityCell.getModel().getName();
		xCursor = (width - fm.stringWidth(title)) / 2;
		yCursor += (TITLE_PADDING + borderWidth + 7);
		Stroke textStroke = new BasicStroke(1.0f);

		
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
		double y = 20;
		
		float wrappingWidth = width - 15;
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
		
		URL url = getClass().getClassLoader().getResource("gif/activity_peq.gif");
		ImageIcon activityIcon = new ImageIcon(url);
		Image i = activityIcon.getImage();
		
	}

	/**
	 * Returns <code>true</code> if <code>key</code> is a supported
	 * attribute in the renderer. Supported attributes affect the visual
	 * appearance of the renderer.
	 * 
	 * @param key
	 *            the key that defines the attribute to be checked.
	 * @return true if <code>key</code> is supported by this renderer.
	 */
	public boolean supportsAttribute(Object key) {
		return key.equals(GraphConstants.BOUNDS)
				|| key.equals(GraphConstants.BACKGROUND)
				|| key.equals(GraphConstants.BORDER)
				|| key.equals(GraphConstants.FOREGROUND)
				|| key.equals(GraphConstants.BORDERCOLOR)
				|| key.equals(GraphConstants.LINEWIDTH)
				|| key.equals(GraphConstants.OPAQUE);
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void validate() {
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void revalidate() {
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void repaint(long tm, int x, int y, int width, int height) {
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void repaint(Rectangle r) {
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, byte oldValue,
			byte newValue) {
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, char oldValue,
			char newValue) {
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, short oldValue,
			short newValue) {
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, int oldValue,
			int newValue) {
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, long oldValue,
			long newValue) {
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, float oldValue,
			float newValue) {
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, double oldValue,
			double newValue) {
	}

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	public void firePropertyChange(String propertyName, boolean oldValue,
			boolean newValue) {
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
