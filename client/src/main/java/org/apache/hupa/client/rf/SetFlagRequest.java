package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.SetFlagService;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.SetFlagAction;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = SetFlagService.class, locator = IocRfServiceLocator.class)
public interface SetFlagRequest extends RequestContext {
	Request<GenericResult> set(SetFlagAction action);
}
