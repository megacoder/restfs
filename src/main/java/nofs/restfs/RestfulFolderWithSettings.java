package nofs.restfs;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.FolderObject;
import nofs.Library.Annotations.NeedsContainerManager;
import nofs.Library.Containers.IDomainObjectContainer;
import nofs.Library.Containers.IDomainObjectContainerManager;

@DomainObject
@FolderObject
public class RestfulFolderWithSettings extends RestfulFolder<BaseFileObject> {
	
	public RestfulFolderWithSettings() {
	}
	
	private IDomainObjectContainerManager _containerManager;
	@NeedsContainerManager
	public void setContainerManager(IDomainObjectContainerManager containerManager) {
		_containerManager = containerManager;
	}
	
	@SuppressWarnings("unchecked")
	private void ObjectChanged() {
		try {
			IDomainObjectContainer<RestfulFolderWithSettings> container = _containerManager.GetContainer(RestfulFolderWithSettings.class);
			container.ObjectChanged(this);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private RestfulSetting CreateSettings(String name) {
		RestfulSetting settings = null;
		String settingsName = "." + name;
		try {
			IDomainObjectContainer<RestfulSetting> settingsContainer = _containerManager.GetContainer(RestfulSetting.class);
			settings = settingsContainer.NewPersistentInstance();
			settings.setName(settingsName);
			settingsContainer.ObjectChanged(settings);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return settings;
	}
	
	private RestfulSetting FindSettingsFor(RestfulFile obj) {
		final String targetName = "." + obj.getName();
		for(BaseFileObject possibleSettings : this) {
			if(possibleSettings instanceof RestfulSetting) {
				RestfulSetting settings = (RestfulSetting)possibleSettings;
				if(settings.getName().compareTo(targetName) == 0) {
					return settings;
				}
			}
		}
		return null;
	}
	
	@Override
	protected void AddingObject(BaseFileObject baseFile) {
		if(baseFile instanceof RestfulFile) {
			RestfulFile file = (RestfulFile)baseFile;
			if(file.getSettings() == null) {
				RestfulSetting settings = FindSettingsFor(file);
				if(settings == null) {
					settings = CreateSettings(file.getName());
					add(settings);
					ObjectChanged();
				}
				file.setSettings(settings);
			}
		}
	}
	
	@Override
	protected void RemovingObject(BaseFileObject baseFile) {
		if(baseFile instanceof RestfulFile) {
			RestfulFile file = (RestfulFile)baseFile;
			if(file.getSettings() != null && contains(file.getSettings())) {
				remove(file.getSettings());
			}
		}
	}
}
