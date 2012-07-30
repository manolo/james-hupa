package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(LogoutUserResult.class)
public interface LogoutUserResult extends ValueProxy{
	User getUser();
}
