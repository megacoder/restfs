package nofs.restfs.tests.util;

public class RestSettingHelper {
	public static String CreateSettingsXml(
			String fsMethod, String webMethod, String formName, 
			String resource, String host, String port) {
		return 
			//"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			//"\n" + 
			"<RestfulSetting>\n" + 
			"  <FsMethod>" + fsMethod + "</FsMethod>\n" + 
			"  <WebMethod>" + webMethod + "</WebMethod>\n" +
			"  <FormName>" + formName + "</FormName>\n" +
			"  <Resource>" + resource + "</Resource>\n" + 
			"  <Host>" + host + "</Host>\n" +
			"  <Port>" + port + "</Port>\n" +
			"</RestfulSetting>\n";
	}
}
