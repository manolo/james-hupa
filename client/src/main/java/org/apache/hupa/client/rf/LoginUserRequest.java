package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.LoginUserService;
import org.apache.hupa.shared.domain.User;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = LoginUserService.class, locator = IocRfServiceLocator.class)
public interface LoginUserRequest extends RequestContext {
	Request<User> login(String username, String password);
}
