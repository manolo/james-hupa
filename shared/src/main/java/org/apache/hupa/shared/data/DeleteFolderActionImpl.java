package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.DeleteFolderAction;
import org.apache.hupa.shared.domain.ImapFolder;

public class DeleteFolderActionImpl implements DeleteFolderAction{

    private ImapFolder folder;
	public DeleteFolderActionImpl() {
    }
	public DeleteFolderActionImpl(ImapFolder folder) {
	    this.folder = folder;
    }
	@Override
    public ImapFolder getFolder() {
	    return folder;
    }
	@Override
	public void setFolder(ImapFolder folder) {
    	this.folder = folder;
    }
}
