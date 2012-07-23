package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(DeleteFolderAction.class)
public interface DeleteFolderAction extends ValueProxy{
	ImapFolder getFolder();
	void setFolder(ImapFolder folder);
}
