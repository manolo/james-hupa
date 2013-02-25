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

package org.apache.hupa.client.activity;

import org.apache.hupa.client.place.IMAPMessagePlace;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.rf.GetMessageDetailsRequest;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.GetMessageDetailsResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.ExpandMessageEventHandler;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class MessageListActivity extends AppBaseActivity {

	// @Inject private Provider<IMAPMessagePlace> messagePlaceProvider;
	private User user;
	private String searchValue;

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		container.setWidget(display.asWidget());
		bindTo(eventBus);
	}

	private void bindTo(EventBus eventBus) {
		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {
			public void onLogin(LoginEvent event) {
				user = event.getUser();
				searchValue = null;
			}
		});
		eventBus.addHandler(ExpandMessageEvent.TYPE, new ExpandMessageEventHandler() {
			public void onExpandMessage(ExpandMessageEvent event) {
				// final boolean decreaseUnseen;
				final Message message = event.getMessage();
				// check if the message was already seen in the past
				if (event.getMessage().getFlags().contains(IMAPFlag.SEEN) == false) {
					// decreaseUnseen = true;//TODO 1209
				} else {
					// decreaseUnseen = false;
				}

				GetMessageDetailsRequest req = requestFactory.messageDetailsRequest();
				GetMessageDetailsAction action = req.create(GetMessageDetailsAction.class);
				final ImapFolder f = req.create(ImapFolder.class);
				// event.getFolder().setFolderTo(f);
				cloneFolder(f, event.getFolder());
				action.setFolder(f);
				action.setUid(message.getUid());
				req.get(action).fire(new Receiver<GetMessageDetailsResult>() {
					@Override
					public void onSuccess(GetMessageDetailsResult response) {
						/*
						 * TODO if (decreaseUnseen) { eventBus.fireEvent(new
						 * DecreaseUnseenEvent(user, folder)); }
						 */
						placeController.goTo(new IMAPMessagePlace(String.valueOf(message.getUid())).with(user, f,
								message, response.getMessageDetails()));

					}
				});
			}
		});
	}
	
	public MessageListActivity with(MailFolderPlace place){
		display.setFolder(place.getFolder());
		return this;
	}

	private void cloneFolder(ImapFolder desc, ImapFolder src) {
		desc.setChildren(src.getChildren());
		desc.setDelimiter(src.getDelimiter());
		desc.setFullName(src.getFullName());
		desc.setMessageCount(src.getMessageCount());
		desc.setName(src.getName());
		desc.setSubscribed(src.getSubscribed());
		desc.setUnseenMessageCount(src.getUnseenMessageCount());
	}

	@Inject
	private Displayable display;

	public interface Displayable extends WidgetDisplayable {
		void setFolder(ImapFolder folder);
	}
}
