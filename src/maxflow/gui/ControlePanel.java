package maxflow.gui;

import java.awt.Dimension;

import javax.swing.*;

public class ControlePanel extends JPanel {
	
	private int diameter = 10;
	/*
	public void paintComponent( Graphics g ) {
		super.paintComponent( g );
		g.fill
	}
	*/

	public Dimension getPreferredSize() {
		
		return new Dimension(100,100);
		
	}
	
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	
}

