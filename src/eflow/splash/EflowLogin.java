package eflow.splash;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import eflow.gpd.action.ImportEflowAction;
import eflow.model.user.impl.UserImpl;
import eflow.remote.RemoteFactory;


/**
 * Tela de Login
 * @author
 * @modify Isac Velozo Aguiar - www.isacvelozo.com
 */
public class EflowLogin extends JFrame {
	
	private JPanel contentPane;
	private JPanel centro = null;
	private JLabel lbNome = null;
	private JLabel lbTitulo = null;
	private JLabel lbSenha = null;
	private JTextField ctNome = null;
	private JPasswordField ctSenha = null;
	private JButton btLogar = null;
	private JButton btSemLogar = null;
	
	/**
	 * Método construtor
	 */
	public EflowLogin() {
		super();
		initialize();
	}
	
	/**
	 * Método de inicialização
	 * @return void
	 */
	private void initialize() {
		
		Dimension dim = new Dimension(300, 185);
		this.setSize(dim);
		this.setPreferredSize(dim);
		this.setMaximumSize(dim);
		
		contentPane = (JPanel) this.getContentPane();
	    contentPane.setBackground(Color.ORANGE);
	    contentPane.setLayout(null);
	    
	    painelLogin();
	    
		this.setTitle("Eflow");
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int)(screenSize.width-dim.getWidth())/2, (int)(screenSize.height-dim.getHeight())/2, (int)dim.getWidth(), (int)dim.getHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
	}
	
	/**
	 * Método que adiciona os campos e paineis a Janela
	 * @return JPanel
	 */
	private void painelLogin() {
		
		contentPane.add( painelCentro() );
		contentPane.add( labelTitulo() );
		
		painelCentro().add( labelNome() );
		painelCentro().add( campoNome() );
		painelCentro().add( labelSenha() );
		painelCentro().add( campoSenha() );
		painelCentro().add( botaoLogar() );
		painelCentro().add( botaoSemLogar() );
	}
	
	/**
	 * Método que inicializa o painel Centro
	 * @return JPanel
	 */
	private JPanel painelCentro() {
		if ( centro == null ) {
			centro = new JPanel();
			centro.setBounds(new Rectangle(0, 20, 300, 120));
			centro.setBackground(new Color(240,240,255));
			centro.setLayout(null);
		}
		return centro;
	}
	
	/**
	 * Método que inicializa e define as propriedades do Título da página
	 * @return JLabel
	 */
	private JLabel labelTitulo() {
		if ( lbTitulo == null ) {
			lbTitulo = new JLabel();
			lbTitulo.setText("* TELA DE LOGIN DO E-FLOW *");
			lbTitulo.setFont(new java.awt.Font("Dialog", 1, 12));
			lbTitulo.setBounds(new Rectangle(62, 0, 200, 22));
		}
		return lbTitulo;
	}
	
	/**
	 * Método que inicializa o Label Nome
	 * @return
	 */
	private JLabel labelNome() {
		
		if (lbNome == null) {
			lbNome = new JLabel();
			lbNome.setText("Nome:");
			lbNome.setFont(new java.awt.Font("Dialog", 1, 11));
			lbNome.setBounds(new Rectangle(10, 5, 120, 18));
		}
		return lbNome;
	}
	
	/**
	 * Método que inicializa o campo de nome e define suas propriedades
	 * @return JTextField
	 */
	private JTextField campoNome() {
		if ( ctNome == null ) {
			ctNome = new JTextField();
			ctNome.setBackground(Color.white);
		    ctNome.setBounds(new Rectangle(10, 20, 170, 18));
		}
	    return ctNome;
	}
	
	/**
	 * Método que cria o label Senha
	 * @return
	 */
	private JLabel labelSenha() {
		if ( lbSenha == null ) {
			lbSenha = new JLabel();
		    lbSenha.setText("Senha:");
		    lbSenha.setFont(new java.awt.Font("Dialog", 1, 11));
		    lbSenha.setBounds(new Rectangle(10, 40, 120, 18));
		}  
	    return lbSenha;
	}
	
	/**
	 * Método que cria o campo de senha e suas propriedades
	 * @return JPasswordField
	 */
	private JPasswordField campoSenha() {
		if (ctSenha == null) {
			ctSenha = new JPasswordField();
			ctSenha.setBackground(Color.white);
			ctSenha.setBounds(new Rectangle(10, 55, 170, 18));
		}
		return ctSenha;
	}
	
	
	/**
	 * Método que cria e define as propriedades do Botão Logar
	 * @return JButton
	 */
	private JButton botaoLogar() {
		if (btLogar == null ) {
			btLogar = new JButton("Logar");
			btLogar.setBounds(new Rectangle(10, 80, 68, 20));
			btLogar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					logar();
				}
			});
		}
		return btLogar;
	}
	
	/**
	 * Método que cria e define as propriedades do Botão Sem Logar
	 * @return JButton
	 */
	private JButton botaoSemLogar() {
		if ( btSemLogar == null ) {
			btSemLogar = new JButton("Sem logar");
			btSemLogar.setBounds(new Rectangle(85, 80, 98, 20));
			btSemLogar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setVisible(false);
					dispose();
					iniciar();
				}
			});
		}
		return btSemLogar;
	}
	
	/**
	 * Método que adiciona a logomarca na tela.
	 */
	public void paint( Graphics g ) {
		try {
			super.paint( g );
			g.setColor(Color.lightGray);
			g.fillArc(190,55,100,100,0,90);
			g.setColor(Color.orange);
			g.fillArc(190,55,100,100,90,90);
			g.setColor(Color.lightGray);
			g.fillArc(190,55,100,100,180,90);
			g.setColor(Color.orange);
			g.fillArc(190,55,100,100,270,90);
			//centro
			g.setColor(new Color(240,240,255));
			g.fillArc(223,88,35,35,50,360);
		}catch (Exception exception) {
			JOptionPane.showMessageDialog(null,"Erro: " + exception);
		}
	}
	
	public void logar() {
		UserImpl user = new UserImpl();
		user.setLogin(campoNome().getText());
		user.setPassword(new String(campoSenha().getPassword()));
		try {
			boolean ret = false;
			try {
				ret = RemoteFactory.getRemote().login(user);
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(EflowLogin.this, "Falha na comunicação com o servidor");
				return;
			}
			
			if(ret) {
				ImportEflowAction.user = user;
				this.setVisible(false);
				this.dispose();
				iniciar();
			}
			else {
				JOptionPane.showMessageDialog(this, "Login inválido!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método de inicialização
	 */
	public void iniciar() {
		new EflowFrame().setVisible(true);	
	}
	
	/**
	 * Main Method 
	 */
	public static void main(String[] args) {
		new EflowLogin().setVisible(true);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
