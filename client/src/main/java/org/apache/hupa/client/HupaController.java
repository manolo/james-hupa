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

import org.apache.hupa.client.bundles.HupaResources;
import org.apache.hupa.client.evo.ActivityManagerInitializer;
import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.rf.CheckSessionRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.ui.AppLayout;
import org.apache.hupa.client.ui.HupaLayoutable;

import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class HupaController {

	@Inject private PlaceHistoryHandler placeHistoryHandler;
	@Inject private HupaLayoutable hupaLayout;
	@Inject private PlaceController placeController;
	@Inject private HupaRequestFactory requestFactory;
	private Place currentPlace;

	@Inject
	public HupaController(EventBus eventBus) {
		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangHandler());
	}

	public void start() {
		StyleInjector.inject(HupaResources.INSTANCE.stylesheet().getText());// TODO
																			// need
																			// this?
		RootLayoutPanel.get().add(hupaLayout.get());
		placeHistoryHandler.handleCurrentHistory();
	}

	private final class PlaceChangHandler implements PlaceChangeEvent.Handler {
		@Override
		public void onPlaceChange(PlaceChangeEvent event) {
			if (placeChange(event)) {
				checkSession();
			}
			refreshActivities(event);
		}

		private void refreshActivities(PlaceChangeEvent event) {
			Place newPlace = event.getNewPlace();
			if (newPlace != currentPlace) {
				if (isAuth(newPlace, currentPlace)) {
					// appPanelView.setDefaultLayout();
				} else if (newPlace instanceof DefaultPlace) {
					// appPanelView.setLoginLayout();
				}
				currentPlace = newPlace;
			}
		}

		private void checkSession() {
			CheckSessionRequest checkSession = requestFactory.sessionRequest();
			checkSession.isValid().fire(new Receiver<Boolean>() {
				@Override
				public void onSuccess(Boolean sessionValid) {
					if (!sessionValid) {
						HupaController.this.placeController
								.goTo(new DefaultPlace());
					}
				}
			});
		}

		private boolean placeChange(PlaceChangeEvent event) {
			return currentPlace != null
					&& !(currentPlace instanceof DefaultPlace)
					&& event.getNewPlace() != currentPlace;
		}

		private boolean isAuth(Place newPlace, Place currentPlace) {
			return (newPlace instanceof MailFolderPlace)
					&& !(currentPlace instanceof MailFolderPlace);
		}
	}

}
