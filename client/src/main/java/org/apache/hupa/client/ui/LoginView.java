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

import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.activity.LoginActivity;
import org.apache.hupa.widgets.ui.Loading;
import org.apache.hupa.widgets.ui.RndPanel;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class LoginView extends Composite implements KeyUpHandler, LoginActivity.Displayable {
	private Button loginButton = new Button();
	private SubmitButton submitButton;
	private Button resetButton;
	private Loading loading;
	// We wrap login/password boxes with a form which must be in the html
	// document,
	// in this way, the browser knows that we are sending a login form and
	// offers the save password dialog to the user
	private TextBox usernameTextBox = TextBox.wrap(DOM.getElementById("email"));
	private PasswordTextBox passwordTextBox = PasswordTextBox.wrap(DOM.getElementById("password"));
	// wrap the form after inputs so as they are in the dom when are wrapped
	final private FormPanel formPanel = FormPanel.wrap(DOM.getElementById("loginForm"), true);

	@Inject
	public LoginView(HupaConstants constants) {

		VerticalPanel mainContainer = new VerticalPanel();
		RndPanel rPanel = new RndPanel();
		FlexTable flexTable = new FlexTable();
		Panel buttonBar = new FlowPanel();
		submitButton = new SubmitButton(constants.loginButton());
		resetButton = new Button(constants.resetButton());
		submitButton.getElement().setClassName(HupaCSS.C_button);
		resetButton.getElement().setClassName(HupaCSS.C_button);
		submitButton.getElement().setClassName(resetButton.getElement().getClassName());
		loading = new Loading(constants.loading());

		mainContainer.setStyleName(HupaCSS.C_login_container);
		flexTable.addStyleName(HupaCSS.C_login_form);
		usernameTextBox.addStyleName(HupaCSS.C_login_box);
		passwordTextBox.addStyleName(HupaCSS.C_login_box);

		buttonBar.add(submitButton);
		buttonBar.add(resetButton);

		flexTable.setText(0, 0, constants.usernameLabel());
		flexTable.setWidget(0, 1, usernameTextBox);
		flexTable.setText(1, 0, constants.passwordLabel());
		flexTable.setWidget(1, 1, passwordTextBox);
		flexTable.getFlexCellFormatter().setColSpan(2, 0, 2);
		flexTable.setWidget(2, 0, buttonBar);

		rPanel.add(formPanel);
		formPanel.add(flexTable);
		mainContainer.add(formPanel);
		mainContainer.add(loading);

		usernameTextBox.addKeyUpHandler(this);
		usernameTextBox.setFocus(true);
		passwordTextBox.addKeyUpHandler(this);

		loading.hide();

		// The user submits the form so as the browser detect it and displays
		// the save password dialog. Then we click on the hidden loginButton
		// which
		// stores the presenter clickHandler.
		formPanel.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				if (!usernameTextBox.getValue().trim().isEmpty() && !passwordTextBox.getValue().trim().isEmpty()) {
					loginButton.click();
				}
				// event.cancel();
			}
		});
		// loginButton must be in the document to handle the click() method
		mainContainer.add(loginButton);
		loginButton.setVisible(false);
		initWidget(mainContainer);
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			if (event.getSource().equals(usernameTextBox)) {
				passwordTextBox.setFocus(true);
			} else if (event.getSource().equals(passwordTextBox)) {
				submitButton.click();
				// formPanel.submit();
			}
		}

	}

	@Override
	public HasClickHandlers getLoginClick() {
		return loginButton;
	}

	@Override
	public HasClickHandlers getResetClick() {
		return resetButton;
	}

	@Override
	public HasValue<String> getUserNameValue() {
		return usernameTextBox;
	}

	@Override
	public HasValue<String> getPasswordValue() {
		return passwordTextBox;
	}

	@Override
	public Focusable getUserNameFocus() {
		return usernameTextBox;
	}

	@Override
	public void setLoading(boolean load) {
		if (load) {
			loading.show();
		} else {
			loading.hide();
		}

	}

	@Override
	public Widget asWidget() {
		return this;
	}

}
