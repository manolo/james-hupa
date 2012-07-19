package org.apache.hupa.server.service;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.User;

import com.google.inject.Inject;

public abstract class AbstractService {
	
    @Inject protected IMAPStoreCache cache;
    @Inject protected HttpSession httpSession;
    @Inject protected Log logger;

	protected User getUser() {
		return (User) getHttpSession().getAttribute(SConsts.USER_SESS_ATTR);
	}

	protected HttpSession getHttpSession() {
		return httpSession;
	}
}
