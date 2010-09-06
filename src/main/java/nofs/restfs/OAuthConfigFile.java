package nofs.restfs;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.HideMethod;
import nofs.Library.Containers.IListensToEvents;
import nofs.restfs.oauth.IOAuthFacade;
import nofs.restfs.oauth.impl.OAuthFacade;

@DomainObject
public class OAuthConfigFile extends BaseFileObject implements IListensToEvents {
	
	private String _key = "";
	private String _secret = "";
	private String _requestTokenURL = "";
	private String _userAuthURL = "";
	private String _accessTokenURL = "";
	private String _verifierPin = "";
	private String _accessToken = "";
	private OAuthInstanceFolder _parent;
	private volatile IOAuthFacade _facade;
	
	@HideMethod
	public void setupParent(OAuthInstanceFolder parent) {
		_parent = parent;
	}
	public String getAccessToken() {
		return _accessToken;
	}
	public void setAccessToken(String value) {
		_accessToken = value;
	}
	public String getVerifierPin() {
		return _verifierPin;
	}
	public void setVerifierPin(String value) {
		_verifierPin = value;
	}
	public void setAccessTokenURL(String accessTokenURL) {
		_accessTokenURL = accessTokenURL;
	}
	public String getAccessTokenURL() {
		return _accessTokenURL;
	}
	public void setUserAuthURL(String userAuthURL) {
		_userAuthURL = userAuthURL;
	}
	public String getUserAuthURL() {
		return _userAuthURL;
	}
	public void setRequestTokenURL(String requestTokenURL) {
		_requestTokenURL = requestTokenURL;
	}
	public String getRequestTokenURL() {
		return _requestTokenURL;
	}
	public void setSecret(String secret) {
		_secret = secret;
	}
	public String getSecret() {
		return _secret;
	}
	public void setKey(String key) {
		_key = key;
	}
	public String getKey() {
		return _key;
	}
	
	private boolean allAuthSettingsAreSet() {
		return 
			!(_key == "" &&
			  _secret == "" &&
			  _requestTokenURL == "" &&
			  _userAuthURL == "" &&
			  _accessTokenURL == "");
	}
	
	private boolean verifierPinSet() {
		return !(_verifierPin == "");
	}
	
	private boolean haveAccessToken() {
		return !(_accessToken == "");
	}
	
	private volatile boolean _updaterRunning = false;
	
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
				System.out.println("waiting for authorization...");
				while(!_facade.waitForAuthorization(100)) {
					if(_facade.getError() != null && _facade.getError().length() > 0) {
						System.out.println(_facade.getError());
						_parent.StatusFile().SetState(_facade.getError());
					} else if(_facade.getAuthorizationURL() != null && _facade.getAuthorizationURL().length() > 0) {
						System.out.println("Auth url: " + _facade.getAuthorizationURL());
						_parent.StatusFile().SetState(_facade.getAuthorizationURL() + "\n");
					} else {
						_parent.StatusFile().SetState("Authorizing...");
					}
				}
				System.out.println("got token: " + _facade.getAccessToken());
				_parent.TokenFile().SetState(_facade.getAccessToken() + "\n");
			} catch(Exception e) {
				e.printStackTrace();
			}
			_updaterRunning = false;
		}
	}
	
	private IOAuthFacade Facade() throws Exception {
		if(_facade == null) {
			_facade = new OAuthFacade(_key, _secret, _requestTokenURL, _userAuthURL, _accessTokenURL, true);
		}
		return _facade;
	}
	
	@Override
	@HideMethod
	public void Closed() throws Exception {
		if(haveAccessToken()) {
			Facade().setAccessToken(_accessToken);
		} else if(!_updaterRunning && allAuthSettingsAreSet()) {
			System.out.println("authorizing...");
			_facade = null;
			_updaterRunning = true;
			
			Facade().beginAuthorization();
			_parent.StatusFile().SetState("Authorizing...");
			_parent.TokenFile().SetState("");
			UpdaterThread updater = new UpdaterThread(Facade(), _parent);
			updater.setDaemon(true);
			updater.start();
		}
		if(verifierPinSet()) {
			System.out.println("setting PIN...");
			Facade().setVerifier(_verifierPin);
		}
	}
	@Override
	@HideMethod
	public void Created() throws Exception {
		//NOOP	
	}
	@Override
	@HideMethod
	public void Deleting() throws Exception {
		//NOOP		
	}
	@Override
	@HideMethod
	public void Opened() throws Exception {
		//NOOP
	}	
}
