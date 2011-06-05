package nofs.restfs.tests;

import java.nio.ByteBuffer;

import nofs.restfs.tests.util.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fuse.Errno;
import fuse.FuseFtypeConstants;

import nofs.FUSE.Impl.NoFSFuseDriver;
import nofs.Factories.IPersistenceFactory;

import nofs.metadata.AnnotationDriver.NoFSClassLoader;
import nofs.metadata.interfaces.IMetadataFactory;
import nofs.metadata.interfaces.INoFSClassLoader;

import javax.accessibility.AccessibleStateSet;

public class RestFsFuseTest extends BaseFuseTests {
	private TemporaryTestFolder _tmpFolder;
	private NoFSFuseDriver _fs;

	@Before
	public void Setup() throws Exception {
		_tmpFolder = new TemporaryTestFolder();
		_fs = RestFsTestHelper.BuildFS(_tmpFolder);
	}

	@After
	public void TearDown() throws Exception {
		_fs.CleanUp();
		_tmpFolder.CleanUp();
	}

	@Test
	public void TestInitialFS() throws Exception {
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("auth", FuseFtypeConstants.TYPE_DIR | 0755),
            new DirFillerExpect("rules", FuseFtypeConstants.TYPE_DIR | 0755)
		});
	}

    @Test
    public void TestRulesCompilation() throws Exception {
        TestFolderContents(_fs, Fix("/rules"), new DirFillerExpect[] {
		});
        Assert.assertEquals(0, _fs.mknod(Fix("/rules/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
        TestFolderContents(_fs, Fix("/rules"), new DirFillerExpect[] {
            new DirFillerExpect("x", FuseFtypeConstants.TYPE_FILE | 0755),
            new DirFillerExpect("x.ast", FuseFtypeConstants.TYPE_FILE | 0555),
            new DirFillerExpect("x.err", FuseFtypeConstants.TYPE_FILE | 0555),
		});
        final String configText =
                "MKNOD(\"/foo/bar\")\n" +
                "XFORM FROM \"json\" TO \"xml\"\n" +
                "GET FROM \"http://foo/bar\" USING OAUTH TOKEN \"/auth/baz\"";
        final String expectedXML =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n" +
                "<RulesASTFile>\n" +
                  "<Statements>\n" +
                    "<nofs.restfs.query.ast.FSRuleStm>\n" +
                      "<Operations>\n" +
                        "<nofs.restfs.query.ast.ApplyTransformationOperation>\n" +
                          "<FromType>json</FromType>\n" +
                          "<ToType>xml</ToType>\n" +
                        "</nofs.restfs.query.ast.ApplyTransformationOperation>\n" +
                        "<nofs.restfs.query.ast.ApplyWebMethodOperation>\n" +
                          "<Url>http://foo/bar</Url>\n" +
                          "<Token>/auth/baz</Token>\n" +
                          "<Method>get</Method>\n" +
                        "</nofs.restfs.query.ast.ApplyWebMethodOperation>\n" +
                      "</Operations>\n" +
                      "<Action>\n" +
                        "<FileName>/foo/bar</FileName>\n" +
                        "<Action>MKNOD</Action>\n" +
                      "</Action>\n" +
                    "</nofs.restfs.query.ast.FSRuleStm>\n" +
                  "</Statements>\n" +
                "</RulesASTFile>";
        WriteToFile(Fix("/rules/x"), configText);
        final String srcFileContents = ReadFromFile(Fix("/rules/x"));
        Assert.assertEquals(configText, srcFileContents);
        final String astText = ReadFromFile(Fix("/rules/x.ast"));
        final String errText = ReadFromFile(Fix("/rules/x.err"));
        XMLComparison.Compare(expectedXML, astText);
        Assert.assertEquals("", errText);
    }

    @Test
    public void TestRulesCompilation_Error() throws Exception {
        TestFolderContents(_fs, Fix("/rules"), new DirFillerExpect[] {
		});
        Assert.assertEquals(0, _fs.mknod(Fix("/rules/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
        TestFolderContents(_fs, Fix("/rules"), new DirFillerExpect[] {
            new DirFillerExpect("x", FuseFtypeConstants.TYPE_FILE | 0755),
            new DirFillerExpect("x.ast", FuseFtypeConstants.TYPE_FILE | 0555),
            new DirFillerExpect("x.err", FuseFtypeConstants.TYPE_FILE | 0555),
		});
        final String configText = "blah";
        WriteToFile(Fix("/rules/x"), configText);
        final String srcFileContents = ReadFromFile(Fix("/rules/x"));
        Assert.assertEquals(configText, srcFileContents);
        final String astText = ReadFromFile(Fix("/rules/x.ast"));
        final String errText = ReadFromFile(Fix("/rules/x.err"));
        final String expectedXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n" +
                "<RulesASTFile>\n" +
                  "<Statements/>\n" +
                "</RulesASTFile>";
        XMLComparison.Compare(expectedXml, astText);
        final String expectedErr = "No result when parsing failed";
        Assert.assertEquals(expectedErr, errText);
    }
	
	@Test
	public void TestMkdirInAuth() throws Exception {
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("auth", FuseFtypeConstants.TYPE_DIR | 0755),
            new DirFillerExpect("rules", FuseFtypeConstants.TYPE_DIR | 0755)
		});
		TestFolderContents(_fs, Fix("/auth"), new DirFillerExpect[] {
		});
		Assert.assertEquals(0, _fs.mkdir(Fix("/auth/x"), FuseFtypeConstants.TYPE_DIR | 0755));
		TestFolderContents(_fs, Fix("/auth"), new DirFillerExpect[] {
			new DirFillerExpect("x", FuseFtypeConstants.TYPE_DIR | 0755)
		});
	}
	
	@Test
	public void TestMkdirInAuthInstanceFails() throws Exception {
		Assert.assertEquals(0, _fs.mkdir(Fix("/auth/x"), FuseFtypeConstants.TYPE_DIR | 0755));
		TestFolderContents(_fs, Fix("/auth/x"), new DirFillerExpect[] {
			new DirFillerExpect("token", FuseFtypeConstants.TYPE_FILE | 0555),
			new DirFillerExpect("status", FuseFtypeConstants.TYPE_FILE | 0555),
			new DirFillerExpect("config", FuseFtypeConstants.TYPE_FILE | 0755),
			new DirFillerExpect("verifier", FuseFtypeConstants.TYPE_FILE | 0755)
		});
		Assert.assertEquals(Errno.EACCES, _fs.mkdir(Fix("/auth/x/1"), FuseFtypeConstants.TYPE_DIR | 0755));
		TestFolderContents(_fs, Fix("/auth/x"), new DirFillerExpect[] {
			new DirFillerExpect("token", FuseFtypeConstants.TYPE_FILE | 0555),
			new DirFillerExpect("status", FuseFtypeConstants.TYPE_FILE | 0555),
			new DirFillerExpect("config", FuseFtypeConstants.TYPE_FILE | 0755),
			new DirFillerExpect("verifier", FuseFtypeConstants.TYPE_FILE | 0755)
		});
	}
	
	@Test
	public void TestMknodInAuthInstanceFails() throws Exception {
		Assert.assertEquals(0, _fs.mkdir(Fix("/auth/x"), FuseFtypeConstants.TYPE_DIR | 0755));
		TestFolderContents(_fs, Fix("/auth/x"), new DirFillerExpect[] {
			new DirFillerExpect("token", FuseFtypeConstants.TYPE_FILE | 0555),
			new DirFillerExpect("status", FuseFtypeConstants.TYPE_FILE | 0555),
			new DirFillerExpect("config", FuseFtypeConstants.TYPE_FILE | 0755),
			new DirFillerExpect("verifier", FuseFtypeConstants.TYPE_FILE | 0755)
		});
		Assert.assertEquals(Errno.EACCES, _fs.mknod(Fix("/auth/x/1"), FuseFtypeConstants.TYPE_DIR | 0755, 0));
		TestFolderContents(_fs, Fix("/auth/x"), new DirFillerExpect[] {
			new DirFillerExpect("token", FuseFtypeConstants.TYPE_FILE | 0555),
			new DirFillerExpect("status", FuseFtypeConstants.TYPE_FILE | 0555),
			new DirFillerExpect("config", FuseFtypeConstants.TYPE_FILE | 0755),
			new DirFillerExpect("verifier", FuseFtypeConstants.TYPE_FILE | 0755)
		});
	}
	
	@Test
	public void TestMknodInAuth() throws Exception {
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("auth", FuseFtypeConstants.TYPE_DIR | 0755),
            new DirFillerExpect("rules", FuseFtypeConstants.TYPE_DIR | 0755)
		});
		TestFolderContents(_fs, Fix("/auth"), new DirFillerExpect[] {
		});
		Assert.assertEquals(Errno.EACCES, _fs.mknod(Fix("/auth/x"), FuseFtypeConstants.TYPE_DIR | 0755, 0));
		TestFolderContents(_fs, Fix("/auth"), new DirFillerExpect[] {
		});
	}
	
	@Test
	public void TestMkdir() throws Exception {
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("auth", FuseFtypeConstants.TYPE_DIR | 0755),
            new DirFillerExpect("rules", FuseFtypeConstants.TYPE_DIR | 0755)
		});
		Assert.assertEquals(0, _fs.mkdir(Fix("/x"), FuseFtypeConstants.TYPE_DIR | 0755));
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("auth", FuseFtypeConstants.TYPE_DIR | 0755),
			new DirFillerExpect("x", FuseFtypeConstants.TYPE_DIR | 0755),
            new DirFillerExpect("rules", FuseFtypeConstants.TYPE_DIR | 0755)
		});
	}
	
	@Test
	public void TestRmdir() throws Exception {
		Assert.assertEquals(0, _fs.mkdir(Fix("/x"), FuseFtypeConstants.TYPE_DIR | 0755));
		Assert.assertEquals(0, _fs.rmdir(Fix("/x")));
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("auth", FuseFtypeConstants.TYPE_DIR | 0755),
            new DirFillerExpect("rules", FuseFtypeConstants.TYPE_DIR | 0755)
		});
	}
	
	@Test
	public void TestUnlink() throws Exception {
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		Assert.assertEquals(0, _fs.unlink(Fix("/x")));
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("auth", FuseFtypeConstants.TYPE_DIR | 0755),
            new DirFillerExpect("rules", FuseFtypeConstants.TYPE_DIR | 0755)
		});
	}
	
	@Test
	public void TestMknod() throws Exception {
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("auth", FuseFtypeConstants.TYPE_DIR | 0755),
            new DirFillerExpect("rules", FuseFtypeConstants.TYPE_DIR | 0755)
		});
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("auth", FuseFtypeConstants.TYPE_DIR | 0755),
			new DirFillerExpect("x", FuseFtypeConstants.TYPE_FILE | 0755),
			new DirFillerExpect(".x", FuseFtypeConstants.TYPE_FILE | 0755),
            new DirFillerExpect("rules", FuseFtypeConstants.TYPE_DIR | 0755)
		});
	}
	
	@Test
	public void TestMkdirMknod() throws Exception {
		Assert.assertEquals(0, _fs.mkdir(Fix("/x"), FuseFtypeConstants.TYPE_DIR | 0755));
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("auth", FuseFtypeConstants.TYPE_DIR | 0755),
            new DirFillerExpect("rules", FuseFtypeConstants.TYPE_DIR | 0755),
			new DirFillerExpect("x", FuseFtypeConstants.TYPE_DIR | 0755)
		});
		TestFolderContents(_fs, Fix("/x"), new DirFillerExpect[] {});
		Assert.assertEquals(0, _fs.mknod(Fix("/x/y"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		TestFolderContents(_fs, Fix("/x"), new DirFillerExpect[] {
			new DirFillerExpect("y", FuseFtypeConstants.TYPE_FILE | 0755),
			new DirFillerExpect(".y", FuseFtypeConstants.TYPE_FILE | 0755)
		});
	}
	
	@Test
	public void TestMknodThenUTime() throws Exception {
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("auth", FuseFtypeConstants.TYPE_DIR | 0755),
            new DirFillerExpect("rules", FuseFtypeConstants.TYPE_DIR | 0755)
		});
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		Assert.assertEquals(0, _fs.utime(Fix("/x"), (int)System.currentTimeMillis(), (int)System.currentTimeMillis()));
	}
	
	private static void WriteToFile(NoFSFuseDriver fs, String path, MockFuseOpenSetter handle, String value) throws Exception {
		ByteBuffer buffer = WrapInBuffer(value);
		Assert.assertEquals(0, fs.write(path, handle.getFh(), false, buffer, 0));
	}
		
	@Test
	public void TestGetOnUTimeMethod() throws Exception {
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		MockFuseOpenSetter handle = new MockFuseOpenSetter();
		String xml = RestSettingHelper.CreateSettingsXml("UTime","GET", "","~joe/uptime","joekaylor.net", "80", "");
		Assert.assertEquals(0, _fs.open(Fix("/.x"), 0, handle));
		Assert.assertEquals(0, _fs.truncate(Fix("/.x"), 0));
		WriteToFile(_fs, Fix("/.x"), handle, xml);
		Assert.assertEquals(0, _fs.release(Fix("/.x"), handle.getFh(), 0));
		
		int time = (int)System.currentTimeMillis();
		Assert.assertEquals(0, _fs.utime(Fix("/x"), time, time));
		
		handle = new MockFuseOpenSetter();
		ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
		Assert.assertEquals(0, _fs.open(Fix("/x"), 0, handle));
		Assert.assertEquals(0, _fs.read(Fix("/x"), handle.getFh(), buffer, 0));
		Assert.assertEquals(0, _fs.release(Fix("/x"), handle.getFh(), 0));
		String data = ConvertToString(buffer);
		System.out.println(data);
	}
	
	@Test
	public void TestOpenTruncateWriteSettingsFile() throws Exception {
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		MockFuseOpenSetter handle = new MockFuseOpenSetter();
		String xml = RestSettingHelper.CreateSettingsXml("a","b","","c","d", "80", "");
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
	
	private void WriteToFile(String path, String xml) throws Exception {
		MockFuseGetattrSetter attr = new MockFuseGetattrSetter();
		int stat = _fs.getattr(path, attr);
		if(stat == 0) {
			Assert.assertEquals(0, _fs.truncate(path, 0));
		} else {
			Assert.assertEquals(Errno.ENOENT, stat);
		}
		MockFuseOpenSetter handle = new MockFuseOpenSetter();
		Assert.assertEquals(0, _fs.open(Fix(path), 0, handle));
		Assert.assertEquals(0, _fs.truncate(Fix(path), 0));
		ByteBuffer buffer = WrapInBuffer(xml);
		Assert.assertEquals(0, _fs.write(path, handle.getFh(), false, buffer, 0));
		Assert.assertEquals(0, _fs.release(Fix(path), handle.getFh(), 0));
	}
	
	private String ReadFromFile(String path) throws Exception {
		MockFuseOpenSetter handle = new MockFuseOpenSetter();
		ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
		Assert.assertEquals(0, _fs.open(Fix(path), 0, handle));
		Assert.assertEquals(0, _fs.read(Fix(path), handle.getFh(), buffer, 0));
		Assert.assertEquals(0, _fs.release(Fix(path), handle.getFh(), 0));
		return ConvertToString(buffer);
	}
	
	@Test
	public void TestWriteToSettingsFileSmallerThenBigger() throws Exception {
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		
		final String start = "";//"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
		
		String xml = RestSettingHelper.CreateSettingsXml("","","","","","", "");
		
		//Assert.assertEquals(start + xml, ReadFromFile(Fix("/.x")));
        XMLComparison.Compare(start+xml, ReadFromFile(Fix("/.x")));
		
		xml = RestSettingHelper.CreateSettingsXml("aa","bb","cc","dd","ee","16000", "");
		WriteToFile(Fix("/.x"), xml);
		Assert.assertEquals(start + xml, ReadFromFile(Fix("/.x")));
		
		xml = RestSettingHelper.CreateSettingsXml("f","g","h","i","j","6500", "");
		WriteToFile(Fix("/.x"), xml);
		Assert.assertEquals(start + xml, ReadFromFile(Fix("/.x")));
		
		xml = RestSettingHelper.CreateSettingsXml("lll","mmm","nnn","ooo","ppp","6500", "");
		WriteToFile(Fix("/.x"), xml);
		Assert.assertEquals(start + xml, ReadFromFile(Fix("/.x")));
	}
	
	@Test
	public void TestWriteToSettingsFile() throws Exception {
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("auth", FuseFtypeConstants.TYPE_DIR | 0755),
            new DirFillerExpect("rules", FuseFtypeConstants.TYPE_DIR | 0755)
		});
		Assert.assertEquals(0, _fs.mknod(Fix("/x"), FuseFtypeConstants.TYPE_FILE | 0755, 0));
		
		MockFuseOpenSetter handle = new MockFuseOpenSetter();
		
		String xml = RestSettingHelper.CreateSettingsXml("","","","","", "", "");
		
		ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
		Assert.assertEquals(0, _fs.open(Fix("/.x"), 0, handle));
		Assert.assertEquals(0, _fs.read(Fix("/.x"), handle.getFh(), buffer, 0));
		Assert.assertEquals(0, _fs.release(Fix("/.x"), handle.getFh(), 0));
		AssertEquals(xml, buffer);
		
		handle = new MockFuseOpenSetter();
		Assert.assertEquals(0, _fs.truncate(Fix("/.x"),	0));
		Assert.assertEquals(0, _fs.open(Fix("/.x"), 0, handle));
		WriteToFile(_fs, Fix("/.x"), handle, "blah");
		Assert.assertEquals(Errno.EDOM, _fs.release(Fix("/.x"), handle.getFh(), 0));
		
		handle = new MockFuseOpenSetter();
		buffer = ByteBuffer.allocate(1024*1024);
		Assert.assertEquals(0, _fs.open(Fix("/.x"), 0, handle));
		Assert.assertEquals(0, _fs.read(Fix("/.x"), handle.getFh(), buffer, 0));
		Assert.assertEquals(0, _fs.release(Fix("/.x"), handle.getFh(), 0));
		AssertEquals(xml, buffer);
		
		xml = RestSettingHelper.CreateSettingsXml("a","b","","c","d", "80", "");
		handle = new MockFuseOpenSetter();
		Assert.assertEquals(0, _fs.truncate(Fix("/.x"),	0));
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
		TestFolderContents(_fs, Fix("/"), new DirFillerExpect[] {
			new DirFillerExpect("auth", FuseFtypeConstants.TYPE_DIR | 0755),
            new DirFillerExpect("rules", FuseFtypeConstants.TYPE_DIR | 0755)
		});
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
