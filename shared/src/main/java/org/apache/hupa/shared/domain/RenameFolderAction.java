package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(RenameFolderAction.class)
public interface RenameFolderAction extends ValueProxy{
	ImapFolder getFolder();
	String getNewName();
	void setFolder(ImapFolder folder);
	void setNewName(String newName);
}
