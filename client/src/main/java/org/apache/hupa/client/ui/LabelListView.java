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
import java.util.Collections;
import java.util.List;

import org.apache.hupa.client.activity.LabelListActivity;
import org.apache.hupa.client.activity.LabelPropertiesActivity;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.shared.domain.ImapFolder;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class LabelListView extends Composite implements LabelListActivity.Displayable {

	@Inject LabelPropertiesActivity.Displayable labelProperties;
	@UiField SimplePanel thisView;

	@UiField Button add;
	@UiField Button delete;

	@UiHandler("add")
	public void handleAdd(ClickEvent e) {
		labelProperties.cascade(selectionModel.getSelectedObject(), data.getDataList(), CASCADE_TYPE_ADD);
	}

	private final ImapLabelListDataProvider data;

	@Inject
	public LabelListView(final HupaRequestFactory rf) {
		initWidget(binder.createAndBindUi(this));
		data = new ImapLabelListDataProvider(rf);
		CellList<LabelNode> cellList = new CellList<LabelNode>(new LabelCell());
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				labelProperties.cascade(selectionModel.getSelectedObject(), data.getDataList(), CASCADE_TYPE_RENAME);
			}
		});
		data.addDataDisplay(cellList);
		thisView.setWidget(cellList);
	}

	@Override
	public SingleSelectionModel<LabelNode> getSelectionModel() {
		return this.selectionModel;
	}

	public final SingleSelectionModel<LabelNode> selectionModel = new SingleSelectionModel<LabelNode>(
			new ProvidesKey<LabelNode>() {
				@Override
				public Object getKey(LabelNode item) {
					return item == null ? null : item.getPath();
				}
			});

	static class LabelCell extends AbstractCell<LabelNode> {

		public LabelCell() {
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, LabelNode value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}

			if (value.getFolder().getSubscribed()) {
				sb.appendHtmlConstant(value.getName());
			} else {
				sb.appendHtmlConstant("<span style='color:gray;'>");
				sb.appendHtmlConstant(value.getName());
				sb.appendHtmlConstant("</span>");
			}
		}
	}

	public class ImapLabelListDataProvider extends AsyncDataProvider<LabelNode> implements HasRefresh {

		private HupaRequestFactory rf;
		private List<LabelNode> folderNodes = new ArrayList<LabelNode>();
		HasData<LabelNode> display;

		public List<LabelNode> getDataList() {
			return folderNodes;
		}

		public ImapLabelListDataProvider(HupaRequestFactory rf) {
			this.rf = rf;
		}

		@Override
		public void addDataDisplay(HasData<LabelNode> display) {
			super.addDataDisplay(display);
			this.display = display;
		}

		@Override
		protected void onRangeChanged(HasData<LabelNode> display) {

			rf.fetchFoldersRequest().fetch(null, Boolean.TRUE).fire(new Receiver<List<ImapFolder>>() {
				@Override
				public void onSuccess(List<ImapFolder> response) {
					folderNodes.clear();
					if (response == null || response.size() == 0) {
						updateRowCount(-1, true);
					} else {
						for (ImapFolder folder : response) {
							fillCellList(folderNodes, folder, LabelNode.ROOT);
						}
						updateRowData(0, folderNodes);
					}
				}

				private void fillCellList(List<LabelNode> folderNodes, ImapFolder curFolder, LabelNode parent) {
					LabelNode labelNode = new LabelNode();
					labelNode.setFolder(curFolder);
					labelNode.setName(curFolder.getName());
					labelNode.setParent(parent);
					labelNode.setPath(curFolder.getFullName());
					folderNodes.add(labelNode);
					if (curFolder.getHasChildren()) {
						for (ImapFolder subFolder : curFolder.getChildren()) {
							fillCellList(folderNodes, subFolder, labelNode);
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

		@Override
		public void refresh() {
			this.onRangeChanged(display);
		}
	}

	interface LabelListUiBinder extends UiBinder<DockLayoutPanel, LabelListView> {
	}

	private static LabelListUiBinder binder = GWT.create(LabelListUiBinder.class);

	@Override
	public HasClickHandlers getAdd() {
		return add;
	}

	@Override
	public HasClickHandlers getDelete() {
		return delete;
	}

	@Override
	public void refresh() {
		data.refresh();
	}

}
