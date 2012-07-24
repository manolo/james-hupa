package org.apache.hupa.shared.data;

import java.util.List;

import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
import org.apache.hupa.shared.domain.ImapFolder;

public class DeleteMessageByUidActionImpl extends DeleteMessageActionImpl implements DeleteMessageByUidAction {
	
	public DeleteMessageByUidActionImpl(){}
	public DeleteMessageByUidActionImpl(ImapFolder folder, List<Long> messageUids){
		super(folder);
		this.messageUids = messageUids;
	}

	private List<Long> messageUids;

	@Override
	public List<Long> getMessageUids() {
		return messageUids;
	}

	@Override
	public void setMessageUids(List<Long> messageUids) {
		this.messageUids = messageUids;
	}

}
