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

package org.apache.hupa.client;

import org.apache.hupa.client.mapper.ActivityManagerInitializer;
import org.apache.hupa.client.place.ComposePlace;
import org.apache.hupa.client.rf.CheckSessionRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.ui.HupaLayoutable;
import org.apache.hupa.client.ui.LoginLayoutable;
import org.apache.hupa.client.ui.LoginView;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.LoginEvent;

import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class HupaController {

	private PlaceController placeController;
	private PlaceHistoryHandler placeHistoryHandler;
	@Inject private HupaLayoutable hupaLayout;
	@Inject private HupaRequestFactory requestFactory;
	@Inject private LoginLayoutable loginLayout;
	private EventBus eventBus;

	@Inject
	public HupaController(PlaceController placeController,
			PlaceHistoryHandler placeHistoryHandler,
			EventBus eventBus, 
			ActivityManagerInitializer initializeActivityManagerByGin) {
		this.placeController = placeController;
		this.placeHistoryHandler = placeHistoryHandler;
		this.eventBus = eventBus;
		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangHandler());
	}

	public void start() {
		bindCss();
		placeHistoryHandler.handleCurrentHistory();
	}

	private void bindCss() {
		// TODO:replace with a more gentle approach
		StyleInjector.inject(LoginView.Resources.INSTANCE.stylesheet()
				.getText());
	}

	private final class PlaceChangHandler implements PlaceChangeEvent.Handler {
		@Override
		public void onPlaceChange(PlaceChangeEvent event) {
			adjustLayout(event);
			checkSession();
		}
	}

	private void adjustLayout(PlaceChangeEvent event) {
		Place place = event.getNewPlace();
		if (place instanceof ComposePlace) {
			hupaLayout.switchToCompose();
		} else {
			hupaLayout.switchToMessage();
		}
	}

	private void checkSession() {
		CheckSessionRequest checkSession = requestFactory.sessionRequest();
		checkSession.getUser().fire(new Receiver<User>() {
			@Override
			public void onSuccess(User user) {
				if (user == null) {
					RootLayoutPanel.get().clear();
					RootLayoutPanel.get().add(loginLayout.get());
				} else {
					RootLayoutPanel.get().clear();
					RootLayoutPanel.get().add(hupaLayout.get());
                    eventBus.fireEvent(new LoginEvent(user));
				}
			}

			@Override
			public void onFailure(ServerFailure error) {
				RootLayoutPanel.get().clear();
				RootLayoutPanel.get().add(loginLayout.get());
			}
		});
//		checkSession.isValid().fire(new Receiver<Boolean>() {
//			@Override
//			public void onSuccess(Boolean sessionValid) {
//				if (!sessionValid) {
//					RootLayoutPanel.get().clear();
//					RootLayoutPanel.get().add(loginLayout.get());
//				} else {
//					RootLayoutPanel.get().clear();
//					RootLayoutPanel.get().add(hupaLayout.get());
//				}
//			}
//
//			@Override
//			public void onFailure(ServerFailure error) {
//				RootLayoutPanel.get().clear();
//				RootLayoutPanel.get().add(loginLayout.get());
//			}
//		});
	}
}
