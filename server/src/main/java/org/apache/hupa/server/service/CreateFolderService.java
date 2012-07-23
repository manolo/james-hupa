package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.CreateFolderAction;
import org.apache.hupa.shared.domain.GenericResult;

public interface CreateFolderService {
	GenericResult create(CreateFolderAction action) throws Exception;
}
