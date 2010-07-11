package nofs.restfs.tests;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Assert;

import nofs.restfs.tests.util.RestExampleRunner;
import nofs.restfs.tests.util.HttpTestClient;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.jackrabbit.webdav.DavConstants;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.client.methods.DavMethod;
import org.apache.jackrabbit.webdav.client.methods.OptionsMethod;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.apache.commons.httpclient.Header;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.apache.jackrabbit.webdav.property.DefaultDavProperty;
import org.apache.jackrabbit.webdav.version.OptionsResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RestFsIntegrationTest {
	private RestExampleRunner _restRunner;

	@Before
	public void Setup() throws Exception {
		_restRunner = new RestExampleRunner();
		_restRunner.StartRunner();
	}

	@After
	public void TearDown() throws Exception {
		_restRunner.StopRunner();
	}

	private static void TestHttpMethod(String line) throws Exception {
		// final String host = "joekaylor.net";
		// final int port = 80;
		final String host = "127.0.0.1";
		final int port = 8100;
		HttpTestClient client = new HttpTestClient();
		WriteResponse(client.WriteHTTP(host, port, line));
	}

	private static void WriteResponse(String response) {
		System.out.println("***************");
		System.out.println(response);
		System.out.println("***************");
	}

	@Test
	public void TestOptionsMethodBehavior() throws Exception {
		TestHttpMethod("OPTIONS /users/1 HTTP/1.1");
	}

	@Test
	public void TestGetHttpMethodBehavior() throws Exception {
		TestHttpMethod("GET /users/1 HTTP/1.1");
	}

	@Test
	public void TestPropDefMethodBehavior() throws Exception {
		TestHttpMethod("PROPDEF /users/1 HTTP/1.1");
	}

	@Test
	public void TestOptionsMethod() throws Exception {
		final String proto = "http://";
		final String host = "127.0.0.1:8100";
		final String resourcePath = "/users";
		final String user = "userID";
		final String password = "pw";
		HostConfiguration hostConfig = new HostConfiguration();
		hostConfig = new HostConfiguration();
        hostConfig.setHost(host); 
        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        int maxHostConnections = 20;
        params.setMaxConnectionsPerHost(hostConfig, maxHostConnections);
        connectionManager.setParams(params);    
        HttpClient client = new HttpClient(connectionManager);
        Credentials creds = new UsernamePasswordCredentials(user, password);
        client.getState().setCredentials(AuthScope.ANY, creds);
        client.setHostConfiguration(hostConfig);
        
        OptionsMethod method = new OptionsMethod(proto + host + resourcePath);
        client.executeMethod(method);

        System.out.println("********************");
        for(Header header : method.getResponseHeaders()) {
        	System.out.println("name: {" + header.getName() + "} value {" + header.getValue() + "}");
        }
        System.out.println("********************");
        
        try {
        	OptionsResponse optionsResponse = method.getResponseAsOptionsResponse();
			System.out.println(optionsResponse.toString());
			Assert.fail("Expected exception of type IOException");
        } catch(IOException ioe) {
        	Assert.assertEquals("XML parsing error", ioe.getMessage());
        }
	}
	
	@Test
	public void TestList() throws Exception {
		final String proto = "http://";
		final String host = "127.0.0.1:8100";
		final String resourcePath = "/users";
		final String user = "userID";
		final String password = "pw";
		HostConfiguration hostConfig = new HostConfiguration();
		hostConfig = new HostConfiguration();
        hostConfig.setHost(host); 
        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        int maxHostConnections = 20;
        params.setMaxConnectionsPerHost(hostConfig, maxHostConnections);
        connectionManager.setParams(params);    
        HttpClient client = new HttpClient(connectionManager);
        Credentials creds = new UsernamePasswordCredentials(user, password);
        client.getState().setCredentials(AuthScope.ANY, creds);
        client.setHostConfiguration(hostConfig);
		DavMethod pFind = new PropFindMethod(proto + host + resourcePath,
				DavConstants.PROPFIND_ALL_PROP, DavConstants.DEPTH_1);
		client.executeMethod(pFind);

		MultiStatus multiStatus = pFind.getResponseBodyAsMultiStatus();
		MultiStatusResponse[] responses = multiStatus.getResponses();
		MultiStatusResponse currResponse;
		System.out.println("Folders and files in " + resourcePath + ":");
		for (int i = 0; i < responses.length; i++) {
			currResponse = responses[i];
			if (!(currResponse.getHref().equals(resourcePath) || currResponse.getHref()
					.equals(resourcePath + "/"))) {
				System.out.println(currResponse.getHref());
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void TestPropDef() throws Exception {
		final String host = "127.0.0.1:8100";
		final String path = "/users/1";

		HostConfiguration hostConfig = new HostConfiguration();
		hostConfig.setHost(host);
		HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		int maxHostConnections = 20;
		params.setMaxConnectionsPerHost(hostConfig, maxHostConnections);
		connectionManager.setParams(params);
		HttpClient client = new HttpClient(connectionManager);
		Credentials creds = new UsernamePasswordCredentials("userId", "pw");
		client.getState().setCredentials(AuthScope.ANY, creds);
		client.setHostConfiguration(hostConfig);

		DavMethod pFind = new PropFindMethod("http://" + host + path,
				DavConstants.PROPFIND_ALL_PROP, DavConstants.DEPTH_INFINITY);
		client.executeMethod(pFind);

		MultiStatus multiStatus = pFind.getResponseBodyAsMultiStatus();

		// Not quite nice, but for a example ok
		DavPropertySet props = multiStatus.getResponses()[0].getProperties(200);

		Collection<DefaultDavProperty> propertyColl = (Collection<DefaultDavProperty>) props
				.getContent();
		propertyColl.iterator();
		for (Iterator<DefaultDavProperty> iterator = propertyColl.iterator(); iterator
				.hasNext();) {
			DefaultDavProperty tmpProp = iterator.next();
			System.out.println(tmpProp.getName() + "  " + tmpProp.getValue());
		}

	}
}
