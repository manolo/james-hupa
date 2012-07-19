package org.apache.hupa.server.service;

import org.apache.hupa.shared.domain.User;

public class CheckSessionServiceImpl extends AbstractService implements CheckSessionService {
	
	public User getUser(){
		return super.getUser();
	}
	
	public Boolean isValid() {
		return getUser() != null && getUser().getAuthenticated();
	}
}
