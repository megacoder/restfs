package nofs.restfs.http;

import java.util.ArrayList;
import java.util.List;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthServiceProvider;
import net.oauth.OAuth.Parameter;
import net.oauth.client.OAuthClient;
import net.oauth.client.URLConnectionClient;
import org.apache.commons.httpclient.NameValuePair;

public class OAuthWebFacade implements IWebExecutorFacade {

	private final OAuthAccessor _accessor;
	private final OAuthClient _client;
	
	public OAuthWebFacade(String token) {
		OAuthServiceProvider provider = new OAuthServiceProvider(null,null,null);
		OAuthConsumer consumer = new OAuthConsumer(null,null,null,provider);
		_accessor = new OAuthAccessor(consumer);
		_accessor.accessToken = token;
		_client = new OAuthClient(new URLConnectionClient());
	}
	
	private static String ConvertToString(byte[] data) {
		StringBuffer buff = new StringBuffer();
		for(int i = 0 ; i < data.length; i++) {
			buff.append((char)data[i]);
		}
		return buff.toString();
	}

	private static List<Parameter> GetParameters(String formName, byte[] representation) throws Exception {
		JSONParser parser = new JSONParser();
		String stringRep = ConvertToString(representation);
		List<Parameter> oAuthParameters = new ArrayList<Parameter>();
		if(parser.DataIsJSONData(stringRep)) {
			NameValuePair[] parameters = parser.ParseJSONIntoPairs(formName, stringRep);
			for(NameValuePair parameter : parameters) {
				oAuthParameters.add(new Parameter(parameter.getName(), parameter.getValue()));
			}
		}
		return oAuthParameters;
	}
	
	private static byte[] GetRepresentation(OAuthMessage message) throws Exception {
		return StreamReaderHelper.ReadStreamCompletely(message.getBodyAsStream());
	}
	
	@Override
	public DeleteAnswer DeleteMethod(String host, String port, String resource)	throws Exception {
		throw new Exception("not implemented");
	}

	@Override
	public GetAnswer GetMethod(String host, String port, String resource) throws Exception {
		List<Parameter> parameters = GetParameters("", new byte[0]);
		OAuthMessage message = _client.invoke(_accessor, OAuthMessage.GET, URIHelper.GetURI(host, port, resource), parameters);
		return new GetAnswer(0, GetRepresentation(message));
	}

	@Override
	public PostAnswer PostMethod(String host, String port, String resource, String formName, byte[] data) throws Exception {
		List<Parameter> parameters = GetParameters(formName, data);
		OAuthMessage message = _client.invoke(_accessor, OAuthMessage.POST, URIHelper.GetURI(host,port,resource), parameters);
		return new PostAnswer(0, GetRepresentation(message));
	}

	@Override
	public PutAnswer PutMethod(String host, String port, String resource, byte[] data) throws Exception {
		List<Parameter> parameters = GetParameters("", data);
		OAuthMessage message = _client.invoke(_accessor, OAuthMessage.PUT, URIHelper.GetURI(host,port,resource), parameters);
		return new PutAnswer(0, GetRepresentation(message));
	}

}
