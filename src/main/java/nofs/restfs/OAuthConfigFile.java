package nofs.restfs;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Containers.IListensToEvents;
import nofs.restfs.oauth.IOAuthFacade;
import nofs.restfs.oauth.impl.OAuthFacade;

@DomainObject
public class OAuthConfigFile extends BaseFileObject implements IListensToEvents {
	
	private String Key = "";
	private String Secret = "";
	private String RequestTokenURL = "";
	private String UserAuthURL = "";
	private String AccessTokenURL = "";
	private OAuthInstanceFolder _parent;
	private IOAuthFacade _facade;
	
	public void setupParent(OAuthInstanceFolder parent) {
		_parent = parent;
	}
	
	public void setAccessTokenURL(String accessTokenURL) {
		AccessTokenURL = accessTokenURL;
	}
	public String getAccessTokenURL() {
		return AccessTokenURL;
	}
	public void setUserAuthURL(String userAuthURL) {
		UserAuthURL = userAuthURL;
	}
	public String getUserAuthURL() {
		return UserAuthURL;
	}
	public void setRequestTokenURL(String requestTokenURL) {
		RequestTokenURL = requestTokenURL;
	}
	public String getRequestTokenURL() {
		return RequestTokenURL;
	}
	public void setSecret(String secret) {
		Secret = secret;
	}
	public String getSecret() {
		return Secret;
	}
	public void setKey(String key) {
		Key = key;
	}
	public String getKey() {
		return Key;
	}
	
	private boolean allSettingsAreSet() {
		return 
			!(Key == "" &&
			  Secret == "" &&
			  RequestTokenURL == "" &&
			  UserAuthURL == "" &&
			  AccessTokenURL == "");
	}
	
	private class UpdaterThread extends Thread {
		private final IOAuthFacade _facade;
		private final OAuthInstanceFolder _parent;
		public UpdaterThread(IOAuthFacade facade, OAuthInstanceFolder parent) {
			_facade = facade;
			_parent = parent;
		}
		
		@Override
		public void run() {
			try {
				while(!_facade.waitForAuthorization(100)) {
					if(_facade.getError() != null && _facade.getError().length() > 0) {
						_parent.StatusFile().SetState(_facade.getError());
					} else if(_facade.getAuthorizationURL() != null && _facade.getAuthorizationURL().length() > 0) {
						_parent.StatusFile().SetState(_facade.getAuthorizationURL());
					} else {
						_parent.StatusFile().SetState("Authorizing...");
					}
				}
				_parent.TokenFile().SetState(_facade.getAccessToken());
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void Closed() throws Exception {
		if(allSettingsAreSet()) {
			_facade = new OAuthFacade(Key, Secret, RequestTokenURL, UserAuthURL, AccessTokenURL);
			_facade.beginAuthorization();
			_parent.StatusFile().SetState("Authorizing...");
			_parent.TokenFile().SetState("");
			UpdaterThread updater = new UpdaterThread(_facade, _parent);
			updater.setDaemon(true);
			updater.start();
		}
	}
	@Override
	public void Created() throws Exception {
		//NOOP	
	}
	@Override
	public void Deleting() throws Exception {
		//NOOP		
	}
	@Override
	public void Opened() throws Exception {
		//NOOP
	}	
}
