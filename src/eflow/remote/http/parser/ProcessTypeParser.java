package eflow.remote.http.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eflow.model.definition.impl.ProcessTypeImpl;

public class ProcessTypeParser extends EflowParser {

	private Pattern pattern = null;

	private Matcher matcher = null;
	
	public ProcessTypeParser() {
		pattern = Pattern.compile("(\\d*);("+ALFA+"*);");
	}

	public Object parse(String resposta) throws Exception {
		List ret = new ArrayList();
		int i = getLine(resposta);

		while (resposta.length() > 1) {
			String linha = resposta.substring(0, i);
			resposta = resposta.substring(i).trim();
			matcher = pattern.matcher(linha);

			if (matcher.find()) {
				ProcessTypeImpl processType = new ProcessTypeImpl();
				processType.setId(new Long(matcher.group(1)));
				processType.setName(matcher.group(2));
				ret.add(processType);
			}
			i = getLine(resposta);
		}

		return ret;
	}


}
