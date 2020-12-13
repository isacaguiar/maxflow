package eflow.splash;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jbpm.gpd.Configuration;
import org.jbpm.gpd.ProcessDesigner;
import org.jbpm.gpd.action.SaveAction;

import eflow.gpd.action.ImportEflowAction;
import eflow.gpd.action.ImportEflowAction.ImportThread;
import eflow.versao.Versao;

public class EflowFrame extends JFrame {

	private JPanel jContentPane = null;
	private ProcessDesigner pDesigner = null;

	/**
	 * This is the default constructor
	 */
	public EflowFrame() {
		super();
		initialize();
		iniciar();
	}

	public void iniciar() {
		PainelSplash.getInstance().abrir();
		Configuration.getInstance();
		PainelSplash.getInstance().setMensagem("Configurando...");
		// Construct Frame
		try {
			UIManager.setLookAndFeel(eflow.config.Configuration.getLookAndFeel());
		} catch (Exception e) {
			//JOptionPane.showMessageDialog(null,e);
		}
		// Set Close Operation to Exit
		pDesigner = new ProcessDesigner();
		this.setJMenuBar(pDesigner.createMenuBar());
		this.getContentPane().add(pDesigner);
		// Fetch URL to Icon Resource
		URL jgraphUrl = ProcessDesigner.class.getClassLoader().getResource("gif/jgraph.gif");
		// If Valid URL
		if (jgraphUrl != null) {
			// Load Icon
			ImageIcon jgraphIcon = new ImageIcon(jgraphUrl);
			// Use in Window
			this.setIconImage(jgraphIcon.getImage());
		}
		this.setTitle(this.getTitle()+" " + Versao.getVersao());
		// Set Default Size
		this.pack();
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = this.getSize();
        this.setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
		// Show Frame
		// frame.requestFocusInWindow();
		PainelSplash.getInstance().setMensagem("Comunicando com o servidor...");

		/*
		 * EFLOW carrega os dados do servidor
		 */
		
		try {
			Thread t = new Thread(
					new ImportEflowAction(null).new ImportThread());
			t.setPriority(Thread.MIN_PRIORITY);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		new Thread() {
			public void run() {
				try {
					Thread.sleep(2000);
				}catch(Exception e){} 
				PainelSplash.getInstance().fechar();
			}
		}.start();
		
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		Dimension size = new Dimension(800, 600);
		this.setSize(size);
		this.setPreferredSize(size);
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("E-Flow Editor");
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				int result = JOptionPane.showConfirmDialog(EflowFrame.this, "Salvar alterações?","E-flow Editor", JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION){
					//salvar
					new SaveAction(pDesigner.getGraph()).actionPerformed(null);
				}
			}
		});
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
		}
		return jContentPane;
	}
	
	public static void main(String args[]) {
		new EflowFrame();
	}

}
