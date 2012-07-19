package org.apache.hupa.server.ioc;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

/**
 * This is Guice-injected ServiceLocator.  Use this class to provide dependency-injected
 * instances of your RequestFactory services. When defining a new Request interface,
 * set this in the locator attribute of the @Service annotation.
 */
public class IocRfServiceLocator implements ServiceLocator {

  @Inject
  private Injector injector;
  
  @Override
  public Object getInstance(Class<?> clazz) {
    return injector.getInstance(clazz);
  }

}