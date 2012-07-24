package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.DeleteMessageResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;

public class DeleteMessageResultImpl implements DeleteMessageResult {

	private User user;
	private ImapFolder folder;
	private int deleteCount;

	public DeleteMessageResultImpl() {
	}

	public DeleteMessageResultImpl(User user, ImapFolder folder, int deleteCount) {
		this.user = user;
		this.folder = folder;
		this.deleteCount = deleteCount;
	}
	@Override
	public int getCount() {
		return deleteCount;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public ImapFolder getFolder() {
		return folder;
	}

}
