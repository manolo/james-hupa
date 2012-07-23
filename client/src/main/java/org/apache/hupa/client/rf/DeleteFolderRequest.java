package org.apache.hupa.client.rf;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.DeleteFolderService;
import org.apache.hupa.shared.domain.DeleteFolderAction;
import org.apache.hupa.shared.domain.GenericResult;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = DeleteFolderService.class, locator = IocRfServiceLocator.class)
public interface DeleteFolderRequest extends RequestContext {
	Request<GenericResult> delete(DeleteFolderAction action);
}
