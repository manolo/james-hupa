package org.apache.hupa.client.rf;

import java.util.List;

import org.apache.hupa.server.service.ImapFolderService;
import org.apache.hupa.shared.proxy.ImapFolder;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(ImapFolderService.class)
public interface ImapFolderRequestContext extends RequestContext {
	Request<List<ImapFolder>> requestFolders();
	Request<String> echo(String s);
}
