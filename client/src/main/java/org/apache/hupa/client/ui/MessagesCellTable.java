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

import java.util.Date;
import java.util.List;

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.bundles.HupaImageBundle;
import org.apache.hupa.client.place.AbstractPlace;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.place.MessagePlace;
import org.apache.hupa.client.rf.FetchMessagesRequest;
import org.apache.hupa.client.rf.GetMessageDetailsRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.GetMessageDetailsResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.events.RefreshUnreadEvent;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class MessagesCellTable extends DataGrid<Message> {

	public static final int PAGE_SIZE = 25;

	private HupaImageBundle imageBundle;
	CheckboxColumn checkboxCol = new CheckboxColumn();
	Column<Message, ?> fromCol = new FromColumn();
	Column<Message, ?> subjectCol = new SubjectColumn();
	Column<Message, ?> attachedCol = new AttachmentColumn();
	Column<Message, ?> dateCol = new DateColumn();

	public interface Resources extends DataGrid.Resources {

		Resources INSTANCE = GWT.create(Resources.class);

		@Source("res/CssMessagesCellTable.css")
		CustomStyle dataGridStyle();
	}

	public interface CustomStyle extends Style {
		String fontBold();
		String fontNormal();
	}

	public CheckboxColumn getCheckboxCol() {
		return checkboxCol;
	}

	public final ProvidesKey<Message> KEY_PROVIDER = new ProvidesKey<Message>() {
		@Override
		public Object getKey(Message item) {
			return item == null ? null : item.getUid();
		}
	};
	private final MultiSelectionModel<? super Message> selectionModel = new MultiSelectionModel<Message>(KEY_PROVIDER);
//	public final NoSelectionModel<Message> noSelectionModel = new NoSelectionModel<Message>(KEY_PROVIDER);

	PlaceController pc;
	HupaRequestFactory rf;

	private MessageListDataProvider dataProvider;

	public class MessageListDataProvider extends AsyncDataProvider<Message> implements HasRefresh {

		HasData<Message> display;

		@Override
		public void addDataDisplay(HasData<Message> display) {
			super.addDataDisplay(display);
			this.display = display;
		}

		@Override
		public void refresh() {
			this.onRangeChanged(display);
		}

		@Override
		protected void onRangeChanged(HasData<Message> display) {
			FetchMessagesRequest req = rf.messagesRequest();
			FetchMessagesAction action = req.create(FetchMessagesAction.class);
			final ImapFolder f = req.create(ImapFolder.class);
			f.setFullName(parseFolderName(pc));
			action.setFolder(f);
			action.setOffset(display.getVisibleRange().getLength());
			action.setSearchString(searchValue);
			action.setStart(display.getVisibleRange().getStart());
			req.fetch(action).fire(new Receiver<FetchMessagesResult>() {
				@Override
				public void onSuccess(final FetchMessagesResult response) {
					if (response == null || response.getRealCount() == 0) {
						updateRowCount(-1, true);
					} else {
						updateRowData(0, response.getMessages());
					}
					hc.hideTopLoading();
				}

				@Override
				public void onFailure(ServerFailure error) {
					if (error.isFatal()) {
						throw new RuntimeException(error.getMessage());
					}
					hc.hideTopLoading();
				}
			});

		}

	}

	@Inject private ToolBarActivity.Displayable toolBar;
	private String folderName;
	private String searchValue;
	@Inject protected HupaController hc;
	@Inject EventBus eventBus;

//	private HandlerRegistration selectionManagerReg = addCellPreviewHandler(DefaultSelectionEventManager
//			.<Message> createCheckboxManager(0));

	@Inject
	public MessagesCellTable(final HupaImageBundle imageBundle, final HupaConstants constants,
			final PlaceController pc, final HupaRequestFactory rf) {
		super(PAGE_SIZE, Resources.INSTANCE);
		this.pc = pc;
		this.rf = rf;
		this.imageBundle = imageBundle;

		CheckboxCell headerCheckbox = new CheckboxCell();
		ImageResourceCell headerAttached = new ImageResourceCell();
		Header<Boolean> header = new Header<Boolean>(headerCheckbox) {
			@Override
			public Boolean getValue() {
				return false;
			}
		};
		Header<ImageResource> attachedPin = new Header<ImageResource>(headerAttached) {
			@Override
			public ImageResource getValue() {
				return imageBundle.attachmentIcon();
			}
		};
		header.setUpdater(new ValueUpdater<Boolean>() {
			@Override
			public void update(Boolean value) {
				List<Message> displayedItems = MessagesCellTable.this.getVisibleItems();
				for (Message msg : displayedItems) {
					checkboxCol.getFieldUpdater().update(0, msg, value);
				}
			}
		});

		addColumn(checkboxCol, header);
		setColumnWidth(checkboxCol, 3, Unit.EM);
		addColumn(fromCol, constants.mailTableFrom());
		setColumnWidth(fromCol, 40, Unit.PCT);
		addColumn(subjectCol, constants.mailTableSubject());
		setColumnWidth(subjectCol, 60, Unit.PCT);
		addColumn(attachedCol, attachedPin);
		setColumnWidth(attachedCol, 33, Unit.PX);
		addColumn(dateCol, constants.mailTableDate());
		setColumnWidth(dateCol, 10, Unit.EM);
		setRowCount(PAGE_SIZE, false);
		this.setStyleBaseOnTag();
		// redraw();
		setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		setAutoHeaderRefreshDisabled(true);
		// setSelectionModel(selectionModel,
		// DefaultSelectionEventManager.<Message> createCheckboxManager(0));

		setSelectionModel(selectionModel, DefaultSelectionEventManager.<Message> createBlacklistManager(0));

//		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
//			@Override
//			public void onSelectionChange(SelectionChangeEvent event) {
//				message = eventselectionModel.get;
//				GetMessageDetailsRequest req = rf.messageDetailsRequest();
//				GetMessageDetailsAction action = req.create(GetMessageDetailsAction.class);
//				final ImapFolder f = req.create(ImapFolder.class);
//
//				f.setFullName(parseFolderName(pc));
//				action.setFolder(f);
//				action.setUid(message.getUid());
//				req.get(action).fire(new Receiver<GetMessageDetailsResult>() {
//					@Override
//					public void onSuccess(GetMessageDetailsResult response) {
//						// display.getGrid().getSelectionModel().setSelected(event.getValue(),
//						// true);
//						// noSelectionModel.setSelected(message, true);
//						toolBar.enableAllTools(true);
//						ToolBarView.Parameters p = new ToolBarView.Parameters(null, folderName, message, response
//								.getMessageDetails());
//						toolBar.setParameters(p);
//						MessagePlace place = new MessagePlace(folderName + AbstractPlace.SPLITTER + message.getUid());
//						refresh();
//						eventBus.fireEvent(new RefreshUnreadEvent());
//						pc.goTo(place);
//					}
//
//					@Override
//					public void onFailure(ServerFailure error) {
//						if (error.isFatal()) {
//							// log.log(Level.SEVERE, error.getMessage());
//							// TODO write the error message to
//							// status bar.
//							toolBar.enableAllTools(false);
//							throw new RuntimeException(error.getMessage());
//						}
//					}
//				});
//			}
//
//		});

		if (dataProvider == null) {
			dataProvider = new MessageListDataProvider();
			dataProvider.addDataDisplay(this);
		}
		refresh();
	}

	private String parseFolderName(final PlaceController pc) {
		Place place = pc.getWhere();
		if (place instanceof FolderPlace) {
			folderName = ((FolderPlace) place).getToken();
		} else if (place instanceof MessagePlace) {
			folderName = ((MessagePlace) place).getTokenWrapper().getFolder();
		}
		return folderName;
	}

	Message message; // the object selected by selectionModel

	public String getMessageStyle(Message row) {
		return haveRead(row) ? getReadStyle() : getUnreadStyle();
	}
	private String getUnreadStyle() {
		return Resources.INSTANCE.dataGridStyle().fontBold();
	}

	private String getReadStyle() {
		return Resources.INSTANCE.dataGridStyle().fontNormal();
	}

	private boolean haveRead(Message row) {
		return row.getFlags().contains(IMAPFlag.SEEN);
	}
	public void markRead(final Message message, final boolean read) {
		flush();
	}

	public class CheckboxColumn extends Column<Message, Boolean> {

		public CheckboxColumn() {
			super(new CheckboxCell(false, false));
			setFieldUpdater(new FieldUpdater<Message, Boolean>() {
				@Override
				public void update(int index, Message object, Boolean value) {
					selectionModel.setSelected(object, value);
					int size = selectionModel.getSelectedSet().size();
					if (size >= 1) {
						toolBar.enableDealingTools(true);
						toolBar.enableSendingTools(false);
					} else {
						toolBar.enableAllTools(false);
					}
				}
			});
		}

		@Override
		public Boolean getValue(Message object) {
			return selectionModel.isSelected(object);
		}
	}

	private class FromColumn extends Column<Message, String> {
		public FromColumn() {
			super(new TextCell());
		}

		@Override
		public String getValue(Message object) {
			return object.getFrom();
		}
	}

	private class SubjectColumn extends Column<Message, String> {
		public SubjectColumn() {
			super(new TextCell());
		}

		@Override
		public String getValue(Message object) {
			return object.getSubject();
		}
	}

	private class AttachmentColumn extends Column<Message, ImageResource> {
		public AttachmentColumn() {
			super(new ImageResourceCell());
		}

		@Override
		public ImageResource getValue(Message object) {
			return object.hasAttachment() ? imageBundle.attachmentIcon() : null;
		}
	}

	private class DateColumn extends Column<Message, Date> {
		private static final String DATE_FORMAT = "dd.MMM.yyyy";

		public DateColumn() {
			super(new DateCell(DateTimeFormat.getFormat(DATE_FORMAT)));
		}

		@Override
		public Date getValue(Message object) {
			return object.getReceivedDate();
		}
	}

	public void setStyleBaseOnTag() {
		setRowStyles(new RowStyles<Message>() {
			@Override
			public String getStyleNames(Message row, int rowIndex) {
				return getMessageStyle(row);
			}
		});
	}
	public void refresh() {
		dataProvider.refresh();
	}

}
