package nofs.restfs;

import nofs.Library.Annotations.DomainObject;
import nofs.Library.Annotations.HideMethod;
import nofs.Library.Containers.IListensToEvents;

@DomainObject
public class OAuthVerifierFile extends BaseFileObject implements IListensToEvents {

	private String _pin;
	public String getPin() { 
		return _pin;
	}
	public void setPin(String value) {
		_pin = value;
	}
	
	private OAuthInstanceFolder _parent;
	@HideMethod
	public void setParent(OAuthInstanceFolder parent) {
		_parent = parent;
	}
	
	@Override
	public void Closed() throws Exception {
		_parent.ConfigFile().setVerifierPin(_pin);
	}

	@Override
	public void Created() throws Exception {
	}

	@Override
	public void Deleting() throws Exception {
	}

	@Override
	public void Opened() throws Exception {
	}

}
