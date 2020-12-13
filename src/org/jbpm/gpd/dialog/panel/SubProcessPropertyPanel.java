package org.jbpm.gpd.dialog.panel;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jbpm.gpd.cell.SubProcessCell;
import org.jbpm.gpd.dialog.controller.Controller;
import org.jbpm.gpd.dialog.controller.MasterDetailCellController;
import org.jbpm.gpd.model.SubProcessVO;

public class SubProcessPropertyPanel extends AbstractPropertyPanel {
	
	private JLabel headline;

	/**
	 * @param controller
	 */
	public SubProcessPropertyPanel(Controller controller) {
		super(controller);
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#createComponents()
	 */
	protected void createComponents() {
		
		headline=new JLabel("SubProcess Properties");
		//build tree
		SubProcessVO model = ((SubProcessCell)getController().getModel()).getModel();
		//set the default detailpanel
		((MasterDetailCellController)getController()).activateDetailPanel(this,model);

		DefaultMutableTreeNode root =  new DefaultMutableTreeNode(model);

		//add actions Tree
		DefaultMutableTreeNode actions = new DefaultMutableTreeNode("actions");
		root.add(actions);
		createActionSubTree(actions,model.getActionList());

		//add fiels Tree
		DefaultMutableTreeNode filds = new DefaultMutableTreeNode("fields");
		root.add(filds);
		createFieldSubTree(filds,model.getFieldList());

		propertytree = new JTree(root);	
		//select the node
		propertytree.addSelectionRow(0);
		((MasterDetailCellController)getController()).setLastSelection(root);
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#arrangeComponents()
	 */
	protected void arrangeComponents() {
		expandJTree(propertytree,3);
		setMasterHeadline(headline);
		setMaster(new JScrollPane(propertytree));
	}

}
