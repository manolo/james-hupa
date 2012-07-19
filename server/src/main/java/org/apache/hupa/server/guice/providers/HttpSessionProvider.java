package org.apache.hupa.server.guice.providers;


import javax.servlet.http.HttpSession;

import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

public class HttpSessionProvider implements Provider<HttpSession>{

	@Override
    public HttpSession get() {
		return RequestFactoryServlet.getThreadLocalRequest().getSession();
    }

}
