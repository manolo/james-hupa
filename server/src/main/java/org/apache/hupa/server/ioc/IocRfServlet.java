package org.apache.hupa.server.ioc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;

/**
 * This is a Guice-injected RequestFactoryServlet. Use this to replace the standard
 * non-dependency-injected RequestFactory servlet.
 */
@Singleton
public class IocRfServlet extends RequestFactoryServlet {

  private static final long serialVersionUID = 1L;

  @Inject
  protected IocRfServlet(ExceptionHandler e, ServiceLayerDecorator d) {
    super(e, d);
  }

}