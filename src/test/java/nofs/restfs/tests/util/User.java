package nofs.restfs.tests.util;

import org.json.JSONObject;

public class User {

	private String id = null;
	private String name = null;

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Convert this object to a JSON object for representation
	 */
	public JSONObject toJSON() {
		try {
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("id", this.id);
			jsonobj.put("name", this.name);
			return jsonobj;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Convert this object to a string for representation
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id:");
		sb.append(this.id);
		sb.append(",name:");
		sb.append(this.name);
		return sb.toString();
	}
}