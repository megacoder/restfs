package nofs.restfs;

import java.util.List;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.FolderObject;

@FolderObject(CanAdd = false, CanRemove=false)
@DomainObject
public class OAuthInstanceFolder extends RestfulFolder<BaseFileObject> {
	@Override
	protected void AddingObject(BaseFileObject baseFile) {
	}
	@Override
	protected void RemovingObject(BaseFileObject baseFile) {
	}
	@Override
	protected void CreatingList(List<BaseFileObject> newList) {
	}
}
