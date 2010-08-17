package nofs.restfs.http;

public class PostAnswer extends HttpAnswer {
	public PostAnswer(int code, byte[] data) {
		super(code);
		_data = data;
	}
	
	private final byte[] _data;
	public byte[] getData() {
		return _data;
	}
}
