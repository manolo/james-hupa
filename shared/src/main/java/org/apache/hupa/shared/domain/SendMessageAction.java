package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(SendMessageAction.class)
public interface SendMessageAction extends ValueProxy {

	SmtpMessage getMessage();
	void setMessage(SmtpMessage message);

	String getReferences();

	String getInReplyTo();

	void setInReplyTo(String inReplyTo);
	void setReferences(String references);

}
