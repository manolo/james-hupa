package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.CheckSessionService;
import org.apache.hupa.shared.domain.User;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = CheckSessionService.class, locator = IocRfServiceLocator.class)
public interface CheckSessionRequest extends RequestContext {
	Request<User> getUser();
	Request<Boolean> isValid();
}
