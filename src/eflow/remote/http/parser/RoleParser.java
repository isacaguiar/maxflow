package eflow.remote.http.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eflow.model.organisation.impl.RoleImpl;

public class RoleParser extends EflowParser {
	private Pattern pattern = null;
    private Matcher matcher = null;
    
    
    public RoleParser() {
	    pattern = Pattern.compile("(\\d*);("+ALFA+"*);("+ALFA+"*);");
    }
    
	public Object parse(String resposta) throws Exception {
		List ret = new ArrayList();
		int i = getLine(resposta);
		
		while(resposta.length()>0) {
		    String linha = resposta.substring(0, i);
		    resposta = resposta.substring(i).trim();
			matcher = pattern.matcher(linha);
		    if(matcher.find()) {
		    	RoleImpl role = new RoleImpl();
		    	role.setId(new Long(matcher.group(1)));
		    	role.setName(matcher.group(2));
		    	ret.add(role);
		    }
	    	i = getLine(resposta);
		}
	    
		return ret;
	}

}
