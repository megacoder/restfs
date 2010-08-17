package nofs.restfs.tests;

import nofs.restfs.http.GetAnswer;
import nofs.restfs.http.WebDavFacade;

import org.junit.Assert;
import org.junit.Test;

public class WebDavFacadeTest {

	@Test
	public void TestGetMethod() throws Exception {
		GetAnswer answer = WebDavFacade.Instance().GetMethod("joekaylor.net", "80", "/~joe/uptime");
		Assert.assertTrue(0 != answer.getData().length);
	}
}
