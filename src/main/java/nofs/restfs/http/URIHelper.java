package nofs.restfs.http;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class URIHelper {
	public static String GetURI(String host, String port, String resource) throws MalformedURLException, URISyntaxException {
		if(!resource.startsWith("/")) {
			resource = "/" + resource;
		}
		int portValue = 80;
		if(port.length() > 0)
		{
			portValue = Integer.parseInt(port);
		}
		return new URL("http", host, portValue, resource).toURI().toString(); 
	}
}
