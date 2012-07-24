package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(DeleteMessageResult.class)
public interface DeleteMessageResult extends ValueProxy {
	int getCount();
	User getUser();
	ImapFolder getFolder();
}
