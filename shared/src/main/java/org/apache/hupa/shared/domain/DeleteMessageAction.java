package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ValueProxy;

public interface DeleteMessageAction extends ValueProxy{
	ImapFolder getFolder();
	void setFolder(ImapFolder folder);
}
