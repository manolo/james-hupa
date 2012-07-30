package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(GetMessageRawResult.class)
public interface GetMessageRawResult extends ValueProxy{
	String getRawMessage();
}
