package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.RenameFolderAction;

public interface RenameFolderService {
	GenericResult rename(RenameFolderAction action) throws Exception;
}
