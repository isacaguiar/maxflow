package eflow.versao;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Versao {

	private static ResourceBundle resources;

	static {
		try {
			resources = ResourceBundle.getBundle("eflow.versao.resources.Versao", Locale.getDefault());
		} catch (MissingResourceException mre) {
			mre.printStackTrace();
		}
	}
	
	
	public static String getVersao() {
		return resources.getString("VERSAO");
	}
}
