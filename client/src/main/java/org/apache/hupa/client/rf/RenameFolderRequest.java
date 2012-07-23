package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.RenameFolderService;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.RenameFolderAction;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = RenameFolderService.class, locator = IocRfServiceLocator.class)
public interface RenameFolderRequest extends RequestContext {
	Request<GenericResult> rename(RenameFolderAction action);
}
