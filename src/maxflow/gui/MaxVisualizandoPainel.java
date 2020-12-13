package maxflow.gui;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ComponentListener;
import java.util.Observer;

import javax.swing.JPanel;

import org.jbpm.gpd.GpdGraph;
import org.jbpm.gpd.ProcessDesigner;
import org.jbpm.gpd.GpdOverviewPanel.ViewRedirector;
import org.jgraph.JGraph;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.GraphLayoutCache;

public class MaxVisualizandoPainel {
	/*
	protected GpdGraph graph;
	protected JGraph originalGraph;

	protected Rectangle r;
	double graphWindowToPannerScale = 0.5;
	static final int PANEL_BUFFER = 2;
	
	protected MaxVisualizandoPainel(GpdGraph g, Main m) {
		originalGraph = g;
        //GpdOverviewPanel.realViewST = g.getGraphLayoutCache();
		GraphLayoutCache view = new ViewRedirector(g, g.getGraphLayoutCache());
        graph = new GpdGraph(g.getModel(), view);
		graph.setAntiAliased(true);
		graph.getModel().addGraphModelListener(this);
		graph.setEnabled(false);
		graph.addMouseListener(v);
		graph.addMouseMotionListener(v);

		g.addPropertyChangeListener(JGraph.SCALE_PROPERTY, v);

		addComponentListener(this);
		//g.getGraphLayoutCache().addObserver(this);
		setLayout(new BorderLayout());
		add(graph, BorderLayout.CENTER);
	}
	*/
}
