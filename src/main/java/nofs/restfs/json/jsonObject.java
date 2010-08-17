package nofs.restfs.json;

import java.util.ArrayList;
import java.util.List;
import nofs.restfs.json.jsonParser.object_return;
import org.antlr.runtime.tree.CommonTree;

public class jsonObject {

	public List<jsonMember> Members = new ArrayList<jsonMember>();
	
	public jsonObject(object_return obj) throws Exception {
		CommonTree tree = (CommonTree)obj.getTree();
		if(tree.token.getText().compareTo("OBJECT") == 0) {
			for(Object child : tree.getChildren()) {
				CommonTree childTree = (CommonTree)child;
				if(childTree.getToken().getText().compareTo("FIELD") == 0) {
					Members.add(new jsonMember(childTree));
				} else {
					throw new Exception("Expected tree of type field, but was " + childTree.getToken().getText());
				}
			}
		} else {
			throw new Exception("Tree passed to jsonObject was not of type OBJECT, but " + tree.token.getText());
		}
	}
}
