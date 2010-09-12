package nofs.restfs.tests.util;

public class RestSettingHelper {
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
		final String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
		final String pad = "  ";
		String xml = 
			xmlHeader +
			Begin(restfulSettingNode) + "\n" +
				pad + Element(fsMethodNode, fsMethod) +
				pad + Element(webMethodNode, webMethod) +
				pad + Element(formNameNode, formName) +
				pad + Element(resourceNode, resource) +
				pad + Element(hostNode, host) +
				pad + Element(portNode, port) +
				pad + Element(oauthNode, oauthPath) +
			End(restfulSettingNode);
		return xml;
	}
	
	public static String CreateAuthXml(
			String key,
			String accessToken, String secret, 
			String pin, String accessTokenURL,
			String userAuthURL, String requestTokenURL,
			String callbackURL)
	{
		final String OAuthConfigFileNode = "OAuthConfigFile";
		final String CallBackURLNode = "CallBackURL";
		final String KeyNode = "Key";
		final String AccessTokenNode = "AccessToken";
		final String VerifierPinNode = "VerifierPin";
		final String AccessTokenURLNode = "AccessTokenURL";
		final String UserAuthURLNode = "UserAuthURL";
		final String RequestTokenURLNode = "RequestTokenURL";
		final String SecretNode = "Secret";
		final String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
		final String pad = "  ";
		String xml =
			xmlHeader +
			Begin(OAuthConfigFileNode) + "\n" +
				pad + Element(CallBackURLNode, callbackURL) +
				pad + Element(KeyNode, key) +
				pad + Element(AccessTokenNode, accessToken) +
				pad + Element(VerifierPinNode, pin) +
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
