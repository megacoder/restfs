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
		_settings = new RestfulSettingsFolder();
	}
	
	@Override
	public boolean add(RestfulFile file) {
		if(file.getSettings() == null) {
			RestfulSetting settings = null;
			for(RestfulSetting possibleSettings : _settings) {
				if(possibleSettings.getName().compareTo(file.getName()) == 0) {
					settings = possibleSettings;
					break;
				}
			}
			if(settings == null) {
				settings = new RestfulSetting();
				_settings.add(settings);
			}
			file.setSettings(settings);
		}
		return super.add(file);
	}
	
	@ProvidesName
	public String getName() { return _name; }
	@ProvidesName
	public void setName(String name) { _name = name; }
	
	private RestfulSettingsFolder _settings;
	public RestfulSettingsFolder getSettings() { return _settings; }
}
