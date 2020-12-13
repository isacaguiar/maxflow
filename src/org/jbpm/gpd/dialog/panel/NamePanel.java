package org.jbpm.gpd.dialog.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;

import maxflow.painel.ImagePanel;

import org.jbpm.gpd.cell.EndCell;
import org.jbpm.gpd.cell.ForkCell;
import org.jbpm.gpd.cell.JoinCell;
import org.jbpm.gpd.dialog.controller.Controller;
import org.jbpm.gpd.dialog.controller.MasterDetailCellController;
import org.jbpm.gpd.dialog.event.EventInitializer;
import org.jbpm.gpd.dialog.panel.detailpanel.StatePanel;
import org.jbpm.gpd.dialog.panel.detailpanel.TransitionPanel;
import org.jbpm.gpd.model.EndVO;
import org.jbpm.gpd.model.TransitionVO;

import com.jgoodies.forms.layout.CellConstraints;

import eflow.splash.PainelSplash;

public class NamePanel extends AbstractPropertyPanel {
//public class extends AbstractPanel {
	
	private Object cell;
	
	public JTextField nameTextField;
	private JLabel nameLabel;
	public JButton saveNamePanelDataButton;
	
	public JPanel painel;

	/**
	 * @param controller
	 */
	public NamePanel(Controller controller) {
		super(controller);
		// Tirar  comentário da linha abaixo caso estenda AbstractFormPanel
		EventInitializer.addEventListeners(this,controller);
	}
	
	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#createComponents()
	 */
	protected void createComponents() {
		FocusListener focusListener = new FocusListener(){
			
            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
            	
            	((MasterDetailCellController)getController()).saveNamePanelDataButton_actionPerformed(null);
            	
            }
        };
        
		painel = new JPanel();
		URL url = getClass().getClassLoader().getResource("gif/bg_painel.jpg");
		ImagePanel panel = new ImagePanel(new ImageIcon(url).getImage());
		
		
		JPanel p = new JPanel();
		//p.setBackground(Color.ORANGE);
		p.setBounds(new Rectangle(3, 100, 100, 200));
		p.add(panel);
		p.setBounds(new Rectangle(3, 30, 100, 200));
		p.add(panel);
		
		
		nameLabel=wFac.createLabel("Nome:");
		nameTextField=wFac.createTextField();
		Object model = getController().getModel();
		//JOptionPane.showMessageDialog(null,": "+model);
		if (model instanceof EndCell){
			nameTextField.setText(((EndCell)model).getModel().getName());
			((MasterDetailCellController)getController()).activateDetailPanel(this,null);
			//JOptionPane.showMessageDialog(null,": "+model);
		} else if (model instanceof ForkCell){
			nameTextField.setText(((ForkCell)model).getModel().getName());
		} else if (model instanceof JoinCell){
			nameTextField.setText(((JoinCell)model).getModel().getName());
		}
		saveNamePanelDataButton=wFac.createSavePanelButton();
		
		painel.setLayout(null);
		
		nameLabel.setBounds(new Rectangle(25, 5, 120, 18));
		nameTextField.setBounds(new Rectangle(25, 25, 180, 18));
		saveNamePanelDataButton.setBounds(new Rectangle(25, 45, 90, 18));
		
		/**
		 * Trecho de código inserido por Isac Velozo
		 */
		JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.LIGHT_GRAY);
        tabbedPane.setMinimumSize(new Dimension(200,300));
		
        JComponent panelPropriedades = makeTextPanel("");
        panelPropriedades.setLayout(null);
        panelPropriedades.setBackground(Color.WHITE);
        panelPropriedades.add(nameLabel);
        panelPropriedades.add(nameTextField);
        panelPropriedades.add(panel, BorderLayout.CENTER);
        //panelPropriedades.add(saveNamePanelDataButton);
        panelPropriedades.add(p);
        tabbedPane.addTab("Propriedades", null, panelPropriedades,"Painel de Propriedades.");
		
		add(tabbedPane, BorderLayout.CENTER);
		nameTextField.addFocusListener(focusListener);
		
		/*
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1);
				}catch(Exception e){} 
				//EventInitializer.addEventListeners(this,super);
			}
			
		}.start();
		*/
		
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#arrangeComponents()
	 */
	protected void arrangeComponents() {
		/*
		toButtonPanel(nameLabel,5,5,180,18);
		toButtonPanel(nameTextField,5,25,180,18);
		toButtonPanel(saveNamePanelDataButton,5,45,180,18);
		*/
		
	}

}
