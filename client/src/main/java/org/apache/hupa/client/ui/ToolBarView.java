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

package org.apache.hupa.client.ui;

import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.place.ComposePlace;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.domain.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;

public class ToolBarView extends Composite implements ToolBarActivity.Displayable {

	@Inject PlaceController placeController;

	@UiField Anchor refresh;
	@UiField Anchor compose;
	@UiField Anchor reply;
	@UiField HTMLPanel replyAllGroup;
	@UiField Anchor replyAll;
	@UiField HTMLPanel forwardGroup;
	@UiField Anchor forward;
	@UiField Anchor delete;
	@UiField Anchor mark;
	@UiField Anchor more;

	@UiField Style style;

	private Parameters parameters;

	public Parameters getParameters() {
		return parameters;
	}

	@Override
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public static class Parameters {
		private User user;
		private ImapFolder folder;
		private Message oldmessage;
		private MessageDetails oldDetails;

		public Parameters(User user, ImapFolder folder, Message oldmessage, MessageDetails oldDetails) {
			this.user = user;
			this.folder = folder;
			this.oldmessage = oldmessage;
			this.oldDetails = oldDetails;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public ImapFolder getFolder() {
			return folder;
		}

		public void setFolder(ImapFolder folder) {
			this.folder = folder;
		}

		public Message getOldmessage() {
			return oldmessage;
		}

		public void setOldmessage(Message oldmessage) {
			this.oldmessage = oldmessage;
		}

		public MessageDetails getOldDetails() {
			return oldDetails;
		}

		public void setOldDetails(MessageDetails oldDetails) {
			this.oldDetails = oldDetails;
		}
	}

	interface Style extends CssResource {
		String disabledButton();
	}

	@UiHandler("compose")
	void handleClick(ClickEvent e) {
		placeController.goTo(new ComposePlace("new").with(parameters));
	}

	@UiHandler("reply")
	void handleReplyClick(ClickEvent e) {
		placeController.goTo(new ComposePlace("reply").with(parameters));
	}

	@UiHandler("forward")
	void handleForwardClick(ClickEvent e) {
		placeController.goTo(new ComposePlace("forward").with(parameters));
	}

	public ToolBarView() {
		initWidget(binder.createAndBindUi(this));
	}

	@Override
	public HasClickHandlers getReply() {
		return reply;
	}

	@Override
	public HasClickHandlers getReplyAll() {
		return replyAll;
	}

	@Override
	public HasClickHandlers getForward() {
		return forward;
	}

	// TODO realy disabled the click event of the tool bar coupled with graying
	@Override
	public void disableMessageTools() {
		reply.addStyleName(style.disabledButton());
		replyAllGroup.addStyleName(style.disabledButton());
		forwardGroup.addStyleName(style.disabledButton());
		delete.addStyleName(style.disabledButton());
	}

	@Override
	public void enableMessageTools() {
		reply.removeStyleName(style.disabledButton());
		replyAllGroup.removeStyleName(style.disabledButton());
		forwardGroup.removeStyleName(style.disabledButton());
		delete.removeStyleName(style.disabledButton());
	}

	interface ToolBarUiBinder extends UiBinder<FlowPanel, ToolBarView> {
	}

	private static ToolBarUiBinder binder = GWT.create(ToolBarUiBinder.class);

}
