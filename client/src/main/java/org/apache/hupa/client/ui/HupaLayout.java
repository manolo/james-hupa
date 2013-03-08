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
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class HupaLayout implements HupaLayoutable {

	@UiField SimplePanel topBarContainer;

	@UiField SimplePanel logoContainer;
	@UiField SimpleLayoutPanel navigationContainer;

	@UiField _ToolPanel toolPanel;

	@UiField _CenterPanel centerPanel;

	private LayoutPanel hupaMainPanel;

	public HupaLayout() {
		hupaMainPanel = binder.createAndBindUi(this);
	}

	@Override
	public LayoutPanel get() {
		return hupaMainPanel;
	}

	@Override
	public void switchToCompose() {
		if (isMessageOccupied()) {
			changeToCompose();
		}
	}

	@Override
	public void switchToMessage() {
		System.out.println("compose=====1"+(centerPanel.thisPanel.getWidgetIndex(centerPanel.composePanel)));
		System.out.println("content-----1"+(centerPanel.thisPanel.getWidgetIndex(centerPanel.contentPanel)));
		if (isMessageOccupied())
			return;
		if (isComposeOccupied()) {
			changeToMessage();
		}
		System.out.println("compose====="+(centerPanel.thisPanel.getWidgetIndex(centerPanel.composePanel)));
		System.out.println("content-----"+(centerPanel.thisPanel.getWidgetIndex(centerPanel.contentPanel)));
	}

	private void changeToCompose() {
		centerPanel.thisPanel.remove(centerPanel.contentPanel);
		centerPanel.thisPanel.add(centerPanel.composePanel);
		toolPanel.toggleToCompose(true);
		centerPanel.temporarilyHiddenTheUnimplementedContactPanel(true);
	}

	private boolean isMessageOccupied() {
		return centerPanel.thisPanel.getWidgetIndex(centerPanel.contentPanel) >= 0;
	}

	private void changeToMessage() {
		centerPanel.thisPanel.remove(centerPanel.composePanel);
		centerPanel.thisPanel.add(centerPanel.contentPanel);
		toolPanel.toggleToCompose(false);
		centerPanel.temporarilyHiddenTheUnimplementedContactPanel(false);
	}

	private boolean isComposeOccupied() {
		return centerPanel.thisPanel.getWidgetIndex(centerPanel.composePanel) >= 0;
	}

	@Override
	public AcceptsOneWidget getTopBarView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				topBarContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

	@Override
	public AcceptsOneWidget getLogoView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				logoContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

	@Override
	public AcceptsOneWidget getNavigationView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				navigationContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

	@Override
	public AcceptsOneWidget getToolBarView() {
		return toolPanel.getToolBarView();
	}

	@Override
	public AcceptsOneWidget getComposeToolBarView() {
		return toolPanel.getComposeToolBarView();
	}

	@Override
	public AcceptsOneWidget getFolderListView() {
		return centerPanel.getFolderListView();
	}

	@Override
	public AcceptsOneWidget getMessageListView() {
		return centerPanel.getMessageListView();
	}

	@Override
	public AcceptsOneWidget getMessageListFooterView() {
		return centerPanel.getMessageListFooterView();
	}

	@Override
	public AcceptsOneWidget getMessageContentView() {
		return centerPanel.getMessageContentView();
	}

	@Override
	public AcceptsOneWidget getStatusView() {
		return centerPanel.getStatusView();
	}

	@Override
	public AcceptsOneWidget getComposeHeader() {
		return centerPanel.getComposeHeader();
	}

	@Override
	public AcceptsOneWidget getComposeContent() {
		return centerPanel.getComposeContent();
	}

	@Override
	public AcceptsOneWidget getComposeStatus() {
		return centerPanel.getComposeStatus();
	}

	interface HupaLayoutUiBinder extends UiBinder<LayoutPanel, HupaLayout> {
	}

	private static HupaLayoutUiBinder binder = GWT
			.create(HupaLayoutUiBinder.class);
}
