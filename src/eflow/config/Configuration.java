/*
 * Created on 30/09/2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eflow.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

/**
 * @author Victor Costa
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Configuration {
	
	public static final String CONFIGURATION_FILE = "resources/configuration.properties";

	/**
	 * lê o arquivo de propriedades
	 * @throws IOException 
	 */
	public static Properties readProperties() throws IOException {
		
		/*
		 * pegando a propriedade externa que indica
		 * o caminho do arquivo de propriedades
		 */
		String caminho = System.getProperty("user.dir");
        caminho+=System.getProperty("file.separator");
        caminho+="resources";
        caminho+=System.getProperty("file.separator");
        caminho+="configuration.properties";
        
        
		if(caminho==null)
			caminho = CONFIGURATION_FILE;
		
		FileInputStream arquivo = new FileInputStream(new File(caminho));
		Properties xConfig = new Properties();
		xConfig.load(arquivo);
		return xConfig;
	}
	
	/**
	 * escreve no arquivo de propriedades
	 * @throws IOException 
	 */
	public static boolean writeProperties(String p_label) throws IOException {
		
		/*
		 * pegando a propriedade externa que indica
		 * o caminho do arquivo de propriedades
		 */
		String caminho = System.getProperty("user.dir");
        caminho+=System.getProperty("file.separator");
        caminho+="resources";
        caminho+=System.getProperty("file.separator");
        caminho+="configuration.properties";
        
		if(caminho==null)
			caminho = CONFIGURATION_FILE;
		
		FileInputStream arquivo = new FileInputStream(new File(caminho));
		Properties xConfig = new Properties();
		xConfig.load(arquivo);
		//arquivo.
		try {
		BufferedWriter out = new BufferedWriter(new FileWriter(caminho));
		out.write("IP_SERVER="+xConfig.getProperty("IP_SERVER")+"\n");
        out.write("PORT_SERVER="+xConfig.getProperty("PORT_SERVER")+"\n");
		out.write("LOOK_AND_FEEL="+p_label);
        out.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @return the IP_SERVER
	 * @throws IOException 
	 */
	public static String getIpServer() throws IOException {
		return readProperties().getProperty("IP_SERVER");
	}
	/**
	 * 
	 * @return the PORT_SERVER
	 * @throws IOException 
	 */
	public static String getPortServer() throws IOException {
		return readProperties().getProperty("PORT_SERVER");
	}
	
	/**
	 * 
	 * @return the LOOK_AND_FEEL
	 * @throws IOException 
	 */
	public static String getLookAndFeel() throws IOException {
		return readProperties().getProperty("LOOK_AND_FEEL");
	}
	
	public static String setLookAndFeel() throws IOException {
		return readProperties().getProperty("LOOK_AND_FEEL");
	}
	
	public static void main(String args[]) {
		try {
			System.out.println(Configuration.getIpServer());
            String caminho = System.getProperty("user.dir");
            caminho+=System.getProperty("file.separator");
            caminho+="resources";
            caminho+=System.getProperty("file.separator");
            caminho+="configuration.properties";
            System.out.println(caminho);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
