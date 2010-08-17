package nofs.restfs.tests.util;

import java.io.File;
import java.util.UUID;

import junit.framework.Assert;

public class TemporaryTestFolder {
	private String _tempFolder;
	public TemporaryTestFolder() {
		String currDir = System.getProperty("user.dir");
		 _tempFolder = currDir + File.separatorChar + UUID.randomUUID().toString();
		 Assert.assertTrue(new File(_tempFolder).mkdir());
	}

	public String getBasePath() {
		return _tempFolder;
	}
	
	public String getPath(String fileName) {
		return _tempFolder + File.separatorChar + fileName;
	}

	public void CleanUp() throws Exception {
		CleanUp(false);
	}

	public void CleanUp(boolean throwOnFailure) throws Exception {
		for(String fileName : new File(_tempFolder).list()) {
			if(!new File(_tempFolder, fileName).delete() && throwOnFailure) {
				throw new Exception("couldn't delete: " + fileName);
			}
		}
		if(!new File(_tempFolder).delete() && throwOnFailure) {
			throw new Exception("couldn't delete: " + _tempFolder);
		}
	}
}
