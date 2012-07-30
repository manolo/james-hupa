package org.apache.hupa.shared.domain;

import java.util.List;

import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(SetFlagAction.class)
public interface SetFlagAction extends ValueProxy {
	boolean getValue();
	IMAPFlag getFlag();
	List<Long> getUids();
	ImapFolder getFolder();

	void setFlag(IMAPFlag flag);

	void setUids(List<Long> uids);

	void setFolder(ImapFolder folder);

	void setValue(boolean value);
}
