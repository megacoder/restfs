package nofs.restfs;

import java.nio.ByteBuffer;
import java.util.Date;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.ProvidesLastAccessTime;
import nofs.Library.Annotations.ProvidesLastModifiedTime;
import nofs.Library.Containers.IListensToEvents;
import nofs.Library.Containers.IProvidesUnstructuredData;
import nofs.restfs.http.GetAnswer;
import nofs.restfs.http.WebDavFacade;

@DomainObject
public class RestfulFile extends BaseRestfulFileObject implements IProvidesUnstructuredData, IListensToEvents {

	private volatile byte[] _representation;
	private Date _aTime;
	private Date _mTime;
	private RestfulSetting _settings;
	
	public RestfulFile() {
		super();
		_representation = new byte[0];
		_aTime = new Date(System.currentTimeMillis());
		_mTime = new Date(System.currentTimeMillis());
		_settings = null;
	}
	
	public void setSettings(RestfulSetting settings) {
		_settings = settings;
	}
	public RestfulSetting getSettings() {
		return _settings;
	}
	
	@ProvidesLastAccessTime
	public Date getATime() { return _aTime; }
	@ProvidesLastAccessTime
	public void setATime(Date value) { _aTime = value; AfterUTime(); }
	
	@ProvidesLastModifiedTime
	public Date getMTime() { return _mTime; }
	@ProvidesLastModifiedTime
	public void setMTime(Date value) { _mTime = value; AfterUTime(); }
		
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
		if(offset+length > _representation.length) {
			Truncate(offset+length);
		}
		for(long i = offset; i < offset + length && i < _representation.length; i++) {
			_representation[(int)i] = buffer.get();
		}
		AfterWrite();
	}

	@Override
	public void Closed() {
		if(getSettings().getFsMethod().compareTo("Closed") == 0) {
			try {
				GetAnswer answer = WebDavFacade.Instance().GetMethod(getSettings().getHost(), getSettings().getResource());
				_representation = answer.getData();
			} catch(Exception e) {
				_representation = e.getMessage().getBytes();
			}			
		}
	}

	@Override
	public void Opened() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Created() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Deleting() {
		// TODO Auto-generated method stub
		
	}
	
	private void AfterUTime() {
		
	}
	
	private void BeforeRead() {
		
	}
	
	private void AfterWrite() {
		
	}

}
