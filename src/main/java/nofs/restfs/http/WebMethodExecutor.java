package nofs.restfs.http;

import nofs.restfs.OAuthInstanceFolder;
import nofs.restfs.RestfulSetting;

public class WebMethodExecutor {
	
	private static IWebExecutorFacade Choose(OAuthInstanceFolder oauthInstance) throws Exception {
		if(oauthInstance == null) {
			System.out.println("picked webdav facade");
			return WebDavFacade.Instance();
		} else {
			System.out.println("picked oauth facade");
			return new OAuthWebFacade(oauthInstance);
		}
	}
	
	public static byte[] PerformMethod(
			String fileName, RestfulSetting settings, byte[] representation, 
			OAuthInstanceFolder oauthInstance) throws Exception {
		try {
			System.out.println(
					fileName + " PerformMethod() - web method: " + settings.getWebMethod() + 
					" - fs method: " + settings.getFsMethod() + 
					" - host: " + settings.getHost() + 
					" - resource: " + settings.getResource());
			if(settings.getWebMethod().toLowerCase().compareTo("get") == 0) {
				System.out.println(fileName + " get...");
				GetAnswer answer = Choose(oauthInstance).GetMethod(
						settings.getHost(), settings.getPort(), 
						settings.getResource());
				System.out.println(fileName + " get completed (" + answer == null ? "<null>" : answer.getCode() + ")");
				return answer.getData();
			} else if(settings.getWebMethod().toLowerCase().compareTo("post") == 0) {
				System.out.println(fileName + " post...");
				PostAnswer answer = Choose(oauthInstance).PostMethod(
						settings.getHost(), settings.getPort(), 
						settings.getResource(), settings.getFormName(),
						representation);
				System.out.println(fileName + " post completed (" + answer == null ? "<null>" : answer.getCode() + ")");
				return answer == null ? null : answer.getData();
			} else if(settings.getWebMethod().toLowerCase().compareTo("put") == 0) {
				System.out.println(fileName + " put...");
				PutAnswer answer = Choose(oauthInstance).PutMethod(
						settings.getHost(), settings.getPort(), 
						settings.getResource(), representation);
				System.out.println(fileName + " put completed (" + answer == null ? "<null>" : answer.getCode() + ")");
				return answer == null ? null : answer.getData();
			} else if(settings.getWebMethod().toLowerCase().compareTo("delete") == 0) {
				System.out.println(fileName + " delete...");
				DeleteAnswer answer = Choose(oauthInstance).DeleteMethod(
						settings.getHost(), settings.getPort(),
						settings.getResource());
				System.out.println(fileName + " delete completed (" + answer == null ? "<null>" : answer.getCode() + ")");
				return answer == null ? null : answer.getData();
			} else {
				throw new Exception("web method " + settings.getWebMethod() + " not implemented yet ");
			}
		} catch(Exception e) {
			System.out.println("method failed");
			e.printStackTrace();
			throw e;
		}
	}
}
