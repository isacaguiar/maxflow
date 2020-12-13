package org.jbpm.gpd.dialog.panel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jbpm.gpd.cell.EndCell;
import org.jbpm.gpd.cell.ForkCell;
import org.jbpm.gpd.cell.JoinCell;
import org.jbpm.gpd.dialog.controller.Controller;
import org.jbpm.gpd.dialog.event.EventInitializer;

//public class BasicPanel extends AbstractFormPanel {
public class BasicPanel extends AbstractPanel {

	
	public JTextField nameTextField;
	private JLabel nameLabel;
	public JButton saveNamePanelDataButton;
	

	/**
	 * @param controller
	 */
	public BasicPanel(Controller controller) {
		super(controller);
		// Tirar  comentário da linha abaixo caso estenda AbstractFormPanel
		EventInitializer.addEventListeners(this,controller);
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#createComponents()
	 */
	protected void createComponents() {
		nameLabel=wFac.createLabel("Nome:");
		nameTextField=wFac.createTextField();
		Object model = getController().getModel();
		if (model instanceof EndCell){
			nameTextField.setText(((EndCell)model).getModel().getName());
		} else if (model instanceof ForkCell){
			nameTextField.setText(((ForkCell)model).getModel().getName());
		} else if (model instanceof JoinCell){
			nameTextField.setText(((JoinCell)model).getModel().getName());
		}
		saveNamePanelDataButton=wFac.createSavePanelButton();
		
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#arrangeComponents()
	 */
	protected void arrangeComponents() {
		//toButtonPanel(nameLabel,nameTextField);
		//toButtonPanel(saveNamePanelDataButton);
		
	}
	

}

