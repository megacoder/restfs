package nofs.restfs.http;

public class GetAnswer extends HttpAnswer {
	
	public GetAnswer(int code, byte[] data) {
		super(code);
		_data = data;
	}
	
	private final byte[] _data;
	public byte[] getData() {
		return _data;
	}

}
