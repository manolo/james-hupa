package org.apache.hupa.server.service;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.hupa.shared.domain.DeleteMessageAction;
import org.apache.hupa.shared.domain.DeleteMessageAllAction;
import org.apache.hupa.shared.domain.User;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class DeleteMessageAllServiceImpl extends DeleteMessageBaseServiceImpl implements DeleteMessageAllService {

	@Override
	protected Message[] getMessagesToDelete(DeleteMessageAction actionBase) throws Exception {
		DeleteMessageAllAction action = (DeleteMessageAllAction) actionBase;
		User user = getUser();
		try {
			logger.info("Delete all messages in folder " + action.getFolder() + " for user " + user);
			IMAPStore store = cache.get(user);
			IMAPFolder folder = (IMAPFolder) store.getFolder(action.getFolder().getFullName());
			if (folder.isOpen() == false) {
				folder.open(Folder.READ_WRITE);
			}
			return folder.getMessages();
		} catch (MessagingException e) {
			String errorMsg = "Error while deleting all messages in folder " + action.getFolder() + " for user " + user;
			logger.error(errorMsg, e);
			throw new Exception(errorMsg);

		}

	}

}
