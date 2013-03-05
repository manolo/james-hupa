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
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class __ComposePanel extends Composite {

	@UiField FlexTable headerTable;
	@UiField Style style;

	interface Style extends CssResource {
		String hiddenInput();

		String add();

		String iconlink();

		String formlinks();
		String left();
		String right();
		String operation();
	}

	public interface Resources extends ClientBundle {

		Resources INSTANCE = GWT.create(Resources.class);

		@NotStrict
		@Source("res/CssComposePanel.css")
		public Css stylesheet();

		public interface Css extends CssResource {
		}
	}

	public __ComposePanel() {
		initWidget(binder.createAndBindUi(this));
		FlexCellFormatter cellFormatter = headerTable.getFlexCellFormatter();
		RowFormatter rowFormatter = headerTable.getRowFormatter();

		// Add some text
		// cellFormatter.setHorizontalAlignment(0, 1,
		// HasHorizontalAlignment.ALIGN_RIGHT);

		headerTable.setWidget(0, 0, new Label("From"));

		headerTable.setWidget(1, 0, new Label("To"));
		headerTable.setWidget(2, 0, new Label("Cc"));
		headerTable.setWidget(3, 0, new Label("Bcc"));
		headerTable.setWidget(4, 0, new Label("Reply-To"));
		headerTable.setWidget(5, 0, new Label("Followup-To"));
		headerTable.setWidget(6, 0, new Label(""));
		headerTable.setWidget(7, 0, new Label("Subject"));
		// cellFormatter.setColSpan(0, 0, 2);

		// Add a button that will add more rows to the table
		ListBox lb = new ListBox();
		lb.addItem("echowdx#googlemail.com");
		lb.addItem("bar");
		Button addRowButton = new Button("Send message");
		Button removeRowButton = new Button("Save as draft");
		Button cancel = new Button("Cancel");
		FlowPanel operationPanel = new FlowPanel();
		FlowPanel contactPanel = new FlowPanel();
		FlowPanel buttonPanel = new FlowPanel();
		
		contactPanel.add(lb);
		contactPanel.addStyleName(style.left());
		// buttonPanel.add(new Anchor("Edit identities"));
		buttonPanel.add(addRowButton);
		buttonPanel.add(removeRowButton);
		buttonPanel.add(cancel);
		buttonPanel.addStyleName(style.right());
		operationPanel.add(contactPanel);
		operationPanel.add(buttonPanel);
		operationPanel.addStyleName(style.operation());
		headerTable.setWidget(0, 1, operationPanel);
		headerTable.setWidget(1, 1, create());

		headerTable.setWidget(2, 1, create());
		headerTable.setWidget(3, 1, create());
		headerTable.setWidget(4, 1, create());
		headerTable.setWidget(5, 1, create());

		FlowPanel linkPanel = new FlowPanel();
		Anchor cc = new Anchor("Add Cc");
		cc.addStyleName(style.iconlink());
		cc.addStyleName(style.add());
		Anchor bcc = new Anchor("Add Bcc");
		bcc.addStyleName(style.iconlink());
		bcc.addStyleName(style.add());
		Anchor replyTo = new Anchor("Add Reply-To");
		replyTo.addStyleName(style.iconlink());
		replyTo.addStyleName(style.add());
		Anchor followupTo = new Anchor("Add Followup-To");
		followupTo.addStyleName(style.iconlink());
		followupTo.addStyleName(style.add());
		linkPanel.add(cc);
		linkPanel.add(bcc);
		linkPanel.add(replyTo);
		linkPanel.add(followupTo);
		headerTable.setWidget(6, 1, linkPanel);
		cellFormatter.addStyleName(6, 1, style.formlinks());
		headerTable.setWidget(7, 1, create());

		rowFormatter.addStyleName(2, style.hiddenInput());
		rowFormatter.addStyleName(3, style.hiddenInput());
		rowFormatter.addStyleName(4, style.hiddenInput());
		rowFormatter.addStyleName(5, style.hiddenInput());

		// cellFormatter
		// .setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);

		// Add two rows to start
		// addRow(headerTable);
		// addRow(headerTable);

		// Return the panel
		headerTable.ensureDebugId("cwFlexTable");
	}

	private TextBox create() {
		TextBox t = new TextBox();
		t.setWidth("100%");
		return t;
	}

	interface __ComposePanelUiBinder extends
			UiBinder<DockLayoutPanel, __ComposePanel> {
	}

	private static __ComposePanelUiBinder binder = GWT
			.create(__ComposePanelUiBinder.class);

}
