package org.apache.hupa.shared.domain;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(DeleteMessageByUidAction.class)
public interface DeleteMessageByUidAction extends DeleteMessageAction {
	List<Long> getMessageUids();
	void setMessageUids(List<Long> messageUids);
}
