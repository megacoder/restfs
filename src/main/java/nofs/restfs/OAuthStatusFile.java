package nofs.restfs;

import java.nio.ByteBuffer;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Containers.IProvidesUnstructuredData;

@DomainObject(CanWrite = false)
public class OAuthStatusFile extends BaseFileObject implements IProvidesUnstructuredData {

	private final StringBuilder _data;
	
	public OAuthStatusFile() {
		_data = new StringBuilder();
	}
	
	public String GetData() { 
		synchronized(_data) {
			return _data.toString();
		}
	}
	
	public void SetState(String text) {
		synchronized(_data) {
			_data.delete(0, _data.length());
			_data.append(text);
		}
	}
	
	public void Append(String text) {
		synchronized(_data) {
			_data.append(text);
		}
	}
	
	@Override
	public boolean Cacheable() {
		return false;
	}

	@Override
	public long DataSize() {
		synchronized(_data) {
			return _data.length();
		}
	}

	@Override
	public void Read(ByteBuffer buffer, long offset, long length) throws Exception {
		synchronized(_data) {
			for(long i = offset; i < offset + length && i < _data.length(); i++) {
				buffer.put((byte)_data.charAt((int)i));
			}
		}
	}

	@Override
	public void Truncate(long length) {
		//NOOP
	}

	@Override
	public void Write(ByteBuffer buffer, long offset, long length) throws Exception {
		//NOOP
	}
}
