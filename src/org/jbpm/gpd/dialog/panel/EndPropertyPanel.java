package org.jbpm.gpd.dialog.panel;

import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jbpm.gpd.cell.EndCell;
import org.jbpm.gpd.cell.StartCell;
import org.jbpm.gpd.dialog.controller.Controller;
import org.jbpm.gpd.dialog.controller.MasterDetailCellController;
import org.jbpm.gpd.model.AttributeVO;
import org.jbpm.gpd.model.EndVO;
import org.jbpm.gpd.model.ProcessDefinition;
import org.jbpm.gpd.model.StartStateVO;

public class EndPropertyPanel extends AbstractPropertyPanel {
	
	/**
	 * @param controller
	 */
	public EndPropertyPanel(Controller controller) {
		super(controller);
	}
	
	public JPanel currentSelection;
	private JLabel headline;

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#createComponents()
	 */
	protected void createComponents() {
		//headline=new JLabel("Process and Start Properties");
		headline=new JLabel();

		//build tree
		EndVO model = ((EndCell)getController().getModel()).getModel();
		((MasterDetailCellController)getController()).activateDetailPanel(this,null);
		DefaultMutableTreeNode root =  new DefaultMutableTreeNode(null);
		
		//createProcessSubTree(root,null);

		//createEndSubTree(root,model);
		
		currentSelection = new JPanel();

		//propertytree = new JTree(root);
		propertytree = new JTree(root);

		//select the node
		propertytree.addSelectionRow(0);
		
		((MasterDetailCellController)getController()).setLastSelection(root);
		
		
	}

	/**
	 * @param root
	 * @param model
	 */
	private void createEndSubTree(DefaultMutableTreeNode root, StartStateVO model) {
		
		DefaultMutableTreeNode start= new DefaultMutableTreeNode(model);
		root.add(start);
		DefaultMutableTreeNode filds = new DefaultMutableTreeNode("fields");
		start.add(filds);
		createFieldSubTree(filds,model.getFieldList());

		DefaultMutableTreeNode actions =  new DefaultMutableTreeNode("actions");
		start.add(actions);
		createActionSubTree(actions,model.getActionList());
		
	}

	/**
	 * @param root
	 * @param list
	 */
	private void createProcessSubTree(DefaultMutableTreeNode attributes, ProcessDefinition definition) {

		DefaultMutableTreeNode actions =  new DefaultMutableTreeNode("actions");
		attributes.add(actions);
		Iterator it = definition.getAttributeList().iterator();
		while (it.hasNext()){
			AttributeVO attributeVO = (AttributeVO)it.next();
			DefaultMutableTreeNode attribute = new DefaultMutableTreeNode(attributeVO);
			attributes.add(attribute);
		}
	}

	/**
	 * @see org.jbpm.gpd.dialog.panel.AbstractPanel#arrangeComponents()
	 */
	protected void arrangeComponents() {
		/**
		 * Trecho comentado por Isac
		 * marcador Isac código comentado
		 * o código abaixo exibe o painel do processo
		 */
		expandJTree(propertytree,4);
		setMasterHeadline(headline);
		setMaster(new JScrollPane(propertytree));
	}
}
