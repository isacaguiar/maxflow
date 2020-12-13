package eflow.remote.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eflow.config.Configuration;
import eflow.model.user.User;
import eflow.remote.Remote;
import eflow.remote.http.parser.EflowParser;
import eflow.remote.http.parser.LoginParser;
import eflow.remote.http.parser.ProcessTypeParser;
import eflow.remote.http.parser.RoleParser;

public class HttpRemote implements Remote {

	//pegar do configuraton.properties
	public String ip = "localhost";
	public String porta = "9090";
	
	
	public HttpRemote() throws IOException {
		ip = Configuration.getIpServer();
		porta = Configuration.getPortServer();
		porta = "9090";
		if(porta==null) {
			//default do eflow na help
			porta = "9090";
		}
	}
	
	
	private URL getUrl(Map params) throws MalformedURLException {
		String url = "http://"+ip+":"+porta+"/eflow/editor.do?";
		Iterator it = params.keySet().iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			String value = (String) params.get(key);
			url += key+"="+value+"&";
		}
		return new URL(url);
	}
	
	private String getString(Map params) throws Exception {
		try {
			//URL url = new URL("http://"+ip+":"+porta+"/eflow/editor.do?method="+method);
			URL url = getUrl(params);
			URLConnection uc = url.openConnection();
			uc.setDefaultUseCaches(false);
			uc.setUseCaches(false);
			uc.setRequestProperty("Cache-Control","max-age=0,no-cache");
			uc.setRequestProperty("Pragma","no-cache");
			
			InputStream in = uc.getInputStream();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte b[] = new byte[1];
			int i = 0;
			do {
				i = in.read(b);
				bout.write(b);
			}while(i>0);
			return new String(bout.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} 
	}
	
	
	public String getProcessTypeString() throws Exception {
		Map params = new HashMap();
		params.put("method", "listProcessType");
		return this.getString(params).trim();
	}
	public String getRoleString(String user) throws Exception {
		Map params = new HashMap();
		params.put("method", "listRole");
		params.put("user", user);
		return this.getString(params).trim();
	}
	
	
	
	public List getAllProcessType() throws Exception {
		ProcessTypeParser parser = new ProcessTypeParser();
		return (List) parser.parse(this.getProcessTypeString());
	}

	public List getAllRole(User user) throws Exception {
		RoleParser parser = new RoleParser();
		return (List) parser.parse(this.getRoleString(user.getLogin()));
	}

	
	public boolean login(User user) throws Exception {
		EflowParser parser = new LoginParser();
		return ((Boolean) parser.parse(this.getLoginString(user.getLogin(), user.getPassword()))).booleanValue();
	}

	private String getLoginString(String login, String password) throws Exception {
		Map params = new HashMap();
		params.put("method", "login");
		params.put("user", login);
		params.put("password", password);
		return this.getString(params).trim();
	}
	
}
