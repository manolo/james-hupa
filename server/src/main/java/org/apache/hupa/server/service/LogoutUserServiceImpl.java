package org.apache.hupa.server.service;

import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.shared.data.LogoutUserResultImpl;
import org.apache.hupa.shared.domain.LogoutUserResult;
import org.apache.hupa.shared.domain.User;

public class LogoutUserServiceImpl extends AbstractService implements LogoutUserService {
	@Override
	public LogoutUserResult logout() {

		User user = getUser();
		user.setAuthenticated(false);

		// delete cached store
		cache.delete(user);

		// remove user attributes from session
		SessionUtils.cleanSessionAttributes(httpSession);

		return new LogoutUserResultImpl(user);

	}
}
