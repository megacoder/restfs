package nofs.restfs.http;

public abstract class HttpAnswer {
	
	public HttpAnswer(int code) {
		_code = code;
	}
	
	private final int _code;
	public int getCode() {
		return _code;
	}
}
