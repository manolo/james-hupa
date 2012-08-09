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

import java.util.ArrayList;
import java.util.List;

import org.apache.hupa.client.place.IMAPMessagePlace;
import org.apache.hupa.client.rf.DeleteMessageByUidRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
import org.apache.hupa.shared.domain.DeleteMessageResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.MessageAttachment;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.BackEvent;
import org.apache.hupa.shared.events.ForwardMessageEvent;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.ReplyMessageEvent;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class IMAPMessageActivity extends AbstractActivity {

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		updateDisplay();
		bind();
		container.setWidget(display.asWidget());
	}

	public IMAPMessageActivity with(IMAPMessagePlace place) {
		this.message = place.getMessage();
		this.messageDetails = place.getMessageDetails();
		this.folder = place.getFolder();
		this.user = place.getUser();
		return this;
	}

	private void updateDisplay() {
		display.setAttachments(messageDetails.getMessageAttachments(), folder.getFullName(), message.getUid());
		display.setHeaders(message);
		display.setContent(messageDetails.getText());
	}

	protected void bind() {
		display.getDeleteButtonClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ArrayList<Long> uidList = new ArrayList<Long>();
				uidList.add(message.getUid());
				DeleteMessageByUidRequest req = requestFactory.deleteMessageByUidRequest();
				DeleteMessageByUidAction action = req.create(DeleteMessageByUidAction.class);
				action.setMessageUids(uidList);
				action.setFolder(folder);
				req.delete(action).fire(new Receiver<DeleteMessageResult>() {
					@Override
					public void onSuccess(DeleteMessageResult response) {
						eventBus.fireEvent(new LoadMessagesEvent(user, folder));
					}
				});
			}

		});
		display.getForwardButtonClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ForwardMessageEvent(user, folder, message, messageDetails));
			}

		});
		display.getReplyButtonClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ReplyMessageEvent(user, folder, message, messageDetails, false));
			}

		});
		display.getReplyAllButtonClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ReplyMessageEvent(user, folder, message, messageDetails, true));
			}

		});
		display.getBackButtonClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new BackEvent());
			}

		});
		display.getShowRawMessageClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				String message_url = GWT.getModuleBaseURL() + SConsts.SERVLET_SOURCE + "?" + SConsts.PARAM_UID + "="
				        + message.getUid() + "&" + SConsts.PARAM_FOLDER + "=" + folder.getFullName();
				Window.open(message_url, "_blank", "");
			}

		});

	}

	private MessageDetails messageDetails;
	private Message message;
	private ImapFolder folder;
	private User user;
	// @Inject private CachingDispatchAsync dispatcher;
	@Inject private Displayable display;
	@Inject private EventBus eventBus;
	@Inject private PlaceController placeController;
	@Inject private HupaRequestFactory requestFactory;

	public interface Displayable extends WidgetDisplayable {
		public void setHeaders(Message msg);
		public void setAttachments(List<MessageAttachment> attachements, String folder, long uid);
		public void setContent(String content);

		public HasClickHandlers getShowRawMessageClick();
		public HasClickHandlers getDeleteButtonClick();
		public HasClickHandlers getReplyButtonClick();
		public HasClickHandlers getReplyAllButtonClick();
		public HasClickHandlers getForwardButtonClick();
		public HasClickHandlers getBackButtonClick();
	}
}
