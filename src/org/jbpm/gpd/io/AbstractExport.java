package org.jbpm.gpd.io;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jbpm.gpd.GpdGraph;
import org.jbpm.gpd.GpdMarqueeHandler;
import org.jbpm.gpd.cell.ActivityCell;
import org.jbpm.gpd.cell.DecisionCell;
import org.jbpm.gpd.cell.DefaultGpdCell;
import org.jbpm.gpd.cell.EndCell;
import org.jbpm.gpd.cell.ForkCell;
import org.jbpm.gpd.cell.JoinCell;
import org.jbpm.gpd.cell.NcCell;
import org.jbpm.gpd.cell.StartCell;
import org.jbpm.gpd.cell.SubProcessCell;
import org.jbpm.gpd.cell.Transition;
import org.jbpm.gpd.model.SubProcessVO;
import org.jbpm.gpd.model.TransitionVO;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;

public abstract class AbstractExport implements GraphModelFileFormat {
	private Map stateMap = new HashMap();

	protected GpdGraph graph = null;

	protected void init(GpdGraph gpGraph) {
		graph = gpGraph;
		Object[] cellArray = gpGraph.getAll();
		for (int i = 0; i < cellArray.length; i++) {
			if (!(cellArray[i] instanceof Transition || cellArray[i] instanceof DefaultPort)) {
				stateMap.put(cellArray[i].toString(), cellArray[i]);
			}
		}
	}

	// Insert a new Vertex at point
	public void insert(Point2D point, DefaultGraphCell vertex, Dimension size) {
		if (stateMap.get(getCellName(vertex)) == null) {
			// Add one Floating Port
			vertex.add(new DefaultPort());
			// Snap the Point to the Grid
			// point = graph.snap(new Point2D(point));
			point = graph.snap((Point2D) point.clone());
			// Default Size for the new Vertex
			if (size == null) {
				size = new Dimension(25, 25);
			}
			// Create a Map that holds the attributes for the Vertex
			// Map map = GraphConstants.createMap();
			// Add a Bounds Attribute to the Map
			// rect.setRect(point.getX(), point.getY(), size.getWidth(),
			// size.getHeight());
			GraphConstants.setBounds(vertex.getAttributes(), new Rectangle(
					(int) point.getX(), (int) point.getY(), (int) size
							.getWidth(), (int) size.getHeight()));
			// Add a Border Color Attribute to the Map
			GraphConstants.setBorderColor(vertex.getAttributes(), Color.black);
			// Add a White Background
			GraphConstants.setBackground(vertex.getAttributes(), Color.white);
			// Make Vertex Opaque
			GraphConstants.setOpaque(vertex.getAttributes(), true);
			// Construct a Map from cells to Maps (for insert)
			Hashtable attributes = new Hashtable();
			// Associate the Vertex with its Attributes
			attributes.put(vertex, vertex.getAttributes());
			// Insert the Vertex and its Attributes (can also use model)
			graph.getGraphLayoutCache().insert(new Object[] { vertex },
					attributes, null, null, null);
			// add to stateMap
			stateMap.put(getCellName(vertex), vertex);
		}
	}

	protected DefaultGpdCell getCellByName(String name) {
		Object cell = stateMap.get(name);
		if (cell == null) {
			return null;
		}
		return (DefaultGpdCell) cell;
	}

	/**
	 * @param vertex
	 * @return
	 */
	protected String getCellName(Object vertex) {
		if (vertex instanceof ActivityCell) {
			return ((ActivityCell) vertex).getModel().getName();
		} else if (vertex instanceof DecisionCell) {
			return ((DecisionCell) vertex).getModel().getName();
		} else if (vertex instanceof EndCell) {
			return ((EndCell) vertex).getModel().getName();
		} else if (vertex instanceof ForkCell) {
			return ((ForkCell) vertex).getModel().getName();
		} else if (vertex instanceof StartCell) {
			return ((StartCell) vertex).getModel().getName();
		} else if (vertex instanceof JoinCell) {
			return ((JoinCell) vertex).getModel().getName();
		} else if (vertex instanceof SubProcessCell) {
			return ((SubProcessCell) vertex).getModel().getName();
		}
		return null;
	}

	protected GraphModel getGraphModel() {
		return graph.getModel();
	}

	/**
	 * Return a list of all transition that has the same source
	 * 
	 * @param model
	 * @param startCell
	 * @return
	 */
	// ((GpdMarqueeHandler)graph.getMarqueeHandler())
	protected List findTransitionBySource(DefaultGpdCell sourceCell) {
		List list = ((GpdMarqueeHandler) graph.getMarqueeHandler())
				.findTransitionBySource(sourceCell);
		/*
		if (sourceCell instanceof ActivityCell) {
			ActivityCell cell = ((ActivityCell) sourceCell);
			String nc = cell.getModel().getNc();
			if (nc != null && !nc.equals("")) {
				Iterator it = list.iterator();
				while(it.hasNext()) {
					Transition t = (Transition) it.next();
					if(t.getModel().getName()==null || t.getModel().getName().equals("")) {
						t.getModel().setName("Ok");
					}
				}
				
				SubProcessVO ncVO = new SubProcessVO();
				ncVO.setName(nc);
				ncVO.setSubProcess(nc);
				ncVO.setOrder(-1);
				NcCell ncCell = new NcCell(ncVO);
				ncCell.add(new DefaultPort());
				ncCell.setModel(ncVO);
				
				Transition transitionNc = new Transition();
				transitionNc.add(new DefaultPort());
				transitionNc.setSource(ncCell.getFirstChild());
				transitionNc.setTarget(this.getEndCell().getFirstChild());

				Transition transitionCell = new Transition();
				transitionCell.add(new DefaultPort());
				TransitionVO transVO = new TransitionVO();
				transVO.setName(nc);
				transitionCell.setModel(transVO);
				transitionCell.setSource(cell.getFirstChild());
				transitionCell.setTarget(ncCell.getFirstChild());
				list.add(transitionCell);
			}
		}
		if (sourceCell instanceof NcCell) {
			NcCell ncCell = (NcCell) sourceCell;
			Transition transitionNc = new Transition();
			transitionNc.add(new DefaultPort());
			transitionNc.setSource(ncCell.getFirstChild());
			transitionNc.setTarget(this.getEndCell().getFirstChild());
			list.add(transitionNc);
		}
		*/
		return list;
	}

	protected EndCell getEndCell() {
		int max = getGraphModel().getRootCount();
		EndCell endCell = null;
		for (int i = 0; i < max; i++) {
			Object c = getGraphModel().getRootAt(i);
			if (c instanceof EndCell) {
				endCell = (EndCell) c;
			}
		}
		return endCell;
	}
	
	protected DefaultEdge findConnection(DefaultGpdCell source,
			DefaultGpdCell target) {
		return ((GpdMarqueeHandler) graph.getMarqueeHandler()).findConnection(
				source, target);
	}
}
