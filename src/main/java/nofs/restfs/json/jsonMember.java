package nofs.restfs.json;

import org.antlr.runtime.tree.CommonTree;

public class jsonMember {
	public final String Key;
	public final String Value;
	
	public jsonMember(CommonTree tree) {
		String key = null, value = null;
		for(Object child : tree.getChildren()) {
			CommonTree childTree = (CommonTree)child;
			if(childTree.getToken().getText().toLowerCase().compareTo("string") == 0) {
				value = childTree.getChild(0).getText();
				value = value.substring(1, value.length() - 1);
			} else {
				key = childTree.getText();
				key = key.substring(1, key.length() - 1);
			}			
		}
		Key = key;
		Value = value;
	}
}
