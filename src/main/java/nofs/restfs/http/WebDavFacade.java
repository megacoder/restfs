package nofs.restfs.http;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public class WebDavFacade implements IWebExecutorFacade {
	private final Map<String, HttpConnectionManager> _connectionManagers;
	private final Map<String, HostConfiguration> _hostConfigurations;
	
	public WebDavFacade() {
		_connectionManagers = new TreeMap<String, HttpConnectionManager>();
		_hostConfigurations = new TreeMap<String, HostConfiguration>();
	}
	
	private static WebDavFacade _instance = new WebDavFacade();
	public static IWebExecutorFacade Instance() {
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

	@Override
	public GetAnswer GetMethod(
			String host, String port, String resource) throws Exception {
		HttpConnectionManager manager = GetManager(host);
		HttpClient client = new HttpClient(manager);
		client.setHostConfiguration(GetConfig(host));
		String uri = URIHelper.GetURI(host, port, resource);
		GetMethod getMethod = new GetMethod(uri);
		client.executeMethod(getMethod);
		return new GetAnswer(getMethod.getStatusCode(), getMethod.getResponseBody());
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public PutAnswer PutMethod(String host, String port, String resource, byte[] data) throws Exception {
		PutMethod putMethod = new PutMethod(URIHelper.GetURI(host,port,resource));
		putMethod.setRequestBody(new ByteArrayInputStream(data));
		HttpConnectionManager manager = GetManager(host);
		HttpClient client = new HttpClient(manager);
		client.setHostConfiguration(GetConfig(host));
		client.executeMethod(putMethod);
		return new PutAnswer(putMethod.getStatusCode(), putMethod.getResponseBody());
	}
	
	@Override
	public PostAnswer PostMethod(
			String host, String port, String resource, 
			String formName, byte[] data) throws Exception {
		JSONParser parser = new JSONParser();
		String representation = ConvertToString(data);
		PostMethod postMethod = new PostMethod(URIHelper.GetURI(host, port, resource));
		if(parser.DataIsJSONData(representation)) {
			NameValuePair[] parameters = parser.ParseJSONIntoPairs(formName, representation);
			postMethod.setRequestBody(parameters);
		} else if(representation.contains("<?xml version=\"1.0\"?>")){
			postMethod.setRequestEntity(new StringRequestEntity(representation, "text/xml", "US-ASCII"));
		} else {
			return null;
		}
		HttpConnectionManager manager = GetManager(host);
		HttpClient client = new HttpClient(manager);
		client.setHostConfiguration(GetConfig(host));
		client.executeMethod(postMethod);
		return new PostAnswer(postMethod.getStatusCode(), postMethod.getResponseBody());
	}
	
	@Override
	public DeleteAnswer DeleteMethod(String host, String port, String resource) throws Exception {
		DeleteMethod deleteMethod = new DeleteMethod(URIHelper.GetURI(host, port, resource));
		HttpConnectionManager manager = GetManager(host);
		HttpClient client = new HttpClient(manager);
		client.setHostConfiguration(GetConfig(host));
		client.executeMethod(deleteMethod);
		return new DeleteAnswer(deleteMethod.getStatusCode(), deleteMethod.getResponseBody());
	}
	
	private static String ConvertToString(byte[] data) {
		StringBuffer buff = new StringBuffer();
		for(int i = 0 ; i < data.length; i++) {
			buff.append((char)data[i]);
		}
		return buff.toString();
	}
}
