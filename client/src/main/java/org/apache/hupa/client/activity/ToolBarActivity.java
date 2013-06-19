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

import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.place.ComposePlace;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.rf.SetFlagRequest;
import org.apache.hupa.client.ui.MessagesCellTable;
import org.apache.hupa.client.ui.ToolBarView.Parameters;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.SetFlagAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class ToolBarActivity extends AppBaseActivity {

	@Inject private Displayable display;
	@Inject private MessagesCellTable table;
	@Inject private MessageListActivity.Displayable messagesDisplay;
	@Inject private MessageListActivity messageListActivity;
	@Inject private HupaController hupaController;
	//FIXME messagesDisplay can not be injected into ToolBarView, why?
	private String folderName;

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		container.setWidget(display.asWidget());
		bindTo(eventBus);
	}

	public ToolBarActivity with(String folder) {
		this.folderName = folder;
		return this;
	}
	
	public Displayable getDisplay(){
		return display;
	}

	private void bindTo(EventBus eventBus) {

		registerHandler(display.getMark().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Widget source = (Widget) event.getSource();
				int left = source.getAbsoluteLeft();
				int top = source.getAbsoluteTop() + source.getOffsetHeight();
				display.getPopup().setPopupPosition(left, top);
				display.getPopup().show();
			}
		}));
		registerHandler(display.getMarkRead().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hupaController.showTopLoading("Loading");
				toMarkRead(true);
				display.getPopup().hide();
			}
		}));
		registerHandler(display.getMarkUnread().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hupaController.showTopLoading("Loading");
				toMarkRead(false);
				display.getPopup().hide();
			}
		}));
		registerHandler(display.getDelete().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {		
				if (!(placeController.getWhere() instanceof MailFolderPlace))
					return;
				messageListActivity.deleteSelectedMessages();
			}
		}));
	}

	protected void toMarkRead(boolean read) {
		List<Long> uids = new ArrayList<Long>();
		for (Message msg : table.getVisibleItems()) {
			if (table.getSelectionModel().isSelected(msg)) {
				uids.add(msg.getUid());
				table.markRead(msg, read);
			}
		}
		SetFlagRequest req = this.requestFactory.setFlagRequest();
		SetFlagAction action = req.create(SetFlagAction.class);
		ImapFolder f = req.create(ImapFolder.class);
		f.setFullName(folderName);
		action.setFolder(f);
		action.setFlag(IMAPFlag.SEEN);
		action.setValue(read);
		action.setUids(uids);
		req.set(action).fire(new Receiver<GenericResult>() {
			@Override
			public void onSuccess(GenericResult response) {
				hupaController.hideTopLoading();
				table.refresh();
				table.setStyleBaseOnTag();
			}
		});
	}

	public interface Displayable extends WidgetDisplayable {

		void enableSendingTools(boolean is);
		void enableDealingTools(boolean is);
		void enableAllTools(boolean is);

		HasClickHandlers getReply();

		HasClickHandlers getReplyAll();

		HasClickHandlers getForward();

		void setParameters(Parameters parameters);
		Parameters getParameters();

		HasClickHandlers getMarkUnread();

		HasClickHandlers getMarkRead();

		HasClickHandlers getMark();
		HasClickHandlers getDelete();

		PopupPanel getPopup();
	}
}
