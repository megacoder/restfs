package nofs.restfs.tests.util;

import java.io.IOException;
import org.restlet.Component;
import org.restlet.data.Protocol;

public class RestExampleRunner extends Thread {
	private Component _component;

	public RestExampleRunner() throws IOException {
		_component = new Component();
		_component.getServers().add(Protocol.HTTP, 8100);

		RestExampleApp application = new RestExampleApp(_component.getContext());

		// Attach the application to the component and start it
		_component.getDefaultHost().attach(application);
	}
	
	public void StartRunner() throws Exception {
		start();
	}
	
	@Override
	public void run() {
		try {
			_component.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void StopRunner() throws Exception {
		_component.stop();
		join();
	}
}
