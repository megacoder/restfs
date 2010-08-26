package nofs.restfs.http;

public class PutAnswer extends HttpAnswer {
	public PutAnswer(int code, byte[] data) {
		super(code);
		_data = data;
	}
	
	private final byte[] _data;
	public byte[] getData() {
		return _data;
	}
}
