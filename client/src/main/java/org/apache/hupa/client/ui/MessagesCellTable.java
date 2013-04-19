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
import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.bundles.HupaImageBundle;
import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.Message;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Inject;

public class MessagesCellTable extends DataGrid<Message> {

	public static final int PAGE_SIZE = 25;
	@Inject ToolBarActivity.Displayable display;

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
	private final SelectionModel<? super Message> selectionModel = new MultiSelectionModel<Message>(KEY_PROVIDER);

	@Inject
	public MessagesCellTable(final HupaImageBundle imageBundle, final HupaConstants constants) {
		super(PAGE_SIZE, Resources.INSTANCE);

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
					selectionModel.setSelected(msg, value);
				}
			}
		});

		addColumn(checkboxCol, header);
		setColumnWidth(checkboxCol, 3, Unit.EM);
		// addColumn(fromCol, new
		// SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant(constants.mailTableFrom())),
		// fromFooter);
		addColumn(fromCol, constants.mailTableFrom());
		setColumnWidth(fromCol, 40, Unit.PCT);
		addColumn(subjectCol, constants.mailTableSubject());
		setColumnWidth(subjectCol, 60, Unit.PCT);
		addColumn(attachedCol, attachedPin);
		setColumnWidth(attachedCol, 33, Unit.PX);
		addColumn(dateCol, constants.mailTableDate());
		setColumnWidth(dateCol, 10, Unit.EM);
		setRowCount(PAGE_SIZE, false);
		setRowStyles(new RowStyles<Message>() {
			@Override
			public String getStyleNames(Message row, int rowIndex) {
				return getMessageStyle(row);
			}
		});
//		redraw();
		setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		setAutoHeaderRefreshDisabled(true);
		setSelectionModel(selectionModel, DefaultSelectionEventManager.<Message> createCheckboxManager(0));
	}

	private String getMessageStyle(Message row) {
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
		setRowStyles(new RowStyles<Message>() {
			@Override
			public String getStyleNames(Message row, int rowIndex) {
				if (message.equals(row)) {
					return (read ? getReadStyle() : getUnreadStyle());
				} else {
					return getMessageStyle(row);// keep original
				}
			}
		});
		redraw();
	}

	public class CheckboxColumn extends Column<Message, Boolean> {

		public CheckboxColumn() {
			super(new CheckboxCell(false, false));
			setFieldUpdater(new FieldUpdater<Message, Boolean>() {
				@Override
				public void update(int index, Message object, Boolean value) {
					selectionModel.setSelected(object, value);
					display.disableMessageTools();
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

}
