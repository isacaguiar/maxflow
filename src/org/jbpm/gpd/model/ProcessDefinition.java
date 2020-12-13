package org.jbpm.gpd.model;

import java.util.LinkedList;
import java.util.List;

public class ProcessDefinition extends AbstractVO{
	private String name="";
	private String description="";
    private String responsible="";
	private List attributeList=new LinkedList();
	private List actionList= new LinkedList();

    public ProcessDefinition(){
        
	}
	/**
	 * @return
	 */
	public List getAttributeList() {
		return attributeList;
	}

	/**
	 * @return
	 */
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
	public String getResponsible() {
		return responsible;
	}

	/**
	 * @param list
	 */
	public void setAttributeList(List list) {
		attributeList = list;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
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
	public void setResponsible(String string) {
		responsible = string;
	}

	/**
	 * @return
	 */
	public List getActionList() {
		return actionList;
	}

	/**
	 * @param list
	 */
	public void setActionList(List list) {
		actionList = list;
	}

	public String toString(){
		return "Process";
	}
    
    public void addAttribute(AttributeVO attribute) {
        if(this.attributeList.contains(attribute)) {
            AttributeVO attrib = (AttributeVO) this.attributeList.get(this.attributeList.indexOf(attribute));
            attrib.setInitialValue(attribute.getInitialValue());
        }
        else {
            this.attributeList.add(attribute);
        }
    }
    
}
