package nofs.restfs.tests;

import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fuse.FuseFtypeConstants;

import nofs.FUSE.Impl.NoFSFuseDriver;
import nofs.Factories.IPersistenceFactory;
import nofs.restfs.tests.util.MockFuseOpenSetter;

import nofs.metadata.AnnotationDriver.NoFSClassLoader;
import nofs.metadata.interfaces.IMetadataFactory;
import nofs.metadata.interfaces.INoFSClassLoader;
import nofs.restfs.tests.util.BaseFuseTests;
import nofs.restfs.tests.util.DirFillerExpect;
import nofs.restfs.tests.util.TemporaryTestFolder;

public class RestFsFuseTest extends BaseFuseTests {
	private TemporaryTestFolder _tmpFolder;
	private NoFSFuseDriver _fs;

	@Before
	public void Setup() throws Exception {
		_tmpFolder = new TemporaryTestFolder();
		_fs = BuildFS();
	}

	@After
	public void TearDown() throws Exception {
		_fs.CleanUp();
		_tmpFolder.CleanUp();
	}
	
	private NoFSFuseDriver BuildFS() throws Exception {
		final String objectStore = _tmpFolder.getPath("fs.db");
		final String metaFile = _tmpFolder.getPath("meta.db");
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
	
	@Test
	public void TestInitialFS() throws Exception {
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {});
	}
	
	@Test
	public void TestMkdir() throws Exception {
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {});
		Assert.assertEquals(0, _fs.mkdir(Fix("/x"), FuseFtypeConstants.TYPE_DIR | 0755));
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("x", FuseFtypeConstants.TYPE_DIR | 0755)
		});
	}
	
	@Test
	public void TestMknod() throws Exception {
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {});
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("x", FuseFtypeConstants.TYPE_FILE | 0755)
		});
	}
	
	@Test
	public void TestMknodThenUTime() throws Exception {
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {});
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		Assert.assertEquals(0, _fs.utime(Fix("/x"), (int)System.currentTimeMillis(), (int)System.currentTimeMillis()));
	}
	
	private static void WriteToFile(NoFSFuseDriver fs, String path, MockFuseOpenSetter handle, String value) throws Exception {
		ByteBuffer buffer = WrapInBuffer(value);
		Assert.assertEquals(0, fs.write(path, handle.getFh(), false, buffer, 0));
	}
	
	@Test
	public void TestWriteToFile() throws Exception {
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {});
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		MockFuseOpenSetter handle = new MockFuseOpenSetter();
		Assert.assertEquals(0, _fs.open(Fix("/x"), 0, handle));
		WriteToFile(_fs, Fix("/x"), handle, "blah");
		Assert.assertEquals(0, _fs.release(Fix("/x"), handle.getFh(), 0));
		
		ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
		Assert.assertEquals(0, _fs.open(Fix("/x"), 0, handle));
		Assert.assertEquals(0, _fs.read(Fix("/x"), handle.getFh(), buffer, 0));
		Assert.assertEquals(0, _fs.release(Fix("/x"), handle.getFh(), 0));
		AssertEqualsRaw("blah", buffer);
	}
}
