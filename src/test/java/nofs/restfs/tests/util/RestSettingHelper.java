package nofs.restfs.tests.util;

public class RestSettingHelper {
	
	private static final String _xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
	
	public static String CreateVerifierXml(String pin) {
		final String OAuthVerifierNode = "OAuthVerifierFile";
		final String PinNode = "Pin";
		final String pad = "  ";
		String xml = 
			_xmlHeader +
			Begin(OAuthVerifierNode) + "\n" +
				pad + Element(PinNode, pin) +
			End(OAuthVerifierNode);
		return xml;
	}
	
	public static String CreateTokenXml(String accessToken, String requestToken, String tokenSecret) {
		final String accessTokenNode = "AccessToken";
		final String requestTokenNode = "RequestToken";
		final String tokenSecretNode = "TokenSecret";
		final String OAuthTokenFileNode = "OAuthTokenFile";
		final String pad = "  ";
		String xml = 
			_xmlHeader +
			Begin(OAuthTokenFileNode) + "\n" +
				pad + Element(accessTokenNode, accessToken) +
				pad + Element(requestTokenNode, requestToken) +
				pad + Element(tokenSecretNode, tokenSecret) +
			End(OAuthTokenFileNode);
		return xml;			
	}
	
	public static String CreateSettingsXml(
			String fsMethod, String webMethod, String formName, 
			String resource, String host, String port, String oauthPath) {
		final String restfulSettingNode = "RestfulSetting";
		final String fsMethodNode = "FsMethod";
		final String webMethodNode = "WebMethod";
		final String formNameNode = "FormName";
		final String resourceNode = "Resource";
		final String hostNode = "Host";
		final String portNode = "Port";
		final String oauthNode = "OAuthTokenPath";
		
		final String pad = "  ";
		String xml = 
			_xmlHeader +
			Begin(restfulSettingNode) + "\n" +
				pad + Element(webMethodNode, webMethod) +
				pad + Element(fsMethodNode, fsMethod) +
				pad + Element(formNameNode, formName) +
				pad + Element(oauthNode, oauthPath) +
				pad + Element(resourceNode, resource) +
				pad + Element(hostNode, host) +
				pad + Element(portNode, port) +
			End(restfulSettingNode);
		return xml;
	}
	
	public static String CreateAuthXml(
			String key, String secret, String accessTokenURL,
			String userAuthURL, String requestTokenURL,
			String callbackURL)
	{
		final String OAuthConfigFileNode = "OAuthConfigFile";
		final String CallBackURLNode = "CallBackURL";
		final String KeyNode = "Key";
		final String AccessTokenURLNode = "AccessTokenURL";
		final String UserAuthURLNode = "UserAuthURL";
		final String RequestTokenURLNode = "RequestTokenURL";
		final String SecretNode = "Secret";
		final String pad = "  ";
		String xml =
			_xmlHeader +
			Begin(OAuthConfigFileNode) + "\n" +
				pad + Element(CallBackURLNode, callbackURL) +
				pad + Element(KeyNode, key) +
				pad + Element(AccessTokenURLNode, accessTokenURL) +
				pad + Element(UserAuthURLNode, userAuthURL) + 
				pad + Element(RequestTokenURLNode, requestTokenURL) +
				pad + Element(SecretNode, secret) +
			End(OAuthConfigFileNode);
		
		return xml;
	}
	
	private static String Element(String node, String value) {
		return Begin(node) + value + End(node);
	}
	
	private static String Begin(String name) {
		return "<" + name + ">";
	}
	
	private static String End(String name) {
		return "</" + name + ">\n";
	}
}
