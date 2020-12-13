package org.jbpm.gpd.model;

import java.awt.Color;


public class EndVO extends AbstractVO {
	private String name = "end";
	private Color backColor = new Color(190,0,0);
	

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}
	
	public Color getBackColor() {
		return backColor;
	}

	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}
	
	 /**
     * Getter for property backColor.
     * @return Value of property backColor.
     */
    public int getRGBBackColor() {
        return backColor.getRGB();
    } // Marshaller for .gpd not support Color
    
    public String toString(){
		if (name==null){
			return "";
		}
		return name;
	}
    
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

}
