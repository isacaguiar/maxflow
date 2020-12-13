
package org.jbpm.gpd.dialog.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import maxflow.painel.ImagePanel;

import org.jbpm.gpd.dialog.controller.Controller;
import org.jbpm.gpd.dialog.panel.detailpanel.ProcessPanel;
import org.jbpm.gpd.dialog.util.GpdSplitPane;

public abstract class MasterDetailCellPanel extends AbstractPanel {

	private JPanel south;
	private JPanel north;
	private GpdSplitPane mainPanel;
	

	/**
	 * @param controller
	 */
	public MasterDetailCellPanel(Controller controller) {
		super(controller);
		setLayout(new BorderLayout());
		north=new JPanel();
		north.setLayout(new BorderLayout());
		south=new JPanel();
		south.setLayout(new BorderLayout());
		mainPanel = new GpdSplitPane(JSplitPane.VERTICAL_SPLIT,north,new JScrollPane(south));
		
		
		
		
		/**
		 * Trecho de código inserido por Isac Velozo
		 */
		JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.LIGHT_GRAY);
        tabbedPane.setMinimumSize(new Dimension(200,300));
		
        JComponent panelPropriedades = makeTextPanel("");
        panelPropriedades.setLayout(new BorderLayout());
        panelPropriedades.add(mainPanel, BorderLayout.CENTER);
        
        
		tabbedPane.setBackground(Color.LIGHT_GRAY);
		
		tabbedPane.addTab("Propriedades", null, panelPropriedades,"Painel de Propriedades.");
		
        /*
         * Trecho comentado para não adicionao painel de propriedades
         */
        
        JComponent panelProcesso = makeTextPanel("");
		panelProcesso.setLayout(new BorderLayout());
		panelProcesso.add(new JScrollPane(north), BorderLayout.CENTER);
        //tabbedPane.addTab("Avançadas", null, panelProcesso, "Painel de Processo.");
        
		/**
		 * Fim do trecho adiocionado por Isac
		 */ 
        
        
        //add(mainPanel, BorderLayout.CENTER);
        add(tabbedPane, BorderLayout.CENTER);
        buildGUI();
		//buildGUI();
		
        
	}

	/**
	 * Adiciona ao painel o texto 
	 * @param c
	 */
	public void setMasterHeadline(Component c){
		north.add(c, BorderLayout.NORTH);
	}

	/**
	 * Adiciona a tela a treeview do processo. 
	 * @param c
	 */
	public void setMaster(Component c){
		north.add(c, BorderLayout.CENTER);
	}

	public void setDetail(Component c){
//		if (mainPanel.getRightComponent() instanceof AbstractPanel){
//			Controller southController = ((AbstractPanel)mainPanel.getRightComponent()).getController();
//			if (southController instanceof DataManipulationController){
//				((DataManipulationController)southController).saveChanges();	
//			}
//		}
		mainPanel.setRightComponent(c);
	}
	public Component getDetail(){
		return mainPanel.getRightComponent();
	}
	
	/**
	 * Método adiocionado por Isac Velozo
	 * @param text
	 * @return
	 */
	protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        
        return panel;
    }
}
