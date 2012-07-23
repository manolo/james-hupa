package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.CreateFolderService;
import org.apache.hupa.shared.domain.CreateFolderAction;
import org.apache.hupa.shared.domain.GenericResult;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = CreateFolderService.class, locator = IocRfServiceLocator.class)
public interface CreateFolderRequest extends RequestContext {
	Request<GenericResult> create(CreateFolderAction action);
}
