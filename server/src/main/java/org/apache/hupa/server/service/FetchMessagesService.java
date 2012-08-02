package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.ImapFolder;

public interface FetchMessagesService {
	FetchMessagesResult fetch(FetchMessagesAction action);
}
