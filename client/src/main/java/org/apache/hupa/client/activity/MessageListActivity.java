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

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.hupa.client.place.AbstractPlace;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.MessagePlace;
import org.apache.hupa.client.rf.DeleteMessageByUidRequest;
import org.apache.hupa.client.rf.FetchMessagesRequest;
import org.apache.hupa.client.rf.GetMessageDetailsRequest;
import org.apache.hupa.client.ui.HasRefresh;
import org.apache.hupa.client.ui.MessagesCellTable;
import org.apache.hupa.client.ui.ToolBarView;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.domain.DeleteMessageByUidAction;
import org.apache.hupa.shared.domain.DeleteMessageResult;
import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.GetMessageDetailsResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.DeleteClickEvent;
import org.apache.hupa.shared.events.DeleteClickEventHandler;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.RefreshUnreadEvent;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class MessageListActivity extends AppBaseActivity {

	@Inject private Displayable display;
	@Inject private ToolBarActivity.Displayable toolBar;
	@Inject private TopBarActivity.Displayable topBar;
	private String folderName;
	private String searchValue;
	private User user;

	@Override
	public void start(AcceptsOneWidget container, final EventBus eventBus) {
		container.setWidget(display.asWidget());
		bindTo(eventBus);
		display.refresh();
		this.registerHandler(display.getGrid().addCellPreviewHandler(new Handler<Message>() {
			@Override
			public void onCellPreview(final CellPreviewEvent<Message> event) {
				if (hasClickedButFirstCol(event)) {
					antiSelectMessages(display.getGrid().getVisibleItems());
					GetMessageDetailsRequest req = rf.messageDetailsRequest();
					GetMessageDetailsAction action = req.create(GetMessageDetailsAction.class);
					final ImapFolder f = req.create(ImapFolder.class);
					f.setFullName(folderName);
					action.setFolder(f);
					action.setUid(event.getValue().getUid());
					req.get(action).fire(new Receiver<GetMessageDetailsResult>() {
						@Override
						public void onSuccess(GetMessageDetailsResult response) {
							eventBus.fireEvent(new ExpandMessageEvent(user, new ImapFolderImpl(folderName), event
									.getValue(), response.getMessageDetails()));
							// display.getGrid().getSelectionModel().setSelected(event.getValue(),
							// true);
							display.getGrid().getSelectionModel().setSelected(event.getValue(), true);
							toolBar.enableAllTools(true);
							ToolBarView.Parameters p = new ToolBarView.Parameters(user, folderName, event.getValue(),
									response.getMessageDetails());
							toolBar.setParameters(p);
							MessagePlace place = new MessagePlace(folderName + AbstractPlace.SPLITTER
									+ event.getValue().getUid());
							pc.goTo(place);
							display.refresh();
							eventBus.fireEvent(new RefreshUnreadEvent());
						}

						@Override
						public void onFailure(ServerFailure error) {
							if (error.isFatal()) {
								// log.log(Level.SEVERE, error.getMessage());
								// TODO write the error message to
								// status bar.
								toolBar.enableAllTools(false);
								throw new RuntimeException(error.getMessage());
							}
						}
					});
				}
			}

		}));
	}
	private boolean hasClickedButFirstCol(CellPreviewEvent<Message> event) {
		return "click".equals(event.getNativeEvent().getType()) && 0 != event.getColumn();
	}

	private void bindTo(EventBus eventBus) {
		eventBus.addHandler(DeleteClickEvent.TYPE, new DeleteClickEventHandler() {
			@Override
			public void onDeleteClickEvent(DeleteClickEvent event) {
				deleteSelectedMessages();
			}
		});

	}

	public MessageListActivity with(String folderName) {
		this.folderName = folderName;
		return this;
	}

	public interface Displayable extends WidgetDisplayable {
		MessagesCellTable getGrid();

		void refresh();

		List<Long> getSelectedMessagesIds();

		Set<Message> getSelectedMessages();
	}

	private void antiSelectMessages(Collection<Message> c) {
		toolBar.enableAllTools(false);
		for (Message msg : c) {
			if (!display.getGrid().getSelectionModel().isSelected(msg))
				continue;
			display.getGrid().getSelectionModel().setSelected(msg, false);
		}
	}
	private void deleteSelectedMessages() {
		hc.showTopLoading("Deleting...");
		String fullName = null;
		if (pc.getWhere() instanceof FolderPlace) {
			fullName = ((FolderPlace) pc.getWhere()).getToken();
		} else {
			fullName = ((MessagePlace) pc.getWhere()).getTokenWrapper().getFolder();
		}
		final List<Long> uids = display.getSelectedMessagesIds();
		DeleteMessageByUidRequest req = rf.deleteMessageByUidRequest();
		DeleteMessageByUidAction action = req.create(DeleteMessageByUidAction.class);
		ImapFolder f = req.create(ImapFolder.class);
		f.setFullName(fullName);
		action.setMessageUids(uids);
		action.setFolder(f);
		req.delete(action).fire(new Receiver<DeleteMessageResult>() {
			@Override
			public void onSuccess(DeleteMessageResult response) {
				antiSelectMessages(display.getSelectedMessages());
				display.refresh();
				hc.hideTopLoading();
				eventBus.fireEvent(new RefreshUnreadEvent());
			}
		});
	}
}
