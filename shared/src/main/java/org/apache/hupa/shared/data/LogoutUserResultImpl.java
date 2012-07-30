package org.apache.hupa.shared.data;

import org.apache.hupa.shared.domain.LogoutUserResult;
import org.apache.hupa.shared.domain.User;

public class LogoutUserResultImpl implements LogoutUserResult{

	private User user;

	public LogoutUserResultImpl(User user) {
		this.user = user;
	}

	public LogoutUserResultImpl() {
	}

	public User getUser() {
		return user;
	}
}
