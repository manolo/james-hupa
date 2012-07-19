package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.User;

public interface CheckSessionService {
	public User getUser();
	public Boolean isValid();
}
