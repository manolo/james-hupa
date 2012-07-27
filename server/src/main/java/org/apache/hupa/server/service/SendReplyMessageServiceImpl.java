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
import org.apache.hupa.shared.domain.SendMessageAction;
import org.apache.hupa.shared.domain.SendReplyMessageAction;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class SendReplyMessageServiceImpl extends SendMessageBaseServiceImpl implements SendReplyMessageService{

	@Inject
	public SendReplyMessageServiceImpl(UserPreferencesStorage preferences, @Named("SMTPServerAddress") String address,
	        @Named("SMTPServerPort") int port, @Named("SMTPAuth") boolean auth, @Named("SMTPS") boolean useSSL, IMAPStoreCache cache) {
	    super(preferences, address, port, auth, useSSL,cache);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected List getAttachments(SendMessageAction action) throws MessagingException {
    	SendReplyMessageAction replyAction = (SendReplyMessageAction)action;
        List<?> items = new ArrayList();
        IMAPStore store = cache.get(getUser());

        IMAPFolder folder = (IMAPFolder) store.getFolder(replyAction.getFolder().getFullName());
        if (folder.isOpen() == false) {
            folder.open(Folder.READ_ONLY);
        }

        // Only original inline images have to be added to the list 
        Message msg = folder.getMessageByUID(replyAction.getReplyMessageUid());
        try {
            items = MessageUtils.extractInlineImages(logger, msg.getContent());
            if (items.size() > 0)
                logger.debug("Replying a message, extracted: " + items.size() + " inline image from");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Put into the list the attachments uploaded by the user
        items.addAll(super.getAttachments(replyAction));
        
        return items;
    }


}
