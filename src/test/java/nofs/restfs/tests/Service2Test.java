package nofs.restfs.tests;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import nofs.restfs.tests.util.Item;
import nofs.restfs.tests.util.RestExampleRunner2;

public class Service2Test {
	private RestExampleRunner2 _app;
	
	@Before
	public void Setup() throws Exception {
		_app = new RestExampleRunner2();
		_app.StartRunner();
	}

	@After
	public void TearDown() throws Exception {
		_app.StopRunner();
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void TestService() throws Exception {
		 ClientResource itemsResource = new ClientResource("http://localhost:8100/firstResource/items");  
         ClientResource itemResource = null;  
   
         // Create a new item  
         Item item = new Item("item1", "this is an item.");  
         Representation r = itemsResource.post(getRepresentation(item));  
         if (itemsResource.getStatus().isSuccess()) {  
             itemResource = new ClientResource(r.getIdentifier());  
         }  
   
         if (itemResource != null) {  
             // Prints the representation of the newly created resource.  
             get(itemResource);  
   
             // Prints the list of registered items.  
             get(itemsResource);  
   
             // Update the item  
             item.setDescription("This is an other description");  
             itemResource.put(getRepresentation(item));  
   
             // Prints the list of registered items.  
             get(itemsResource);  
   
             // delete the item  
             itemResource.delete();  
   
             // Print the list of registered items.  
             get(itemsResource);  
         }  
	 }
	
     public static void get(ClientResource clientResource) throws IOException, ResourceException {  
         clientResource.get();  
         if (clientResource.getStatus().isSuccess()  
                 && clientResource.getResponseEntity().isAvailable()) {  
             clientResource.getResponseEntity().write(System.out);  
         }  
     }  
  
     public static Representation getRepresentation(Item item) {  
         // Gathering informations into a Web form.  
         Form form = new Form();  
         form.add("name", item.getName());  
         form.add("description", item.getDescription());  
         return form.getWebRepresentation();  
     }
}
