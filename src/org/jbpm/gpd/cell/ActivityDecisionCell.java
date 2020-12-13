package org.jbpm.gpd.cell;

import org.jbpm.gpd.model.DecisionVO;

public class ActivityDecisionCell extends ActivityCell {
	private DecisionVO model=new DecisionVO();
	//	Empty Constructor
	public ActivityDecisionCell() {
		super();
	}
	//	Construct Cell for Userobject
	public ActivityDecisionCell(Object userObject) {
		super(userObject);
	}
	

}
