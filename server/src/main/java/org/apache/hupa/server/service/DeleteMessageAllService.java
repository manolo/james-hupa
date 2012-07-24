package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.DeleteMessageAction;
import org.apache.hupa.shared.domain.DeleteMessageResult;

public interface DeleteMessageAllService {
	public DeleteMessageResult delete(DeleteMessageAction action) throws Exception;
}
