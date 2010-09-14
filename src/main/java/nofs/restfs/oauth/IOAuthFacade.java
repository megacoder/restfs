package nofs.restfs.oauth;

import java.util.List;

import net.oauth.OAuthMessage;
import net.oauth.OAuth.Parameter;

public interface IOAuthFacade {

	public String getRequestToken();
	public String getRequestTokenSecret();
	public String getAccessToken();
	public void setAccessToken(String value);

	public String getAuthorizationURL();
	
	public void setVerifier(String verifier);

	public String getError();

	public void beginAuthorization();

	public boolean waitForAuthorization(int time) throws InterruptedException;

	public OAuthMessage Invoke(String method, String url, List<Parameter> parameters) throws Exception;
	
}