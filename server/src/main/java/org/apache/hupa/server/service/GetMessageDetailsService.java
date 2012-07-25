package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.GetMessageDetailsResult;

public interface GetMessageDetailsService {
	GetMessageDetailsResult get(GetMessageDetailsAction action) throws Exception;
}
