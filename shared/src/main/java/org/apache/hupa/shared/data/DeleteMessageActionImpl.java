package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.DeleteMessageAction;
import org.apache.hupa.shared.domain.ImapFolder;

public class DeleteMessageActionImpl implements DeleteMessageAction {

	protected ImapFolder folder;

	public DeleteMessageActionImpl() {
	}
	public DeleteMessageActionImpl(ImapFolder folder) {
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
