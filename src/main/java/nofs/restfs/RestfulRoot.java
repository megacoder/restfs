package nofs.restfs;

import java.util.List;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.FolderObject;
import nofs.Library.Annotations.NeedsContainerManager;
import nofs.Library.Annotations.RootFolderObject;
import nofs.Library.Containers.IDomainObjectContainer;
import nofs.Library.Containers.IDomainObjectContainerManager;
import nofs.restfs.rules.RulesFolder;

@RootFolderObject
@FolderObject(ChildTypeFilterMethod="Filter")
@DomainObject
public class RestfulRoot extends RestfulFolderWithSettings {
	public boolean Filter(Class<?> possibleChildType) {
		return 
			possibleChildType == RestfulFolderWithSettings.class ||
			possibleChildType == RestfulFile.class;
	}
	
	private IDomainObjectContainerManager _containerManager;
	@NeedsContainerManager
	public void setContainerManager(IDomainObjectContainerManager containerManager) {
		_containerManager = containerManager;
		super.setContainerManager(containerManager);
	}
	
	@Override
	protected void CreatingList(List<BaseFileObject> list) {
		try {
			list.add(CreateAuthFolder());
            list.add(CreateRulesFolder());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private RulesFolder CreateRulesFolder() throws Exception {
        IDomainObjectContainer<RulesFolder> container = _containerManager.GetContainer(RulesFolder.class);
        RulesFolder folder;
        if(container.GetAllInstances().size() > 0) {
            folder = container.GetAllInstances().iterator().next();
        } else {
            folder = container.NewPersistentInstance();
            folder.setName("rules");
            container.ObjectChanged(folder);
        }
        return folder;
    }
		
	@SuppressWarnings("unchecked")
	private OAuthFolder CreateAuthFolder() throws Exception {
		IDomainObjectContainer<OAuthFolder> container = _containerManager.GetContainer(OAuthFolder.class);
		OAuthFolder folder;
		if(container.GetAllInstances().size() > 0) {
			folder = container.GetAllInstances().iterator().next();
		} else {
			folder = container.NewPersistentInstance();
			folder.setName("auth");
			container.ObjectChanged(folder);
		}
		return folder;
	}
}
