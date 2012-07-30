package org.apache.hupa.server.service;

import java.io.ByteArrayOutputStream;

import javax.mail.Folder;
import javax.mail.Message;

import org.apache.hupa.shared.data.GetMessageRawResultImpl;
import org.apache.hupa.shared.domain.GetMessageRawAction;
import org.apache.hupa.shared.domain.GetMessageRawResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class GetMessageRawServiceImpl extends AbstractService implements GetMessageRawService {

	@Override
	public GetMessageRawResult get(GetMessageRawAction action) throws Exception {
		User user = getUser();
		long uid = action.getUid();
		ImapFolder folder = action.getFolder();
		try {
			IMAPStore store = cache.get(user);
			IMAPFolder f = (IMAPFolder) store.getFolder(folder.getFullName());
			if (f.isOpen() == false) {
				f.open(Folder.READ_ONLY);
			}
			Message m = f.getMessageByUID(action.getUid());

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			m.writeTo(out);
			if (f.isOpen()) {
				f.close(false);
			}
			return new GetMessageRawResultImpl(out.toString());
		} catch (Exception e) {
			logger.error("Unable to get raw content of msg for user " + user + " in folder " + folder + " with uid "
			        + uid, e);
			throw new Exception("Unable to et raw content of msg for user " + user + " in folder " + folder
			        + " with uid " + uid);
		}

	}
}
