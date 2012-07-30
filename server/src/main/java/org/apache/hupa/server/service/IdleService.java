package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.IdleAction;
import org.apache.hupa.shared.domain.IdleResult;

public interface IdleService {

	IdleResult idle(IdleAction action) throws Exception;

}
