package nofs.restfs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class RestExampleRunner {
	private Process _proc;
	private final String _baseDir;
	private List<String> _jarFiles;
	private CmdDump _cmdDump;

	public RestExampleRunner() throws IOException {
		_baseDir = "src/test/resources";
		_jarFiles = new LinkedList<String>();
		_jarFiles.add(_baseDir + "/" + "org.restlet.ext.json-2.0-M7.jar");
		_jarFiles.add(_baseDir + "/" + "org.restlet-2.0-M7.jar");
		_jarFiles.add(_baseDir + "/" + "rest.example2-0.0.1.jar");
	}

	public void Start() throws Exception {
		System.out.println("cwd: " + new File(".").getCanonicalPath());
		String cmd = "java -classpath ";
		boolean first = true;
		for (String jarFile : _jarFiles) {
			if (!new File(jarFile).exists()) {
				throw new Exception("JAR file " + jarFile + " does not exist");
			}
			if (first) {
				first = false;
			} else {
				cmd += ":";
			}
			cmd += jarFile;
		}
		cmd += " rest.example2.App";
		System.out.println("running command: " + cmd);
		_proc = Runtime.getRuntime().exec(cmd);
		_cmdDump = new CmdDump(_proc);
		_cmdDump.start();
	}

	private class CmdDump extends Thread {
		private Process _proc;

		public CmdDump(Process proc) {
			_proc = proc;
		}

		@Override
		public void run() {
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(_proc.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(_proc.getErrorStream()));
			String s;
			
			// read any errors from the attempted command
			System.out.println("Here is the standard error of the command (if any):\n");
			try {
				while ((s = stdError.readLine()) != null) {
					System.out.println(s);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			try {
				while ((s = stdInput.readLine()) != null) {
					System.out.println(s);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void Stop() throws InterruptedException {
		_proc.destroy();
		_cmdDump.join();
	}
}
