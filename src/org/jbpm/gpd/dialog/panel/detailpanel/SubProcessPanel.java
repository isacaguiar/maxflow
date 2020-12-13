package org.jbpm.gpd.dialog.panel.detailpanel;

import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import org.jbpm.gpd.dialog.controller.Controller;
import org.jbpm.gpd.dialog.controller.MasterDetailCellController;
import org.jbpm.gpd.dialog.event.EventInitializer;
import org.jbpm.gpd.dialog.panel.AbstractFormPanel;

import eflow.model.organisation.impl.RoleImpl;

public class SubProcessPanel extends AbstractFormPanel {

	public JTextField subProcessTextField;
	private JLabel subProcessLabel;
    public JButton saveSubProcessDataButton;

	public JTextField orderTextField;
	private JLabel orderLabel;
    
	public JTextField nameTextField;
	private JLabel nameLabel;
	public JTextArea descriptionSubProcessTextField;
	private JLabel descriptionLabel;
	public JButton descriptionSubProcessDataButton;

	/**
	 * @param controller
	 */
	public SubProcessPanel(Controller controller) {
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
        
        nameLabel=wFac.createLabel("nome:");
		nameTextField=wFac.createTextField();
        subProcessLabel=wFac.createLabel("sub-processo:");
        subProcessTextField=wFac.createTextField();
		descriptionLabel=wFac.createLabel("descrição:");
        descriptionSubProcessTextField=wFac.createTextArea();
		saveSubProcessDataButton=wFac.createSavePanelButton();
		descriptionSubProcessDataButton = new JButton("Descrição");

		orderLabel=wFac.createLabel("ordem:");
		orderTextField=wFac.createTextField();

		
        nameTextField.addFocusListener(focusListener);
        descriptionSubProcessTextField.addFocusListener(focusListener);
        subProcessTextField.addFocusListener(focusListener);
		orderTextField.addFocusListener(focusListener);
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#arrangeComponents()
	 */
	protected void arrangeComponents() {
		toButtonPanel(nameLabel,25,5,180,18);
		toButtonPanel(nameTextField,25,25,180,18);
		toButtonPanel(subProcessLabel,25,45,180,18);
		toButtonPanel(subProcessTextField,25,65,180,18);
		toButtonPanel(orderLabel,25,85,180,18);
		toButtonPanel(orderTextField,25,105,180,18);
        toButtonPanel(descriptionSubProcessDataButton,25,125,90,18);
        //toButtonPanel(saveSubProcessDataButton,5,145,90,18);
    }
}
