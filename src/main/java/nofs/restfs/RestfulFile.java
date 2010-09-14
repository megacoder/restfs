package nofs.restfs;

import java.nio.ByteBuffer;
import java.util.Date;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.NeedsContainerManager;
import nofs.Library.Annotations.ProvidesLastAccessTime;
import nofs.Library.Annotations.ProvidesLastModifiedTime;
import nofs.Library.Containers.IDomainObjectContainerManager;
import nofs.Library.Containers.IListensToEvents;
import nofs.Library.Containers.IProvidesUnstructuredData;
import nofs.restfs.http.WebMethodExecutor;

@DomainObject
public class RestfulFile extends BaseFileObject implements IProvidesUnstructuredData, IListensToEvents {

	private volatile byte[] _representation;
	private Date _aTime;
	private Date _mTime;
	private RestfulSetting _settings;
	private IDomainObjectContainerManager _containerManager;
	
	public RestfulFile() {
		super();
		_representation = new byte[0];
		_aTime = new Date(System.currentTimeMillis());
		_mTime = new Date(System.currentTimeMillis());
		_settings = null;
	}
	
	@NeedsContainerManager
	public void setContainerManager(IDomainObjectContainerManager manager) {
		_containerManager = manager;
	}
	
	public void setSettings(RestfulSetting settings) {
		_settings = settings;
	}
	
	public RestfulSetting getSettings() {
		return _settings;
	}
	
	private OAuthInstanceFolder getConfigFolder() throws Exception {
		String path = _settings.getOAuthTokenPath();
		if(path.length() > 0) {
			Object fileObj = _containerManager.TranslatePath(path);
			if(fileObj != null && fileObj instanceof OAuthInstanceFolder) {
				return (OAuthInstanceFolder)fileObj;
			}
		}
		return null;
	}
	
	@ProvidesLastAccessTime
	public Date getATime() { return _aTime; }
	@ProvidesLastAccessTime
	public void setATime(Date value) throws Exception { _aTime = value; AfterUTime(); }
	
	@ProvidesLastModifiedTime
	public Date getMTime() { return _mTime; }
	@ProvidesLastModifiedTime
	public void setMTime(Date value) throws Exception { _mTime = value; /*AfterUTime();*/ }
		
	@Override
	public boolean Cacheable() {
		return false;
	}
	
	@Override
	public long DataSize() {
		return _representation.length;
	}

	@Override
	public void Read(ByteBuffer buffer, long offset, long length) throws Exception {
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
	public void Write(ByteBuffer buffer, long offset, long length) throws Exception {
		if(offset+length > _representation.length) {
			Truncate(offset+length);
		}
		for(long i = offset; i < offset + length && i < _representation.length; i++) {
			_representation[(int)i] = buffer.get();
		}
		AfterWrite();
	}

	@Override
	public void Closed() throws Exception {
		if(getSettings().getFsMethod().compareTo("Closed") == 0) {
			PerformMethod();
		}
	}
	
	private void PerformMethod() throws Exception {
		SetRepresentation(WebMethodExecutor.PerformMethod(getName(), getSettings(), _representation, getConfigFolder()));
	}
	
	private void SetRepresentation(byte[] data) {
		_representation = data == null ? new byte[0] : data;
	}
	
	@Override
	public void Opened() throws Exception {
		System.out.println(getName() + " opened");
		if(getSettings().getFsMethod().toLowerCase().compareTo("opened") == 0) {
			PerformMethod();
		}
	}

	@Override
	public void Created() throws Exception {
		System.out.println(getName() + " created");
		if(getSettings().getFsMethod().toLowerCase().compareTo("created") == 0) {
			PerformMethod();
		}
	}

	@Override
	public void Deleting() throws Exception {
		System.out.println(getName() + " deleting");
		if(getSettings().getFsMethod().toLowerCase().compareTo("deleting") == 0) {
			PerformMethod();
		}
	}
	
	private void AfterUTime() throws Exception {
		System.out.println(getName() + " afterutime");
		if(getSettings().getFsMethod().toLowerCase().compareTo("utime") == 0) {
			PerformMethod();
		}
	}
	
	private void BeforeRead() throws Exception {
		System.out.println(getName() + " beforeread");
		if(getSettings().getFsMethod().toLowerCase().compareTo("beforeread") == 0) {
			PerformMethod();
		}
	}
	
	private void AfterWrite() throws Exception {
		System.out.println(getName() + " afterwrite");
		if(getSettings().getFsMethod().toLowerCase().compareTo("AfterWrite") == 0) {
			PerformMethod();
		}
	}

}
