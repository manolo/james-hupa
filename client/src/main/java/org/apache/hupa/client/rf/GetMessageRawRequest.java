package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.GetMessageRawService;
import org.apache.hupa.shared.domain.GetMessageRawAction;
import org.apache.hupa.shared.domain.GetMessageRawResult;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = GetMessageRawService.class, locator = IocRfServiceLocator.class)
public interface GetMessageRawRequest {
	Request<GetMessageRawResult> get(GetMessageRawAction action);
}
