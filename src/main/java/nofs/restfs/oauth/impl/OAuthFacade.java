package nofs.restfs.oauth.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.OAuthServiceProvider;
import net.oauth.OAuth.Parameter;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;
import net.oauth.http.HttpMessage;
import nofs.restfs.oauth.IOAuthFacade;

public class OAuthFacade implements IOAuthFacade {

	private final String _key;
	private final String _secret;
	private final String _requestTokenURL;
	private final String _userAuthURL;
	private final String _accessTokenURL;
	private final OAuthServiceProvider _provider;
	private final OAuthConsumer _consumer;
	private final OAuthClient _client;
	private final OAuthAccessor _accessor;

	private String _authorizationURL;
	private String _error;

	public OAuthFacade(String key, String secret, String requestTokenURL,
			String userAuthURL, String accessTokenURL) {
		_key = key;
		_secret = secret;
		_requestTokenURL = requestTokenURL;
		_userAuthURL = userAuthURL;
		_accessTokenURL = accessTokenURL;
		_provider = new OAuthServiceProvider(_requestTokenURL, _userAuthURL,
				_accessTokenURL);
		_consumer = new OAuthConsumer(null, _key, _secret, _provider);
		_client = new OAuthClient(new HttpClient4());
		_accessor = new OAuthAccessor(_consumer);

		_authorizationURL = null;
		_error = "";
	}

	public String getAccessToken() {
		synchronized(_accessor) {
			return _accessor.accessToken;
		}
	}

	public String getAuthorizationURL() {
		return _authorizationURL;
	}

	public String getError() {
		return _error;
	}
	
	public void beginAuthorization() {
		AsyncAuthorize authThread = new AsyncAuthorize(this);
		authThread.setDaemon(true);
		authThread.start();
	}
	
	public void waitForAuthorization() throws InterruptedException {
		synchronized(_accessor) {
			while(_accessor.accessToken == null) {
				_accessor.wait();
			}
		}
	}
	
	private class AsyncAuthorize extends Thread {
		private final OAuthFacade _facade;
		public AsyncAuthorize(OAuthFacade facade) {
			_facade = facade;
		}
		@Override
		public void run() {
			_facade.Authorize();
		}
	}

	private void Authorize() {
		Server server = null;
		try {
			try {
				synchronized (_accessor) {
					List<OAuth.Parameter> callback = null;

					while (_accessor.accessToken == null) {
						if (server == null) {
							final int freePort = getFreePort();
							server = new Server(freePort);
							for (Connector c : server.getConnectors()) {
								c.setHost("localhost");
							}
							server.setHandler(newCallback());
							server.start();
							callback = OAuth.newList(OAuth.OAUTH_CALLBACK, "http://localhost:" + freePort + CALLBACK_PATH);
						}
						OAuthMessage response = _client.getRequestTokenResponse(_accessor, null, callback);
						String authorizationURL = OAuth.addParameters(
								_accessor.consumer.serviceProvider.userAuthorizationURL,
								OAuth.OAUTH_TOKEN, _accessor.requestToken);
						if (response.getParameter(OAuth.OAUTH_CALLBACK_CONFIRMED) == null) {
							authorizationURL = OAuth.addParameters(authorizationURL, callback);
						}
						_authorizationURL = authorizationURL;
						_accessor.wait();
						if (_accessor.accessToken == null) {
							List<Parameter> parameters = null;
							if(_verifier != null) {
								parameters = OAuth.newList(OAuth.OAUTH_VERIFIER, _verifier.toString());
							}
							_client.getAccessToken(_accessor, null, parameters);
						}
						_accessor.notifyAll();
					}
				}
			} finally {
				if (server != null) {
					try {
						server.stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (OAuthProblemException p) {
			StringBuilder msg = new StringBuilder();
			String problem = p.getProblem();
			if (problem != null) {
				msg.append(problem);
			}
			Object response = p.getParameters().get(HttpMessage.RESPONSE);
			if (response != null) {
				String eol = System.getProperty("line.separator", "\n");
				msg.append(eol).append(response);
			}
			_error += msg.toString();
		} catch (Exception e) {
			_error += e.getMessage();
		}
	}

	private static int getFreePort() throws IOException {
		Socket s = new Socket();
		s.bind(null);
		try {
			return s.getLocalPort();
		} finally {
			s.close();
		}
	}

	private String _verifier;

	private void proceed(String requestToken, String verifier) {
		synchronized (_accessor) {
			if (requestToken == null
					|| requestToken.equals(_accessor.requestToken)) {
				_verifier = verifier;
				_accessor.notifyAll();
				return;
			}
		}
		System.err.println("ignored authorization of request token "
				+ requestToken);
	}

	private Handler newCallback() {
		return new Callback(this);
	}

	private static final String CALLBACK_PATH = "/oauth/callback";

	private static class Callback extends AbstractHandler {

		protected Callback(OAuthFacade client) {
			this._facade = client;
		}

		protected final OAuthFacade _facade;

		public void handle(String target, HttpServletRequest request,
				HttpServletResponse response, int dispatch) throws IOException,
				ServletException {
			if (!CALLBACK_PATH.equals(target)) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				conclude(response);
				_facade.proceed(request.getParameter(OAuth.OAUTH_TOKEN),
						request.getParameter(OAuth.OAUTH_VERIFIER));
				((Request) request).setHandled(true);
			}
		}

		protected void conclude(HttpServletResponse response)
				throws IOException {
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("text/html");
			final PrintWriter doc = response.getWriter();
			doc.println("<HTML>");
			doc.println("<body onLoad=\"window.close();\">");
			doc.println("Thank you.  You can close this window now.");
			doc.println("</body>");
			doc.println("</HTML>");
		}

	}
}
