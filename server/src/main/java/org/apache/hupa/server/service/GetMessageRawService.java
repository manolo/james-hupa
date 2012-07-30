package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.GetMessageRawAction;
import org.apache.hupa.shared.domain.GetMessageRawResult;

public interface GetMessageRawService {

	GetMessageRawResult get(GetMessageRawAction action) throws Exception;

}
