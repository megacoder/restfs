package nofs.restfs.tests.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;

public class HttpTestClient {
	
	public String WriteHTTP(String host, int port, String req) throws Exception {
    	final String nl = "\r\n";
        final String data = req + nl + "Host: " + host + nl + nl;
        Socket sock = new Socket(host, port);
        OutputStream out = sock.getOutputStream();
        try {
        	WriteData(out, data);
        	InputStream in = sock.getInputStream();
        	DataReader reader = new DataReader(in);
        	reader.start();
        	reader.join(2500);
        	return reader.getData();
        } finally {
        	sock.close();
        }        
    }
    
    private static void WriteData(OutputStream out, String data) throws Exception {
    	for(byte value : data.getBytes()) {
    		out.write(value);
    	}
    	out.flush();
    }
    
    private class DataReader extends Thread {
    	private final InputStream _in;
    	private final StringBuilder _buffer;
    	
    	public DataReader(InputStream in) {
    		_in = in;
    		_buffer = new StringBuilder();
    		super.setDaemon(true);
    	}
    	
    	public String getData() {
    		synchronized(_buffer) {
    			return _buffer.toString();
    		}
    	}
    	
    	@Override
    	public void run() {
    		try {
	    		Reader reader = new InputStreamReader(_in);
	        	LineNumberReader lineReader = new LineNumberReader(reader);
	        	String line;
	    		while((line = lineReader.readLine()) != null) {
	        		synchronized(_buffer) {
	        			_buffer.append(line);
	        			_buffer.append("\r\n");
	        		}
	        	}
    		} catch (IOException ioe) {    			
    		}
    	}
    }
}
