package org.jbpm.gpd.dialog.panel.detailpanel;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jbpm.gpd.dialog.controller.Controller;
import org.jbpm.gpd.dialog.controller.MasterDetailCellController;
import org.jbpm.gpd.dialog.event.EventInitializer;
import org.jbpm.gpd.dialog.panel.AbstractFormPanel;

public class TransitionPanel extends AbstractFormPanel {

	public JTextField nameTextField;
	private JLabel nameLabel;
	public JButton saveTransitionDataButton;

    

    /**
	 * @param controller
	 */
	public TransitionPanel(Controller controller) {
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
                //System.out.println("!foco");
                ((MasterDetailCellController)getController()).saveTransitionDataButton_actionPerformed(null);
            }
        };
        
        
		nameLabel=wFac.createLabel("Nome:");
		nameTextField=wFac.createTextField();
		saveTransitionDataButton=wFac.createSavePanelButton();
        
        nameTextField.addFocusListener(focusListener);

	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#arrangeComponents()
	 */
	protected void arrangeComponents() {
		/*
		toButtonPanel(nameLabel, nameTextField);
		toButtonPanel(saveTransitionDataButton);
		*/
		toButtonPanel(nameLabel,25,5,180,18);
		toButtonPanel(nameTextField,25,25,180,18);
		//toButtonPanel(saveTransitionDataButton,15,45,100,30);
	}
    

  

}
