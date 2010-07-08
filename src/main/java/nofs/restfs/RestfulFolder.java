package nofs.restfs;

import java.util.LinkedList;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.FolderObject;
import nofs.Library.Annotations.ProvidesName;

@SuppressWarnings("serial")
@FolderObject
@DomainObject
public class RestfulFolder extends LinkedList<RestfulFile> {
	private String _name;
	public RestfulFolder() {
		_name = "";
	}
	
	@Override
	public boolean add(RestfulFile file) {
			
		
		return super.add(file);
	}
	
	@ProvidesName
	public String getName() { return _name; }
	@ProvidesName
	public void setName(String name) { _name = name; }
}
