package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.CreateFolderAction;
import org.apache.hupa.shared.domain.ImapFolder;

public class CreateFolderActionImpl implements CreateFolderAction{

    private ImapFolder folder;
	public CreateFolderActionImpl() {
    }
	public CreateFolderActionImpl(ImapFolder folder) {
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
