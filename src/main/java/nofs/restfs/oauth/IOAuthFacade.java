package nofs.restfs.oauth;

public interface IOAuthFacade {

	public String getAccessToken();
	public void setAccessToken(String value);

	public String getAuthorizationURL();
	
	public void setVerifier(String verifier);

	public String getError();

	public void beginAuthorization();

	public boolean waitForAuthorization(int time) throws InterruptedException;

}