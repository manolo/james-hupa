package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.DeleteFolderAction;
import org.apache.hupa.shared.domain.GenericResult;

public interface DeleteFolderService {
	GenericResult delete(DeleteFolderAction action) throws Exception;
}
