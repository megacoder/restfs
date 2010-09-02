package nofs.restfs;

import java.util.List;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.FolderObject;

@FolderObject
@DomainObject
public class OAuthFolder extends RestfulFolder<OAuthInstanceFolder> {

	@Override
	protected void AddingObject(BaseFileObject baseFile) {
	}

	@Override
	protected void RemovingObject(BaseFileObject baseFile) {
	}

	@Override
	protected void CreatingList(List<OAuthInstanceFolder> newList) {
		
	}
}
