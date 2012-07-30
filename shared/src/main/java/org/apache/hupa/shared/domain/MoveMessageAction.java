package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(MoveMessageAction.class)
public interface MoveMessageAction extends ValueProxy {
	long getMessageUid();
	ImapFolder getOldFolder();
	ImapFolder getNewFolder();

	void setMessageUid(long messageUid);

	void setOldFolder(ImapFolder oldFolder);

	void setNewFolder(ImapFolder newFolder);
}
