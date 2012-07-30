package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(GetMessageRawAction.class)
public interface GetMessageRawAction extends ValueProxy{
	ImapFolder getFolder();
	long getUid();
}
