package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(GetMessageDetailsAction.class)
public interface GetMessageDetailsAction extends ValueProxy{

	ImapFolder getFolder();

	void setFolder(ImapFolder folder);

	long getUid();

	void setUid(long uid);

}
