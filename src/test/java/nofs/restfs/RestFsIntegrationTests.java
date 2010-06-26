package nofs.restfs;

import nofs.restfs.util.RestExampleRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RestFsIntegrationTests {
	private RestExampleRunner _restRunner;
	
	@Before
	public void Setup() throws Exception {
		_restRunner = new RestExampleRunner();
		_restRunner.Start();
	}
	
	@After
	public void TearDown() throws Exception {
		_restRunner.Stop();
	}
	
	@Test
	public void Test1() throws Exception {
		Thread.sleep(15000);
	}
}
