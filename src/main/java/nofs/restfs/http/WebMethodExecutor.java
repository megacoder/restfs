package nofs.restfs.http;

import nofs.restfs.RestfulSetting;

public class WebMethodExecutor {
	
	private static IWebExecutorFacade Choose(String oAuthToken) {
		if(oAuthToken == null || oAuthToken.length() == 0) {
			return WebDavFacade.Instance();
		} else {
			return new OAuthWebFacade(oAuthToken);
		}
	}
	
	public static byte[] PerformMethod(
			String fileName, RestfulSetting settings, 
			byte[] representation, String oAuthToken) throws Exception {
		try {
			System.out.println(
					fileName + " PerformMethod() - web method: " + settings.getWebMethod() + 
					" - fs method: " + settings.getFsMethod() + 
					" - host: " + settings.getHost() + 
					" - resource: " + settings.getResource());
			if(settings.getWebMethod().toLowerCase().compareTo("get") == 0) {
				System.out.println(fileName + " get...");
				GetAnswer answer = Choose(oAuthToken).GetMethod(
						settings.getHost(), settings.getPort(), 
						settings.getResource());
				System.out.println(fileName + " get completed");
				return answer.getData();
			} else if(settings.getWebMethod().toLowerCase().compareTo("post") == 0) {
				System.out.println(fileName + " post...");
				PostAnswer answer = Choose(oAuthToken).PostMethod(
						settings.getHost(), settings.getPort(), 
						settings.getResource(), settings.getFormName(),
						representation);
				System.out.println(fileName + " post completed");
				return answer == null ? null : answer.getData();
			} else if(settings.getWebMethod().toLowerCase().compareTo("put") == 0) {
				System.out.println(fileName + " put...");
				PutAnswer answer = Choose(oAuthToken).PutMethod(
						settings.getHost(), settings.getPort(), 
						settings.getResource(), representation);
				System.out.println(fileName + " put completed");
				return answer == null ? null : answer.getData();
			} else if(settings.getWebMethod().toLowerCase().compareTo("delete") == 0) {
				System.out.println(fileName + " delete...");
				DeleteAnswer answer = Choose(oAuthToken).DeleteMethod(
						settings.getHost(), settings.getPort(),
						settings.getResource());
				System.out.println(fileName + " delete completed");
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
