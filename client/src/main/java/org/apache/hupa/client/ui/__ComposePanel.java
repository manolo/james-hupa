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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class __ComposePanel extends Composite {

	@UiField FlexTable headerTable;

	public __ComposePanel() {
		initWidget(binder.createAndBindUi(this));
		FlexCellFormatter cellFormatter = headerTable.getFlexCellFormatter();
		headerTable.setCellSpacing(5);
		headerTable.setCellPadding(3);

		// Add some text
		cellFormatter.setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_RIGHT);
		headerTable.setWidget(0, 0, new Label("From"));

		headerTable.setWidget(1, 0, new Label("To"));
		headerTable.setWidget(2, 0, new Label("Cc"));
		headerTable.setWidget(3, 0, new Label("Bcc"));
		headerTable.setWidget(4, 0, new Label("Reply-To"));
		headerTable.setWidget(5, 0, new Label("Followup-To"));
		headerTable.setWidget(6, 0, new Label("Links"));
		headerTable.setWidget(7, 0, new Label("Subject"));
//		cellFormatter.setColSpan(0, 0, 2);

		// Add a button that will add more rows to the table
		ListBox lb = new ListBox();
		lb.addItem("foo");
		lb.addItem("bar");
		Button addRowButton = new Button("Send message");
		Button removeRowButton = new Button("Save as draft");
		Button cancel = new Button("Cancel");
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(lb);
		buttonPanel.add(new Anchor("Edit identities"));
		buttonPanel.add(addRowButton);
		buttonPanel.add(removeRowButton);
		buttonPanel.add(cancel);
		headerTable.setWidget(0, 1, buttonPanel);
		headerTable.setWidget(1, 1, create());
		headerTable.setWidget(2, 1, create());
		headerTable.setWidget(3, 1, create());
		headerTable.setWidget(4, 1, create());
		headerTable.setWidget(5, 1, create());
		headerTable.setWidget(6, 1, create());
		headerTable.setWidget(7, 1, create());
		
//		cellFormatter
//				.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);

		// Add two rows to start
//		addRow(headerTable);
//		addRow(headerTable);

		// Return the panel
		headerTable.ensureDebugId("cwFlexTable");
	}

	private TextBox create() {
		TextBox t = new TextBox();
		t.setWidth("100%");
		return t;
	}

	/**
	 * Add a row to the flex table.
	 */
	private void addRow(FlexTable flexTable) {
		int numRows = flexTable.getRowCount();
		ListBox lb = new ListBox();
		lb.addItem("foo");
		lb.addItem("bar");
		Button addRowButton = new Button("cwFlexTableAddRow");
		Button removeRowButton = new Button("cwFlexTableRemoveRow");
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(new Anchor("logo"));
		buttonPanel.add(lb);
		buttonPanel.add(addRowButton);
		buttonPanel.add(removeRowButton);
		
		flexTable.setWidget(numRows, 0, buttonPanel);
		flexTable.setWidget(numRows, 1, new TextBox());
//		flexTable.getFlexCellFormatter().setRowSpan(0, 1, numRows + 1);
	}

	/**
	 * Remove a row from the flex table.
	 */
	private void removeRow(FlexTable flexTable) {
		int numRows = flexTable.getRowCount();
		if (numRows > 1) {
			flexTable.removeRow(numRows - 1);
			flexTable.getFlexCellFormatter().setRowSpan(0, 1, numRows - 1);
		}
	}

	interface __ComposePanelUiBinder extends
			UiBinder<DockLayoutPanel, __ComposePanel> {
	}

	private static __ComposePanelUiBinder binder = GWT
			.create(__ComposePanelUiBinder.class);

}
