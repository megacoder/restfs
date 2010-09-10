package nofs.restfs.tests.oauthexample;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class WebServerFixture {
	private final Server _server;
	
	public WebServerFixture(int port) throws Exception {
		_server = new Server(port);
		for(Connector c : _server.getConnectors()) {
			c.setHost("localhost");
		}
		Context root = new Context(_server,"/", Context.SESSIONS);
		root.addServlet(new ServletHolder(new AccessTokenServlet()), "/oauth-provider/access_token");
		root.addServlet(new ServletHolder(new AuthorizationServlet()), "/oauth-provider/authorize");
		root.addServlet(new ServletHolder(new RequestTokenServlet()), "/oauth-provider/request_token");
		root.addServlet(new ServletHolder(new EchoServlet()), "/echo");
	}
	
	public void StartServer() throws Exception {
		_server.start();
	}
	
	public void StopServer() throws Exception {
		_server.stop();
	}
}
