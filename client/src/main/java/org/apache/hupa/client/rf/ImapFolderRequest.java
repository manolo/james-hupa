package org.apache.hupa.client.rf;

import java.util.List;

import org.apache.hupa.server.ioc.IocRfServiceLocator;
import org.apache.hupa.server.service.ImapFolderService;
import org.apache.hupa.shared.domain.ImapFolder;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = ImapFolderService.class, locator = IocRfServiceLocator.class)
public interface ImapFolderRequest extends RequestContext {
	Request<List<ImapFolder>> requestFolders();
}
