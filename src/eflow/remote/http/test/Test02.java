package eflow.remote.http.test;

import eflow.remote.http.parser.ProcessTypeParser;

public class Test02 {

	
	public static void main(String[] args) {
		try {
			ProcessTypeParser parser = new ProcessTypeParser();
			System.out.println(parser.parse("0;procedimento;\n2;teste;\n"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
