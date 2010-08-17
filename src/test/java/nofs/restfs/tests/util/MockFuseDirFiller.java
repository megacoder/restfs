package nofs.restfs.tests.util;

import java.util.LinkedList;

import fuse.FuseDirFiller;

public class MockFuseDirFiller implements FuseDirFiller
{
	public LinkedList<String> items = new LinkedList<String>();
	public LinkedList<Integer> modes = new LinkedList<Integer>();
	public LinkedList<Integer> uids = new LinkedList<Integer>();
	public LinkedList<Integer> gids = new LinkedList<Integer>();

	//@Override
	public void add(String name, long inode, int mode) {
		items.add(name);
		modes.add(mode);
	}

	public String Dump() {
		String value = "";
		for(String item : items) {
			if(value.length() > 0) {
				value += ", ";
			}
			value += item;
		}
		return "(" + value + ")";
	}
}
