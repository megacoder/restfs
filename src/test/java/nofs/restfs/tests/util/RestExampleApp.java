package nofs.restfs.tests.util;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.routing.Router;

/**
 * Hello world!
 * 
 */
public class RestExampleApp extends Application {
	
	public RestExampleApp() {
		super();
	}

	public RestExampleApp(Context context) {
		super(context);
	}

	@Override
	public Restlet createRoot() {

		Router router = new Router(getContext());
		router.attach("/users", UserResource.class);
		router.attach("/users/{id}", UserResource.class);

		Restlet mainpage = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				StringBuilder stringBuilder = new StringBuilder();

				stringBuilder.append("<html>");
				stringBuilder
						.append("<head><title>Sample Application Servlet Page</title></head>");
				stringBuilder.append("<body bgcolor=white>");

				stringBuilder.append("<table border=\"0\">");
				stringBuilder.append("<tr>");
				stringBuilder.append("<td>");
				stringBuilder.append("<h1>2048Bits.com example - REST</h1>");
				stringBuilder.append("</td>");
				stringBuilder.append("</tr>");
				stringBuilder.append("</table>");
				stringBuilder.append("</body>");
				stringBuilder.append("</html>");

				response.setEntity(new StringRepresentation(stringBuilder
						.toString(), MediaType.TEXT_HTML));

			}
		};
		router.attach("", mainpage);
		return router;
	}
}
