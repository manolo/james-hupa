package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(FetchMessagesAction.class)
public interface FetchMessagesAction extends ValueProxy {
	ImapFolder getFolder();
	int getStart();
	int getOffset();
	String getSearchString();
	void setFolder(ImapFolder folder);
	void setStart(int start);
	void setOffset(int offset);
	void setSearchString(String searchString);
}
