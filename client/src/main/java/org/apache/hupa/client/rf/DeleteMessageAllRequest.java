package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.DeleteMessageAllService;
import org.apache.hupa.shared.domain.DeleteMessageAllAction;
import org.apache.hupa.shared.domain.DeleteMessageResult;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = DeleteMessageAllService.class, locator = IocRfServiceLocator.class)
public interface DeleteMessageAllRequest  extends RequestContext {
	Request<DeleteMessageResult> delete(DeleteMessageAllAction action);
}
