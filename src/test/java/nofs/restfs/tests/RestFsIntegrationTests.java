package nofs.restfs.tests;

import nofs.restfs.tests.util.RestExampleRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RestFsIntegrationTests {
	private RestExampleRunner _restRunner;
	
	@Before
	public void Setup() throws Exception {
		_restRunner = new RestExampleRunner();
		_restRunner.StartRunner();
	}
	
	@After
	public void TearDown() throws Exception {
		_restRunner.StopRunner();
	}
	
	@Test
	public void Test1() throws Exception {
		Thread.sleep(15000);
	}
}
