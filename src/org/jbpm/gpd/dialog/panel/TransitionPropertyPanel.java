package org.jbpm.gpd.dialog.panel;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jbpm.gpd.cell.Transition;
import org.jbpm.gpd.dialog.controller.Controller;
import org.jbpm.gpd.dialog.controller.MasterDetailCellController;
import org.jbpm.gpd.model.TransitionVO;

public class TransitionPropertyPanel extends AbstractPropertyPanel {
    
    /**
     * @param controller
     */
    public TransitionPropertyPanel(Controller controller) {
        super(controller);
    }
    
    private JLabel headline;
    
    /**
     * @see org.jbpm.gpd.dialog.panel.AbstractPanel#createComponents()
     */
    protected void createComponents() {
    	
        headline=new JLabel("Transiton Properties");
        //build tree
        TransitionVO model = ((Transition)getController().getModel()).getModel();
        
        try {
            String name = (String) ((Transition)getController().getModel()).getAttributes().get("value");
            
            if(name!=null && !name.equals("")) {
                model.setName(name);
            }
        }catch(Exception e) {
        }
        
        
        //set the default detailpanel
        ((MasterDetailCellController)getController()).activateDetailPanel(this,model);
        
        DefaultMutableTreeNode root =  new DefaultMutableTreeNode(model);
        DefaultMutableTreeNode actions =  new DefaultMutableTreeNode("actions");
        root.add(actions);
        createActionSubTree(actions,model.getActionList());
        
        propertytree = new JTree(root);	
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
