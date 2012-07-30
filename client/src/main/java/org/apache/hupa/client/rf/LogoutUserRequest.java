package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.LogoutUserService;
import org.apache.hupa.shared.domain.LogoutUserResult;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;


@Service(value = LogoutUserService.class, locator = IocRfServiceLocator.class)
public interface LogoutUserRequest extends RequestContext{
	Request<LogoutUserResult> logout();
}
