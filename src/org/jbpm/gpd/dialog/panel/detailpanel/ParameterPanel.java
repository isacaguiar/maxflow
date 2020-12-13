package org.jbpm.gpd.dialog.panel.detailpanel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jbpm.gpd.dialog.controller.Controller;
import org.jbpm.gpd.dialog.event.EventInitializer;
import org.jbpm.gpd.dialog.panel.AbstractFormPanel;

public class ParameterPanel extends AbstractFormPanel {

	public JTextField valueTextField;
	private JLabel valueLabel;
	public JTextField nameTextField;
	private JLabel nameLabel;
	private JButton valueButton;
	public JButton saveParameterDataButton;

	/**
	 * @param controller
	 */
	public ParameterPanel(Controller controller) {
		super(controller);
		EventInitializer.addEventListeners(this,controller);
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#createComponents()
	 */
	protected void createComponents() {
		
		nameLabel=wFac.createLabel("name:");
		nameTextField=wFac.createTextField();
		valueLabel=wFac.createLabel("value:");
		valueTextField=wFac.createTextField();
		valueButton=wFac.createButtonPopdown();
		saveParameterDataButton=wFac.createSavePanelButton();
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#arrangeComponents()
	 */
	protected void arrangeComponents() {
		toButtonPanel(nameLabel,nameTextField);
		toButtonPanel(valueLabel,valueTextField,valueButton);
		toButtonPanel(saveParameterDataButton);
	}

}
