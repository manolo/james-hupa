package org.apache.hupa.client.rf;

import java.util.List;

import org.apache.hupa.server.locator.ImapFolderServiceLocator;
import org.apache.hupa.server.service.ImapFolderService;
import org.apache.hupa.shared.domain.ImapFolder;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value=ImapFolderService.class, locator=ImapFolderServiceLocator.class)

public interface ImapFolderRequestContext extends RequestContext {
	Request<List<ImapFolder>> requestFolders();
}
