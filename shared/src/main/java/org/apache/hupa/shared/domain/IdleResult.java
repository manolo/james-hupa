package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(IdleResult.class)
public interface IdleResult extends ValueProxy{
	boolean isSupported();
}
