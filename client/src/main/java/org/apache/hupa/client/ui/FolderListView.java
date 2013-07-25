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

import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.activity.FolderListActivity;
import org.apache.hupa.client.activity.MessageListActivity;
import org.apache.hupa.client.activity.ToolBarActivity;
import org.apache.hupa.client.place.FolderPlace;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.shared.domain.ImapFolder;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class FolderListView extends Composite implements FolderListActivity.Displayable {
	@UiField ScrollPanel thisView;
	@Inject private HupaController controller;
	@Inject private ToolBarActivity.Displayable toolBar;
	@Inject private MessageListActivity.Displayable msgListDisplay;
	@Inject private PlaceController placeController;
	private CellList<LabelNode> cellList;

	public interface Resources extends CellList.Resources {

		Resources INSTANCE = GWT.create(Resources.class);

		@Source("res/CssLabelListView.css")
		public CellList.Style cellListStyle();
	}

	@Inject
	public FolderListView(final HupaRequestFactory rf) {
		initWidget(binder.createAndBindUi(this));

		data = new ImapLabelListDataProvider(rf);
		cellList = new CellList<LabelNode>(new FolderCell(), Resources.INSTANCE);
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				controller.showTopLoading("Loading...");
				toolBar.enableAllTools(false);
				placeController.goTo(new FolderPlace(selectionModel.getSelectedObject().getFolder().getFullName()));
				msgListDisplay.refresh();
			}
		});
		data.addDataDisplay(cellList);
		thisView.setWidget(cellList);
	}

	@Override
	public void refresh() {
		data.refresh();
	}

	public final SingleSelectionModel<LabelNode> selectionModel = new SingleSelectionModel<LabelNode>(
			new ProvidesKey<LabelNode>() {
				@Override
				public Object getKey(LabelNode item) {
					return item == null ? null : item.getPath();
				}
			});

	class FolderCell extends AbstractCell<LabelNode> {
		public FolderCell(String... consumedEvents) {
			super(consumedEvents);
		}
		// TODO different images for each folder
		@Override
		public void render(Context context, LabelNode value, SafeHtmlBuilder sb) {
			if (value != null) {
				sb.appendEscaped(value.getName());
			}
			if (value.getFolder().getUnseenMessageCount() > 0) {
				sb.appendHtmlConstant("<span style='position:absolute;right:6px;top:3px;font-weight:bold;'>(");
				sb.appendHtmlConstant("" + value.getFolder().getUnseenMessageCount());
				sb.appendHtmlConstant(")</span>");
			}
		}
	}

	private final ImapLabelListDataProvider data;

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

			final int start = display.getVisibleRange().getStart();

			rf.fetchFoldersRequest().fetch(null, Boolean.TRUE).fire(new Receiver<List<ImapFolder>>() {

				private String INTENTS = "&nbsp;&nbsp;&nbsp;&nbsp;";

				@Override
				public void onSuccess(List<ImapFolder> response) {
					folderNodes.clear();
					if (response == null || response.size() == 0) {
						updateRowCount(-1, true);
					} else {
						for (ImapFolder folder : response) {
							fillCellList(folderNodes, folder, LabelNode.ROOT, "");
						}
						updateRowData(start, folderNodes);
					}
				}

				private void fillCellList(List<LabelNode> folderNodes, ImapFolder curFolder, LabelNode parent,
						String intents) {
					LabelNode labelNode = new LabelNode();
					labelNode.setFolder(curFolder);
					labelNode.setName(curFolder.getName());
					labelNode.setNameForDisplay(intents + curFolder.getName());
					labelNode.setParent(parent);
					labelNode.setPath(curFolder.getFullName());
					folderNodes.add(labelNode);
					if (curFolder.getHasChildren()) {
						for (ImapFolder subFolder : curFolder.getChildren()) {
							fillCellList(folderNodes, subFolder, labelNode, intents + INTENTS);
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

	interface FolderListUiBinder extends UiBinder<SimplePanel, FolderListView> {
	}

	private static FolderListUiBinder binder = GWT.create(FolderListUiBinder.class);

}
