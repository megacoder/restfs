package nofs.restfs;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.ProvidesName;

@DomainObject
public class RestfulSetting extends BaseFileObject {
	private String _name;
	public RestfulSetting() {
		_name = "";
	}
	
	@ProvidesName
	public String getName() { return _name; }
	@ProvidesName
	public void setName(String name) { _name = name; }
	
	private String _method = "";
	public String getWebMethod() { return _method; }
	public void setWebMethod(String value) { _method = value; }
	
	private String _port = "";
	public String getPort() { return _port; }
	public void setPort(String value) { _port = value; }
	
	private String _fsMethod = "";
	public String getFsMethod() { return _fsMethod; }
	public void setFsMethod(String value) { _fsMethod = value; }
	
	private String _host = "";
	public String getHost() { return _host; }
	public void setHost(String value) { _host = value; }
	
	private String _resource = "";
	public String getResource() { return _resource; }
	public void setResource(String value) { _resource = value; }
}
