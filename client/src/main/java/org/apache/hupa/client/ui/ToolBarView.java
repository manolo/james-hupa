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

import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.place.ComposePlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;

public class ToolBarView extends Composite implements
		ToolBarActivity.Displayable {

	@Inject PlaceController placeController;
	
	@UiField Style style;

	@UiField Anchor refresh;
	@UiField Anchor compose;
	@UiField Anchor reply;
	@UiField HTMLPanel replyAllGroup;
	@UiField Anchor replyAll;
	@UiField HTMLPanel forwardGroup;
	@UiField Anchor forward;
	@UiField Anchor delete;
	@UiField Anchor mark;
	@UiField Anchor more;

	@UiHandler("compose")
	void handleClick(ClickEvent e) {
		placeController.goTo(new ComposePlace());
	}

	public ToolBarView() {
		initWidget(binder.createAndBindUi(this));
	}

	interface Style extends CssResource {
		String disabledButton();
	}

	@Override
	public void disableMessageTools() {
		reply.addStyleName(style.disabledButton());
		replyAllGroup.addStyleName(style.disabledButton());
		forwardGroup.addStyleName(style.disabledButton());
		delete.addStyleName(style.disabledButton());
	}

	@Override
	public void enableMessageTools() {
		reply.removeStyleName(style.disabledButton());
		replyAllGroup.removeStyleName(style.disabledButton());
		forwardGroup.removeStyleName(style.disabledButton());
		delete.removeStyleName(style.disabledButton());
	}

	interface ToolBarUiBinder extends UiBinder<FlowPanel, ToolBarView> {
	}

	private static ToolBarUiBinder binder = GWT.create(ToolBarUiBinder.class);

}
