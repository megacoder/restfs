package nofs.restfs.http;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.jackrabbit.webdav.client.methods.OptionsMethod;

public class WebDavFacade {
	private final Map<String, HttpConnectionManager> _connectionManagers;
	private final Map<String, HostConfiguration> _hostConfigurations;
	
	public WebDavFacade() {
		_connectionManagers = new TreeMap<String, HttpConnectionManager>();
		_hostConfigurations = new TreeMap<String, HostConfiguration>();
	}
	
	private static WebDavFacade _instance = new WebDavFacade();
	public static WebDavFacade Instance() {
		return _instance;
	}
	
	private HostConfiguration GetConfig(String host) throws Exception {
		if(!_hostConfigurations.containsKey(host)) {
			HostConfiguration hostConfig = new HostConfiguration();
			hostConfig = new HostConfiguration();
	        hostConfig.setHost(host);
	        _hostConfigurations.put(host, hostConfig);
		}
		HostConfiguration config = _hostConfigurations.get(host);
		if(config == null) {
			throw new Exception("Config for host " + host + " is null");
		}
		return config;
	}
	
	private HttpConnectionManager GetManager(String host) throws Exception {
		if(!_connectionManagers.containsKey(host)) {
	        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
	        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
	        int maxHostConnections = 20;
	        params.setMaxConnectionsPerHost(GetConfig(host), maxHostConnections);
	        connectionManager.setParams(params);    
	        _connectionManagers.put(host, connectionManager);	        
		}
		return _connectionManagers.get(host);
	}
	
	private static String GetURI(String host, String port, String resource) throws MalformedURLException, URISyntaxException {
		if(!resource.startsWith("/")) {
			resource = "/" + resource;
		}
		int portValue = 80;
		if(port.length() > 0)
		{
			portValue = Integer.parseInt(port);
		}
		return new URL("http", host, portValue, resource).toURI().toString(); 
	}
	
	public GetAnswer GetMethod(String host, String port, String resource) throws Exception {
		HttpConnectionManager manager = GetManager(host);
		HttpClient client = new HttpClient(manager);
		client.setHostConfiguration(GetConfig(host));
		String uri = GetURI(host, port, resource);
		GetMethod getMethod = new GetMethod(uri);
		client.executeMethod(getMethod);
		return new GetAnswer(getMethod.getStatusCode(), getMethod.getResponseBody());
	}
	
	public OptionsAnswer OptionsMethod(String host, String resource) throws Exception {
		HttpConnectionManager manager = GetManager(host);
		HttpClient client = new HttpClient(manager);
		client.setHostConfiguration(GetConfig(host));
		OptionsMethod method = new OptionsMethod("http://" + host + resource);
		client.executeMethod(method);
		ArrayList<HttpMethods> methods = new ArrayList<HttpMethods>();
		if(method.succeeded()) {
			Header optionsHeader = method.getResponseHeader("Allow");
			String value = optionsHeader.getValue().toLowerCase();
			if(value.contains("get")) {
				methods.add(HttpMethods.GET);
			}
			if(value.contains("head")) {
				methods.add(HttpMethods.HEAD);
			}
			if(value.contains("post")) {
				methods.add(HttpMethods.POST);
			}
			if(value.contains("delete")) {
				methods.add(HttpMethods.DELETE);
			}
			if(value.contains("propfind")) {
				methods.add(HttpMethods.PROPFIND);
			}
			if(value.contains("options")) {
				methods.add(HttpMethods.OPTIONS);
			}
			if(value.contains("put")) {
				methods.add(HttpMethods.PUT);
			}
			if(value.split(",").length != methods.size()) {
				throw new Exception("Found " + methods.size() + " methods, but returned these methods: {" + value + "}");
			}
		}
		return new OptionsAnswer(method.getStatusCode(), methods);
	}
}
