package org.jbpm.gpd.model;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import org.jbpm.gpd.model.util.Order;

import eflow.gpd.action.ImportEflowAction;
import eflow.model.organisation.impl.RoleImpl;
//import org.jgraph.graph.DefaultGraphCell.ValueChangeHandler;

public class ActivityStateVO extends AbstractVO implements StateVO {
    private int count = Order.next();
    // <activity-state name="">
    private String name ="atividade " + count;
    //<description/>
    private String description ="";
    private String role = "0";
    private AssignmentVO assignment= null;
    private List actionList= new LinkedList();
    private List fieldList=new LinkedList();
    private Color backColor=new Color(240,237,237);
    
    public ActivityStateVO(){
    	FieldVO field = new FieldVO();
        field.setAttribute("@order");
        field.setDescription(String.valueOf(count));
        fieldList.add(field);
        field = new FieldVO();
        field.setAttribute("@time");
        field.setDescription("0");
        fieldList.add(field);
        field = new FieldVO();
        field.setAttribute("@roleName");
        field.setDescription(null);
        fieldList.add(field);
        field = new FieldVO();
        field.setAttribute("@nc");
        field.setDescription("");
        fieldList.add(field);
        count++;
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
    public String getRole() {
        return role;
    }
    
    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }
    
    /**
     * @param string
     */
    public void setRole(String string) {
        role = string;
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
    public AssignmentVO getAssignment() {
        return assignment;
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
     * @param assignmentVO
     */
    public void setAssignment(AssignmentVO assignmentVO) {
        assignment = assignmentVO;
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
    
	public long getTime() {
		FieldVO field = new FieldVO();
        field.setAttribute("@time");
		field = (FieldVO) this.fieldList.get(fieldList.indexOf(field));
		
		return Long.valueOf(field.getDescription()).longValue();
	}

	public void setTime(long time) {
		FieldVO field = new FieldVO();
        field.setAttribute("@time");
		field = (FieldVO) this.fieldList.get(fieldList.indexOf(field));
		field.setDescription(String.valueOf(time));
	}	    
	
	
	public String getRoleName() {
		FieldVO field = new FieldVO();
        field.setAttribute("@roleName");
		field = (FieldVO) this.fieldList.get(fieldList.indexOf(field));
		
		if(field.getDescription()==null) {
	        java.util.List roleList = ImportEflowAction.roleList;
	        RoleImpl r = new RoleImpl();
	        r.setId(new Long(role));
	        String roleView = "";
	        try {
				RoleImpl b = (RoleImpl) roleList.get(roleList.indexOf(r));
				roleView = b.getName();
			} catch (Exception e) {
				//e.printStackTrace();
			}
			return roleView;
		}
		return field.getDescription();
	}

	public void setRoleName(String roleName) {
		FieldVO field = new FieldVO();
        field.setAttribute("@roleName");
		field = (FieldVO) this.fieldList.get(fieldList.indexOf(field));
		field.setDescription(roleName);
	}
	
	
	public String getNc() {
		FieldVO field = new FieldVO();
        field.setAttribute("@nc");
		field = (FieldVO) this.fieldList.get(fieldList.indexOf(field));
		
		return field.getDescription();
	}

	public void setNc(String nc) {
		FieldVO field = new FieldVO();
        field.setAttribute("@nc");
		field = (FieldVO) this.fieldList.get(fieldList.indexOf(field));
		field.setDescription(String.valueOf(nc));
	}	    
	
	
}
