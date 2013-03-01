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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class HupaLayout implements HupaLayoutable {

	@UiField SimplePanel topBarContainer;

	@UiField SimplePanel logoContainer;
	@UiField SimpleLayoutPanel navigationContainer;

	@UiField SimplePanel toolBarContainer;

	@UiField SplitLayoutPanel messageSpace;
	@UiField DockLayoutPanel folderArea;

	@UiField SplitLayoutPanel messageArea;
	@UiField ScrollPanel folderListContainer;
	@UiField DockLayoutPanel messageListBox;
	@UiField SimpleLayoutPanel messageListContainer;
	@UiField SimplePanel messageListFooterContainer;
	@UiField ScrollPanel messageContentContainer;
	@UiField SimplePanel statusContainer;

	@UiField HTMLPanel contactBox;

	private LayoutPanel hupaMainPanel;

	public HupaLayout() {
		hupaMainPanel = binder.createAndBindUi(this);
		messageSpace.setWidgetMinSize(folderArea, 144);
		messageArea.setWidgetHidden(contactBox, true);
		messageArea.setWidgetMinSize(messageListBox, 130);
	}

	@Override
	public LayoutPanel get() {
		return hupaMainPanel;
	}

	interface HupaLayoutUiBinder extends UiBinder<LayoutPanel, HupaLayout> {
	}

	private static HupaLayoutUiBinder binder = GWT
			.create(HupaLayoutUiBinder.class);

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
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				toolBarContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

	@Override
	public AcceptsOneWidget getFolderListView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				folderListContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

	@Override
	public AcceptsOneWidget getMessageListView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				messageListContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

	@Override
	public AcceptsOneWidget getMessageListFooterView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				messageListFooterContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

	@Override
	public AcceptsOneWidget getMessageContentView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				messageContentContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

	@Override
	public AcceptsOneWidget getStatusView() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				statusContainer.setWidget(Widget.asWidgetOrNull(w));
			}
		};
	}

}
