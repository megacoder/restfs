package nofs.restfs.tests.util;

public class RestSettingHelper {
	public static String CreateSettingsXml(
			String fsMethod, String webMethod, String formName, 
			String resource, String host, String port) {
		final String restfulSettingNode = "RestfulSetting";
		final String fsMethodNode = "FsMethod";
		final String webMethodNode = "WebMethod";
		final String formNameNode = "FormName";
		final String resourceNode = "Resource";
		final String hostNode = "Host";
		final String portNode = "Port";
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
			End(restfulSettingNode);
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
