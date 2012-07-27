package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(SendForwardMessageAction.class)
public interface SendForwardMessageAction extends SendMessageAction{

	ImapFolder getFolder();

	long getReplyMessageUid();

	void setFolder(ImapFolder folder);

	void setUid(long uid);
	

}
