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

package org.apache.hupa.client.activity;

import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.ExpandMessageEventHandler;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class ToolBarActivity extends AppBaseActivity {

	@Inject private Displayable display;

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		container.setWidget(display.asWidget());
		bindTo(eventBus);
	}

	private void bindTo(EventBus eventBus) {
		eventBus.addHandler(LoadMessagesEvent.TYPE,
				new LoadMessagesEventHandler() {
					public void onLoadMessagesEvent(
							LoadMessagesEvent loadMessagesEvent) {
						display.disableMessageTools();
					}
				});
		eventBus.addHandler(ExpandMessageEvent.TYPE,
				new ExpandMessageEventHandler() {
					public void onExpandMessage(ExpandMessageEvent event) {
						display.enableMessageTools();
					}
				});
	}

	public interface Displayable extends WidgetDisplayable {
		void disableMessageTools();

		void enableMessageTools();
	}
}
