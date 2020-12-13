package org.jbpm.gpd.model;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import org.jbpm.gpd.model.util.Order;
//import org.jgraph.graph.DefaultGraphCell.ValueChangeHandler;

public class SubProcessVO extends AbstractVO {
    private int count = Order.next();
    // <activity-state name="">
    private String name ="processo "+count;
    //<description/>
    private String description ="";
    private String subProcess = "";
    private List fieldList=new LinkedList();
    private List actionList= new LinkedList();
    private Color backColor=new Color(240,237,237);
    
    public SubProcessVO(){
		FieldVO field = new FieldVO();
        field.setAttribute("@order");
        field.setDescription(String.valueOf(count));
        fieldList.add(field);
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * @return
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return
     */
    public String getSubProcess() {
        return this.subProcess;
    }
    
    /**
     * @param string
     */
    public void setSubProcess(String subProcess) {
    	this.subProcess = subProcess;
    }
    
   
    
    /**
     * @return
     */
    public List getActionList() {
        return actionList;
    }
   
    
    /**
     * @return
     */
    public List getFieldList() {
        return fieldList;
    }
    
    /**
     * @param actionVO
     */
    public void setActionList(List actionVOlist) {
        actionList = actionVOlist;
    }
    
    
    /**
     * @param fieldVO
     */
    public void setFieldList(List fieldVOlist) {
        fieldList = fieldVOlist;
    }
    
    /**
     * Getter for property backColor.
     * @return Value of property backColor.
     */
    public java.awt.Color getBackColor() {
        return backColor;
    }
    
    /**
     * Setter for property backColor.
     * @param backColor New value of property backColor.
     */
    public void setBackColor(java.awt.Color backColor) {
        this.backColor = backColor;
    }
    
    /**
     * Getter for property backColor.
     * @return Value of property backColor.
     */
    public int getRGBBackColor() {
        return backColor.getRGB();
    } // Marshaller for .gpd not support Color
    
    /**
     * Setter for property backColor.
     * @param bColor New value of property backColor.
     */
    public void setRGBBackColor(int rgbColor) {
        this.backColor = new Color(rgbColor);
    } // Marshaller for .gpd not support Color
    
    /**
     * @see org.jgraph.graph.DefaultGraphCell.ValueChangeHandler#valueChanged(java.lang.Object)
     */
    public Object valueChanged(Object arg0) {
        if (arg0 instanceof String){
            name=(String)arg0;
            return this;
        }
        return arg0;
    }
    
    /**
     * @see org.jgraph.graph.DefaultGraphCell.ValueChangeHandler#clone()
     */
    public Object clone() {
        return null;
    }
    
    public String toString(){
        return name;
    }
    
    
    
    public void addField(FieldVO field) {
        if(this.fieldList.contains(field)) {
            FieldVO f = (FieldVO) this.fieldList.get(this.fieldList.indexOf(field));
            f.setDescription(field.getDescription());
        }
        else {
            this.fieldList.add(field);
        }
    }


	public int getOrder() {
		FieldVO field = new FieldVO();
        field.setAttribute("@order");
		field = (FieldVO) this.fieldList.get(fieldList.indexOf(field));
		
		return Integer.parseInt(field.getDescription());
	}

	public void setOrder(int order) {
		FieldVO field = new FieldVO();
        field.setAttribute("@order");
		field = (FieldVO) this.fieldList.get(fieldList.indexOf(field));
		field.setDescription(String.valueOf(order));
	}
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}
