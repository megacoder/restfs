package nofs.restfs.tests;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import junit.framework.Assert;

import nofs.restfs.http.StreamReaderHelper;
import nofs.restfs.tests.util.TemporaryTestFolder;

import org.junit.Test;

public class StreamReaderHelperTest {

	@Test
	public void TestRead() throws Exception {
		TemporaryTestFolder folder = new TemporaryTestFolder();
		try {
			byte[] data1 = new byte[1060];
			for(int i = 0; i < data1.length; i++) {
				data1[i] = (byte)(i % Byte.MAX_VALUE);
			}
			FileOutputStream out = new FileOutputStream(folder.getPath("foo.txt"));
			try {
				out.write(data1);
			} finally {
				out.close();
			}
			FileInputStream in = new FileInputStream(folder.getPath("foo.txt"));
			byte[] data2;
			try {
				data2 = StreamReaderHelper.ReadStreamCompletely(in);
			} finally {
				in.close();
			}
			Assert.assertEquals(data1.length, data2.length);
			for(int i = 0; i < data1.length; i++) {
				Assert.assertEquals("data1[" + i + "] = " + data1[i] + " != data2[" + i + "] = " + data2[i], 
						data1[i], data2[i]);
			}
		} finally {
			folder.CleanUp(false);
		}
	}
}
