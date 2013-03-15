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

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnCancelUploaderHandler;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.OnStatusChangedHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hupa.client.place.ComposePlace;
import org.apache.hupa.client.rf.SendForwardMessageRequest;
import org.apache.hupa.client.rf.SendMessageRequest;
import org.apache.hupa.client.rf.SendReplyMessageRequest;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.client.validation.EmailListValidator;
import org.apache.hupa.shared.data.MessageAttachmentImpl;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.MessageAttachment;
import org.apache.hupa.shared.domain.SendForwardMessageAction;
import org.apache.hupa.shared.domain.SendMessageAction;
import org.apache.hupa.shared.domain.SendReplyMessageAction;
import org.apache.hupa.shared.domain.SmtpMessage;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.ListBox;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;

public class ComposeActivity extends AppBaseActivity {
	@Inject private Displayable display;
	private List<MessageAttachment> attachments = new ArrayList<MessageAttachment>();
	static private User user;
	private ComposePlace place;

	public Activity with(ComposePlace place) {
		this.place = place;
		return this;
	}

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		container.setWidget(display.asWidget());
		bindTo(eventBus);
		if (user != null)
			display.getFromList().addItem(user.getName());
	}

	private void bindTo(EventBus eventBus) {
		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {
			@Override
			public void onLogin(LoginEvent event) {
				user = event.getUser();
			}
		});
		registerHandler(display.getSendClick().addClickHandler(sendClickHandler));

		registerHandler(display.getCcClick().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.showCc();
			}
		}));
		registerHandler(display.get_CcClick().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.hideCc();
			}
		}));
		registerHandler(display.getBccClick().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.showBcc();
			}
		}));
		registerHandler(display.get_BccClick().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.hideBcc();
			}
		}));
		registerHandler(display.getReplyClick().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.showReply();
			}
		}));
		registerHandler(display.get_ReplyClick().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.hideReply();
			}
		}));
		registerHandler(display.getFollowupClick().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.showFollowup();
			}
		}));
		registerHandler(display.get_FollowupClick().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				display.hideFollowup();
			}
		}));
		registerHandler(display.getUploader().addOnStatusChangedHandler(onStatusChangedHandler));
		registerHandler(display.getUploader().addOnFinishUploadHandler(onFinishUploadHandler));
		registerHandler(display.getUploader().addOnCancelUploadHandler(onCancelUploadHandler));
	}

	private OnFinishUploaderHandler onFinishUploadHandler = new OnFinishUploaderHandler() {
		public void onFinish(IUploader uploader) {
			if (uploader.getStatus() == Status.SUCCESS) {
				String name = uploader.getInputName();
				MessageAttachment attachment = new MessageAttachmentImpl();
				attachment.setName(name);
				attachments.add(attachment);
			}
		}
	};

	private OnStatusChangedHandler onStatusChangedHandler = new OnStatusChangedHandler() {
		public void onStatusChanged(IUploader uploader) {
			// TODO buttons disabled
			// Status stat = display.getUploader().getStatus();

			// if (stat == Status.INPROGRESS)
			// display.getSendEnable().setEnabled(false);
			// else
			// display.getSendEnable().setEnabled(true);
		}
	};

	private OnCancelUploaderHandler onCancelUploadHandler = new OnCancelUploaderHandler() {
		public void onCancel(IUploader uploader) {
			for (Iterator<MessageAttachment> i = attachments.iterator(); i.hasNext();) {
				MessageAttachment attachment = i.next();
				if (attachment.getName().equals(uploader.getInputName()))
					i.remove();
			}
		}
	};

	protected ClickHandler sendClickHandler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			if (!validate())
				return;

			if ("new".equals(place.getToken())) {
				SendMessageRequest sendReq = requestFactory.sendMessageRequest();
				SendMessageAction sendAction = sendReq.create(SendMessageAction.class);
				sendAction.setMessage(parseMessage(sendReq));
				sendReq.send(sendAction).fire(new Receiver<GenericResult>() {
					@Override
					public void onSuccess(GenericResult response) {
						afterSend(response);
					}
				});
			} else if ("forward".equals(place.getToken())) {
				// FIXME will get a NullPointerException given accessing
				// directly from some URL like #/compose:forward
				SendForwardMessageRequest req = requestFactory.sendForwardMessageRequest();
				SendForwardMessageAction action = req.create(SendForwardMessageAction.class);
				action.setMessage(parseMessage(req));
				ImapFolder f = req.create(ImapFolder.class);
				f.setFullName(place.getParameters().getFolder().getFullName());
				action.setFolder(f);
				action.setUid(place.getParameters().getOldmessage().getUid());
				req.send(action).fire(new Receiver<GenericResult>() {
					@Override
					public void onSuccess(GenericResult response) {
						afterSend(response);
					}
				});
			} else {
				SendReplyMessageRequest replyReq = requestFactory.sendReplyMessageRequest();
				SendReplyMessageAction action = replyReq.create(SendReplyMessageAction.class);
				action.setMessage(parseMessage(replyReq));
				ImapFolder folder = replyReq.create(ImapFolder.class);
				folder.setFullName(place.getParameters().getFolder().getFullName());
				action.setFolder(folder);
				action.setUid(place.getParameters().getOldmessage().getUid());
				replyReq.send(action).fire(new Receiver<GenericResult>() {
					@Override
					public void onSuccess(GenericResult response) {
						afterSend(response);
					}
				});
			}
		}
	};

	private boolean validate() {
		// Don't trust only in view validation
		return display.validate() && display.getTo().getText().trim().length() > 0
				&& EmailListValidator.isValidAddressList(display.getTo().getText())
				&& EmailListValidator.isValidAddressList(display.getCc().getText())
				&& EmailListValidator.isValidAddressList(display.getBcc().getText());
	}

	private SmtpMessage parseMessage(RequestContext rc) {
		SmtpMessage message = rc.create(SmtpMessage.class);
		List<MessageAttachment> attaches = new ArrayList<MessageAttachment>();
		for (MessageAttachment attach : attachments) {
			MessageAttachment attachMent = rc.create(MessageAttachment.class);
			attachMent.setName(attach.getName());
			attachMent.setSize(attach.getSize());
			attachMent.setContentType(attach.getContentType());
			attaches.add(attachMent);
		}
		message.setFrom(display.getFromText());
		message.setSubject(display.getSubject().getText());
		message.setText(display.getMessageHTML().getHTML());
		message.setMessageAttachments(attaches);
		message.setTo(emailTextToArray(display.getTo().getText()));
		message.setCc(emailTextToArray(display.getCc().getText()));
		message.setBcc(emailTextToArray(display.getBcc().getText()));
		return message;
	}

	private List<String> emailTextToArray(String emails) {
		List<String> cc = new ArrayList<String>();
		for (String ccRaw : emails.split("[,;]+")) {
			String ccRecip = ccRaw.trim();
			if (ccRecip.length() > 0) {
				cc.add(ccRaw.trim());
			}
		}
		return cc;
	}

	private void afterSend(GenericResult response) {
		Window.alert("//TODO send result is: " + response.isSuccess());
	}

	public interface Displayable extends WidgetDisplayable {
		String getFromText();
		void showCc();
		void hideCc();
		void showBcc();
		void hideBcc();
		void showReply();
		void hideReply();
		void showFollowup();
		void hideFollowup();
		HasText getTo();
		HasText getCc();
		HasText getBcc();
		HasText getSubject();
		HasClickHandlers getSendClick();
		HasClickHandlers getCcClick();
		HasClickHandlers get_CcClick();
		HasClickHandlers getBccClick();
		HasClickHandlers get_BccClick();
		HasClickHandlers getReplyClick();
		HasClickHandlers get_ReplyClick();
		HasClickHandlers getFollowupClick();
		HasClickHandlers get_FollowupClick();
		boolean validate();
		HasText getMessage();
		HasHTML getMessageHTML();
		ListBox getFromList();
		IUploader getUploader();
	}
}
