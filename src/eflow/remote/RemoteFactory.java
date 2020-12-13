package eflow.remote;

import eflow.remote.http.HttpRemote;

public class RemoteFactory {

	public static Remote getRemote() throws Exception {
		return new HttpRemote();
	}
	
}
