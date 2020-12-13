package org.jbpm.gpd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;
import javax.swing.event.UndoableEditEvent;

import org.jbpm.gpd.action.ExportJpegAction;
import org.jbpm.gpd.action.FormatFillColor;
import org.jbpm.gpd.action.NewAction;
import org.jbpm.gpd.action.OpenAction;
import org.jbpm.gpd.action.PageSetupAction;
import org.jbpm.gpd.action.PrintAction;
import org.jbpm.gpd.action.PrintPreviewAction;
import org.jbpm.gpd.action.SaveAction;
import org.jbpm.gpd.action.TiltAction;
import org.jbpm.gpd.cell.ActivityCell;
import org.jbpm.gpd.cell.ActivityDecisionCell;
import org.jbpm.gpd.cell.Comment;
import org.jbpm.gpd.cell.DecisionCell;
import org.jbpm.gpd.cell.EndCell;
import org.jbpm.gpd.cell.ForkCell;
import org.jbpm.gpd.cell.JoinCell;
import org.jbpm.gpd.cell.StartCell;
import org.jbpm.gpd.cell.SubProcessCell;
import org.jbpm.gpd.dialog.controller.Controller;
import org.jbpm.gpd.dialog.controller.MasterDetailCellController;
import org.jbpm.gpd.dialog.util.GpdSplitPane;
import org.jbpm.gpd.view.ActivityDecisionView;
import org.jbpm.gpd.view.ActivityView;
import org.jbpm.gpd.view.DecisionView;
import org.jbpm.gpd.view.EndView;
import org.jbpm.gpd.view.ForkView;
import org.jbpm.gpd.view.JoinView;
import org.jbpm.gpd.view.StartView;
import org.jbpm.gpd.view.SubProcessView;
import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.AbstractCellView;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphUndoManager;
import org.jgraph.graph.Port;

import eflow.gpd.action.ImportEflowAction;
import eflow.gpd.action.SaveExportAction;
import maxflow.action.Colar;
import maxflow.action.Copiar;
import maxflow.action.LookAndFeelCross;
import maxflow.action.LookAndFeelSistem;
import maxflow.action.Recortar;
import maxflow.action.Remover;
import maxflow.action.Sair;

public class ProcessDesigner extends JPanel implements GraphSelectionListener,
		KeyListener {

	public static final double INITIAL_SCALE = 1;

	public static final double ZOOM_SCALE = 1.1;

	// const
	public static final String ACTIVITY_CELL = "ActivityCell";

	public static final String DECISION_CELL = "DecisionCell";

	public static final String ACTIVITYDECISION_CELL = "ActivityDecisionCell";

	public static final String SUBPROCESS_CELL = "SubProcessCell";

	public static final String END_CELL = "EndCell";

	public static final String FORK_CELL = "ForkCell";

	public static final String JOIN_CELL = "JoinCell";

	public static final String START_CELL = "StartCell";

	public static final String TRANSITION = "TransitionCell";
	
	public static final String COLORS_CELL= "ColorsCell";

	// JGraph instance
	protected GpdGraph graph;

	private JScrollPane mainPane = null;

	private JSplitPane librarySplit = null;

	private JPanel propertyPanel = new JPanel();

	private Controller propertyController = null;

	private ButtonGroup buttongroup = new ButtonGroup();

	// Undo Manager
	protected GraphUndoManager undoManager;

	// Actions which Change State
	protected Action undo, redo, remove, group, ungroup, tofront, toback, cut,
			copy, paste, color, zoomIn, zoomOut, zoomStd;
	
	// aumentando o escopo do botão para toda a classe pois necessita desta
	// referência em outros métodos
	private JToggleButton connectonButton = null;
	
	private JToggleButton gridToggleButton =  null;

	/**
	 * Método construtor
	 */
	public ProcessDesigner() {
		// init ExceptionHandler
		ExceptionHandler.getInstance().init(this);
		// Use Border Layout
		setLayout(new BorderLayout());
		// Construct the Graph
		graph = new GpdGraph(new GpdGraphModel());

		graph.setGridMode(graph.DOT_GRID_MODE);
		graph.setGridVisible(true);
		graph.setPortsVisible(true);

		graph.setScale(INITIAL_SCALE * graph.getScale());
		// Create a GraphUndoManager which also Updates the ToolBar
		undoManager = new GraphUndoManager() {
			// Override Superclass
			public void undoableEditHappened(UndoableEditEvent e) {
				// First Invoke Superclass
				super.undoableEditHappened(e);
				// Then Update Undo/Redo Buttons
				updateHistoryButtons();
			}
		};

		// Add Listeners to Graph
		//
		// Register UndoManager with the Model
		graph.getModel().addUndoableEditListener(undoManager);
		// Update ToolBar based on Selection Changes
		graph.getSelectionModel().addGraphSelectionListener(this);
		// Listen for Delete Keystroke when the Graph has Focus
		graph.addKeyListener(this);
		// Construct Panel
		//
		// Add a ToolBar
		add(createToolBar(), BorderLayout.EAST);
		add(createToolBarBasic(), BorderLayout.NORTH);
		// Add the Graph as Center Component

		mainPane = new JScrollPane(graph);
		JPanel overviewPane = GpdOverviewPanel.createOverviewPanel(graph, this);
		overviewPane.setMinimumSize(new Dimension(100, 100));
		
		//librarySplit.setMaximumSize(new Dimension(50,50));
		/**
		 * Altera a tela de visualização para baixo do painel de prorpiedades
		 */
		librarySplit = new GpdSplitPane(JSplitPane.VERTICAL_SPLIT,
				propertyPanel, new JScrollPane(overviewPane) );
		librarySplit.setName("DocumentLibrary");

		GpdSplitPane splitPane = new GpdSplitPane(
				GpdSplitPane.HORIZONTAL_SPLIT, librarySplit, mainPane);
		splitPane.setName("DocumentMain");
		splitPane.setDividerSize(splitPane.getDividerSize() * 2);
		splitPane.setOneTouchExpandable(true);
		add(splitPane, BorderLayout.CENTER);
		// mainPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		graph.addMouseListener(new MouseAdapter() {
			private Object lastCell = null;
			
			// EFLOW: mousepressed
			public void mousePressed(MouseEvent e) {
				// Get Cell under Mousepointer
				int x = e.getX(), y = e.getY();
				String command = buttongroup.getSelection().getActionCommand();
				if (command == null) {
					Object cell = graph.getFirstCellForLocation(x, y);
					// Print Cell Label
					if (cell != null && cell != lastCell) {
						if (propertyController == null) {
							propertyController = new MasterDetailCellController();
						}
						/*
						propertyController.setModel(cell);
						librarySplit.setRightComponent(propertyController
								.getView());
						*/
						propertyController.setModel(cell);
						librarySplit.setTopComponent(propertyController.getView());

						lastCell = cell;
					}
				} else {
					x = new Double(x / graph.getScale()).intValue();
					y = new Double(y / graph.getScale()).intValue();
					if (command.equals(ACTIVITY_CELL)) {
						insert(new Point(x, y), new ActivityCell(),
								new Dimension(100, 20));
					} else if (command.equals(DECISION_CELL)) {
						insert(new Point(x, y), new DecisionCell());
					} else if (command.equals(END_CELL)) {
						insert(new Point(x, y), new EndCell(),
								new Dimension(60, 60));
					} else if (command.equals(FORK_CELL)) {
						insert(new Point(x, y), new ForkCell());
					} else if (command.equals(JOIN_CELL)) {
						insert(new Point(x, y), new JoinCell());
					} else if (command.equals(START_CELL)) {
						insert(new Point(x, y), new StartCell(),
							new Dimension(60, 60));
					} else if (command.equals(ACTIVITYDECISION_CELL)) {
						insert(new Point(x, y), new ActivityDecisionCell(),
								//new Dimension(100, 20));
								new Dimension(100, 50));
					} else if (command.equals(SUBPROCESS_CELL)) {
						insert(new Point(x, y), new SubProcessCell(),
								//new Dimension(100, 20));
								new Dimension(100, 50));
					}
					// voltando botão para TRANSITION
					buttongroup.setSelected(connectonButton.getModel(), true);
				}
			}
		});
		
	}
	
	public void insert(Point2D point, DefaultGraphCell vertex) {
		insert(point, vertex, null);
	}

	/**
	 * Métod Insert a new Vertex at point  
	 */
	public void insert(Point2D point, DefaultGraphCell vertex, Dimension size) {
		// Construct Vertex with no Label
		// DefaultGraphCell vertex = new DefaultGraphCell();
		// Add one Floating Port
		vertex.add(new DefaultPort(vertex));

		// Snap the Point to the Grid
		point = graph.snap((Point2D) point.clone());
		// Default Size for the new Vertex
		if (size == null) {
			size = new Dimension(25, 25);
		}
		// Create a Map that holds the attributes for the Vertex
		// Map map = GraphConstants.createMap();
		AttributeMap map = new AttributeMap();
		// Add a Bounds Attribute to the Map
		//define o tamanho do objeto no gráfico
		GraphConstants.setBounds(map, new Rectangle((int) point.getX(),
				(int) point.getY(), (int) size.getWidth(), (int) size
						.getHeight()));
		// Add a Border Color Attribute to the Map
		GraphConstants.setBorderColor(map, Color.black);
		// Add a White Background
		GraphConstants.setBackground(map, Color.white);
		// Make Vertex Opaque
		GraphConstants.setOpaque(map, true);
		// Construct a Map from cells to Maps (for insert)
		// AttributeMap attributes = new AttributeMap();
		// Associate the Vertex with its Attributes
		// attributes.put(vertex, map);
		vertex.setAttributes(map);

		AbstractCellView view = null;

		if (vertex instanceof StartCell)
			view = new StartView(vertex);
		else if (vertex instanceof EndCell)
			view = new EndView(vertex);
		else if (vertex instanceof SubProcessCell)
			// adicionado para criar sub-processo
			view = new SubProcessView(vertex);
		else if (vertex instanceof ActivityDecisionCell)
			// adicionado para criar losango
			view = new ActivityDecisionView(vertex);
		else if (vertex instanceof ActivityCell)
			view = new ActivityView(vertex);
		else if (vertex instanceof ForkCell)
			view = new ForkView(vertex);
		else if (vertex instanceof JoinCell)
			view = new JoinView(vertex);
		else if (vertex instanceof DecisionCell)
			view = new DecisionView(vertex);

		graph.getGraphLayoutCache().insert(new Object[] { vertex });

		// só usa o ActivityRenderer se tirar esse IF
		// if(!(vertex instanceof ActivityCell)) {
		graph.getGraphLayoutCache().insertViews(new CellView[] { view });
		graph.getGraphLayoutCache().putMapping(vertex, view);
		// }
	}

	// Create a Group that Contains the Cells
	public void group(Object[] cells) {
		// EFLOW
		DefaultGraphCell group = new DefaultGraphCell();
		cells = DefaultGraphModel.order(graph.getModel(), graph
				.getSelectionCells());
		Rectangle2D bounds = graph.getCellBounds(cells);
		if (bounds != null) {
			bounds = new Rectangle2D.Double(bounds.getX() + bounds.getWidth()
					/ 4, bounds.getY() + bounds.getHeight() / 4, bounds
					.getWidth() / 2, bounds.getHeight() / 2);
			GraphConstants.setBounds(group.getAttributes(), bounds);
		}
		graph.getGraphLayoutCache().insertGroup(group, cells);
	}

	// Returns the total number of cells in a graph
	protected int getCellCount(JGraph graph) {
		Object[] cells = graph.getDescendants(graph.getRoots());
		return cells.length;
	}

	// Ungroup the Groups in Cells and Select the Children
	public void ungroup(Object[] cells) {
		// If any Cells
		if (cells != null && cells.length > 0) {
			// List that Holds the Groups
			ArrayList groups = new ArrayList();
			// List that Holds the Children
			ArrayList children = new ArrayList();
			// Loop Cells
			for (int i = 0; i < cells.length; i++) {
				// If Cell is a Group
				if (isGroup(cells[i])) {
					// Add to List of Groups
					groups.add(cells[i]);
					// Loop Children of Cell
					for (int j = 0; j < graph.getModel()
							.getChildCount(cells[i]); j++) {
						// Get Child from Model
						Object child = graph.getModel().getChild(cells[i], j);
						// If Not Port
						if (!(child instanceof Port))
							// Add to Children List
							children.add(child);
					}
				}
			}
			// Remove Groups from Model (Without Children)
			graph.getGraphLayoutCache().remove(groups.toArray());
			// Select Children
			graph.setSelectionCells(children.toArray());
		}
	}

	// Determines if a Cell is a Group
	public boolean isGroup(Object cell) {
		// Map the Cell to its View
		CellView view = graph.getGraphLayoutCache().getMapping(cell, false);
		if (view != null)
			return !view.isLeaf();
		return false;
	}

	// Brings the Specified Cells to Front
	public void toFront(Object[] c) {
		graph.getGraphLayoutCache().toFront(c);
	}

	// Sends the Specified Cells to Back
	public void toBack(Object[] c) {
		graph.getGraphLayoutCache().toBack(c);
	}

	// Undo the last Change to the Model or the View
	public void undo() {
		try {
			undoManager.undo(graph.getGraphLayoutCache());
		} catch (Exception ex) {
			System.err.println(ex);
		} finally {
			updateHistoryButtons();
		}
	}

	// Redo the last Change to the Model or the View
	public void redo() {
		try {
			undoManager.redo(graph.getGraphLayoutCache());
		} catch (Exception ex) {
			System.err.println(ex);
		} finally {
			updateHistoryButtons();
		}
	}

	// Update Undo/Redo Button State based on Undo Manager
	protected void updateHistoryButtons() {
		// The View Argument Defines the Context
		undo.setEnabled(undoManager.canUndo(graph.getGraphLayoutCache()));
		redo.setEnabled(undoManager.canRedo(graph.getGraphLayoutCache()));
	}

	//
	// Listeners
	//

	// From GraphSelectionListener Interface
	// EFLOW invocado sempre que muda o estado do editor
	public void valueChanged(GraphSelectionEvent e) {

		// Group Button only Enabled if more than One Cell Selected
		group.setEnabled(graph.getSelectionCount() > 1);
		// Update Button States based on Current Selection
		boolean enabled = !graph.isSelectionEmpty();
		remove.setEnabled(enabled);
		ungroup.setEnabled(enabled);
		tofront.setEnabled(enabled);
		toback.setEnabled(enabled);
		copy.setEnabled(enabled);
		cut.setEnabled(enabled);
	}

	//
	// KeyListener for Delete KeyStroke
	//
	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		// Listen for Delete Key Press
		if (e.getKeyCode() == KeyEvent.VK_DELETE)
			// Execute Remove Action on Delete Key Press
			remove.actionPerformed(null);
	}

	public JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu filemenu = new JMenu("Arquivo");
		
		filemenu.add(new NewAction(graph));
		filemenu.add(new OpenAction(graph));
		filemenu.add(new SaveAction(graph));
		filemenu.addSeparator();
		//impressao
		filemenu.add(new PrintAction(graph)); 
		filemenu.add(new PrintPreviewAction(graph)); 
		filemenu.add(new PageSetupAction(graph));
		
		filemenu.addSeparator();
		filemenu.add(new Sair(this));
		
		menuBar.add(filemenu);
		
		/**
		 * Menu Exportar
		 */
		/*
		JMenu export = new JMenu("Exportar");
		export.add(new ExportJBPMFile(graph));
		export.addSeparator();
		export.add(new ExportJpegAction(graph));
		export.addSeparator();
		filemenu.add(export);
		
		*/
		
		/**
		 * Menu Editar
		 */
		JMenu editmenu = new JMenu("Editar");
		editmenu.add(undo);
		editmenu.add(redo);
		editmenu.addSeparator();
		editmenu.add(copy = new Copiar(graph,TransferHandler.getCopyAction()));
		editmenu.add(paste = new Colar(graph,TransferHandler.getPasteAction()));
		editmenu.add(cut = new Recortar(graph,TransferHandler.getCutAction()));
		remove = new Remover(graph);
		//editmenu.add(remove = new Remover(graph));
		editmenu.addSeparator();
		editmenu.add(new FormatFillColor(graph));
		
		menuBar.add(editmenu);
		
		/**
		 * Menu Exibição
		 */
		JMenu exibicaomenu = new JMenu("Exibição");
		exibicaomenu.add(zoomStd);
		exibicaomenu.add(zoomIn);
		exibicaomenu.add(zoomOut);
		exibicaomenu.addSeparator();
		URL gridUrl = getClass().getClassLoader().getResource("gif/grid.gif");
		ImageIcon gridIcon = new ImageIcon(gridUrl);
		Action gridButton;
		gridButton = new AbstractAction("", gridIcon) {
			public void actionPerformed(ActionEvent e) {
				graph.setGridVisible(!graph.isGridVisible());
				JOptionPane.showMessageDialog(null, graph.isGridVisible());
				if ( graph.isGridVisible() ) 
					gridToggleButton.setSelected(true);
				else
					gridToggleButton.setSelected(false);
			}
		};
		gridButton.putValue( Action.NAME, "Ativar/Desativar exibição do grid" );
		gridButton.putValue( Action.SHORT_DESCRIPTION, "Ativar/Desativar exibição do grid" );
		exibicaomenu.add(gridButton);
		menuBar.add(exibicaomenu);
		
		
		/**
		 * Menu Modificar
		 */
		JMenu modifyMenu = new JMenu("Modificar");
		modifyMenu.add(group);
		modifyMenu.add(ungroup);
		modifyMenu.addSeparator();
		modifyMenu.add(tofront);
		modifyMenu.add(toback);
		modifyMenu.addSeparator();
		modifyMenu.add(new TiltAction(graph));
		menuBar.add(modifyMenu);
		
		/**
		 * Menu Ferramentas
		 */
		JMenu toolsMenu = new JMenu("Ferramentas");
		toolsMenu.add(new SaveExportAction(graph));
		toolsMenu.add(new ImportEflowAction(graph));
		toolsMenu.addSeparator();
		JMenu exportImage = new JMenu("Exportar Imagem");
		exportImage.add(new ExportJpegAction(graph));
		toolsMenu.add(exportImage);
		menuBar.add(toolsMenu);
		
		/**
		 * Adiciona o look and feel ao menu
		 * Isac Velozo Aguiar
		 */
		JMenu windowMenu = new JMenu("Janela");
		JMenu windowMenuLookAndFeel = new JMenu("Look and Feel");
		windowMenuLookAndFeel.add(new LookAndFeelSistem());
		windowMenuLookAndFeel.add(new LookAndFeelCross());
		windowMenu.add(windowMenuLookAndFeel);
		menuBar.add(windowMenu);

		return menuBar;
	}
	
	/**
	 * Barra de ferramentas com comandos basicos.
	 * @modify Isac Velozo Aguiar - www.isacvelozo.com
	 * @return JToolBar
	 */
	public JToolBar createToolBarBasic() {

		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		
		//
		// Edit Block
		//
		toolbar.add(new NewAction(graph));
		toolbar.add(new OpenAction(graph));
		toolbar.add(new SaveAction(graph));
		toolbar.add(new PrintAction(graph)); 
		toolbar.addSeparator();
		toolbar.add(new SaveExportAction(graph));
		toolbar.add(new ImportEflowAction(graph));
		toolbar.addSeparator();
		
		Action action;
		URL url;

		// Undo
		URL undoUrl = getClass().getClassLoader().getResource("gif/undo.gif");
		ImageIcon undoIcon = new ImageIcon(undoUrl);
		
		undo = new AbstractAction("", undoIcon) {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		};
		undo.setEnabled(false);
		undo.putValue( Action.NAME, "Voltar" );
		undo.putValue( Action.SHORT_DESCRIPTION, "Voltar" );
		toolbar.add(undo);

		// Redo
		URL redoUrl = getClass().getClassLoader().getResource("gif/redo.gif");
		ImageIcon redoIcon = new ImageIcon(redoUrl);
		redo = new AbstractAction("", redoIcon) {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		};
		redo.setEnabled(false);
		redo.putValue( Action.NAME, "Avançar" );
		redo.putValue( Action.SHORT_DESCRIPTION, "Avançar" );
		toolbar.add(redo);

		final JTextField zoomText = new JTextField();
		
		// Zoom Std
		toolbar.addSeparator();
		URL zoomUrl = getClass().getClassLoader().getResource("gif/zoom.gif");
		ImageIcon zoomIcon = new ImageIcon(zoomUrl);
		zoomStd = new AbstractAction("", zoomIcon) {
			public void actionPerformed(ActionEvent e) {
				graph.setScale(INITIAL_SCALE);
				zoomText.setText("100%");
			}
		};
		zoomStd.putValue( Action.NAME, "Tamanho normal" );
		zoomStd.putValue( Action.SHORT_DESCRIPTION, "Tamanho normal" );
		//toolbar.add(zoomStd);

		Dimension dz = new Dimension(40,25);
		zoomText.setPreferredSize(dz);
		zoomText.setMaximumSize(dz);
		zoomText.setMinimumSize(dz);
		zoomText.setText(Math.round(graph.getScale() * 100) + "%");
		zoomText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				double valor = graph.getScale();
				String valorStr = zoomText.getText().trim();
				int index = valorStr.indexOf("%");
				try {
					if (index != -1) {
						valorStr = valorStr.substring(0, index);
						valor = new Double(valorStr.substring(0, index).trim())
								.doubleValue();
					}
					valorStr = valorStr.trim();
					valor = new Double(valorStr).doubleValue();
					valor /= 100;
				} catch (Exception e) {
				}
				graph.setScale(valor);
				zoomText.setText(Math.round(valor * 100) + "%");
			}
		});

		// Zoom In
		URL zoomInUrl = getClass().getClassLoader().getResource("gif/zoomin.gif");
		ImageIcon zoomInIcon = new ImageIcon(zoomInUrl);
		zoomIn = new AbstractAction("", zoomInIcon) {
			public void actionPerformed(ActionEvent e) {
				graph.setScale(ZOOM_SCALE * graph.getScale());
				zoomText.setText(Math.round(graph.getScale() * 100) + "%");
			}
		};
		zoomIn.putValue( Action.NAME, "Recuar" );
		zoomIn.putValue( Action.SHORT_DESCRIPTION, "Recuar" );
		toolbar.add(zoomIn);
		
		//Campo de texto do zoom
		toolbar.add(zoomText);
		
		// Zoom Out
		URL zoomOutUrl = getClass().getClassLoader().getResource(
				"gif/zoomout.gif");
		ImageIcon zoomOutIcon = new ImageIcon(zoomOutUrl);
		zoomOut = new AbstractAction("", zoomOutIcon) {
			public void actionPerformed(ActionEvent e) {
				graph.setScale(graph.getScale() / ZOOM_SCALE);
				zoomText.setText(Math.round(graph.getScale() * 100) + "%");
			}
		};
		zoomOut.putValue( Action.NAME, "Aproximar" );
		zoomOut.putValue( Action.SHORT_DESCRIPTION, "Aproximar" );
		toolbar.add(zoomOut);

		// Group
		toolbar.addSeparator();
		URL groupUrl = getClass().getClassLoader().getResource("gif/group.gif");
		ImageIcon groupIcon = new ImageIcon(groupUrl);
		group = new AbstractAction("", groupIcon) {
			public void actionPerformed(ActionEvent e) {
				group(graph.getSelectionCells());
			}
		};
		group.putValue( Action.NAME, "Agrupar" );
		group.putValue( Action.SHORT_DESCRIPTION, "Agrupar" );
		group.setEnabled(false);
		toolbar.add(group);

		// Ungroup
		URL ungroupUrl = getClass().getClassLoader().getResource("gif/ungroup.gif");
		ImageIcon ungroupIcon = new ImageIcon(ungroupUrl);
		ungroup = new AbstractAction("", ungroupIcon) {
			public void actionPerformed(ActionEvent e) {
				ungroup(graph.getSelectionCells());
			}
		};
		ungroup.putValue( Action.NAME, "Desagrupar" );
		ungroup.putValue( Action.SHORT_DESCRIPTION, "Desagrupar" );
		ungroup.setEnabled(false);
		toolbar.add(ungroup);
		
		// To Front
		toolbar.addSeparator();
		URL toFrontUrl = getClass().getClassLoader().getResource("gif/tofront.gif");
		ImageIcon toFrontIcon = new ImageIcon(toFrontUrl);
		tofront = new AbstractAction("", toFrontIcon) {
			public void actionPerformed(ActionEvent e) {
				if (!graph.isSelectionEmpty())
					toFront(graph.getSelectionCells());
			}
		};
		tofront.putValue( Action.NAME, "Trazer para frente" );
		tofront.putValue( Action.SHORT_DESCRIPTION, "Trazer para frente" );
		tofront.setEnabled(false);
		toolbar.add(tofront);

		// To Back
		URL toBackUrl = getClass().getClassLoader().getResource("gif/toback.gif");
		ImageIcon toBackIcon = new ImageIcon(toBackUrl);
		toback = new AbstractAction("", toBackIcon) {
			public void actionPerformed(ActionEvent e) {
				if (!graph.isSelectionEmpty())
					toBack(graph.getSelectionCells());
			}
		};
		toback.putValue( Action.NAME, "Enviar para trás" );
		toback.putValue( Action.SHORT_DESCRIPTION, "Enviar para trás" );
		toback.setEnabled(false);
		toolbar.add(toback);

		//toolbar.addSeparator();
		//toolbar.add(new TiltAction(graph));

		// EFLOW
		toolbar.addSeparator();
		URL gridUrl = getClass().getClassLoader().getResource("gif/grid.gif");
		ImageIcon gridIcon = new ImageIcon(gridUrl);
		gridToggleButton = new JToggleButton(gridIcon);
		gridToggleButton.setToolTipText("Ativar/Desativar exibição do grid");
		gridToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph.setGridVisible(!graph.isGridVisible());
			}
		});
		gridToggleButton.setSelected(true);
		toolbar.add(gridToggleButton);
		
		return toolbar;
		
	}

	/**
	 * Método de inicialização do ToolBar
	 */
	public JToolBar createToolBar() {

		JToolBar toolbar = new JToolBar( JToolBar.VERTICAL );
		toolbar.setFloatable(true);

		toolbar.setLayout(new GridLayout(9,1));
		
		URL startUrl = getClass().getClassLoader().getResource("gif/start.gif");
		ImageIcon startIcon = new ImageIcon(startUrl);
		JToggleButton startButton = new JToggleButton(startIcon);
		startButton.setActionCommand(START_CELL);
		
		URL activityUrl = getClass().getClassLoader().getResource("gif/activity.gif");
		ImageIcon activityIcon = new ImageIcon(activityUrl);
		JToggleButton activityButton = new JToggleButton(activityIcon);
		activityButton.setActionCommand(ACTIVITY_CELL);
		
		URL corUrl = getClass().getClassLoader().getResource("gif/fill_big.gif");
		ImageIcon colorIcon = new ImageIcon(corUrl);
		JToggleButton corButton = new JToggleButton(colorIcon);
		corButton.setActionCommand(COLORS_CELL);
		
		color = new AbstractAction("", colorIcon) {
			public void actionPerformed(ActionEvent e) {
				color();
			}
		};
		
		URL stopUrl = getClass().getClassLoader().getResource("gif/stop.gif");
		ImageIcon stopIcon = new ImageIcon(stopUrl);
		JToggleButton stopButton = new JToggleButton(stopIcon);
		stopButton.setActionCommand(END_CELL);

		URL forkUrl = getClass().getClassLoader().getResource("gif/fork.gif");
		ImageIcon forkIcon = new ImageIcon(forkUrl);
		JToggleButton forkButton = new JToggleButton(forkIcon);
		forkButton.setActionCommand(FORK_CELL);

		URL joinUrl = getClass().getClassLoader().getResource("gif/join.gif");
		ImageIcon joinIcon = new ImageIcon(joinUrl);
		JToggleButton joinButton = new JToggleButton(joinIcon);
		joinButton.setActionCommand(JOIN_CELL);

		// novos
		URL decisionUrl = getClass().getClassLoader().getResource("gif/decision.gif");
		ImageIcon decisionIcon = new ImageIcon(decisionUrl);
		JToggleButton decisionButton = new JToggleButton(decisionIcon);
		decisionButton.setActionCommand(ACTIVITYDECISION_CELL);
		
		URL subProcessUrl = getClass().getClassLoader().getResource("gif/process.gif");
		ImageIcon subProcessIcon = new ImageIcon(subProcessUrl);
		JToggleButton subProcessButton = new JToggleButton(subProcessIcon);
		subProcessButton.setActionCommand(SUBPROCESS_CELL);

		/*
		 * URL decisionUrl =
		 * getClass().getClassLoader().getResource("gif/decision.gif");
		 * ImageIcon decisionIcon = new ImageIcon(decisionUrl); JToggleButton
		 * decisionButton=new JToggleButton(decisionIcon);
		 * decisionButton.setActionCommand(DECISION_CELL);
		 * decisionButton.setToolTipText("Decisão (não usar)");
		 */

		// Toggle Connect Mode
		URL connectUrl = getClass().getClassLoader().getResource(
				"gif/connecton.gif");
		ImageIcon connectIcon = new ImageIcon(connectUrl);
		connectonButton = new JToggleButton(connectIcon);
		//connectonButton.setActionCommand(TRANSITION);
		//connectonButton.setText("Transição");

		buttongroup.add(startButton);
		buttongroup.add(activityButton);
		buttongroup.add(corButton);
		buttongroup.add(stopButton);
		buttongroup.add(forkButton);
		buttongroup.add(joinButton);
		buttongroup.add(subProcessButton);
		buttongroup.add(decisionButton);
		buttongroup.add(connectonButton);
		connectonButton.setSelected(true);

		toolbar.add(startButton);
		toolbar.add(activityButton);
		//toolbar.add(corButton);
		toolbar.add(color);
		toolbar.add(stopButton);
		toolbar.add(forkButton);
		toolbar.add(joinButton);
		toolbar.add(subProcessButton);
		toolbar.add(decisionButton);
		toolbar.add(connectonButton);

		return toolbar;

	}
	
	//Ação disparada pelo icone de cores 
	public void color() {
		
		this.graph = graph;
		setEnabled( true );
		
		int selectCount = graph.getSelectionCount();
        if ( selectCount > 0 ) {
            Component component = graph.getComponent(0);
            Color value = JColorChooser.showDialog(component,"ColorDialog",null);
            if (value != null) {
                Object[] obj = graph.getSelectionCells();
                for (int i = 0; i < selectCount; i++) {
                    Object objCell = graph.getModel().getRootAt(graph.getModel().getIndexOfRoot(obj[i]));
                    if (objCell instanceof ActivityDecisionCell) {
                        ((ActivityDecisionCell)objCell).getModel().setBackColor(value);
                    }
                    else if (objCell instanceof SubProcessCell) {
                        SubProcessView view = (SubProcessView) graph.getGraphLayoutCache().getMapping(objCell, false);
                        ((SubProcessCell)objCell).getModel().setBackColor(value);
                        view.setBackColor(value);
                    }
                    else if (objCell instanceof ActivityCell) {
                        ActivityCell activity = ((ActivityCell)objCell);
                        ActivityView view = (ActivityView) graph.getGraphLayoutCache().getMapping(objCell, false);
                        activity.getModel().setBackColor(value);
                        AttributeMap map = view.getAttributes();
                        GraphConstants.setBackground(map, value);
                        view.setBackColor(value);
                    }
                    else if (objCell instanceof DecisionCell)
                        ((DecisionCell)objCell).getModel().setBackColor(value);
                    else if (objCell instanceof Comment)
                        ((Comment)objCell).getModel().setBackColor(value);
                    else if (objCell instanceof StartCell)
                        ((StartCell)objCell).getModel().setBackColor(value);
                    else if (objCell instanceof EndCell)
                        ((EndCell)objCell).getModel().setBackColor(value);
                    CellView view = graph.getGraphLayoutCache().getMapping(objCell, false);
                    view.update();
                }
            }
        }
	}

	// This will change the source of the actionevent to graph.
	protected class EventRedirector extends AbstractAction {

		protected Action action;

		// Construct the "Wrapper" Action
		public EventRedirector(Action a) {
			super("", (ImageIcon) a.getValue(Action.SMALL_ICON));
			this.action = a;
		}

		// Redirect the Actionevent
		public void actionPerformed(ActionEvent e) {
			e = new ActionEvent(graph, e.getID(), e.getActionCommand(), e
					.getModifiers());
			action.actionPerformed(e);
		}
	}

	/**
	 * 
	 */
	public JScrollPane getScrollPane() {
		return mainPane;
	}

	public GpdGraph getGraph() {
		return graph;
	}
}
