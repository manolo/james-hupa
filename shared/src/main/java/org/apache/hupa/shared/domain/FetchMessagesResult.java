package org.apache.hupa.shared.domain;

import java.util.List;


import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(FetchMessagesResult.class)
public interface FetchMessagesResult extends ValueProxy{
	int getOffset();
	int getStart();
	List<Message> getMessages();
	int getRealCount();
	int getRealUnreadCount();
}
