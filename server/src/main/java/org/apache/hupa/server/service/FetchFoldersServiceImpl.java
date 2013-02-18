/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.hupa.server.service;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.MessagingException;

import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.exception.HupaException;

import com.sun.mail.imap.IMAPStore;

public class FetchFoldersServiceImpl extends AbstractService implements FetchFoldersService {

	@Override
	public List<ImapFolder> fetch(ImapFolder imapFolder) throws MessagingException, HupaException{
		try {
			Folder folder = null;
			IMAPStore store = cache.get(getUser());
			if (imapFolder == null) {
				folder = store.getDefaultFolder();
			} else {
				folder = store.getFolder(imapFolder.getFullName());
			}
			List<ImapFolder> imapFolders = new ArrayList<ImapFolder>();
			for (Folder f : folder.list()) {
				ImapFolder i = createImapFolder(f);
				imapFolders.add(i);
			}
			return imapFolders;
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new MessagingException();
		}
	}

	/**
	 * Create a new IMAPFolder from the given Folder
	 * 
	 * @param folder Current folder
	 * @return imapFolder Created IMAPFolder
	 * @throws HupaException 
	 * @throws Exception If an error occurs
	 * @throws MessagingException If an error occurs
	 */
	private ImapFolder createImapFolder(Folder folder) throws HupaException {
		String fullName = folder.getFullName();
		String delimiter;
		ImapFolder iFolder = null;
		try {
			System.out.println("Creating folder2: " + fullName + " for user: " + this.getUser());
			delimiter = String.valueOf(folder.getSeparator());
			iFolder = new ImapFolderImpl(fullName);
			iFolder.setDelimiter(delimiter);
			if ("[Gmail]".equals(folder.getFullName()))
				return iFolder;
			iFolder.setMessageCount(folder.getMessageCount());
			iFolder.setSubscribed(folder.isSubscribed());
			iFolder.setUnseenMessageCount(folder.getUnreadMessageCount());
			if(folder.list().length != 0){
				iFolder.setHasChildren(true);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return iFolder;
	}

}
