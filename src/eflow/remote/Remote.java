package eflow.remote;

import java.util.List;

import eflow.model.user.User;

public interface Remote {

	
	List getAllProcessType() throws Exception ;
	List getAllRole(User user) throws Exception;
	boolean login(User user) throws Exception; 
}
