package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.SendMessageAction;

public interface SendMessageService {
	GenericResult send(SendMessageAction action)throws Exception;
}
