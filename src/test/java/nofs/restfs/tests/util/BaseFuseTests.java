package nofs.restfs.tests.util;

import java.io.File;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import nofs.Domain.IMethodParameter;
import nofs.Domain.Impl.MethodInvocation;
import nofs.Domain.Impl.MethodParameter;
import nofs.FUSE.INoFSFuseDriver;

import org.junit.Assert;

import fuse.FuseException;

public class BaseFuseTests {
	
	protected static String Fix(String path) {
		if(path.contains("/")) {
			return path.replace('/', File.separatorChar);			
		}
		return path;
	}
	
	protected static int Exec(String pwd, String path, String methodName, String[] parameters, String[] types) throws Exception {
		List<IMethodParameter> methodParameters = new LinkedList<IMethodParameter>();
		for(int i = 0; i < parameters.length; i++) {
			methodParameters.add(new MethodParameter(parameters[i], types[i], i));
		}
		MethodInvocation mInvoke = new MethodInvocation(methodName, pwd, path, methodParameters);
		Socket sock = new Socket("127.0.0.1", 5050);
		int returnValue = (-1);
		try {
			OutputStream out = sock.getOutputStream();
			out.write(mInvoke.ToXML().getBytes());
			out.flush();
			returnValue = sock.getInputStream().read();
		} finally {
			sock.close();
		}
		return returnValue;
	}

	protected static void TestFolderContents(INoFSFuseDriver fs, String path, DirFillerExpect[] expect) throws FuseException {
		MockFuseDirFiller dirFiller = new MockFuseDirFiller();
		int errno = fs.getdir(path, dirFiller);
		Assert.assertEquals(0, errno);
		Compare(dirFiller, expect);
	}

	protected static String ConvertToString(ByteBuffer buffer) {
		StringBuffer buff = new StringBuffer();
		byte[] data = buffer.array();
		for(int i = 0 ; i < buffer.position(); i++) {
			buff.append((char)data[i]);
		}
		return buff.toString();
	}

	protected static ByteBuffer WrapInBuffer(String value) {
		ByteBuffer buffer = ByteBuffer.allocate(value.length());
		for(byte charValue : value.getBytes()) {
			buffer.put(charValue);
		}
		buffer.position(0);
		return buffer;
	}

	protected static void AssertEquals(String expectedValue, ByteBuffer buffer) throws Exception {
		String actualValue = ConvertToString(buffer);
		XMLComparison.Compare(expectedValue, actualValue);
	}

	protected static void Compare(MockFuseDirFiller dir, DirFillerExpect[] expect) {
		LinkedList<DirFillerExpect> expList = new LinkedList<DirFillerExpect>();
		for(int i = 0; i < expect.length; i++) {
			expList.add(expect[i]);
		}
		Compare(dir, expList);
	}

	protected static String DumpExpect(Collection<DirFillerExpect> expect) {
		String value = "";
		for(DirFillerExpect exp : expect) {
			if(value.length() > 0) {
				value += ", ";
			}
			value += exp.name;
		}
		return "(" + value + ")";
	}

	protected static void Compare(MockFuseDirFiller dir, Collection<DirFillerExpect> expect) {
		Assert.assertEquals(DumpExpect(expect) + ", " + dir.Dump(), expect.size(), dir.items.size());
		List<DirFillerExpect> expectations = new LinkedList<DirFillerExpect>(expect);
		for(int i = 0; i < dir.items.size(); i++) {
			for(DirFillerExpect exp : expectations) {
				if(exp.name.compareTo(dir.items.get(i)) == 0) {
					Assert.assertEquals("mode mismatch for " + exp.name, exp.mode, dir.modes.get(i));
					expectations.remove(exp);
					break;
				}
			}
		}
		Assert.assertEquals(DumpExpect(expectations) + ", " + dir.Dump(), 0, expectations.size());
	}

	protected static void AssertFileExists(String fileName) {
		Assert.assertTrue(fileName, new File(fileName).exists());
	}
}
