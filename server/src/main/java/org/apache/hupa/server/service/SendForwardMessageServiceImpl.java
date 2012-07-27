package org.apache.hupa.server.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.preferences.UserPreferencesStorage;
import org.apache.hupa.server.utils.MessageUtils;
import org.apache.hupa.shared.domain.SendForwardMessageAction;
import org.apache.hupa.shared.domain.SendMessageAction;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class SendForwardMessageServiceImpl extends SendMessageBaseServiceImpl implements SendForwardMessageService{

	@Inject
	public SendForwardMessageServiceImpl(UserPreferencesStorage preferences, @Named("SMTPServerAddress") String address,
	        @Named("SMTPServerPort") int port, @Named("SMTPAuth") boolean auth, @Named("SMTPS") boolean useSSL, IMAPStoreCache cache) {
	    super(preferences, address, port, auth, useSSL,cache);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected List getAttachments(SendMessageAction action) throws MessagingException {
    	SendForwardMessageAction forwardAction = (SendForwardMessageAction)action;
        List<?> items = new ArrayList();
        IMAPStore store = cache.get(getUser());

        IMAPFolder folder = (IMAPFolder) store.getFolder(forwardAction.getFolder().getFullName());
        if (folder.isOpen() == false) {
            folder.open(Folder.READ_ONLY);
        }
        // Put the original attachments in the list 
        Message msg = folder.getMessageByUID(forwardAction.getReplyMessageUid());
        try {
            items = MessageUtils.extractMessageAttachments(logger, msg.getContent());
            logger.debug("Forwarding a message, extracted: " + items.size() + " from original.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Put in the list the attachments uploaded by the user
        items.addAll(super.getAttachments(forwardAction));
        return items;
    }
}
