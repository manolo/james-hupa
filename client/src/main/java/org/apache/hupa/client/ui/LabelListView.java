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

import java.util.ArrayList;
import java.util.List;

import org.apache.hupa.client.activity.LabelListActivity;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.shared.domain.ImapFolder;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class LabelListView extends Composite implements LabelListActivity.Displayable {

	@UiField SimplePanel thisView;

	@Inject
	public LabelListView(HupaRequestFactory rf) {
		initWidget(binder.createAndBindUi(this));
		ImapLabelListDataProvider data = new ImapLabelListDataProvider(rf);
		CellList<String> cellList = new CellList<String>(new TextCell());
		data.addDataDisplay(cellList);
		thisView.setWidget(cellList);
	}

	public class ImapLabelListDataProvider extends AsyncDataProvider<String> {

		private HupaRequestFactory rf;

		public ImapLabelListDataProvider(HupaRequestFactory rf) {
			this.rf = rf;
		}

		@Override
		public void addDataDisplay(HasData<String> display) {
			super.addDataDisplay(display);
		}

		@Override
		protected void onRangeChanged(HasData<String> display) {
			rf.fetchFoldersRequest().fetch(null, Boolean.TRUE).fire(new Receiver<List<ImapFolder>>() {
				@Override
				public void onSuccess(List<ImapFolder> response) {
					if (response == null || response.size() == 0) {
						updateRowCount(-1, true);
					} else {
						List<String> fn = new ArrayList<String>();
						for (ImapFolder a : response) {
							fillCellList(fn, a);
						}
						updateRowData(0, fn);
					}
				}

				private void fillCellList(List<String> fn, ImapFolder a) {
					fn.add(a.getFullName());
					if(a.getHasChildren()){
						for(ImapFolder subFolder : a.getChildren()){
							fillCellList(fn, subFolder);
						}
					}
				}

				@Override
				public void onFailure(ServerFailure error) {
					if (error.isFatal()) {
						throw new RuntimeException(error.getMessage());
					}
				}

			});

		}

	}

	interface LabelListUiBinder extends UiBinder<SimplePanel, LabelListView> {
	}

	private static LabelListUiBinder binder = GWT.create(LabelListUiBinder.class);

}
