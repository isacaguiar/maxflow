package org.jbpm.gpd.dialog.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.net.URL;

import javax.swing.ImageIcon;

import maxflow.painel.ImagePanel;

import org.jbpm.gpd.dialog.controller.Controller;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public abstract class AbstractFormPanel extends AbstractPanel{
	private FormLayout layout = new FormLayout(
		"3px ,pref, 6px, pref:grow, 6px,pref, 3px",   
		"pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref, 12px, pref"); 

	private CellConstraints cc = new CellConstraints();
	private int pos=1;
			
	/**
	 * @param controller
	 */
	public AbstractFormPanel(Controller controller) {
		super(controller);
		//Alterado por Isac
		//setLayout(layout);
		setLayout(null);
		buildGUI();
		
		//Adicionado por Isac
		setBackground(Color.WHITE);
		URL url = getClass().getClassLoader().getResource("gif/bg_painel.jpg");
		ImagePanel imgPanel = new ImagePanel(new ImageIcon(url).getImage());
		//panelPropriedades.add(imgPanel);
		add(imgPanel);
	}
	protected void toButtonPanel(Component comp1) {
		add(comp1,  cc.xy(4, pos*2-1));
		pos++;
	}

	protected void toButtonPanel(Component comp1,Component comp2) {
		add(comp1,  cc.xy(2, pos*2-1));
		add(comp2,  cc.xy(4, pos*2-1));
		pos++;
	}

	protected void toButtonPanel(Component comp1,Component comp2,Component comp3) {
		add(comp1,  cc.xy(2, pos*2-1));
		add(comp2,  cc.xy(4, pos*2-1));
		add(comp3,  cc.xy(6, pos*2-1));
		pos++;
	}
	
	/**
	 * Método adicionado por Isac Velozo
	 */
	protected void toButtonPanel(Component comp1,int x,int y,int w,int z) {
		comp1.setBounds(new Rectangle(x, y, w, z));
		add(comp1);
	}
}
