package nofs.restfs.tests.util;

import java.nio.ByteBuffer;

import nofs.FUSE.Impl.NoFSFuseDriver;
import nofs.Factories.IPersistenceFactory;
import nofs.metadata.AnnotationDriver.NoFSClassLoader;
import nofs.metadata.interfaces.IMetadataFactory;
import nofs.metadata.interfaces.INoFSClassLoader;

import org.junit.Assert;

public class RestFsTestHelper extends BaseFuseTests {
	public static void WriteToFile(NoFSFuseDriver fs, String path, String xml) throws Exception {
		MockFuseOpenSetter handle = new MockFuseOpenSetter();
		Assert.assertEquals(0, fs.open(Fix(path), 0, handle));
		Assert.assertEquals(0, fs.truncate(Fix(path), 0));
		ByteBuffer buffer = WrapInBuffer(xml);
		Assert.assertEquals(0, fs.write(path, handle.getFh(), false, buffer, 0));
		Assert.assertEquals(0, fs.release(Fix(path), handle.getFh(), 0));
	}
	
	public static String ReadFromFile(NoFSFuseDriver fs, String path) throws Exception {
		MockFuseOpenSetter handle = new MockFuseOpenSetter();
		ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
		Assert.assertEquals(0, fs.open(Fix(path), 0, handle));
		Assert.assertEquals(0, fs.read(Fix(path), handle.getFh(), buffer, 0));
		Assert.assertEquals(0, fs.release(Fix(path), handle.getFh(), 0));
		return ConvertToString(buffer);
	}
	
	public static NoFSFuseDriver BuildFS(TemporaryTestFolder tmpFolder) throws Exception {
		final String objectStore = tmpFolder.getPath("fs.db");
		final String metaFile = tmpFolder.getPath("meta.db");
		final String metaDataDriver = "nofs.metadata.AnnotationDriver.Factory";
		final String persistenceDriver = "nofs.Factories.Db4oPersistenceFactory";
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		INoFSClassLoader nofsLoader = new NoFSClassLoader(ClassLoader.getSystemClassLoader());
		IMetadataFactory metadataFactory = (IMetadataFactory)loader.loadClass(metaDataDriver).newInstance();
		IPersistenceFactory persistenceFactory = (IPersistenceFactory)loader.loadClass(persistenceDriver).newInstance();
		NoFSFuseDriver fs = new NoFSFuseDriver(nofsLoader, objectStore, metaFile, metadataFactory, persistenceFactory);
		fs.Init();
		return fs;
	}
}
