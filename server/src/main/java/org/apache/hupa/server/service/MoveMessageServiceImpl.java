package org.apache.hupa.server.service;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.hupa.shared.data.GenericResultImpl;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.MoveMessageAction;
import org.apache.hupa.shared.domain.User;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class MoveMessageServiceImpl extends AbstractService implements MoveMessageService {
	public GenericResult move(MoveMessageAction action) throws Exception {
		User user = getUser();
		try {
			IMAPStore store = cache.get(user);
			IMAPFolder folder = (IMAPFolder) store.getFolder(action.getOldFolder().getFullName());
			if (folder.isOpen() == false) {
				folder.open(Folder.READ_WRITE);
			}
			Message m = folder.getMessageByUID(action.getMessageUid());
			Message[] mArray = new Message[] { m };
			folder.copyMessages(mArray, store.getFolder(action.getNewFolder().getFullName()));
			folder.setFlags(mArray, new Flags(Flags.Flag.DELETED), true);
			try {
				folder.expunge(mArray);
				folder.close(false);
			} catch (MessagingException e) {
				// prolly UID expunge is not supported
				folder.close(true);
			}
			return new GenericResultImpl();
		} catch (MessagingException e) {
			logger.error(
			        "Error while moving message " + action.getMessageUid() + " from folder " + action.getOldFolder()
			                + " to " + action.getNewFolder(), e);
			throw new Exception(e);
		}
	}
}
