package org.jbpm.gpd.dialog.panel.detailpanel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jbpm.gpd.dialog.controller.Controller;
import org.jbpm.gpd.dialog.event.EventInitializer;
import org.jbpm.gpd.dialog.panel.AbstractFormPanel;

public class FormatterPanel extends AbstractFormPanel {

	public JTextField formatterclassTextField;
	private JLabel formatterclassLabel;
	private JButton formatterhandlerButton;
	public JButton saveFormatterDataButton;

	/**
	 * @param controller
	 */
	public FormatterPanel(Controller controller) {
		super(controller);
		EventInitializer.addEventListeners(this,controller);
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#createComponents()
	 */
	protected void createComponents() {
		
		formatterclassLabel=wFac.createLabel("formatter class:");
		formatterclassTextField=wFac.createTextField();
		formatterhandlerButton=wFac.createButtonPopdown();
		saveFormatterDataButton=wFac.createSavePanelButton();
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#arrangeComponents()
	 */
	protected void arrangeComponents() {
		toButtonPanel(formatterclassLabel, formatterclassTextField,formatterhandlerButton);
		toButtonPanel(saveFormatterDataButton);
	}

}
