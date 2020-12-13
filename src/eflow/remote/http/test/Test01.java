package eflow.remote.http.test;

import eflow.remote.http.HttpRemote;

public class Test01 {

	
	public static void main(String[] args) {
		try {
			HttpRemote remote = new HttpRemote();
			System.out.println(remote.getProcessTypeString());
			System.out.println(remote.getRoleString(null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
