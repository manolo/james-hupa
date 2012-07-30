package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.IdleService;
import org.apache.hupa.shared.domain.IdleAction;
import org.apache.hupa.shared.domain.IdleResult;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = IdleService.class, locator = IocRfServiceLocator.class)
public interface IdleRequest  extends RequestContext{
	Request<IdleResult> idle(IdleAction action);
}
