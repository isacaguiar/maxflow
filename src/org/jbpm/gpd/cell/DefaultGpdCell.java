package org.jbpm.gpd.cell;

import org.apache.commons.lang.SerializationUtils;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.VertexView;


public abstract class DefaultGpdCell extends DefaultGraphCell {
    
    public static int cont = 0; 
    
    protected int id = 0;
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
	 public DefaultGpdCell(Object userObject){
		super(userObject);
        this.id = ++cont;
	 }
	 
	public Object clone() {
		DefaultGpdCell newCell=(DefaultGpdCell)SerializationUtils.clone(this);
		newCell.children.clear();
		return newCell;
	}
	
	
	public boolean equals(Object o) {
		if(o instanceof DefaultGpdCell) {
			DefaultGpdCell d = (DefaultGpdCell) o;
			return d.getId() == this.id;
		}
		return false;
	}
 
}
