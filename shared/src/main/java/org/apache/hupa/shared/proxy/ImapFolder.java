package org.apache.hupa.shared.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

@ProxyFor(value=ImapFolder.class, locator=ImapFolderLocator.class)
public interface ImapFolder extends AbstractEntityProxy {

	int getUnseenMessageCount();

	String getName();

	String getFullName();

	void setFullName(String oldFullName);

	List<ImapFolder> getChildren();
	void setChildren(List<ImapFolder> children);

	void setUnseenMessageCount(int count);

	void setMessageCount(int realCount);

	String getDelimiter();

	void setDelimiter(String delimiter);

	void setSubscribed(boolean subscribed);
	
}