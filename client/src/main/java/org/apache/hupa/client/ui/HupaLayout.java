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
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class HupaLayout implements HupaLayoutable {

	@UiField SimplePanel topBarContainer;

	@UiField SimplePanel logoContainer;
	@UiField SimplePanel notificationContainer;
	@UiField SimpleLayoutPanel navigationContainer;

	@UiField _ToolPanel toolPanel;

	@UiField _CenterPanel centerPanel;
	@UiField DockLayoutPanel mainBox;
	
	private _CenterSettingPanel settingPanel;

	private LayoutPanel hupaMainPanel;

	@Inject
	public HupaLayout(_CenterSettingPanel settingPanel) {
		this.settingPanel = settingPanel;
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
		if (isMessageOccupied())
			return;
		if (isComposeOccupied()) {
			changeToMessage();
		}
	}

	private void changeToCompose() {
		centerPanel.thisPanel.remove(centerPanel.contentPanel);
		centerPanel.thisPanel.add(centerPanel.composeContainer);
		toolPanel.toggleToCompose(true);
		centerPanel.temporarilyHiddenTheUnimplementedContactPanel(true);
	}

	private boolean isMessageOccupied() {
		return centerPanel.thisPanel.getWidgetIndex(centerPanel.contentPanel) >= 0;
	}

	private void changeToMessage() {
		centerPanel.thisPanel.remove(centerPanel.composeContainer);
		centerPanel.thisPanel.add(centerPanel.contentPanel);
		toolPanel.toggleToCompose(false);
		centerPanel.temporarilyHiddenTheUnimplementedContactPanel(false);
	}

	private boolean isComposeOccupied() {
		return centerPanel.thisPanel.getWidgetIndex(centerPanel.composeContainer) >= 0;
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
	public AcceptsOneWidget getNotificationView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				notificationContainer.setWidget(Widget.asWidgetOrNull(w));
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
	public AcceptsOneWidget getComposeView() {
		return centerPanel.getComposeView();
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
	public AcceptsOneWidget getLabelListView() {
		return settingPanel.getLabelListView();
	}

	interface HupaLayoutUiBinder extends UiBinder<LayoutPanel, HupaLayout> {
	}

	private static HupaLayoutUiBinder binder = GWT
			.create(HupaLayoutUiBinder.class);

	@Override
	public void switchToSetting() {
		centerPanel.removeFromParent();
		mainBox.add(settingPanel);
	}
}
