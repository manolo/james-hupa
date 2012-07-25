package org.apache.hupa.shared.domain;


import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(GetMessageDetailsResult.class)
public interface GetMessageDetailsResult extends ValueProxy{

	MessageDetails getMessageDetails();

	void setMessageDetails(MessageDetails messageDetails);

}
