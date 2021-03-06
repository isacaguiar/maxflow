package org.jbpm.gpd.dialog.panel.detailpanel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jbpm.gpd.dialog.controller.Controller;
import org.jbpm.gpd.dialog.event.EventInitializer;
import org.jbpm.gpd.dialog.panel.AbstractFormPanel;

public class DecisionPanel extends AbstractFormPanel {

	public JTextField nameTextField;
	private JLabel nameLabel;
	public JTextField handlerTextField;
	private JLabel handlerLabel;
	private JButton decisionhandlerButton;
	public JButton saveDesicionDataButton;

	/**
	 * @param controller
	 */
	public DecisionPanel(Controller controller) {
		super(controller);
		EventInitializer.addEventListeners(this,controller);
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#createComponents()
	 */
	protected void createComponents() {
		nameLabel=wFac.createLabel("name:");
		nameTextField=wFac.createTextField();
		handlerLabel=wFac.createLabel("handler:");
		handlerTextField=wFac.createTextField();
		decisionhandlerButton=wFac.createButtonPopdown();
		saveDesicionDataButton=wFac.createSavePanelButton();
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#arrangeComponents()
	 */
	protected void arrangeComponents() {
		toButtonPanel(nameLabel,nameTextField);
		toButtonPanel(handlerLabel,handlerTextField,decisionhandlerButton);
		toButtonPanel(saveDesicionDataButton);
	}

}
