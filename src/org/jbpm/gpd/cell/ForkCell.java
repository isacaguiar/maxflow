package org.jbpm.gpd.cell;

import org.jbpm.gpd.model.ForkJoinVO;

public class ForkCell extends DefaultGpdCell {
	private ForkJoinVO model = new ForkJoinVO("fork");

	//	Empty Constructor
	public ForkCell() {
		this(null);
	}
	//	Construct Cell for Userobject
	public ForkCell(Object userObject) {
		super(userObject);
		setUserObject(model);
	}
	/**
	 * @return
	 */
	public ForkJoinVO getModel() {
		return model;
	}

	/**
	 * @param joinVO
	 */
	public void setModel(ForkJoinVO joinVO) {
		model = joinVO;
		super.userObject=joinVO;
//		setUserObject(joinVO);
	}

	public String toString(){
		return model.getName();
	}

	public Object getUserObject(){
		return model;
	}

}
