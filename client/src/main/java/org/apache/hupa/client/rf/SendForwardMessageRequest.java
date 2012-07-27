package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.SendForwardMessageService;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.SendForwardMessageAction;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = SendForwardMessageService.class, locator = IocRfServiceLocator.class)
public interface SendForwardMessageRequest extends RequestContext {
	Request<GenericResult> send(SendForwardMessageAction action);
}
