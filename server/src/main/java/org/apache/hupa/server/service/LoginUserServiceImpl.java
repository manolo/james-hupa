package org.apache.hupa.server.service;

import javax.mail.MessagingException;

import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.data.UserImpl;
import org.apache.hupa.shared.domain.Settings;
import org.apache.hupa.shared.domain.User;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class LoginUserServiceImpl extends AbstractService implements LoginUserService {

	@Inject private Provider<Settings> settingsProvider;

	public User login(String username, String password) {
		SessionUtils.cleanSessionAttributes(httpSession);
		User user = new UserImpl();
		user.setName(username);
		user.setPassword(password);
		try {
			cache.get(user);
		} catch (MessagingException e) {
			logger.error("Unable to authenticate user: " + username, e);
		}
		user.setAuthenticated(true);
		user.setSettings(settingsProvider.get());
		httpSession.setAttribute(SConsts.USER_SESS_ATTR, user);
		logger.debug("Logged user: " + username);
		return user;
	}
}
