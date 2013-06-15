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

import java.util.ArrayList;

import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.place.ComposePlace;
import org.apache.hupa.client.rf.DeleteMessageByUidRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
import org.apache.hupa.shared.domain.DeleteMessageResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.domain.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class ToolBarView extends Composite implements ToolBarActivity.Displayable {

	@Inject PlaceController placeController;
	@Inject protected HupaRequestFactory requestFactory;

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

	interface Style extends CssResource {
		String disabledButton();
		String popupMenu();
		String activeIcon();
		String toolBarMenu();
		String listicon();
		String read();
		String unread();
	}

	private VerticalPanel popup;
	final DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
	private Anchor markRead;
	private Anchor markUnread;

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

	@UiHandler("compose")
	void handleClick(ClickEvent e) {
		placeController.goTo(new ComposePlace("new").with(parameters));
	}

	@UiHandler("reply")
	void handleReplyClick(ClickEvent e) {
		placeController.goTo(new ComposePlace("reply").with(parameters));
	}

	@UiHandler("replyAll")
	void handleReplyAllClick(ClickEvent e) {
		placeController.goTo(new ComposePlace("replyAll").with(parameters));
	}

	@UiHandler("forward")
	void handleForwardClick(ClickEvent e) {
		placeController.goTo(new ComposePlace("forward").with(parameters));
	}

	@UiHandler("delete")
	void handleDeleteClick(ClickEvent e) {
		if (null == parameters)
			return;
		ArrayList<Long> uidList = new ArrayList<Long>();
		uidList.add(parameters.getOldmessage().getUid());
		DeleteMessageByUidRequest req = requestFactory.deleteMessageByUidRequest();
		DeleteMessageByUidAction action = req.create(DeleteMessageByUidAction.class);
		ImapFolder f = req.create(ImapFolder.class);
		f.setFullName(parameters.getFolder().getFullName());
		action.setMessageUids(uidList);
		action.setFolder(f);
		req.delete(action).fire(new Receiver<DeleteMessageResult>() {
			@Override
			public void onSuccess(DeleteMessageResult response) {
				// TODO how to refresh the message list
				placeController.goTo(placeController.getWhere());
			}
		});
	}

	public ToolBarView() {
		initWidget(binder.createAndBindUi(this));
		simplePopup.addStyleName(style.popupMenu());
		mark.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Reposition the popup relative to the button
				Widget source = (Widget) event.getSource();
				int left = source.getAbsoluteLeft();
				int top = source.getAbsoluteTop() + source.getOffsetHeight();
				simplePopup.setPopupPosition(left, top);
				// Show the popup
				simplePopup.show();
			}
		});
		popup = new VerticalPanel();
		markRead = new Anchor("As Read");
		markUnread = new Anchor("As Unread");
		popup.addStyleName(style.toolBarMenu());
		markRead.addStyleName(style.activeIcon());
		markRead.addStyleName(style.listicon());
		markRead.addStyleName(style.read());
		markUnread.addStyleName(style.activeIcon());
		markUnread.addStyleName(style.listicon());
		markUnread.addStyleName(style.unread());
		popup.add(markRead);
		popup.add(markUnread);
		simplePopup.setWidget(popup);
	}

	@Override
	public HasClickHandlers getMark() {
		return mark;
	}

	@Override
	public HasClickHandlers getMarkRead() {
		return markRead;
	}

	@Override
	public HasClickHandlers getMarkUnread() {
		return markUnread;
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
	@Override
	public PopupPanel getPopup() {
		return simplePopup;
	}

	@Override
	public void enableAllTools(boolean is) {
		this.enableSendingTools(is);
		this.enableDealingTools(is);
	}
	@Override
	public void enableSendingTools(boolean is) {
		if (is) {
			removeSendingDisableds();
		} else {
			addSendingDisableds();
		}
	}

	@Override
	public void enableDealingTools(boolean is) {
		if (is) {
			removeDealingDisableds();
		} else {
			addDealingDisableds();
		}
	}

	private void addSendingDisableds() {
		reply.addStyleName(style.disabledButton());
		replyAllGroup.addStyleName(style.disabledButton());
		forwardGroup.addStyleName(style.disabledButton());
	}

	private void removeSendingDisableds() {
		reply.removeStyleName(style.disabledButton());
		replyAllGroup.removeStyleName(style.disabledButton());
		forwardGroup.removeStyleName(style.disabledButton());
	}

	private void addDealingDisableds() {
		delete.addStyleName(style.disabledButton());
		mark.addStyleName(style.disabledButton());
	}

	private void removeDealingDisableds() {
		delete.removeStyleName(style.disabledButton());
		mark.removeStyleName(style.disabledButton());
	}

	interface ToolBarUiBinder extends UiBinder<FlowPanel, ToolBarView> {
	}

	private static ToolBarUiBinder binder = GWT.create(ToolBarUiBinder.class);

}
