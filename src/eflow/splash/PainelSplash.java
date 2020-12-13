/*
 * @(#)PainelSplash.java - 21/02/2005
 * Copyright 2005 IPQ Tecnologia. Todos os direitos reservados.
 */

package eflow.splash;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JLabel;

public class PainelSplash extends Window {
	
	private static final long serialVersionUID = 1027681774288672177L;

    private static PainelSplash splash = new PainelSplash();
	
	// Imagem que ira aparecer no Splash
	private final String imgName = "/gif/bg_index.gif";
	
	private Image splashImage;
    private JLabel label;

	/**
	 * @throws HeadlessException
	 */
	private PainelSplash() {
		super(new Frame());
		setVisible(true);
        setLayout(null);
		splashImage = null;
        label = new JLabel();
        label.setOpaque(false);
        label.setForeground(Color.BLACK);
        label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
        setAlwaysOnTop(true);
	}
	
	public static PainelSplash getInstance() {
		return splash;
	}
    
    public void abrir() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        initSplash();
        setVisible(true);
    }
    
    public void fechar() {
        setVisible(false);
        dispose();
    }
    
    public void setMensagem(String mensagem) {
        label.setText(mensagem);
    }
	
	/**
	 * Method initSplash.
	 */
	private void initSplash() {
		// Carrega a imagem na memoria
        Toolkit toolkit = Toolkit.getDefaultToolkit();
		MediaTracker media = new MediaTracker(this);
		splashImage = toolkit.getImage(this.getClass().getResource(imgName));
		
		if (splashImage != null) {
			media.addImage(splashImage, 0);
			
			try {
				media.waitForID(0);
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
		
		// Configura o tamanho do splash e a posicao na tela
		setSize(splashImage.getWidth(this), splashImage.getHeight(this));
        
		// Configura o label dentro do splash
        label.setBounds(new java.awt.Rectangle(20, getHeight() - 24, getWidth() - 40, 20));
        this.add(label, null);
		
		// Centraliza o splash na tela
		Dimension screenSize = toolkit.getScreenSize();
		Dimension size = getSize();
		
		if (size.width > screenSize.width)
			size.width = screenSize.width;
		if (size.height > screenSize.height)
			size.height = screenSize.height;
			
		setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 2);
	}

	public void paint(Graphics g) {
	    g.drawImage(splashImage, 0, 0, getBackground(), this);
        super.paint(g);
	}
    
	public static void main(String args[]) {
		new PainelSplash();
	}
}

