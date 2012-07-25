package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.ImapFolder;

public class GetMessageDetailsActionImpl implements GetMessageDetailsAction {
	public GetMessageDetailsActionImpl() {
		super();
	}
	public GetMessageDetailsActionImpl(ImapFolder folder, long uid) {
		super();
		this.folder = folder;
		this.uid = uid;
	}

	private ImapFolder folder;
	private long uid;

	@Override
	public ImapFolder getFolder() {
		return folder;
	}
	@Override
	public void setFolder(ImapFolder folder) {
		this.folder = folder;
	}
	@Override
	public long getUid() {
		return uid;
	}
	@Override
	public void setUid(long uid) {
		this.uid = uid;
	}
}
