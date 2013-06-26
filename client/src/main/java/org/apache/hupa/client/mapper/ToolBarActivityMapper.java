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

import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.place.SettingPlace;
import org.apache.hupa.client.ui.ToolBarView.Parameters;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ToolBarActivityMapper extends MainActivityMapper {
	private final Provider<ToolBarActivity> toolBarActivityProvider;

	@Inject
	public ToolBarActivityMapper(Provider<ToolBarActivity> toolActivityProvider) {
		this.toolBarActivityProvider = toolActivityProvider;
	}

	@Override
	Activity asyncLoadActivity(final Place place) {
		if(place instanceof SettingPlace) return null;
		final ToolBarActivity tba = toolBarActivityProvider.get();
		if (place instanceof MailFolderPlace) { // might be from login page
			MailFolderPlace here = (MailFolderPlace) place;
			tba.getDisplay().setParameters(new Parameters(null, here.getToken(), null, null));
		}

		return new ActivityAsyncProxy() {
			@Override
			protected void doAsync(RunAsyncCallback callback) {
				GWT.runAsync(callback);
			}

			@Override
			protected Activity createInstance() {
				String token = null;
				if (place instanceof MailFolderPlace) {
					token = ((MailFolderPlace) place).getToken();
				}
				return tba.with(token);
			}
		};
	}
}
