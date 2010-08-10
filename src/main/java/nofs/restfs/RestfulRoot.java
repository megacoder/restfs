package nofs.restfs;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.FolderObject;
import nofs.Library.Annotations.RootFolderObject;

@RootFolderObject
@FolderObject(ChildTypeFilterMethod="Filter")
@DomainObject
public class RestfulRoot extends RestfulFolderWithSettings {
	public boolean Filter(Class<?> possibleChildType) {
		return 
			possibleChildType == RestfulFolderWithSettings.class ||
			possibleChildType == RestfulFile.class;
	}
}
