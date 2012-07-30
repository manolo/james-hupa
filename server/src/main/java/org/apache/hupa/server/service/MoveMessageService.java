package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.MoveMessageAction;

public interface MoveMessageService {
	GenericResult move(MoveMessageAction action)throws Exception;
}
