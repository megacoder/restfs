package nofs.restfs;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.ProvidesName;

@DomainObject
public class RestfulSetting {
	private String _name;
	public RestfulSetting() {
		_name = "";
	}
	
	@ProvidesName
	public String getName() { return _name; }
	@ProvidesName
	public void setName(String name) { _name = name; }
}
