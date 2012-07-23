package org.apache.hupa.server.service;

import javax.mail.Folder;

import org.apache.hupa.shared.data.GenericResultImpl;
import org.apache.hupa.shared.domain.DeleteFolderAction;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;

import com.sun.mail.imap.IMAPStore;

public class DeleteFolderServiceImpl extends AbstractService implements DeleteFolderService {

	@Override
	public GenericResult delete(DeleteFolderAction action) throws Exception {
		User user = getUser();
		ImapFolder folder = action.getFolder();
		IMAPStore store = cache.get(user);

		Folder f = store.getFolder(folder.getFullName());

		// close the folder if its open
		if (f.isOpen()) {
			f.close(false);
		}

		// recursive delete the folder
		if (f.delete(true)) {
			logger.info("Successfully delete folder " + folder + " for user " + user);
			return new GenericResultImpl();
		} else {
			throw new Exception("Unable to delete folder " + folder + " for user " + user);
		}
	}

}
