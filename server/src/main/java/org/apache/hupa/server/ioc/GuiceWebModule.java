package org.apache.hupa.server.ioc;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

/**
 */
public class GuiceWebModule extends ServletModule {
  
  @Override
  protected void configureServlets() {

    bind(IocRfServlet.class).in(Singleton.class);
    serve("/gwtRequest").with(IocRfServlet.class);
  }
}
