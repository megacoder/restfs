package nofs.restfs.http;

public class DeleteAnswer  extends HttpAnswer {
	
	public DeleteAnswer(int code, byte[] data) {
		super(code);
		_data = data;
	}
	
	private final byte[] _data;
	public byte[] getData() {
		return _data;
	}

}
