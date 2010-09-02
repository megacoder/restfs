package nofs.restfs.oauth;

public interface IOAuthFacade {

	public String getAccessToken();

	public String getAuthorizationURL();

	public String getError();

	public void beginAuthorization();

	public void waitForAuthorization() throws InterruptedException;

}