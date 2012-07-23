package org.apache.hupa.shared.domain;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(GenericResult.class)
public interface GenericResult extends ValueProxy{
	String getMessage();
	boolean isSuccess();
	void setMessage(String message);
	void setSuccess(boolean success);
	void setError(String message);
}
