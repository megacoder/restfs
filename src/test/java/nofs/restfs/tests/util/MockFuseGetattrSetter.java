package nofs.restfs.tests.util;

import fuse.FuseGetattrSetter;

public class MockFuseGetattrSetter implements FuseGetattrSetter {
	public long inode;
	public int mode, nlink, uid, gid, rdev;
	public long size, blocks;
	public int atime, mtime, ctime;

	//@Override
	public void set(long inode, int mode, int nlink, int uid, int gid, int rdev, long size, long blocks, int atime, int mtime, int ctime) {
		this.inode = inode;
		this.mode = mode;
		this.nlink = nlink;
		this.uid = uid;
		this.gid = gid;
		this.rdev = rdev;
		this.size = size;
		this.blocks = blocks;
		this.atime = atime;
		this.mtime = mtime;
		this.ctime = ctime;
	}
}
