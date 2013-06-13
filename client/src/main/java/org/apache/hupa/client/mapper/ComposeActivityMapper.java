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

package org.apache.hupa.client.mapper;

import org.apache.hupa.client.activity.ComposeActivity;
import org.apache.hupa.client.place.ComposePlace;
import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.place.MailFolderPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ComposeActivityMapper implements ActivityMapper {
	private final Provider<ComposeActivity> composeActivityProvider;

	@Inject protected PlaceController placeController;

	@Inject
	public ComposeActivityMapper(Provider<ComposeActivity> composeActivityProvider) {
		this.composeActivityProvider = composeActivityProvider;
	}

	public Activity getActivity(final Place place) {
		if (!(place instanceof ComposePlace))
			return null;
		final ComposePlace composePlace = (ComposePlace)place;
		if(composePlace.getParameters() == null) return null;
//		if(composePlace.getParameters() == null){
//			placeController.goTo(new MailFolderPlace("Mock-Inbox"));
//		}
		return new ActivityAsyncProxy() {
			@Override
			protected void doAsync(RunAsyncCallback callback) {
				GWT.runAsync(callback);
			}

			@Override
			protected Activity createInstance() {
				return composeActivityProvider.get().with(composePlace);
			}
		};
	}
}
