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

import org.apache.hupa.client.activity.MessageSendActivity.Type;
import org.apache.hupa.client.rf.SendMessageRequest;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.client.validation.EmailListValidator;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.MessageAttachment;
import org.apache.hupa.shared.domain.SendMessageAction;
import org.apache.hupa.shared.domain.SmtpMessage;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class ComposeActivity extends AppBaseActivity {
	@Inject private Displayable display;
	private SendMessageRequest sendReq;
	private SmtpMessage message;
	private List<MessageAttachment> attachments = new ArrayList<MessageAttachment>();
	private Type type = Type.NEW;

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		container.setWidget(display.asWidget());

		bindTo(eventBus);
	}

	private void bindTo(EventBus eventBus) {
		registerHandler(display.getSendClick().addClickHandler(sendClickHandler));
	}

	protected ClickHandler sendClickHandler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			if (!validate())
				return;
			sendReq = requestFactory.sendMessageRequest();
			message = sendReq.create(SmtpMessage.class);
			List<MessageAttachment> attaches = new ArrayList<MessageAttachment>();
			for (MessageAttachment attach : attachments) {// we must use
															// this, else
															// console will
															// complain a
															// NullPointerException
				MessageAttachment attachMent = sendReq.create(MessageAttachment.class);
				attachMent.setName(attach.getName());
				attachMent.setSize(attach.getSize());
				attachMent.setContentType(attach.getContentType());
				attaches.add(attachMent);
			}
			message.setFrom(display.getFromText());
			message.setSubject(display.getSubjectText().getText());
			message.setText(display.getMessageHTML().getHTML());
			message.setMessageAttachments(attaches);
			message.setTo(emailTextToArray(display.getToText().getText()));
			message.setCc(emailTextToArray(display.getCcText().getText()));
			message.setBcc(emailTextToArray(display.getBccText().getText()));

			if (type == Type.NEW) {
				SendMessageAction sendAction = sendReq.create(SendMessageAction.class);
				// SmtpMessage sm = sendReq.edit(message);
				sendAction.setMessage(message);
				sendReq.send(sendAction).fire(new Receiver<GenericResult>() {
					@Override
					public void onSuccess(GenericResult response) {
						afterSend(response);
					}
				});
			} else if (type == Type.FORWARD) {
				// SendForwardMessageRequest forwardReq =
				// requestFactory.sendForwardMessageRequest();
				// SendForwardMessageAction forwardAction =
				// forwardReq.create(SendForwardMessageAction.class);
				// forwardAction.setMessage(message);
				// forwardAction.setFolder(folder);
				// forwardAction.setUid(oldmessage.getUid());
				// forwardReq.send(forwardAction).fire(new
				// Receiver<GenericResult>() {
				// @Override
				// public void onSuccess(GenericResult response) {
				// afterSend(response);
				// }
				// });
			} else {
				// SendReplyMessageRequest replyReq =
				// requestFactory.sendReplyMessageRequest();
				// SendReplyMessageAction replyAction =
				// replyReq.create(SendReplyMessageAction.class);
				// replyAction.setMessage(message);
				// replyAction.setFolder(folder);
				// replyAction.setUid(oldmessage.getUid());
				// replyReq.send(replyAction).fire(new
				// Receiver<GenericResult>() {
				// @Override
				// public void onSuccess(GenericResult response) {
				// afterSend(response);
				// }
				// });
			}
		}
	};

	private boolean validate() {
		// Don't trust only in view validation
		return display.validate() && display.getToText().getText().trim().length() > 0
				&& EmailListValidator.isValidAddressList(display.getToText().getText())
				&& EmailListValidator.isValidAddressList(display.getCcText().getText())
				&& EmailListValidator.isValidAddressList(display.getBccText().getText());
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

		HasText getToText();

		HasText getCcText();

		HasText getBccText();

		HasText getSubjectText();

		HasClickHandlers getSendClick();

		boolean validate();

		HasText getMessageText();

		HasHTML getMessageHTML();
	}
}
