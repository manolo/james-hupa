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
import org.apache.hupa.client.ui.RightCellTree.Css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

public class FolderListView extends Composite implements FolderListActivity.Displayable {
	@UiField SimplePanel thisView;
	private CellTree cellTree;
	private FoldersTreeViewModel viewModel; 

	@Inject
	public FolderListView(final FoldersTreeViewModel viewModel) {
		initWidget(binder.createAndBindUi(this));
		this.viewModel = viewModel;
		cellTree = new CellTree(viewModel,null,Resources.INSTANCE);
//		cellTree.setAnimationEnabled(true);
		thisView.add(cellTree);
	}
	public interface Resources extends CellTree.Resources {

		Resources INSTANCE = GWT.create(Resources.class);

		@Source("res/CssFolderListView.css")
		public Css cellTreeStyle();

		@Source("res/listicons.png")
		public ImageResource listicons();
	}
	
	@Override
	public void refresh(){
		viewModel.refresh();
	}
	
	interface FolderListUiBinder extends UiBinder<SimplePanel, FolderListView> {
	}

	private static FolderListUiBinder binder = GWT.create(FolderListUiBinder.class);

}
