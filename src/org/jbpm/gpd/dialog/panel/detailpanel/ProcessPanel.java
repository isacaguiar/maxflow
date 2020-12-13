package org.jbpm.gpd.dialog.panel.detailpanel;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jbpm.gpd.dialog.controller.Controller;
import org.jbpm.gpd.dialog.controller.MasterDetailCellController;
import org.jbpm.gpd.dialog.event.EventInitializer;
import org.jbpm.gpd.dialog.panel.AbstractFormPanel;
import org.jbpm.gpd.model.ProcessDefinition;

import eflow.model.definition.impl.ProcessTypeImpl;

public class ProcessPanel extends AbstractFormPanel {

    //public JTextField descriptionTextField;
    public JTextArea descriptionTextField;
	private JLabel descriptionLabel;
	public JTextField nameTextField;
	private JLabel nameLabel;
	//public JTextField responsibleTextField;
	//private JLabel responsibleLabel;
    public JButton saveProcessDataButton;

    
    public JTextArea objectiveTextField;
    private JLabel obectiveLabel;
    public JTextArea applicationTextField;
    private JLabel applicationLabel;
    public JTextField processTypeTextField;
    private JLabel processTypeLabel;

    public JTextField codeTextField;
    private JLabel codeLabel;
	
	
    
    public JButton objectiveDataButton;
    public JButton descriptionDataButton;
    public JButton applicationDataButton;

    
    public JComboBox processTypeList;
    
    
	/**
	 * @param controller
	 */
	public ProcessPanel(Controller controller) {
		super(controller);
		EventInitializer.addEventListeners(this,controller);
	}

	
	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#createComponents()
	 */
	protected void createComponents() {
		
		
		
        FocusListener focusListener = new FocusListener(){
            public void focusGained(FocusEvent e) {
                //System.out.println("foco");
            }

            public void focusLost(FocusEvent e) {
                ((MasterDetailCellController)getController()).saveProcessDataButton_actionPerformed(null);
            }
        };
        
        
		nameLabel=wFac.createLabel("Nome:");
		nameTextField=wFac.createTextField();
		descriptionLabel=wFac.createLabel("Definições:");
        //descriptionTextField=wFac.createTextField();
        descriptionTextField=wFac.createTextArea();
		//responsibleLabel=wFac.createLabel("responsible:");
		//responsibleTextField=wFac.createTextField();
		saveProcessDataButton=wFac.createSavePanelButton();
        
        obectiveLabel=wFac.createLabel("Objetivo:");
        objectiveTextField=wFac.createTextArea();
        applicationLabel=wFac.createLabel("Aplicação:");
        applicationTextField=wFac.createTextArea();

		codeLabel=wFac.createLabel("Código:");
        codeTextField=wFac.createTextField();

    
        descriptionDataButton = new JButton("Definições");
        objectiveDataButton = new JButton("Objetivo");
        applicationDataButton = new JButton("Aplicação");

        
        processTypeLabel=wFac.createLabel("Tipo:");
        processTypeTextField=wFac.createTextField();
        
        
        //adicionando o FocusListener
        nameTextField.addFocusListener(focusListener);
        
        descriptionTextField.addFocusListener(focusListener);
        objectiveTextField.addFocusListener(focusListener);
        applicationTextField.addFocusListener(focusListener);
        processTypeTextField.addFocusListener(focusListener);
		codeTextField.addFocusListener(focusListener);
		descriptionDataButton.addFocusListener(focusListener);
		objectiveDataButton.addFocusListener(focusListener);
		applicationDataButton.addFocusListener(focusListener);
		
		
        
        processTypeList = new JComboBox();
        processTypeList.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    ProcessTypeImpl p = (ProcessTypeImpl)e.getItem();
                    processTypeTextField.setText(String.valueOf(p.getId()));
                }
            }
        );
		processTypeList.addFocusListener(focusListener);
		
		//nameTextField.getDocument().addDocumentListener(documentListener);
    }

    
	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#arrangeComponents()
	 */
	protected void arrangeComponents() {
		toButtonPanel(nameLabel,25,5,120,18);
		toButtonPanel(nameTextField,25,25,180,18);
        toButtonPanel(codeLabel,25,45,120,18);
        toButtonPanel(codeTextField,25,65,180,18);
        toButtonPanel(objectiveDataButton,25,85,90,18);
        toButtonPanel(applicationDataButton,25,105,90,18);
		toButtonPanel(descriptionDataButton,25,125,90,18);
		toButtonPanel(processTypeLabel,25,145,180,18);
		toButtonPanel(processTypeList,25,165,180,18);
		//toButtonPanel(saveProcessDataButton,5,185,90,18);
    }
}
