package org.apache.hupa.client.ui;

import org.apache.hupa.shared.domain.ImapFolder;

public class LabelNode implements Comparable<LabelNode> {
	private ImapFolder folder;
	private String name;
	private String path;
	private LabelNode parent;

	public ImapFolder getFolder() {
		return folder;
	}
	public void setFolder(ImapFolder folder) {
		this.folder = folder;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public LabelNode getParent() {
		return parent;
	}
	public void setParent(LabelNode parent) {
		this.parent = parent;
	}
	@Override
	public int compareTo(LabelNode arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
