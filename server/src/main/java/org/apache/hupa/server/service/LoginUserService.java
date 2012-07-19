package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.User;

public interface LoginUserService {
	public User login(String username, String password);
}
