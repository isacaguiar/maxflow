package eflow.remote.http.test;

import eflow.remote.http.HttpRemote;
import eflow.remote.http.parser.ProcessTypeParser;

public class Test03 {

	
	public static void main(String[] args) {
		try {
			HttpRemote remote = new HttpRemote();
			System.out.println(remote.getAllProcessType());
			System.out.println(remote.getAllRole(null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
