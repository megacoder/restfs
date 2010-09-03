package nofs.restfs.oauth;

public interface IOAuthFacade {

	public String getAccessToken();

	public String getAuthorizationURL();

	public String getError();

	public void beginAuthorization();

	public boolean waitForAuthorization(int time) throws InterruptedException;

}