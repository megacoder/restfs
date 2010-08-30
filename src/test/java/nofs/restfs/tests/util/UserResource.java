package nofs.restfs.tests.util;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;

@SuppressWarnings("deprecation")
public class UserResource extends Resource {

	private User user = null;

	public UserResource(Context context, Request request, Response response) {
		super(context, request, response);
		String userid = null;
		userid = (String) getRequest().getAttributes().get("id");
		this.user = findUser(userid);
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
		getVariants().add(new Variant(MediaType.APPLICATION_JSON));
	}

	public boolean allowPropfind() {
		return true;
	}
	
	/**
	 * Allow a PUT http request
	 * 
	 * @return
	 */
	@Override
	public boolean allowPut() {
		return true;
	}

	/**
	 * Allow a POST http request
	 * 
	 * @return
	 */
	@Override
	public boolean allowPost() {
		return true;
	}

	/**
	 * Allow a DELETE http request
	 * 
	 * @return
	 */
	@Override
	public boolean allowDelete() {
		return true;
	}

	/**
	 * Allow the resource to be modified
	 * 
	 * @return
	 */
	public boolean setModifiable() {
		return true;
	}

	/**
	 * Allow the resource to be read
	 * 
	 * @return
	 */
	public boolean setReadable() {
		return true;
	}

	/**
	 * Find the requested user object
	 * 
	 * @param userid
	 * @return
	 */
	private User findUser(String userid) {
		try {
			if (null == userid)
				return null;
			// :TODO {do some database lookup here }
			// user = result of lookup
			// This part should be replaced by a lookup
			User u = new User();
			u.setId("1");
			u.setName("name");
			// end replace
			return u;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Represent the user object in the requested format.
	 * 
	 * @param variant
	 * @return
	 * @throws ResourceException
	 */
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		Representation result = null;
		if (null == this.user) {
			ErrorMessage em = new ErrorMessage();
			return representError(variant, em);
		} else {
			if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
				result = new JsonRepresentation(this.user.toJSON());
			} else {
				result = new StringRepresentation(this.user.toString());
			}
		}
		return result;
	}

	/**
	 * Handle a POST Http request. Create a new user
	 * 
	 * @param entity
	 * @throws ResourceException
	 */
	@Override
	public void acceptRepresentation(Representation entity)
			throws ResourceException {
		// We handle only a form request in this example. Other types could be
		// JSON or XML.
		try {
			if (entity.getMediaType().equals(MediaType.APPLICATION_WWW_FORM, true)) {
				Form form = new Form(entity);
				User u = new User();
				u.setName(form.getFirstValue("user[name]"));
				// :TODO {save the new user to the database}
				getResponse().setStatus(Status.SUCCESS_OK);
				// We are setting the representation in the example always to
				// JSON.
				// You could support multiple representation by using a
				// parameter
				// in the request like "?response_format=xml"
				Representation rep = new JsonRepresentation(u.toJSON());
				getResponse().setEntity(rep);
			} else {
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			}
		} catch (Exception e) {
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
		}
	}

	/**
	 * Handle a PUT Http request. Update an existing user
	 * 
	 * @param entity
	 * @throws ResourceException
	 */
	@Override
	public void storeRepresentation(Representation entity)
			throws ResourceException {
		try {
			if (null == this.user) {
				ErrorMessage em = new ErrorMessage();
				Representation rep = representError(entity.getMediaType(), em);
				getResponse().setEntity(rep);
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return;
			}
			if (entity.getMediaType().equals(MediaType.APPLICATION_WWW_FORM,
					true)) {
				Form form = new Form(entity);
				this.user.setName(form.getFirstValue("user[name]"));
				// :TODO {update the new user in the database}
				getResponse().setStatus(Status.SUCCESS_OK);
				// We are setting the representation in this example always to
				// JSON.
				// You could support multiple representation by using a
				// parameter
				// in the request like "?response_format=xml"
				Representation rep = new JsonRepresentation(this.user.toJSON());
				getResponse().setEntity(rep);
			} else {
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			}
		} catch (Exception e) {
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
		}
	}

	/**
	 * Handle a DELETE Http Request. Delete an existing user
	 * 
	 * @param entity
	 * @throws ResourceException
	 */
	@Override
	public void removeRepresentations() throws ResourceException {
		try {
			if (null == this.user) {
				ErrorMessage em = new ErrorMessage();
				Representation rep = representError(MediaType.APPLICATION_JSON,
						em);
				getResponse().setEntity(rep);
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return;
			}
			// :TODO {delete the user from the database}
			getResponse().setStatus(Status.SUCCESS_OK);
		} catch (Exception e) {
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
		}
	}

	/**
	 * Represent an error message in the requested format.
	 * 
	 * @param variant
	 * @param em
	 * @return
	 * @throws ResourceException
	 */
	private Representation representError(Variant variant, ErrorMessage em)
			throws ResourceException {
		Representation result = null;
		if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
			result = new JsonRepresentation(em.toJSON());
		} else {
			result = new StringRepresentation(em.toString());
		}
		return result;
	}

	protected Representation representError(MediaType type, ErrorMessage em)
			throws ResourceException {
		Representation result = null;
		if (type.equals(MediaType.APPLICATION_JSON)) {
			result = new JsonRepresentation(em.toJSON());
		} else {
			result = new StringRepresentation(em.toString());
		}
		return result;
	}
}