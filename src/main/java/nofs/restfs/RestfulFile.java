package nofs.restfs;

import java.nio.ByteBuffer;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Containers.IProvidesUnstructuredData;

@DomainObject
public class RestfulFile implements IProvidesUnstructuredData  {

	private byte[] _representation;
	
	public RestfulFile() {
		_representation = new byte[0];
	}
	
	private void BeforeRead() {
		
	}
	
	private void AfterWrite() {
		
	}
	
	@Override
	public boolean Cacheable() {
		return false;
	}

	@Override
	public long DataSize() {
		return _representation.length;
	}

	@Override
	public void Read(ByteBuffer buffer, long offset, long length) {
		BeforeRead();
		for(long i = offset; i < offset + length && i < _representation.length; i++) {
			buffer.put(_representation[(int)i]);
		}
	}

	@Override
	public void Truncate(long newLength) {
		byte[] newData = new byte[(int)newLength];
		System.arraycopy(_representation, 0, newData, 0, (int)Math.min(_representation.length, newLength));
		_representation = newData;
	}

	@Override
	public void Write(ByteBuffer buffer, long offset, long length) {
		for(long i = offset; i < offset + length && i < _representation.length; i++) {
			_representation[(int)i] = buffer.get();
		}
		AfterWrite();
	}

	@Override
	public void Closed() {
		// TODO Auto-generated method stub
		
	}

}
