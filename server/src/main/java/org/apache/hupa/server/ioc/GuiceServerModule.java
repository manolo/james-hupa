package org.apache.hupa.server.ioc;

import java.util.Properties;

import javax.mail.Session;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.InMemoryIMAPStoreCache;
import org.apache.hupa.server.guice.providers.DefaultUserSettingsProvider;
import org.apache.hupa.server.guice.providers.JavaMailSessionProvider;
import org.apache.hupa.server.guice.providers.LogProvider;
import org.apache.hupa.server.preferences.InImapUserPreferencesStorage;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.server.service.CheckSessionService;
import org.apache.hupa.server.service.CheckSessionServiceImpl;
import org.apache.hupa.server.service.CreateFolderService;
import org.apache.hupa.server.service.CreateFolderServiceImpl;
import org.apache.hupa.server.service.DeleteFolderService;
import org.apache.hupa.server.service.DeleteFolderServiceImpl;
import org.apache.hupa.server.service.DeleteMessageAllService;
import org.apache.hupa.server.service.DeleteMessageAllServiceImpl;
import org.apache.hupa.server.service.DeleteMessageByUidService;
import org.apache.hupa.server.service.DeleteMessageByUidServiceImpl;
import org.apache.hupa.server.service.FetchMessagesService;
import org.apache.hupa.server.service.FetchMessagesServiceImpl;
import org.apache.hupa.server.service.GetMessageDetailsService;
import org.apache.hupa.server.service.GetMessageDetailsServiceImpl;
import org.apache.hupa.server.service.ImapFolderService;
import org.apache.hupa.server.service.ImapFolderServiceImpl;
import org.apache.hupa.server.service.LoginUserService;
import org.apache.hupa.server.service.LoginUserServiceImpl;
import org.apache.hupa.server.service.RenameFolderService;
import org.apache.hupa.server.service.RenameFolderServiceImpl;
import org.apache.hupa.server.service.SendForwardMessageService;
import org.apache.hupa.server.service.SendForwardMessageServiceImpl;
import org.apache.hupa.server.service.SendMessageBaseServiceImpl;
import org.apache.hupa.server.service.SendMessageService;
import org.apache.hupa.server.service.SendReplyMessageService;
import org.apache.hupa.server.service.SendReplyMessageServiceImpl;
import org.apache.hupa.shared.data.CreateFolderActionImpl;
import org.apache.hupa.shared.data.DeleteFolderActionImpl;
import org.apache.hupa.shared.data.DeleteMessageAllActionImpl;
import org.apache.hupa.shared.data.DeleteMessageByUidActionImpl;
import org.apache.hupa.shared.data.FetchMessagesActionImpl;
import org.apache.hupa.shared.data.FetchMessagesResultImpl;
import org.apache.hupa.shared.data.GenericResultImpl;
import org.apache.hupa.shared.data.GetMessageDetailsActionImpl;
import org.apache.hupa.shared.data.GetMessageDetailsResultImpl;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.MailHeaderImpl;
import org.apache.hupa.shared.data.MessageAttachmentImpl;
import org.apache.hupa.shared.data.MessageDetailsImpl;
import org.apache.hupa.shared.data.RenameFolderActionImpl;
import org.apache.hupa.shared.data.SendForwardMessageActionImpl;
import org.apache.hupa.shared.data.SendMessageActionImpl;
import org.apache.hupa.shared.data.SendReplyMessageActionImpl;
import org.apache.hupa.shared.data.SmtpMessageImpl;
import org.apache.hupa.shared.data.TagImpl;
import org.apache.hupa.shared.data.UserImpl;
import org.apache.hupa.shared.domain.CreateFolderAction;
import org.apache.hupa.shared.domain.DeleteFolderAction;
import org.apache.hupa.shared.domain.DeleteMessageAllAction;
import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.GetMessageDetailsResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.MailHeader;
import org.apache.hupa.shared.domain.MessageAttachment;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.domain.RenameFolderAction;
import org.apache.hupa.shared.domain.SendForwardMessageAction;
import org.apache.hupa.shared.domain.SendMessageAction;
import org.apache.hupa.shared.domain.SendReplyMessageAction;
import org.apache.hupa.shared.domain.SmtpMessage;
import org.apache.hupa.shared.domain.Settings;
import org.apache.hupa.shared.domain.Tag;
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
		
		bind(MailHeader.class).to(MailHeaderImpl.class);
		
		bind(User.class).to(UserImpl.class);
		bind(Settings.class).toProvider(DefaultUserSettingsProvider.class).in(Singleton.class);
		bind(ImapFolder.class).to(ImapFolderImpl.class);
		bind(Tag.class).to(TagImpl.class);
		bind(MessageDetails.class).to(MessageDetailsImpl.class);
		bind(MessageAttachment.class).to(MessageAttachmentImpl.class);
		bind(SmtpMessage.class).to(SmtpMessageImpl.class);
		
		bind(GenericResult.class).to(GenericResultImpl.class);
		bind(FetchMessagesAction.class).to(FetchMessagesActionImpl.class);
		bind(FetchMessagesResult.class).to(FetchMessagesResultImpl.class);
		bind(CreateFolderAction.class).to(CreateFolderActionImpl.class);
		bind(DeleteFolderAction.class).to(DeleteFolderActionImpl.class);
		bind(RenameFolderAction.class).to(RenameFolderActionImpl.class);
		bind(DeleteMessageAllAction.class).to(DeleteMessageAllActionImpl.class);
		bind(DeleteMessageByUidAction.class).to(DeleteMessageByUidActionImpl.class);
		bind(GetMessageDetailsAction.class).to(GetMessageDetailsActionImpl.class);
		bind(GetMessageDetailsResult.class).to(GetMessageDetailsResultImpl.class);
		bind(SendMessageAction.class).to(SendMessageActionImpl.class);
		bind(SendForwardMessageAction.class).to(SendForwardMessageActionImpl.class);
		bind(SendReplyMessageAction.class).to(SendReplyMessageActionImpl.class);
		
		
		bind(CheckSessionService.class).to(CheckSessionServiceImpl.class);
		bind(LoginUserService.class).to(LoginUserServiceImpl.class);
		bind(ImapFolderService.class).to(ImapFolderServiceImpl.class);
		bind(FetchMessagesService.class).to(FetchMessagesServiceImpl.class);
		bind(CreateFolderService.class).to(CreateFolderServiceImpl.class);
		bind(DeleteFolderService.class).to(DeleteFolderServiceImpl.class);
		bind(RenameFolderService.class).to(RenameFolderServiceImpl.class);
		bind(DeleteMessageAllService.class).to(DeleteMessageAllServiceImpl.class);
		bind(DeleteMessageByUidService.class).to(DeleteMessageByUidServiceImpl.class);
		bind(GetMessageDetailsService.class).to(GetMessageDetailsServiceImpl.class);
		bind(SendMessageService.class).to(SendMessageBaseServiceImpl.class);
		bind(SendForwardMessageService.class).to(SendForwardMessageServiceImpl.class);
		bind(SendReplyMessageService.class).to(SendReplyMessageServiceImpl.class);
		
		bind(IMAPStoreCache.class).to(getIMAPStoreCacheClass()).in(Singleton.class);

		bind(Log.class).toProvider(LogProvider.class).in(Singleton.class);
		bind(Session.class).toProvider(JavaMailSessionProvider.class);
        bind(UserPreferencesStorage.class).to(InImapUserPreferencesStorage.class);
		bind(Properties.class).toInstance(properties);
	}

	protected Class<? extends IMAPStoreCache> getIMAPStoreCacheClass() {
		return InMemoryIMAPStoreCache.class;
	}
}
