package nofs.restfs;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.ProvidesName;

@DomainObject
public class BaseRestfulFileObject {
	private String _name;
	@ProvidesName
	public String getName() { return _name; }
	@ProvidesName
	public void setName(String name) { _name = name; }
	
	public BaseRestfulFileObject() {
		_name = "";
	}
}
