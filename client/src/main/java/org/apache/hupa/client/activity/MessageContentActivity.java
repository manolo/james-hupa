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

import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.rf.GetMessageDetailsRequest;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.GetMessageDetailsResult;
import org.apache.hupa.shared.domain.ImapFolder;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class MessageContentActivity extends AppBaseActivity {

	private String fullName;
	private String uid;

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		if (uid != null && uid.matches("\\d+")) {
			GetMessageDetailsRequest req = requestFactory
					.messageDetailsRequest();
			GetMessageDetailsAction action = req
					.create(GetMessageDetailsAction.class);
			final ImapFolder f = req.create(ImapFolder.class);
			f.setFullName(fullName);
			action.setFolder(f);
			action.setUid(Long.parseLong(uid));
			req.get(action).fire(new Receiver<GetMessageDetailsResult>() {
				@Override
				public void onSuccess(GetMessageDetailsResult response) {
					display.fillMessageContent(response.getMessageDetails()
							.getText());

				}
			});
		}
		container.setWidget(display.asWidget());
	}

	@Inject private Displayable display;

	public interface Displayable extends WidgetDisplayable {
		void fillMessageContent(String messageContent);
	}

	public MessageContentActivity with(MailFolderPlace place) {
		this.fullName = place.getFullName();
		this.uid = place.getMessageId();
		return this;
	}
}
