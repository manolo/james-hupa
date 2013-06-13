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

import org.apache.hupa.client.activity.FolderListActivity;
import org.apache.hupa.client.place.ComposePlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class FolderListView extends Composite implements
		FolderListActivity.Displayable {
	@UiField SimplePanel thisView;
	private RightCellTree cellTree;
	// FIXME here we can not support another cell tree, since both of their
	// style
	// would not be cleared.
	private HTMLPanel contactPanel;

	@AssistedInject
	public FolderListView(final FoldersTreeViewModel viewModel,
			final EventBus eventBus, @Assisted Place place) {
		initWidget(binder.createAndBindUi(this));
//		if (place instanceof ComposePlace) {
//			contactPanel = new HTMLPanel("contacts list");
//			if (thisView.getWidget() != null
//					&& thisView.getWidget() instanceof CellTree) {
//				thisView.remove(cellTree);
//			}
//			thisView.add(contactPanel);
//		} else {
			cellTree = new RightCellTree(viewModel);
			cellTree.setAnimationEnabled(true);
//			if (thisView.getWidget() != null
//					&& thisView.getWidget() instanceof HTMLPanel) {
//				thisView.remove(contactPanel);
//			}
			thisView.add(cellTree);
//		}
	}

	interface FolderListUiBinder extends UiBinder<SimplePanel, FolderListView> {
	}

	private static FolderListUiBinder binder = GWT
			.create(FolderListUiBinder.class);

}
