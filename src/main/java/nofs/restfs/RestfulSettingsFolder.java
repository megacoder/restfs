package nofs.restfs;

import java.util.LinkedList;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.FolderObject;
import nofs.Library.Annotations.ProvidesName;

@SuppressWarnings("serial")
@FolderObject
@DomainObject
public class RestfulSettingsFolder extends LinkedList<RestfulSetting> {
	private String _name;
	public RestfulSettingsFolder() {
		_name = ".settings";
	}
		
	@ProvidesName
	public String getName() { return _name; }
	@ProvidesName
	public void setName(String name) { _name = name; }
}
