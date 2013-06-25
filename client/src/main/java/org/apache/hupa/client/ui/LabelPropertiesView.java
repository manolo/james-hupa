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

import org.apache.hupa.client.activity.LabelPropertiesActivity;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.rf.RenameFolderRequest;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.RenameFolderAction;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;

public class LabelPropertiesView extends Composite implements LabelPropertiesActivity.Displayable {

	@Inject HupaRequestFactory rf;
	
	@UiField TextBox _name;
	@UiField ListBox _parent;
	@UiField Button save;
	
	ImapFolder folder;

	@UiHandler("save")
	void handleCompose(ClickEvent e){
		RenameFolderRequest req = rf.renameFolderRequest();
		RenameFolderAction action = req.create(RenameFolderAction.class);
		action.setFolder(folder);
		action.setNewName(_name.getText());
		req.rename(action).fire(new Receiver<GenericResult>() {
			@Override
			public void onSuccess(GenericResult response) {
//				afterSend(response);
				System.out.println("success to rename");
			}
		});
	}
	public LabelPropertiesView() {
		initWidget(binder.createAndBindUi(this));
	}

	interface Binder extends UiBinder<DecoratorPanel, LabelPropertiesView> {
	}

	private static Binder binder = GWT.create(Binder.class);

	@Override
	public void cascade(LabelNode labelNode, List<LabelNode> list) {
		_name.setText(labelNode.getName());
		folder = labelNode.getFolder();
		if (!(labelNode.getFolder().getSubscribed())) {
			_name.setEnabled(false);
		} else {
			_name.setEnabled(true);
		}
		_parent.clear();
		for (LabelNode folderNode : list) {
			_parent.addItem(folderNode.getName(), folderNode.getPath());
		}
		_parent.setSelectedIndex(list.indexOf(labelNode.getParent()));
	}

}
