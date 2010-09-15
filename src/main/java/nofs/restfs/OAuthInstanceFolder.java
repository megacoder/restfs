package nofs.restfs;

import java.util.List;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.FolderObject;
import nofs.Library.Annotations.NeedsContainerManager;
import nofs.Library.Containers.IDomainObjectContainer;
import nofs.Library.Containers.IDomainObjectContainerManager;

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
		try {
			newList.add(TokenFile());
			newList.add(StatusFile());
			newList.add(ConfigFile());
			newList.add(VerifierFile());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private IDomainObjectContainerManager _containerManager;
	@NeedsContainerManager
	public void setContainerManager(IDomainObjectContainerManager containerManager) {
		_containerManager = containerManager;
	}
	
	private volatile OAuthTokenFile _tokenFile = null;
	private volatile OAuthStatusFile _statusFile = null;
	private volatile OAuthConfigFile _configFile = null;
	private volatile OAuthVerifierFile _verifierFile;
	
	@SuppressWarnings("unchecked")
	public OAuthConfigFile ConfigFile() throws Exception {
		if(_configFile == null) {
			IDomainObjectContainer<OAuthConfigFile> container = _containerManager.GetContainer(OAuthConfigFile.class);
			_configFile = container.NewPersistentInstance();
			_configFile.setName("config");
			container.ObjectChanged(_configFile);
			_configFile.setupParent(this);
		}
		return _configFile;
	}	
	
	@SuppressWarnings("unchecked")
	public OAuthStatusFile StatusFile() throws Exception {
		if(_statusFile == null) {
			IDomainObjectContainer<OAuthStatusFile> container = _containerManager.GetContainer(OAuthStatusFile.class);
			_statusFile = container.NewPersistentInstance();
			_statusFile.setName("status");
			container.ObjectChanged(_statusFile);
		}
		return _statusFile;
	}
	
	@SuppressWarnings("unchecked")
	public OAuthTokenFile TokenFile() throws Exception {
		if(_tokenFile == null) {
			IDomainObjectContainer<OAuthTokenFile> container = _containerManager.GetContainer(OAuthTokenFile.class);
			_tokenFile = container.NewPersistentInstance();
			_tokenFile.setName("token");
			container.ObjectChanged(_tokenFile);
		}
		return _tokenFile;
	}
	
	@SuppressWarnings("unchecked")
	public OAuthVerifierFile VerifierFile() throws Exception {
		if(_verifierFile == null) {
			IDomainObjectContainer<OAuthVerifierFile> container = _containerManager.GetContainer(OAuthVerifierFile.class);
			_verifierFile = container.NewPersistentInstance();
			_verifierFile.setName("verifier");
			container.ObjectChanged(_verifierFile);
			_verifierFile.setParent(this);
		}
		return _verifierFile;
	}
}
