package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.SendMessageService;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.SendMessageAction;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = SendMessageService.class, locator = IocRfServiceLocator.class)
public interface SendMessageRequest extends RequestContext {
	Request<GenericResult> send(SendMessageAction action);
}
