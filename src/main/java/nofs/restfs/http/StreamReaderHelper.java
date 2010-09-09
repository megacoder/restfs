package nofs.restfs.http;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StreamReaderHelper {

	public static byte[] ReadStreamCompletely(InputStream in) throws Exception {
		List<byte[]> chunks = new ArrayList<byte[]>();
		int len;
		for(byte[] chunk = new byte[512]; 0 < (len = in.read(chunk)); chunk = new byte[512]) {
			if(len == chunk.length) {
				chunks.add(chunk);
			} else {
				byte[] subChunk = new byte[len];
				System.arraycopy(chunk, 0, subChunk, 0, len);
				chunks.add(subChunk);
			}
		}
		int totalLen = 0;
		for(byte[] chunk : chunks) {
			totalLen += chunk.length;
		}
		byte[] representation = new byte[totalLen];
		int offset = 0;
		for(byte[] chunk : chunks) {
			System.arraycopy(chunk, 0, representation, offset, chunk.length);
			offset += chunk.length;
		}
		return representation;
	}
}
