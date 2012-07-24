package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.DeleteMessageByUidService;
import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
import org.apache.hupa.shared.domain.DeleteMessageResult;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = DeleteMessageByUidService.class, locator = IocRfServiceLocator.class)
public interface DeleteMessageByUidRequest extends RequestContext {
	Request<DeleteMessageResult> delete(DeleteMessageByUidAction action);
}
