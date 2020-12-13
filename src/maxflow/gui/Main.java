package maxflow.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
import java.io.File;
import java.net.URL;

import javax.swing.*;
import javax.swing.event.*;

import org.jbpm.gpd.ExceptionHandler;
import org.jbpm.gpd.GpdGraph;
import org.jbpm.gpd.GpdGraphModel;
import org.jbpm.gpd.GpdOverviewPanel;
import org.jbpm.gpd.ProcessDesigner;
import org.jbpm.gpd.cell.ActivityCell;
import org.jbpm.gpd.cell.ActivityDecisionCell;
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
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

public class Main extends JFrame implements GraphSelectionListener,KeyListener {
	
	public static final double ESCALA_INICIAL = 1;

	public static final double ESCALA_ZOMM = 1.1;
	
	private JFormattedTextField rateField;
	private JTextField textIp;
	private JTextField textSchema;
	private JTextField textUsuario;
	private JPasswordField textSenha;
	private JButton confirmarConfiguracao;
	private JButton btOpen, btSave, btImport;
	private JTextArea log;
	protected Action voltar, avancar, deletar, grupo, desagrupar, parafrente, paratras, recortar, copiar, colar;
    
	private JFileChooser fc;
    
    JMenuBar menuBar;
    JMenu menu, submenu;
    
    DefaultGraphCell[] cells;
    private JMenuItem menuItem,menuNovo;
    
    public final String INICIO = "INICIO"; 
    public final String FIM = "FIM";
    public final String ATIVIDADE = "ATIVIDADE";
    public final String PROCESSO = "PROCESSO";
    public final String DECISAO = "DECISAO";
    public final String TRANSICAO = "TRANSICAO";
    
    private String diretorioGifs = "/maxflow/gif/";
    
    protected GpdGraph graph;
    protected GpdGraph painelGraph;
    //protected JGraph painelGraph;
    
    private ButtonGroup buttongroup = new ButtonGroup();
    
    private Controller propertyController = null;
    
    private JSplitPane librarySplit = null;
    
    JToggleButton iconTransicao;
    
    public Main() {
    	
    	ExceptionHandler.getInstance().init(this);
    	
    	setTitle("Max-Flow Editor de Processos desenv 0.2");
        setSize(600, 350);
        setExtendedState(MAXIMIZED_BOTH);
        
        
       
        
        /* Marcador */
        graph = new GpdGraph(new GpdGraphModel());
        
        painelGraph = new GpdGraph(new GpdGraphModel());//this.maxGraph();
        painelGraph .setScale(ESCALA_INICIAL * painelGraph.getScale());
        //JOptionPane.showMessageDialog(null,painelGraph.getScale()+"");
        
        this.setJMenuBar(menu());
        
        JToolBar toolBarPadrao = new JToolBar("Barra de Ferramentas");
        this.botoesPadrao(toolBarPadrao);
        
        JToolBar toolBarBasico = new JToolBar("Barra de Ferramentas");
        this.botoesBasico(toolBarBasico);
        
        setPreferredSize(new Dimension(450, 130));
        //add(toolBarPadrao, BorderLayout.EAST);
        add( toolBarPadrao, BorderLayout.EAST );      
        add( toolBarBasico, BorderLayout.PAGE_START );
        //add( toolBarBasico, BorderLayout.PAGE_START );
        //JOptionPane.showMessageDialog(this,toolBarPadrao.getLayout().preferredLayoutSize(this) );
        
        toolBarPadrao.setLayout(new GridLayout(8,1));
        toolBarPadrao.doLayout();
        //toolBarPadrao.disable();
        
        JPanel painelPropriedade = new JPanel();
        painelPropriedade.setLayout(new GridLayout(1, 1));
        
        //ProcessDesigner p = new ProcessDesigner();
        //JPanel overviewPane = GpdOverviewPanel.createOverviewPanel( painelGraph,p );
        
        JPanel painelVisualizacao = new JPanel();
        painelVisualizacao.setLayout(new GridLayout(1,1) );
        //painelVisualizacao.add();
        
        /*JScrollPane mainPane = new JScrollPane(painelGraph);
        
        GpdSplitPane splitPane = new GpdSplitPane(GpdSplitPane.HORIZONTAL_SPLIT, librarySplit, mainPane);
		splitPane.setName("DocumentMain");
		splitPane.setDividerSize(splitPane.getDividerSize() * 2);
		splitPane.setOneTouchExpandable(true);
		//painelVisualizacao(splitPane, BorderLayout.CENTER);
        */
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.GRAY);
        tabbedPane.setMinimumSize(new Dimension(200,300));
        
        JComponent panelInternoAcao = makeTextPanel("");
        panelInternoAcao.setLayout(new BorderLayout());
        panelInternoAcao.add(painelImportacao(), BorderLayout.CENTER);
        tabbedPane.addTab("Processo", null, panelInternoAcao, "Realizar a importação de arquivo.");
        //tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panelInternoConfiguracao = makeTextPanel("");
        panelInternoConfiguracao.setLayout(new BorderLayout());
        panelInternoConfiguracao.add(painelConfiguracao(), BorderLayout.CENTER);
        tabbedPane.addTab("Propriedades", null, panelInternoConfiguracao,"Realizar a configuração do Banco de Dados.");
        //tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        painelPropriedade.add(tabbedPane, BorderLayout.CENTER );
        //painelEsquerda.add(tabbedPane, BorderLayout.CENTER );
        
        
        painelGraph.addMouseListener(new MouseAdapter() {
			private Object lastCell = null;
			
			// EFLOW: mousepressed
			public void mousePressed(MouseEvent e) {
				
				// Get Cell under Mousepointer
				int x = e.getX(), y = e.getY();
				String command = buttongroup.getSelection().getActionCommand();
				//JOptionPane.showMessageDialog(null,command);
				if (command == null) {
					Object cell = painelGraph.getFirstCellForLocation(x, y);
					// Print Cell Label
					if (cell != null && cell != lastCell) {
						if (propertyController == null) {
							propertyController = new MasterDetailCellController();
						}
						propertyController.setModel(cell);
						librarySplit.setRightComponent(propertyController.getView());

						lastCell = cell;
					}
				} else {
					x = new Double(x / painelGraph.getScale()).intValue();
					y = new Double(y / painelGraph.getScale()).intValue();
					if (command.equals(INICIO)) {
						//JOptionPane.showMessageDialog(null,"X:"+x +" - Y:"+y);
						//insert(new Point(x, y), new ActivityCell(),new Dimension(100, 50));
						insert(new Point(x, y), new StartCell());
					} else if (command.equals(FIM)) {
						//JOptionPane.showMessageDialog(null,"X:"+x +" - Y:"+y);
						//insert(new Point(x, y), new ActivityCell(),new Dimension(100, 50));
						//JOptionPane.showMessageDialog(null,"X:");
						insert(new Point(x, y), new EndCell());
					} 
					// voltando botão para TRANSIÇÃO
					buttongroup.setSelected(iconTransicao.getModel(), true);
				}
			}
		});
        
        JSplitPane splitpaneEsquerda = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, painelPropriedade, painelVisualizacao );
        JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, splitpaneEsquerda, new JScrollPane(painelGraph));
        
        splitpane.setOneTouchExpandable(true);
        
        getContentPane().add(splitpane);
    
    }
    
    public void insert(Point2D point, DefaultGraphCell vertex) {
		insert(point, vertex, null);
	}

	// Insert a new Vertex at point
	public void insert(Point2D point, DefaultGraphCell vertex, Dimension size) {
		// Construct Vertex with no Label
		// DefaultGraphCell vertex = new DefaultGraphCell();
		// Add one Floating Port
		vertex.add(new DefaultPort(vertex));

		// Snap the Point to the Grid
		point = painelGraph.snap((Point2D) point.clone());
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
		// marcador Isac
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

		painelGraph.getGraphLayoutCache().insert(new Object[] { vertex });

		// só usa o ActivityRenderer se tirar esse IF
		// if(!(vertex instanceof ActivityCell)) {
		painelGraph.getGraphLayoutCache().insertViews(new CellView[] { view });
		painelGraph.getGraphLayoutCache().putMapping(vertex, view);
		// }
	}
    
    public JGraph maxGraph() {

    	JGraph retorno;
    	GraphModel model = new DefaultGraphModel();
    	GraphLayoutCache view = new GraphLayoutCache(model,
    			                            new DefaultCellViewFactory());
    	retorno = new JGraph(model, view); 
    	
    	cells = new DefaultGraphCell[5];
    	
    	cells[0] = new DefaultGraphCell(new String("Hello"));
    	
    	GraphConstants.setBounds(cells[0].getAttributes(), new
    			Rectangle2D.Double(20,20,40,20));
    			GraphConstants.setGradientColor(
    			cells[0].getAttributes(),
    			Color.orange);
    			GraphConstants.setOpaque(cells[0].getAttributes(), true);
    			
		DefaultPort port1 = new DefaultPort();
		cells[0].add(port1);
		DefaultEdge edge = new DefaultEdge();
		edge.setSource(cells[0].getChildAt(0));
		edge.setTarget(cells[0].getChildAt(0));
		cells[0] = edge;
    			
    	retorno.getGraphLayoutCache().insert(cells[0]);
    	
    	return retorno;
    }
    
    
    
    public static void main(String[] args) {
        
        try {
	    	Main exec = new Main();
	        exec.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        exec.setVisible(true);
        } catch(Exception e) {
        	JOptionPane.showMessageDialog(null,"Erro:"+e);
        	System.out.println(e);
        }
        
    }
    
    public JMenuBar menu() {
    	menuBar = new JMenuBar();
    	menu = new JMenu();
    	submenu = new JMenu();
    	menuItem = new JMenuItem();
    	
    	URL imageURL = Main.class.getResource( diretorioGifs + "abrir.gif");;
    	
    	menu = new JMenu("Arquivo");
    	menu.setMnemonic(KeyEvent.VK_A);
    	menu.getAccessibleContext().setAccessibleDescription("Arquivo");
    	menuBar.add(menu);

    	menuNovo = new JMenuItem("Novo",KeyEvent.VK_T);
    	menuNovo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    	//menuNovo.addActionListener(this);
    	menu.add(menuNovo);
    	
    	imageURL = Main.class.getResource(diretorioGifs+"abrir.gif");
    	menuItem = new JMenuItem("Abrir",new ImageIcon(imageURL));
    	menuItem.setMnemonic(KeyEvent.VK_B);
    	menu.add(menuItem);
    	
    	imageURL = Main.class.getResource("/maxflow/gif/abrir.gif");
    	menuItem = new JMenuItem("Salvar",new ImageIcon(imageURL));
    	menuItem.setMnemonic(KeyEvent.VK_B);
    	menu.add(menuItem);
    	
    	imageURL = Main.class.getResource("/maxflow/gif/abrir.gif");
    	menuItem = new JMenuItem("Salvar/Exportar",new ImageIcon(imageURL));
    	menuItem.setMnemonic(KeyEvent.VK_B);
    	menu.add(menuItem);
    	
    	menu.addSeparator();
    	
    	menuItem = new JMenuItem(new ImageIcon("/maxflow/gif/abrir.gif"));
    	menuItem = new JMenuItem("Imprimir",new ImageIcon(imageURL));
    	menuItem.setMnemonic(KeyEvent.VK_D);
    	menu.add(menuItem);
    	
    	menuItem = new JMenuItem(new ImageIcon("/maxflow/gif/abrir.gif"));
    	menuItem = new JMenuItem("Visualizar Impressão",new ImageIcon(imageURL));
    	menuItem.setMnemonic(KeyEvent.VK_D);
    	menu.add(menuItem);
    	
    	menuItem = new JMenuItem(new ImageIcon("/maxflow/gif/abrir.gif"));
    	menuItem = new JMenuItem("Configurar Página",new ImageIcon(imageURL));
    	menuItem.setMnemonic(KeyEvent.VK_D);
    	menu.add(menuItem);
    	
    	menu.addSeparator();

    	submenu = new JMenu("Exportar");
    	submenu.setMnemonic(KeyEvent.VK_S);

    	menuItem = new JMenuItem("Exportar arquivo para");
    	menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
    	submenu.add(menuItem);

    	submenu.addSeparator();
    	
    	menuItem = new JMenuItem("Salvar JPG");
    	submenu.add(menuItem);
    	menu.add(submenu);

//    	Build second menu in the menu bar.
    	menu = new JMenu("Formatar");
    	menu.setMnemonic(KeyEvent.VK_N);
    	menu.getAccessibleContext().setAccessibleDescription(
    	        "This menu does nothing");
    	menuBar.add(menu);

    	imageURL = Main.class.getResource("/maxflow/gif/abrir.gif");
    	menuItem = new JMenuItem("Cor",new ImageIcon(imageURL));
    	menuItem.setMnemonic(KeyEvent.VK_B);
    	menu.add(menuItem);
    	
    	menu = new JMenu("Ferramentas");
    	menu.setMnemonic(KeyEvent.VK_N);
    	menu.getAccessibleContext().setAccessibleDescription(
    	        "This menu does nothing");
    	menuBar.add(menu);
    	
    	imageURL = Main.class.getResource("/maxflow/gif/abrir.gif");
    	menuItem = new JMenuItem("Importar dados do E-flow",new ImageIcon(imageURL));
    	menuItem.setMnemonic(KeyEvent.VK_B);
    	menu.add(menuItem);
    	
    	menu = new JMenu("Ajuda");
    	menu.setMnemonic(KeyEvent.VK_N);
    	menu.getAccessibleContext().setAccessibleDescription(
    	        "This menu does nothing");
    	menuBar.add(menu);
    	
    	menuItem = new JMenuItem("Ajuda");
    	menuItem.setMnemonic(KeyEvent.VK_B);
    	menu.add(menuItem);
    	
    	menu.addSeparator();
    	
    	menuItem = new JMenuItem("Sobre");
    	menuItem.setMnemonic(KeyEvent.VK_B);
    	menu.add(menuItem);
    	
    	return menuBar;
    }
    
    
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
    
    
    
    public JPanel painelImportacao() {
    	JPanel painel = new JPanel();
    	painel.setLayout(new GridLayout(10,2));
    	
    	log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        
        fc = new JFileChooser();
    	
        btOpen = new JButton("Open a File...");
        		//btOpen.addActionListener(this);

        btSave = new JButton("Save a File...");
        		//btSave.addActionListener(this);
        
		btImport = new JButton("Import a File...");
				//btImport.addActionListener(this);
				
		JPanel buttonPanel = new JPanel(); //use FlowLayout
        //buttonPanel.add(btOpen);
        //buttonPanel.add(btSave);
        //buttonPanel.add(btImport);
        
        painel.add(buttonPanel);
        
    	return painel;
    }
    
    public void actionPerformed(ActionEvent e) {

    	boolean sucesso = false;
    	System.out.println("Ação:"+e.getSource());
    	//Handle open button action.
        if (e.getSource() == btOpen) {
            int returnVal = fc.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                log.append("Opening: " + file.getName() + "." + "\n");
            } else {
                log.append("Open command cancelled by user." + "\n");
            }
            log.setCaretPosition(log.getDocument().getLength());

        //Handle save button action.
        } else if (e.getSource() == btSave) {
        	
            int returnVal = fc.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would save the file.
                log.append("Saving: " + file.getName() + "." + "\n");
            } else {
                log.append("Save command cancelled by user." + "\n");
            }
            log.setCaretPosition(log.getDocument().getLength());
            
        } else if ( e.getSource() == menuNovo ) {
        	
        	
        	int ret = JOptionPane.showConfirmDialog(this,"Deseja salvar o arquivo atual?");
        	if (ret == 0) {
        		JOptionPane.showMessageDialog(this,"Arquivo salvo com sucesso!");
        		painelGraph = new GpdGraph(new GpdGraphModel());//this.maxGraph();
        	} else if (ret == 1) {
        		painelGraph = new GpdGraph(new GpdGraphModel());//this.maxGraph();
        	}
        	
        } else if ( e.getSource() == "" ) {
        	
        	DefaultGraphCell cells = new DefaultGraphCell();
        	
        	cells = new DefaultGraphCell(new String("Hello"));
        	
        	GraphConstants.setBounds(cells.getAttributes(), new
        			Rectangle2D.Double(20,20,40,20));
        			GraphConstants.setGradientColor(
        			cells.getAttributes(),
        			Color.orange);
        			GraphConstants.setOpaque(cells.getAttributes(), true);
        			
			DefaultPort port1 = new DefaultPort();
			cells.add(port1);
			DefaultEdge edge = new DefaultEdge();
			edge.setSource(cells.getChildAt(0));
			edge.setTarget(cells.getChildAt(0));
			cells = edge;
        			
        	painelGraph.getGraphLayoutCache().insert(cells);
        	JOptionPane.showMessageDialog(this,"teste");
        }
        
        
    }
    
    public JPanel painelConfiguracao() {
    	
    	JPanel painel = new JPanel();
    	//painel.setBackground(Color.red);
    	painel.setLayout(new GridLayout(10,2));
    	painel.setMinimumSize(new Dimension(200,300));
    	textIp = new JTextField();
    	textSchema = new JTextField();
    	textUsuario = new JTextField();
    	textSenha = new JPasswordField();
    	confirmarConfiguracao = new JButton ("Confirmar");
    	confirmarConfiguracao.setSize(new Dimension(40,40));
    	painel.add(new JLabel("Endereço Ip"),BorderLayout.WEST);
    	painel.add(textIp);
    	painel.add(new JLabel("Schema"));
    	painel.add(textSchema);
    	painel.add(new JLabel("Usuário"));
    	painel.add(textUsuario);
    	painel.add(new JLabel("Senha"));
    	painel.add(textSenha);
    	painel.add(confirmarConfiguracao,BorderLayout.WEST);
    	
    	//return painel;
    	return new JPanel();
    	
    }
    
protected void botoesPadrao(JToolBar toolBar) {
        
        JToggleButton iconInicioProcesso = this.addToggleButton("inicio_processo", INICIO,
                                      						"Inicializa o processo.",
                                      						"Início");
        buttongroup.add(iconInicioProcesso);
        toolBar.add(iconInicioProcesso);
        
        JToggleButton iconFimProcesso = this.addToggleButton("fim_processo", FIM,
										                "Finaliza o processo",
										                "Fim");
		buttongroup.add(iconFimProcesso);
		toolBar.add(iconFimProcesso);
		
		JToggleButton iconFork = this.addToggleButton("fork", FIM,
									                "Fork",
									                "Form");
		buttongroup.add(iconFork);
		toolBar.add(iconFork);
		
		JToggleButton iconJoin = this.addToggleButton("join", FIM,
									                "Join",
									                "Join");
		buttongroup.add(iconJoin);
		toolBar.add(iconJoin);
		
		JToggleButton iconAtividade = this.addToggleButton("atividade", ATIVIDADE,
										                "Atividade",
										                "Atividade");
		buttongroup.add(iconAtividade);
		toolBar.add(iconAtividade);
        
		JToggleButton iconProcesso = this.addToggleButton("processo", PROCESSO,
										                "Processo",
										                "Processo");
		buttongroup.add(iconProcesso);
		toolBar.add(iconProcesso);
		
		JToggleButton iconDecisao = this.addToggleButton("decisao", DECISAO,
										                "Decisão",
										                "Decisão");
		buttongroup.add(iconDecisao);
		toolBar.add(iconDecisao);
		
		iconTransicao = this.addToggleButton("transicao", TRANSICAO,
							                "Transição",
							                "Transição");
		buttongroup.add(iconTransicao);
		toolBar.add(iconTransicao);
		//Seleciona o icone transição como default.
		buttongroup.setSelected(iconTransicao.getModel(), true);
			
	}
    
    protected void botoesBasico(JToolBar toolBar) {
        
        //Voltar
		URL voltarUrl = Main.class.getResource(diretorioGifs+"voltar.gif");
		ImageIcon voltarIcon = new ImageIcon(voltarUrl);
		voltar = new AbstractAction("Voltar", voltarIcon) {
			public void actionPerformed(ActionEvent e) {
				//voltar();
				//JOptionPane.showMessageDialog(null,toolBar+"");
			}
		};
		voltar.setEnabled(false);
		toolBar.add(voltar);
		
		// Avançar
		URL avancarUrl = Main.class.getResource(diretorioGifs+"avancar.gif");
		ImageIcon avancarIcon = new ImageIcon(avancarUrl);
		avancar = new AbstractAction("Avançar", avancarIcon) {
			public void actionPerformed(ActionEvent e) {
				//avancar();
			}
		};
		avancar.setEnabled(false);
		toolBar.add(avancar);
		
		toolBar.addSeparator();
		
		/**
		 * Bloco de edição
		 */
		Action action;
		URL url;

		// copiar
		action = TransferHandler.getCopyAction();
		url = Main.class.getResource(diretorioGifs+"copiar.gif");
		action.putValue(Action.SMALL_ICON, new ImageIcon(url));
		toolBar.add(copiar = new EventRedirector(action));
		
		// Colar
		action = TransferHandler.getPasteAction();
		url = Main.class.getResource(diretorioGifs+"colar.gif");
		action.putValue(Action.SMALL_ICON, new ImageIcon(url));
		toolBar.add(colar = new EventRedirector(action));
		
		// recortar
		action = TransferHandler.getCutAction();
		url = Main.class.getResource(diretorioGifs+"recortar.gif");
		action.putValue(Action.SMALL_ICON, new ImageIcon(url));
		toolBar.add(recortar = new EventRedirector(action));
		
		// deletar
		URL deletarUrl = Main.class.getResource(diretorioGifs+"excluir.gif");
		ImageIcon deletarIcon = new ImageIcon(deletarUrl);
		deletar = new AbstractAction("", deletarIcon) {
			public void actionPerformed(ActionEvent e) {
				if (!graph.isSelectionEmpty()) {
					Object[] cells = graph.getSelectionCells();
					cells = graph.getDescendants(cells);
					graph.getModel().remove(cells);
				}
			}
		};
		deletar.setEnabled(false);
		toolBar.add(deletar);
		
		toolBar.addSeparator();

		/**
		 * Bloco do Zoom
		 */
		final JTextField zoomText = new JTextField();
		
		URL zoomUrl = Main.class.getResource(diretorioGifs+"zoom.gif");
		ImageIcon zoomIcon = new ImageIcon(zoomUrl);
		toolBar.add(new AbstractAction("Zomm", zoomIcon) {
			public void actionPerformed(ActionEvent e) {
				painelGraph.setScale(ESCALA_INICIAL);
				zoomText.setText("100%");
			}
		});
		
		Dimension dz = new Dimension(40,25);
		zoomText.setPreferredSize(dz);
		zoomText.setMaximumSize(dz);
		zoomText.setMinimumSize(dz);
		//System.out.println(painelGraph.getScale());
		zoomText.setText(Math.round(painelGraph.getScale() * 100) + "%");
		
		zoomText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				double valor = painelGraph.getScale();
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
				painelGraph.setScale(valor);
				zoomText.setText(Math.round(valor * 100) + "%");
			}
		});
		
		// Zoom In
		URL zoomInUrl = Main.class.getResource(diretorioGifs+"zoomin.gif");
		ImageIcon zoomInIcon = new ImageIcon(zoomInUrl);
		
		toolBar.add(new AbstractAction("Afastar", zoomInIcon) {
			public void actionPerformed(ActionEvent e) {
				painelGraph.setScale(ESCALA_ZOMM * painelGraph.getScale());
				zoomText.setText(Math.round(painelGraph.getScale() * 100) + "%");
			}
		});
		
		// Zoom Out
		URL zoomOutUrl = Main.class.getResource(diretorioGifs+"zoomout.gif");
		ImageIcon zoomOutIcon = new ImageIcon(zoomOutUrl);
		toolBar.add(new AbstractAction("Aproximar", zoomOutIcon) {
			public void actionPerformed(ActionEvent e) {
				painelGraph.setScale(painelGraph.getScale() / ESCALA_ZOMM);
				zoomText.setText(Math.round(painelGraph.getScale() * 100) + "%");
			}
		});

		toolBar.add(zoomText);
		/*
		// Group
		toolbar.addSeparator();
		URL groupUrl = getClass().getClassLoader().getResource("gif/group.gif");
		ImageIcon groupIcon = new ImageIcon(groupUrl);
		group = new AbstractAction("", groupIcon) {
			public void actionPerformed(ActionEvent e) {
				group(graph.getSelectionCells());
			}
		};
		group.setEnabled(false);
		
		toolbar.add(group);
		*/
    }

    
    protected JButton addButton(String nomeImagem, String acao,
					          		String toolTipText, String texto) {
    	String localImagem = diretorioGifs + nomeImagem + ".gif";

		URL imageURL = Main.class.getResource(localImagem);
		ImageIcon startIcon = new ImageIcon(imageURL);
		JButton button = new JButton(startIcon);
		button.setActionCommand(acao);
		button.setToolTipText(toolTipText);
		
		return button;

    }
    
    protected JToggleButton addToggleButton(String nomeImagem, String acao,
							          			String toolTipText, String texto) {

		String localImagem = diretorioGifs + nomeImagem + ".gif";
		
		//URL imageUrl = getClass().getClassLoader().getResource(localImagem);
		URL imageURL = Main.class.getResource(localImagem);
		ImageIcon startIcon = new ImageIcon(imageURL);
		JToggleButton button = new JToggleButton(startIcon);
		button.setActionCommand(acao);
		button.setToolTipText(toolTipText);
		button.setText(texto);
		//button.setSize(new Dimension(20,20));
		
		return button;
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
			deletar.actionPerformed(null);
	}

	protected class EventRedirector extends AbstractAction {

		protected Action action;

		// Construct the "Wrapper" Action
		public EventRedirector(Action a) {
			super("", (ImageIcon) a.getValue(Action.SMALL_ICON));
			this.action = a;
		}

		// Redirect the Actionevent
		public void actionPerformed(ActionEvent e) {
			e = new ActionEvent(painelGraph, e.getID(), e.getActionCommand(), e
					.getModifiers());
			action.actionPerformed(e);
		}
	}
	
	public void valueChanged(GraphSelectionEvent e) {

		// Group Button only Enabled if more than One Cell Selected
		grupo.setEnabled(painelGraph.getSelectionCount() > 1);
		// Update Button States based on Current Selection
		boolean enabled = !painelGraph.isSelectionEmpty();
		deletar.setEnabled(enabled);
		desagrupar.setEnabled(enabled);
		parafrente.setEnabled(enabled);
		paratras.setEnabled(enabled);
		copiar.setEnabled(enabled);
		recortar.setEnabled(enabled);
	}
}
