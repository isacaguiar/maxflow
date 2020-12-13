package org.jbpm.gpd;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jbpm.gpd.cell.DefaultGpdCell;
import org.jbpm.gpd.cell.Transition;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;
import org.jgraph.graph.PortView;

public class GpdGraph extends JGraph {

	public GpdGraph(GraphModel model, GraphLayoutCache view) {
		super(model, view, null);
	}

	// Construct the Graph using the Model as its Data Source
	public GpdGraph(GraphModel model) {
		super(model);
		// Use a Custom Marquee Handler
		setMarqueeHandler(new GpdMarqueeHandler(this));
		// Tell the Graph to Select new Cells upon Insertion
		// setSelectNewCells(true);
		// Make Ports Visible by Default
		setPortsVisible(true);
		// Use the Grid (but don't make it Visible)
		setGridEnabled(true);
		// Set the Grid Size to 10 Pixel
		setGridSize(10);
		// Set the Tolerance to 2 Pixel
		setTolerance(2);
		setGridVisible(true);

		this.addKeyListener(new DirectionMoveKeyListener(this));
	}

	public Object[] getAll() {
		return getDescendants(getRoots());
	}

	public Object[] getEdges(Object[] cells) {
		if (cells != null) {
			ArrayList result = new ArrayList();
			for (int i = 0; i < cells.length; i++)
				if (isEdge(cells[i]))
					result.add(cells[i]);
			return result.toArray();
		}
		return null;
	}

	public boolean isEdge(Object object) {
		return (object instanceof Edge);
	}

	public Object getSourceVertex(Object edge) {
		Object sourcePort = graphModel.getSource(edge);
		return graphModel.getParent(sourcePort);
	}

	public Object getTargetVertex(Object edge) {
		Object targetPort = graphModel.getTarget(edge);
		return graphModel.getParent(targetPort);
	}

	public CellView getSourceView(Object edge) {
		Object source = getSourceVertex(edge);
		return getGraphLayoutCache().getMapping(source, false);
	}

	public CellView getTargetView(Object edge) {
		Object target = getTargetVertex(edge);
		return getGraphLayoutCache().getMapping(target, false);
	}

	public Object[] getVertices(Object[] cells) {
		if (cells != null) {
			ArrayList result = new ArrayList();
			for (int i = 0; i < cells.length; i++)
				if (isVertex(cells[i]))
					result.add(cells[i]);
			return result.toArray();
		}
		return null;
	}

	/**
	 * Returns true if <code>object</code> is a vertex, that is, if it is not
	 * an instance of Port or Edge, and all of its children are ports, or it has
	 * no children.
	 */
	public boolean isVertex(Object object) {
		if (!(object instanceof Port) && !(object instanceof Edge))
			return !isGroup(object) && object != null;
		return false;
	}

	/**
	 * Returns true if <code>object</code> is a vertex, that is, if it is not
	 * an instance of Port or Edge, and all of its children are ports, or it has
	 * no children.
	 */
	public boolean isGroup(Object cell) {
		// Map the Cell to its View
		CellView view = getGraphLayoutCache().getMapping(cell, false);
		if (view != null)
			return !view.isLeaf();
		return false;
	}

	/*
	 * protected EdgeView createEdgeView(Object e) {
	 * 
	 * if (e instanceof Transition) { return (new TransitionView( e )); } else {
	 * //return (super.createEdgeView( e, cm )); EdgeView ev = new EdgeView(e) {
	 * public CellHandle getHandle(GraphContext context) { return new
	 * PointEdgeHandle(this, context); } }; return ev; } // end of else }
	 */
	/*
	 * // Overrides JGraph.createVertexView protected VertexView
	 * createVertexView(Object v) { // Return an EllipseView for EllipseCells if
	 * (v instanceof StartCell) return new StartView(v, this, null); else if (v
	 * instanceof EndCell) return new EndView(v, this, null); else if (v
	 * instanceof ActivityCell) return new ActivityView(v, this, null); else if
	 * (v instanceof ForkCell) return new ForkView(v, this, null); else if(v
	 * instanceof JoinCell) return new JoinView(v, this, null); else if(v
	 * instanceof DecisionCell) return new DecisionView(v, this, null); // Else
	 * Call Superclass //return super.createVertexView(v, cm); return new
	 * VertexView(v); }
	 */

}

class DirectionMoveKeyListener implements KeyListener {

	private GpdGraph graph;

	public DirectionMoveKeyListener(GpdGraph graph) {
		this.graph = graph;
	}

	public void keyPressed(KeyEvent key) {
		int ch = key.getKeyCode();
		Rectangle2D r = null;
		List list = new ArrayList();

		// so faz o movimento se for DefaultGpdCell
		if (graph.getSelectionCell() instanceof DefaultGpdCell) {
			DefaultGpdCell cell = (DefaultGpdCell) graph.getSelectionCell();
			r = graph.getGraphLayoutCache().getMapping(cell, false).getBounds();

			Object[] temp = graph.getEdges(graph.getAll());
			for (int i = 0; i < temp.length; i++) {
				Transition trans = (Transition) temp[i];

				if (graph.getSourceVertex(trans).equals(cell)
						|| graph.getTargetVertex(trans).equals(cell)) {
					list.add(trans);
				}
			}
		}
		/*
		 * else if (graph.getSelectionCell() instanceof Transition) { Transition
		 * transition = (Transition) graph.getSelectionCell(); r =
		 * transition.getView().getBounds(); }
		 */

		// complementos de x e y
		int compX = 0;
		int compY = 0;

		if (r != null) {
			if (ch == KeyEvent.VK_LEFT) {
				if (r.getX() >= 0)
					compX = -1;
			} else if (ch == KeyEvent.VK_RIGHT) {
				if (r.getX() >= 0)
					compX = 1;
			} else if (ch == KeyEvent.VK_UP) {
				if (r.getY() >= 0)
					compY = -1;
			} else if (ch == KeyEvent.VK_DOWN) {
				if (r.getY() >= 0)
					compY = 1;
			}
			else {
				return;
			}
			r.setRect(r.getX() + compX, r.getY() + compY, r.getWidth(), r
					.getHeight());
			graph.updateUI();
		}
		
		//TODO
		/*
		//se nao tiver pressionado shift, move as transições
		if (!key.isShiftDown()) {
			// atualizando transições
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Transition trans = (Transition) it.next();
				trans.getView().update();		
			}
			//tira o bug do desenho da transição duplicado
			//CellView[] c = graph.getGraphLayoutCache().getRoots();
			//graph.getGraphLayoutCache().reload();
			//graph.getGraphLayoutCache().insertViews(c);
		}
		*/
	}

	public void keyTyped(KeyEvent arg0) {
	}

	public void keyReleased(KeyEvent arg0) {
	}

}