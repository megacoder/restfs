package nofs.restfs.tests.util;

import fuse.FuseOpenSetter;

public class MockFuseOpenSetter implements FuseOpenSetter
{
	private Object _fh = null;
	private boolean _isDirectIO = false;
	private boolean _keepCache = false;

	public MockFuseOpenSetter() {
	}

	public void setFh(Object fh) {
		_fh = fh;
	}

	public Object getFh() {
		return _fh;
	}

   	public boolean isDirectIO() {
   		return _isDirectIO;
   	}

   	public void setDirectIO(boolean directIO) {
   		_isDirectIO = directIO;
   	}

   	public boolean isKeepCache() {
   		return _keepCache;
   	}

	public void setKeepCache(boolean keepCache) {
		_keepCache = keepCache;
	}
}
