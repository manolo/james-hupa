package org.apache.hupa.server.ioc;

import java.util.Properties;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.InMemoryIMAPStoreCache;
import org.apache.hupa.server.guice.providers.DefaultUserSettingsProvider;
import org.apache.hupa.server.guice.providers.HttpSessionProvider;
import org.apache.hupa.server.guice.providers.JavaMailSessionProvider;
import org.apache.hupa.server.guice.providers.LogProvider;
import org.apache.hupa.server.service.CheckSessionService;
import org.apache.hupa.server.service.CheckSessionServiceImpl;
import org.apache.hupa.server.service.ImapFolderService;
import org.apache.hupa.server.service.ImapFolderServiceImpl;
import org.apache.hupa.server.service.LoginUserService;
import org.apache.hupa.server.service.LoginUserServiceImpl;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.SettingsImpl;
import org.apache.hupa.shared.data.UserImpl;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Settings;
import org.apache.hupa.shared.domain.User;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;

/**
 */
public class GuiceServerModule extends AbstractModule {

	Properties properties;

	public GuiceServerModule(Properties properties) {
		this.properties = properties;
	}

	@Override
	protected void configure() {
		try {
			// Bind addresses and ports for imap and smtp
			Names.bindProperties(binder(), properties);
		} catch (Exception e) {
			throw new RuntimeException("Unable to to configure hupa server,"
			        + "\nmake sure that you have a valid /etc/default/hupa file"
			        + "\nor the web container has been started with the appropriate parameter:"
			        + " -Dhupa.config.file=your_hupa_properties_file", e);
		}
		bind(ExceptionHandler.class).to(DefaultExceptionHandler.class);
		bind(ServiceLayerDecorator.class).to(IocRfServiceDecorator.class);
		bind(IocRfServiceLocator.class);
		
		bind(User.class).to(UserImpl.class);
		bind(Settings.class).toProvider(DefaultUserSettingsProvider.class).in(Singleton.class);
		bind(ImapFolder.class).to(ImapFolderImpl.class);
		
		bind(CheckSessionService.class).to(CheckSessionServiceImpl.class);
		bind(LoginUserService.class).to(LoginUserServiceImpl.class);
		bind(ImapFolderService.class).to(ImapFolderServiceImpl.class);
		
		bind(IMAPStoreCache.class).to(getIMAPStoreCacheClass()).in(Singleton.class);

		bind(Log.class).toProvider(LogProvider.class).in(Singleton.class);
		bind(Session.class).toProvider(JavaMailSessionProvider.class);
//		bind(HttpSession.class).toProvider(HttpSessionProvider.class);
		bind(Properties.class).toInstance(properties);
	}

	protected Class<? extends IMAPStoreCache> getIMAPStoreCacheClass() {
		return InMemoryIMAPStoreCache.class;
	}
}
