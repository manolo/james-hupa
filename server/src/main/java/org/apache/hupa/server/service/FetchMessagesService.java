package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;

public interface FetchMessagesService {
	public FetchMessagesResult fetch(FetchMessagesAction action);
}
