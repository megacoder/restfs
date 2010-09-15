package nofs.restfs;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.HideMethod;

@DomainObject(CanWrite=false)
public class OAuthTokenFile extends BaseFileObject {

	private String _accessToken = "";
	private String _requestToken = "";
	private String _tokenSecret = "";
	
	public String getAccessToken() {
		return _accessToken;
	}
	public void setAccessToken(String value) { 
		_accessToken = value;
	}
	public String getRequestToken() {
		return _requestToken;
	}
	public void setRequestToken(String value) {
		_requestToken = value;
	}
	public String getTokenSecret() {
		return _tokenSecret;
	}
	public void setTokenSecret(String value) {
		_tokenSecret = value;
	}
	
	@HideMethod
	public boolean IsBlank() {
		return _accessToken.length() == 0 && _requestToken.length() == 0 && _tokenSecret.length() == 0;
	}
}
