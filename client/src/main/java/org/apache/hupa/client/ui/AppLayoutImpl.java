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

import static com.google.gwt.dom.client.Style.Unit.PCT;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AppLayoutImpl implements AppLayout {

	private static final int MAINMENU_HEIGHT = 10;
	private static final int VMASTER_WIDTH = 15;

	private final LayoutPanel mainLayoutPanel;

	interface AppLayoutUiBinder extends UiBinder<LayoutPanel, AppLayoutImpl> {
	}

	private static AppLayoutUiBinder binder = GWT.create(AppLayoutUiBinder.class);

	@UiField
	SimplePanel topPanel;

	@UiField
	SimplePanel westPanel;

	@UiField
	SimplePanel mainPanel;

	@Inject
	public AppLayoutImpl() {
		mainLayoutPanel = binder.createAndBindUi(this);
		setLoginLayout();
	}

	@Override
	public LayoutPanel getMainLayoutPanel() {
		return mainLayoutPanel;
	}


	@Override
	public AcceptsOneWidget getTopContainer() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				Widget widget = Widget.asWidgetOrNull(w);
				topPanel.setWidget(widget);
			}
		};
	}
	@Override
	public AcceptsOneWidget getWestContainer() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				Widget widget = Widget.asWidgetOrNull(w);
				westPanel.setWidget(widget);
			}
		};
	}

	@Override
	public AcceptsOneWidget getMainContainer() {
		return new AcceptsOneWidget() {
			@Override
			public void setWidget(IsWidget w) {
				Widget widget = Widget.asWidgetOrNull(w);
				mainPanel.setWidget(widget);
			}
		};
	}

	public void setDefaultLayout() {
		int height = 100 - MAINMENU_HEIGHT;
		mainLayoutPanel.setWidgetTopHeight(westPanel, MAINMENU_HEIGHT, PCT, height, PCT);
		mainLayoutPanel.setWidgetLeftWidth(westPanel, 0, PCT, VMASTER_WIDTH, PCT);
		mainLayoutPanel.setWidgetTopHeight(mainPanel, MAINMENU_HEIGHT, PCT, height, PCT);
		mainLayoutPanel.setWidgetLeftWidth(mainPanel, VMASTER_WIDTH, PCT, 100 - VMASTER_WIDTH, PCT);
		// mainLayoutPanel.animate(500);
	}

	public void setLoginLayout() {
		int height = 100 - MAINMENU_HEIGHT;
		mainLayoutPanel.setWidgetTopHeight(topPanel, 0, PCT, MAINMENU_HEIGHT, PCT);
		mainLayoutPanel.setWidgetTopHeight(westPanel, 0, PCT, 0, PCT);
		mainLayoutPanel.setWidgetTopHeight(mainPanel, MAINMENU_HEIGHT, PCT, height, PCT);
		mainLayoutPanel.setWidgetLeftWidth(mainPanel, 0, PCT, 100, PCT);
		// mainLayoutPanel.animate(500);
	}
}