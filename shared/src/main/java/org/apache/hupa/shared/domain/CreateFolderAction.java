package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(CreateFolderAction.class)
public interface CreateFolderAction extends ValueProxy{
	ImapFolder getFolder();
	void setFolder(ImapFolder folder);
}
