package nofs.restfs.tests;

import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fuse.Errno;
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
			new DirFillerExpect("x", FuseFtypeConstants.TYPE_FILE | 0755),
			new DirFillerExpect(".x", FuseFtypeConstants.TYPE_FILE | 0755)
		});
	}
	
	@Test
	public void TestMkdirMknod() throws Exception {
		Assert.assertEquals(0, _fs.mkdir(Fix("/x"), FuseFtypeConstants.TYPE_DIR | 0755));
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("x", FuseFtypeConstants.TYPE_DIR | 0755)
		});
		TestFolderContents(_fs, Fix("/x"), new DirFillerExpect[] {});
		Assert.assertEquals(0, _fs.mknod(Fix("/x/y"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		TestFolderContents(_fs, Fix("/x"), new DirFillerExpect[] {
			new DirFillerExpect("y", FuseFtypeConstants.TYPE_DIR | 0755),
			new DirFillerExpect(".y", FuseFtypeConstants.TYPE_DIR | 0755)
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
	
	private static String CreateSettingsXml(String fsMethod, String webMethod, String resource, String host) {
		return 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<RestfulSetting>\n  <FsMethod>" + fsMethod + "</FsMethod>\n" + 
			"  <WebMethod>" + webMethod + "</WebMethod>\n" + 
			"  <Resource>" + resource + "</Resource>\n" + 
			"  <Host>" + host + "</Host>\n</RestfulSetting>\n";
	}
	
	@Test
	public void TestOpenTruncateWriteSettingsFile() throws Exception {
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		MockFuseOpenSetter handle = new MockFuseOpenSetter();
		String xml = CreateSettingsXml("a","b","c","d");
		Assert.assertEquals(0, _fs.open(Fix("/.x"), 0, handle));
		Assert.assertEquals(0, _fs.truncate(Fix("/.x"), 0));
		WriteToFile(_fs, Fix("/.x"), handle, xml);
		Assert.assertEquals(0, _fs.release(Fix("/.x"), handle.getFh(), 0));
		handle = new MockFuseOpenSetter();
		ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
		Assert.assertEquals(0, _fs.open(Fix("/.x"), 0, handle));
		Assert.assertEquals(0, _fs.read(Fix("/.x"), handle.getFh(), buffer, 0));
		Assert.assertEquals(0, _fs.release(Fix("/.x"), handle.getFh(), 0));
		AssertEquals(xml, buffer);
	}
	
	@Test
	public void TestWriteToSettingsFile() throws Exception {
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {});
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		
		MockFuseOpenSetter handle = new MockFuseOpenSetter();
		
		String xml = CreateSettingsXml("","","","");
		
		ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
		Assert.assertEquals(0, _fs.open(Fix("/.x"), 0, handle));
		Assert.assertEquals(0, _fs.read(Fix("/.x"), handle.getFh(), buffer, 0));
		Assert.assertEquals(0, _fs.release(Fix("/.x"), handle.getFh(), 0));
		AssertEquals(xml, buffer);
		
		handle = new MockFuseOpenSetter();
		Assert.assertEquals(0, _fs.open(Fix("/.x"), 0, handle));
		WriteToFile(_fs, Fix("/.x"), handle, "blah");
		Assert.assertEquals(Errno.EDOM, _fs.release(Fix("/.x"), handle.getFh(), 0));
		
		handle = new MockFuseOpenSetter();
		buffer = ByteBuffer.allocate(1024*1024);
		Assert.assertEquals(0, _fs.open(Fix("/.x"), 0, handle));
		Assert.assertEquals(0, _fs.read(Fix("/.x"), handle.getFh(), buffer, 0));
		Assert.assertEquals(0, _fs.release(Fix("/.x"), handle.getFh(), 0));
		AssertEquals(xml, buffer);
		
		xml = CreateSettingsXml("a","b","c","d");
		handle = new MockFuseOpenSetter();
		Assert.assertEquals(0, _fs.open(Fix("/.x"), 0, handle));
		WriteToFile(_fs, Fix("/.x"), handle, xml);
		Assert.assertEquals(0, _fs.release(Fix("/.x"), handle.getFh(), 0));
		
		handle = new MockFuseOpenSetter();
		buffer = ByteBuffer.allocate(1024*1024);
		Assert.assertEquals(0, _fs.open(Fix("/.x"), 0, handle));
		Assert.assertEquals(0, _fs.read(Fix("/.x"), handle.getFh(), buffer, 0));
		Assert.assertEquals(0, _fs.release(Fix("/.x"), handle.getFh(), 0));
		AssertEquals(xml, buffer);
	}
	
	@Test
	public void TestWriteToFile() throws Exception {
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {});
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		
		MockFuseOpenSetter handle = new MockFuseOpenSetter();
		
		ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
		Assert.assertEquals(0, _fs.open(Fix("/x"), 0, handle));
		Assert.assertEquals(0, _fs.read(Fix("/x"), handle.getFh(), buffer, 0));
		Assert.assertEquals(0, _fs.release(Fix("/x"), handle.getFh(), 0));
		AssertEqualsRaw("", buffer);
		
		Assert.assertEquals(0, _fs.open(Fix("/x"), 0, handle));
		WriteToFile(_fs, Fix("/x"), handle, "blah");
		Assert.assertEquals(0, _fs.release(Fix("/x"), handle.getFh(), 0));
		
		buffer = ByteBuffer.allocate(1024*1024);
		Assert.assertEquals(0, _fs.open(Fix("/x"), 0, handle));
		Assert.assertEquals(0, _fs.read(Fix("/x"), handle.getFh(), buffer, 0));
		Assert.assertEquals(0, _fs.release(Fix("/x"), handle.getFh(), 0));
		AssertEqualsRaw("blah", buffer);
	}
}
