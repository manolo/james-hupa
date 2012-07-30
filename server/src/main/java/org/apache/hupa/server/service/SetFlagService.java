package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.SetFlagAction;

public interface SetFlagService {

	GenericResult set(SetFlagAction action) throws Exception;

}
