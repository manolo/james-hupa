package org.apache.hupa.server.service;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.MessagingException;

import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.User;

import com.sun.mail.imap.IMAPStore;

public class ImapFolderServiceImpl extends AbstractService implements ImapFolderService {

	// List of mail 'root' imap folders
	List<ImapFolder> imapFolders = new ArrayList<ImapFolder>();

	public List<ImapFolder> requestFolders() throws Exception {
		User user = getUser();
		try {
			IMAPStore store = cache.get(user);
			com.sun.mail.imap.IMAPFolder folder = (com.sun.mail.imap.IMAPFolder) store.getDefaultFolder();

			// Create IMAPFolder tree list
			for (Folder f : folder.list()) {
				ImapFolder imapFolder = createIMAPFolder(f);
				imapFolders.add(imapFolder);
				walkFolders(f, imapFolder);
			}
			return imapFolders;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Unable to get folders for User " + user);
		}
	}

	/**
	 * Walk through the folder's sub-folders and add sub-folders to current
	 * imapFolder
	 * 
	 * @param folder Folder to walk
	 * @param imapFolder Current IMAPFolder
	 * @throws ActionException If an error occurs
	 * @throws MessagingException If an error occurs
	 */
	private void walkFolders(Folder folder, ImapFolder imapFolder) throws MessagingException {
		for (Folder f : folder.list()) {
			ImapFolder iFolder = createIMAPFolder(f);
			imapFolder.getChildren().add(iFolder);
			walkFolders(f, iFolder);
		}
	}

	/**
	 * Create a new IMAPFolder from the given Folder
	 * 
	 * @param folder Current folder
	 * @return imapFolder Created IMAPFolder
	 * @throws ActionException If an error occurs
	 * @throws MessagingException If an error occurs
	 */
	private ImapFolder createIMAPFolder(Folder folder) {

		String fullName = folder.getFullName();
		String delimiter;
		ImapFolder iFolder = null;

		try {
			System.out.println("Creating folder: " + fullName + " for user: ");
			delimiter = String.valueOf(folder.getSeparator());
			iFolder = new ImapFolderImpl(fullName);
			iFolder.setDelimiter(delimiter);
			if ("[Gmail]".equals(folder.getFullName()))
				return iFolder;
			iFolder.setMessageCount(folder.getMessageCount());
			iFolder.setSubscribed(folder.isSubscribed());
			iFolder.setUnseenMessageCount(folder.getUnreadMessageCount());
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return iFolder;
	}

	public String toString() {
		StringBuffer ret = new StringBuffer("");
		for (ImapFolder folder : imapFolders) {
			ret.append(folder.getFullName()).append("\n");
			for (ImapFolder f : folder.getChildren()) {
				childFolder(f, ret);
			}
		}
		return ret.toString();
	}

	private void childFolder(ImapFolder child, StringBuffer ret) {
		ret.append(child.getFullName()).append("\n");
		for (ImapFolder folder : child.getChildren()) {
			childFolder(folder, ret);
		}
	}
}
