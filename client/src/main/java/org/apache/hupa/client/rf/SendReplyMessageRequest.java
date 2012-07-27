package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.SendReplyMessageService;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.SendReplyMessageAction;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = SendReplyMessageService.class, locator = IocRfServiceLocator.class)
public interface SendReplyMessageRequest extends RequestContext {
	Request<GenericResult> send(SendReplyMessageAction action);
}
