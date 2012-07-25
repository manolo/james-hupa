package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;


@ProxyFor(MailHeader.class)
public interface MailHeader extends ValueProxy{
	String getName();
	String getValue();
}
