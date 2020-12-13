package eflow.remote.http.parser;

import java.util.List;

public abstract class EflowParser {
	protected String ALFA = "[^;]";

	protected int getLine(String resposta) {
		int i = resposta.indexOf("\n");
		if (i == 0 || i == -1) {
			i = resposta.length();
		}
		return i;
	}
	
	public abstract Object parse(String resposta) throws Exception;
}
