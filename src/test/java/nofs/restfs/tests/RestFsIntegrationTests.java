package nofs.restfs.tests;

import java.util.Collection;
import java.util.Iterator;

import nofs.restfs.tests.util.RestExampleRunner;
import nofs.restfs.tests.util.TestClient;

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
import org.apache.jackrabbit.webdav.client.methods.DavMethod;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.apache.jackrabbit.webdav.property.DefaultDavProperty;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RestFsIntegrationTests {
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
		//final String host = "joekaylor.net";
		//final int port = 80;
		final String host = "127.0.0.1";
		final int port = 8100;
		TestClient client = new TestClient();
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
	
	@SuppressWarnings("unchecked")
	@Test
	public void Test1() throws Exception {
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
        
        DavMethod pFind = new PropFindMethod(
        		"http://" + host + path, 
        		DavConstants.PROPFIND_ALL_PROP, DavConstants.DEPTH_INFINITY);
        client.executeMethod(pFind);

        MultiStatus multiStatus = pFind.getResponseBodyAsMultiStatus();      
             
        //Not quite nice, but for a example ok
        DavPropertySet props = multiStatus.getResponses()[0].getProperties(200);
              
        Collection<DefaultDavProperty> propertyColl= (Collection<DefaultDavProperty>)props.getContent(); 
        propertyColl.iterator();
        for(Iterator<DefaultDavProperty> iterator = propertyColl.iterator(); iterator.hasNext();){
            DefaultDavProperty tmpProp=iterator.next();
            System.out.println(tmpProp.getName() +"  "+ tmpProp.getValue());
        }

	}
}
