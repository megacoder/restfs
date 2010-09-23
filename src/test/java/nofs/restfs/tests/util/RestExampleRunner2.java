package nofs.restfs.tests.util;

import java.io.IOException;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.routing.VirtualHost;

public class RestExampleRunner2 /*extends Thread*/ {
	private Component _component;
	private volatile boolean _started;

	public RestExampleRunner2() throws IOException {
		_started = false;
		_component = new Component();
		_component.getServers().add(Protocol.HTTP, 8100);

		//RestExampleApp application = new RestExampleApp(_component.getContext());
		FirstResourceApplication application = new FirstResourceApplication(/*_component.getContext()*/); 

		// Attach the application to the component and start it
		VirtualHost host = _component.getDefaultHost();
		host.attach("/firstResource", application);
	}
	
	public boolean Started() {
		return _started;
	}
	
	public void StartRunner() throws Exception {
		try {
			_component.start();
		} finally {
			_started = true;
		}
	}
	
	/*@Override
	@Ignore
	public void run() {
		try {
			_component.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			_started = true;
		}
	}*/
	
	public void StopRunner() throws Exception {
		_component.stop();
		//join();
	}
}
