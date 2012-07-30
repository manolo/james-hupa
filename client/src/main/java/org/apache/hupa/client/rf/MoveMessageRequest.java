package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.MoveMessageService;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.MoveMessageAction;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = MoveMessageService.class, locator = IocRfServiceLocator.class)
public interface MoveMessageRequest extends RequestContext {
	Request<GenericResult> move(MoveMessageAction action);
}
