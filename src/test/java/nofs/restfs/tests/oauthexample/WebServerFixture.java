package nofs.restfs.tests.oauthexample;

import java.io.File;

import nofs.restfs.tests.util.RestFsTestHelper;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;

public class WebServerFixture {
	private final Server _server;
	private final boolean UseWar = true;
	
	public WebServerFixture(int port) throws Exception {
		_server = new Server(port);
		for(Connector c : _server.getConnectors()) {
			c.setHost("localhost");
		}
		if(UseWar) {
			String javaHome = PickJavaHome();
			File toolsPath = new File(javaHome + "/lib/tools.jar");
			
			RestFsTestHelper.HackAddAJarToClassPath(toolsPath);

			String rootPath = System.getProperty("user.home");
			String warName = "oauth-provider-20090105.war";
			rootPath += "/.m2/repository/net/oauth/example/oauth-provider/20090105/" + warName;
			rootPath = new File(rootPath).getAbsolutePath();
			if(!new File(rootPath).exists()) {
				throw new Exception("oauth WAR file does not exist where it was expected");
			}
			WebAppContext context = new WebAppContext();
			context.setContextPath("/oauth-provider");
			context.setWar(rootPath);
			//context.setExtraClasspath(toolsPath);
			_server.setHandler(context);
			_server.setStopAtShutdown(true);
		} else {
			Context root = new Context(_server,"/", Context.SESSIONS);
			root.addServlet(new ServletHolder(new AccessTokenServlet()), "/oauth-provider/access_token");
			root.addServlet(new ServletHolder(new AuthorizationServlet()), "/oauth-provider/authorize");
			root.addServlet(new ServletHolder(new RequestTokenServlet()), "/oauth-provider/request_token");
			root.addServlet(new ServletHolder(new EchoServlet()), "/echo");
		}
	}
	
	public void StartServer() throws Exception {
		_server.start();
	}
	
	public void StopServer() throws Exception {
		_server.stop();
	}
	
	private final static String[] Java_Homes = {
		"C:\\Program Files (x86)\\Java\\jdk1.6.0_18",
		"C:\\Program Files (x86)\\Java\\j2sdk1.4.2_19",
		"/usr/lib/jvm/java-6-openjdk",
		"/usr/lib/jvm/java-1.6.0-openjdk"
	};

	private static boolean exeExists(String path) {
		return new File(path).exists() ||
			   new File(path + ".exe").exists();
	}

	private static String PickJavaHome() throws Exception {
		for(String javaHome : Java_Homes) {
			if(new File(javaHome).exists() && 
				exeExists(javaHome + "/bin/javac") &&
				new File(javaHome + "/lib/tools.jar").exists()) {
				return javaHome;
			}
		}
		throw new Exception("could not find a JDK 1.4 home path");
	}
}
