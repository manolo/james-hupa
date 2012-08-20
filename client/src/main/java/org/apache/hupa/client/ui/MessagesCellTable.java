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
import org.apache.hupa.client.bundles.HupaImageBundle;
import org.apache.hupa.shared.domain.Message;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Inject;

public class MessagesCellTable extends DataGrid<Message> {

	public static final int PAGE_SIZE = 25;

	private HupaImageBundle imageBundle;
	CheckboxColumn checkboxCol = new CheckboxColumn();
	Column<Message, ?> fromCol = new FromColumn();
	Column<Message, ?> subjectCol = new SubjectColumn();
	Column<Message, ?> attachedCol = new AttachmentColumn();
	Column<Message, ?> dateCol = new DateColumn();

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
		super(PAGE_SIZE);
		this.imageBundle = imageBundle;

		CheckboxCell headerCheckbox = new CheckboxCell();
		Header<Boolean> header = new Header<Boolean>(headerCheckbox) {
			@Override
			public Boolean getValue() {
				return false;
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
		this.setColumnWidth(checkboxCol, 3, Unit.EM);
		addColumn(fromCol, constants.mailTableFrom());
		this.setColumnWidth(fromCol, 40, Unit.PCT);
		addColumn(subjectCol, constants.mailTableSubject());
		this.setColumnWidth(subjectCol, 60, Unit.PCT);
		addColumn(attachedCol, "Attached");// TODO i18n
		this.setColumnWidth(attachedCol, 7, Unit.EM);
		addColumn(dateCol, constants.mailTableDate());
		this.setColumnWidth(dateCol, 10, Unit.EM);
		setRowCount(PAGE_SIZE, false);
		setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		setSelectionModel(selectionModel, DefaultSelectionEventManager.<Message> createCheckboxManager(0));
	}

	public class CheckboxColumn extends Column<Message, Boolean> {

		public CheckboxColumn() {
			super(new CheckboxCell(false, false));
			setFieldUpdater(new FieldUpdater<Message, Boolean>() {
				@Override
				public void update(int index, Message object, Boolean value) {
					selectionModel.setSelected(object, value);
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
		public DateColumn() {
			super(new DateCell(DateTimeFormat.getFormat("dd.MMM.yyyy")));
		}

		@Override
		public Date getValue(Message object) {
			return object.getReceivedDate();
		}
	}
}
