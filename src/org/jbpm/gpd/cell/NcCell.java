package org.jbpm.gpd.cell;

import org.jbpm.gpd.model.ActivityStateVO;
import org.jbpm.gpd.model.SubProcessVO;

public class NcCell extends SubProcessCell {
	private SubProcessVO model=new SubProcessVO();
	//	Empty Constructor
	public NcCell() {
		this(null);
	}
	//	Construct Cell for Userobject
	public NcCell(Object userObject) {
		super(userObject);
		super.setUserObject(model);
	}
	

}
