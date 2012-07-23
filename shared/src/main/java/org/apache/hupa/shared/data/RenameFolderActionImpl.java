package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.RenameFolderAction;

public class RenameFolderActionImpl implements RenameFolderAction {

	@Override
	public ImapFolder getFolder() {
		return folder;
	}
	@Override
	public void setFolder(ImapFolder folder) {
		this.folder = folder;
	}
	@Override
	public String getNewName() {
		return newName;
	}
	@Override
	public void setNewName(String newName) {
		this.newName = newName;
	}

	private ImapFolder folder;
	private String newName;

}
