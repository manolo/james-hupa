package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.GetMessageDetailsService;
import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.GetMessageDetailsResult;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = GetMessageDetailsService.class, locator = IocRfServiceLocator.class)
public interface GetMessageDetailsRequest extends RequestContext {
	Request<GetMessageDetailsResult> get(GetMessageDetailsAction action);
}
