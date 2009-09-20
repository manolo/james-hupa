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

package org.apache.hupa.server.handler;

import java.util.ArrayList;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.EmptyResult;
import org.apache.hupa.shared.rpc.SetFlag;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.mail.imap.IMAPStore;

public class SetFlagsHandler extends AbstractSessionHandler<SetFlag, EmptyResult>{

	@Inject
	public SetFlagsHandler(IMAPStoreCache cache, Log logger,
			Provider<HttpSession> sessionProvider) {
		super(cache, logger, sessionProvider);
	}

	@Override
	protected EmptyResult executeInternal(SetFlag action,
			ExecutionContext context) throws ActionException {
		User user = getUser(action.getSessionId());
		IMAPFolder folder = action.getFolder();
		ArrayList<Long> uids = action.getUids();
		try {
			IMAPStore store = cache.get(user);

			com.sun.mail.imap.IMAPFolder f = (com.sun.mail.imap.IMAPFolder) store.getFolder(folder.getFullName());
			if (f.isOpen() == false) {
				f.open(Folder.READ_WRITE);
			}
			Message[] msgs = f.getMessagesByUID(toArray(uids));
			Flags flags = JavamailUtil.convert(action.getFlags());
			f.setFlags(msgs, flags, true);
			f.close(false);
			return new EmptyResult();
		} catch (MessagingException e) {
			String errorMsg = "Error while setting flags of messages with uids " + uids + " for user " + user;
			logger.error(errorMsg,e);
			throw new ActionException(errorMsg,e);
		}
	}

	public Class<SetFlag> getActionType() {
		return SetFlag.class;
	}
	
	private long[] toArray(ArrayList<Long> uids) {
		long[] array = new long[uids.size()];
		for (int i = 0; i < uids.size(); i++) {
			array[i] = uids.get(i);
		}
		return array;
	}

}
