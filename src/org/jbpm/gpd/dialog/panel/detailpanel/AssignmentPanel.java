package org.jbpm.gpd.dialog.panel.detailpanel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jbpm.gpd.dialog.controller.Controller;
import org.jbpm.gpd.dialog.event.EventInitializer;
import org.jbpm.gpd.dialog.panel.AbstractFormPanel;

public class AssignmentPanel extends AbstractFormPanel {

	public JTextField assignmentTextField;
	private JLabel handlerLabel;
	private JButton assignmenthandlerButton;
	public JButton saveAssignmentDataButton;

	/**
	 * @param controller
	 */
	public AssignmentPanel(Controller controller) {
		super(controller);
		EventInitializer.addEventListeners(this,controller);
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#createComponents()
	 */
	protected void createComponents() {
		
		handlerLabel=wFac.createLabel("assignment handler:");
		assignmentTextField=wFac.createTextField();
		assignmenthandlerButton=wFac.createButtonPopdown();
		saveAssignmentDataButton=wFac.createSavePanelButton();
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#arrangeComponents()
	 */
	protected void arrangeComponents() {
		toButtonPanel(handlerLabel,assignmentTextField,assignmenthandlerButton);
		toButtonPanel(saveAssignmentDataButton);
	}

}
