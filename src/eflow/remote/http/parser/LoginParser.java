package eflow.remote.http.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eflow.model.organisation.impl.RoleImpl;

public class LoginParser extends EflowParser {
	private Pattern pattern = null;
    private Matcher matcher = null;
    
    
    public LoginParser() {
	    pattern = Pattern.compile("(\\d*);("+ALFA+"*);("+ALFA+"*);");
    }
    
	public Object parse(String resposta) throws Exception {
		resposta = resposta.trim();
		if(resposta.contains("true")) {
			return new Boolean(true);
		}
		else {
			return new Boolean(false);
		}
	}

}
