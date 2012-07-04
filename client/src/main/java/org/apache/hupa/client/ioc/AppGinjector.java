package org.apache.hupa.client.ioc;

import org.apache.hupa.client.evo.AppController;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * This is the application ginjector.
 * It defines the method our EntryPoint will use to initialize GIN dependecy graph
 * and the GIN module where binding is defined
 */
@GinModules(AppGinModule.class)
public interface AppGinjector extends Ginjector {
  AppController getAppController();
}
