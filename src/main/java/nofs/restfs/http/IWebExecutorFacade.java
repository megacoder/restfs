package nofs.restfs.http;

public interface IWebExecutorFacade {

	public GetAnswer GetMethod(String host, String port, String resource)
			throws Exception;

	public PutAnswer PutMethod(String host, String port, String resource,
			byte[] data) throws Exception;

	public PostAnswer PostMethod(String host, String port, String resource,
			String formName, byte[] data) throws Exception;

	public DeleteAnswer DeleteMethod(String host, String port, String resource)
			throws Exception;

}