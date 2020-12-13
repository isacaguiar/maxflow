package org.jbpm.gpd.dialog.panel.detailpanel;

import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import maxflow.painel.ImagePanel;

import org.jbpm.gpd.dialog.controller.Controller;
import org.jbpm.gpd.dialog.controller.MasterDetailCellController;
import org.jbpm.gpd.dialog.event.EventInitializer;
import org.jbpm.gpd.dialog.panel.AbstractFormPanel;

import eflow.model.organisation.impl.RoleImpl;

public class StatePanel extends AbstractFormPanel {

    //public JTextField descriptionTextField;
    public JTextArea descriptionTextField;
	private JLabel descriptionLabel;
	public JTextField nameTextField;
	private JLabel nameLabel;
	public JTextField roleTextField;
	private JLabel roleLabel;
    public JTextField timeTextField;
    private JLabel timeLabel;
    public JButton saveStateDataButton;

	public JTextField orderTextField;
	private JLabel orderLabel;
	
    public JComboBox roleList; 
    
    public JButton descriptionStateDataButton;

    
	private JLabel ncLabel;
    public JTextField ncTextField;

	/**
	 * @param controller
	 */
	public StatePanel(Controller controller) {
		super(controller);
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
                ((MasterDetailCellController)getController()).saveStateDataButton_actionPerformed(null);
            }
        };
        
		nameLabel=wFac.createLabel("Nome:");
		nameTextField=wFac.createTextField();
		descriptionLabel=wFac.createLabel("Descrição:");
        //descriptionTextField=wFac.createTextField();
        descriptionTextField=wFac.createTextArea();
		roleLabel=wFac.createLabel("Papel funcional:");
		roleTextField=wFac.createTestField(4);
        timeLabel=wFac.createLabel("Tempo (minutos):");
        timeTextField=wFac.createTextField();
		saveStateDataButton=wFac.createSavePanelButton();
        descriptionStateDataButton = new JButton("Descrição");

		orderLabel=wFac.createLabel("Ordem:");
		orderTextField=wFac.createTextField();

		ncLabel=wFac.createLabel("Não-Conformidade:");
		ncTextField=wFac.createTextField();

		
        nameTextField.addFocusListener(focusListener);
        descriptionTextField.addFocusListener(focusListener);
        roleTextField.addFocusListener(focusListener);
        timeTextField.addFocusListener(focusListener);
		descriptionStateDataButton.addFocusListener(focusListener);
		orderTextField.addFocusListener(focusListener);
        
        roleList = new JComboBox();
        roleList.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    RoleImpl role = (RoleImpl)e.getItem();
                    roleTextField.setText(String.valueOf(role.getId()));
                }
            }
        );
        roleList.addFocusListener(focusListener);
        
        
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#arrangeComponents()
	 */
	protected void arrangeComponents() {
		
		toButtonPanel(nameLabel,25,5,180,18);
		toButtonPanel(nameTextField,25,25,180,18);
		toButtonPanel(orderLabel,25,45,180,18);
		toButtonPanel(orderTextField,25,65,180,18);
		toButtonPanel(descriptionStateDataButton,25,85,90,18);
        toButtonPanel(timeLabel,25,105,180,18);
        toButtonPanel(timeTextField,25,125,60,18);
        toButtonPanel(roleLabel,25,145,180,18);
        toButtonPanel(roleList,25,165,180,18);
        toButtonPanel(ncLabel,25,185,180,18);
        toButtonPanel(ncTextField,25,205,180,18);
        //toButtonPanel(saveStateDataButton,15,225,100,18);
    }
}
