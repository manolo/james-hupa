package org.apache.hupa.server.service;

import javax.mail.Folder;

import org.apache.hupa.shared.data.GenericResultImpl;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.RenameFolderAction;
import org.apache.hupa.shared.domain.User;

import com.sun.mail.imap.IMAPStore;

public class RenameFolderServiceImpl extends AbstractService implements RenameFolderService {

	@Override
	public GenericResult rename(RenameFolderAction action) throws Exception {
		User user = getUser();
		ImapFolder folder = action.getFolder();
		String newName = action.getNewName();
		IMAPStore store = cache.get(user);
		com.sun.mail.imap.IMAPFolder iFolder = (com.sun.mail.imap.IMAPFolder) store.getFolder(folder.getFullName());
		Folder newFolder = store.getFolder(newName);

		if (iFolder.isOpen()) {
			iFolder.close(false);
		}
		if (iFolder.renameTo(newFolder)) {
			return new GenericResultImpl();
		}
		throw new Exception("Unable to rename Folder " + folder.getFullName() + " to " + newName + " for user " + user);
	}

}
