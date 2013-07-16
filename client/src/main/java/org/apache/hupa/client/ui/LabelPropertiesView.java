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

import java.util.List;

import org.apache.hupa.client.HupaController;
import org.apache.hupa.client.activity.LabelListActivity;
import org.apache.hupa.client.activity.LabelPropertiesActivity;
import org.apache.hupa.client.rf.CreateFolderRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.rf.RenameFolderRequest;
import org.apache.hupa.shared.domain.CreateFolderAction;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.RenameFolderAction;
import org.apache.hupa.shared.events.RefreshLabelListEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class LabelPropertiesView extends Composite implements LabelPropertiesActivity.Displayable {

	@Inject HupaRequestFactory rf;
	@Inject HupaController hc;
	@Inject EventBus eventBus;
	
	@UiField TextBox name;
	@UiField ListBox parent;
	@UiField Button save;
	
	@UiField VerticalPanel propContainer;
	@UiField CaptionPanel information;
	
	private int state;

	ImapFolder folder;

	@UiHandler("save")
	void handleSave(ClickEvent e) {
		hc.showTopLoading("Saving...");
		if(state == LabelListActivity.Displayable.CASCADE_TYPE_RENAME){
			RenameFolderRequest req = rf.renameFolderRequest();
			RenameFolderAction action = req.create(RenameFolderAction.class);
			final ImapFolder f = req.create(ImapFolder.class);
			f.setFullName(folder.getFullName());
			action.setFolder(f);
			action.setNewName(name.getText());
			req.rename(action).fire(new Receiver<GenericResult>() {
				@Override
				public void onSuccess(GenericResult response) {
					hc.hideTopLoading();
					eventBus.fireEvent(new RefreshLabelListEvent());
					hc.showNotice("The label \"" + f.getFullName() + "\" has been renamed to "+name.getText(), 10000);
				}
				@Override
				public void onFailure(ServerFailure error) {
					hc.hideTopLoading();
					hc.showNotice(error.getMessage(), 10000);
				}
			});	
		} else if (state == LabelListActivity.Displayable.CASCADE_TYPE_ADD){
			CreateFolderRequest req = rf.createFolderRequest();
			CreateFolderAction action = req.create(CreateFolderAction.class);
			final ImapFolder f = req.create(ImapFolder.class);
			f.setFullName(folder.getFullName()+"/"+name.getText());
			action.setFolder(f);
			req.create(action).fire(new Receiver<GenericResult>(){
				@Override
				public void onSuccess(GenericResult response) {
					hc.hideTopLoading();
					eventBus.fireEvent(new RefreshLabelListEvent());
					hc.showNotice("The label \"" + f.getFullName() + "\" was created.", 10000);
				}
				@Override
				public void onFailure(ServerFailure error) {
					hc.hideTopLoading();
					hc.showNotice(error.getMessage(), 10000);
				}
			});
			
		}
	}
	public LabelPropertiesView() {
		initWidget(binder.createAndBindUi(this));
	}

	interface Binder extends UiBinder<DecoratorPanel, LabelPropertiesView> {
	}

	private static Binder binder = GWT.create(Binder.class);

	@Override
	public void cascade(LabelNode labelNode, List<LabelNode> wholeList, int type) {
		state = type;
		switch (type) {
		case LabelListActivity.Displayable.CASCADE_TYPE_ADD:
			makeParentList(labelNode, true, wholeList);
			name.setText("");
			information.setVisible(false);
			break;
		case LabelListActivity.Displayable.CASCADE_TYPE_RENAME:
			name.setText(labelNode.getName());
			makeParentList(labelNode, false, wholeList);
			information.setVisible(true);
			break;
		default:
			name.setText("");
		}
		folder = labelNode.getFolder();
		if (!(labelNode.getFolder().getSubscribed())) {
			name.setEnabled(false);
		} else {
			name.setEnabled(true);
		}
	}
	private void makeParentList(LabelNode labelNode, boolean isParent, List<LabelNode> wholeList) {
		parent.clear();
		parent.addItem("---", "root");
		for (LabelNode folderNode : wholeList) {
			parent.addItem(folderNode.getName(), folderNode.getPath());
		}
		int parentIndex = wholeList.indexOf(isParent ? labelNode : labelNode.getParent());
		parent.setSelectedIndex(parentIndex < 0 ? 0 : parentIndex + 1);
	}
	@Override
	public HasClickHandlers getSave() {
		return save;
	}

}
