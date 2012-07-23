package org.apache.hupa.server.service;

import javax.mail.Folder;

import org.apache.hupa.shared.data.GenericResultImpl;
import org.apache.hupa.shared.domain.CreateFolderAction;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;

import com.sun.mail.imap.IMAPStore;

public class CreateFolderServiceImpl extends AbstractService implements CreateFolderService {

	@Override
	public GenericResult create(CreateFolderAction action) throws Exception {
		User user = getUser();
		ImapFolder folder = action.getFolder();

		try {
			IMAPStore store = cache.get(user);
			Folder f = store.getFolder(folder.getFullName());
			if (f.create(Folder.HOLDS_MESSAGES)) {
				logger.info("Successfully create folder " + folder + " for user " + user);
				return new GenericResultImpl();
			} else {
				logger.info("Unable to create folder " + folder + " for user " + user);
				throw new Exception("Unable to create folder " + folder + " for user " + user);

			}
		} catch (Exception e) {
			logger.error("Error while creating folder " + folder + " for user " + user, e);
			throw new Exception("Error while creating folder " + folder + " for user " + user, e);
		}
	}

}
