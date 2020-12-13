package org.jbpm.gpd.cell;

import org.jbpm.gpd.model.ActivityStateVO;
import org.jbpm.gpd.model.SubProcessVO;

public class SubProcessCell extends DefaultGpdCell {
	private SubProcessVO model=new SubProcessVO();
	//	Empty Constructor
	public SubProcessCell() {
		this(null);
	}
	//	Construct Cell for Userobject
	public SubProcessCell(Object userObject) {
		super(userObject);
		super.setUserObject(model);
	}
	
	public void setModel(SubProcessVO properties) {
		this.model = properties;
		super.userObject=properties;
	}

	public SubProcessVO getModel() {
		return model;
	}
	
	public String toString(){
		if(model==null) 
			model=new SubProcessVO();
		return model.getName();
	}
	
	
	public Object getUserObject(){
		return model;
	}

	

}
