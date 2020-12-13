package eflow.remote.http.test;

import eflow.model.user.impl.UserImpl;
import eflow.remote.http.HttpRemote;
import eflow.remote.http.parser.ProcessTypeParser;

public class Test04 {

	
	public static void main(String[] args) {
		try {
			HttpRemote remote = new HttpRemote();
			System.out.println(remote.getAllProcessType());
			UserImpl user = new UserImpl();
			user.setLogin("victor");
			user.setPassword("victor");
			System.out.println(remote.login(user));
			System.out.println(remote.getAllRole(user));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
