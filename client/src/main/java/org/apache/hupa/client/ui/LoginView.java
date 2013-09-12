<<<<<<< HEAD
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

import org.apache.hupa.client.activity.LoginActivity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
=======
package org.apache.hupa.client.ui;

<<<<<<< HEAD
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
=======
import org.apache.hupa.client.HupaCSS;
import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.activity.LoginActivity;
import org.apache.hupa.widgets.ui.Loading;
import org.apache.hupa.widgets.ui.RndPanel;

>>>>>>> Change to new mvp framework - first step
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
<<<<<<< HEAD
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
=======
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.FormPanel;
<<<<<<< HEAD
<<<<<<< HEAD
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class LoginView extends Composite implements KeyUpHandler,
		LoginActivity.Displayable {

	/*
	 * invoke style lived in ui.xml should use this unique name, otherwise
	 * define by ourselves
	 */
	public interface Style extends CssResource {
		String loading();

		String hidden();

		String display();
	}

	@UiField Style style;
	@UiField FlowPanel mainContainer;
	@UiField FlowPanel innerBox;
	@UiField Button loginButton;
	@UiField FlexTable flexTable;
	@UiField FlowPanel boxBottom;
	@UiField FlowPanel messageBox;
	@UiField FlowPanel bottomLine;
	@UiField FormPanel formPanel;
	@UiField HTMLPanel message;
	Resources.Css css = Resources.INSTANCE.stylesheet();
	private SubmitButton submitButton;
	PPanel buttonBar = new PPanel();

	/*
	 * We wrap login/password boxes with a form which must be in the html
	 * document, in this way, the browser knows that we are sending a login form
	 * and offers the save password dialog to the user
	 */
	private TextBox usernameTextBox = TextBox.wrap(DOM.getElementById("email"));
	private PasswordTextBox passwordTextBox = PasswordTextBox.wrap(DOM
			.getElementById("password"));

	public interface Resources extends ClientBundle {

		Resources INSTANCE = GWT.create(Resources.class);

		@NotStrict
		@Source("res/CssLoginView.css")
		public Css stylesheet();

		public interface Css extends CssResource {
			String loginForm();

			String boxInner();

			String tdTitle();

			String tdInput();

			String pFormbuttons();

			String submitButton();

			String boxBottom();

			String messageBox();

			String bottomLine();
		}
	}

	@Inject
	public LoginView() {
		initWidget(binder.createAndBindUi(this));
		mainContainer.addStyleName(css.loginForm());
		innerBox.addStyleName(css.boxInner());
		formPanel = FormPanel.wrap(DOM.getElementById("loginForm"), true);
		submitButton = new SubmitButton("Login");
		submitButton.setStyleName(css.submitButton());
		bottomLine.addStyleName(css.bottomLine());
		buttonBar.add(submitButton);
		buttonBar.addStyleName(css.pFormbuttons());
		createLoginPrompt();
		flexTable.getFlexCellFormatter().setColSpan(2, 0, 2);
		flexTable.setWidget(2, 0, buttonBar);

		formPanel.add(flexTable);
		innerBox.add(formPanel);
=======
=======
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
>>>>>>> Change to new mvp framework - first step
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
		mainContainer.add(rPanel);
		mainContainer.add(loading);
		initWidget(mainContainer);
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter

		usernameTextBox.addKeyUpHandler(this);
		usernameTextBox.setFocus(true);
		passwordTextBox.addKeyUpHandler(this);

<<<<<<< HEAD
		/*
		 * The user submits the form so as the browser detect it and displays
		 * the save password dialog. Then we click on the hidden loginButton
		 * which stores the presenter clickHandler.
		 */
		formPanel.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				if (!usernameTextBox.getValue().trim().isEmpty()
						&& !passwordTextBox.getValue().trim().isEmpty()) {
=======
		loading.hide();

		// The user submits the form so as the browser detect it and displays
		// the save password dialog. Then we click on the hidden loginButton
		// which
		// stores the presenter clickHandler.
		formPanel.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				if (!usernameTextBox.getValue().trim().isEmpty() && !passwordTextBox.getValue().trim().isEmpty()) {
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
					loginButton.click();
				}
				// event.cancel();
			}
		});
<<<<<<< HEAD
		innerBox.add(loginButton);
		loginButton.setVisible(false);
		setLoading(false);
	}

	private void createLoginPrompt() {
		Label userNameLabel = new Label("username");
		Label passWordLabel = new Label("password");
		userNameLabel.addStyleName(css.tdTitle());
		passWordLabel.addStyleName(css.tdTitle());
		flexTable.setWidget(0, 0, userNameLabel);
		flexTable.setWidget(0, 1, usernameTextBox);
		flexTable.setWidget(1, 0, passWordLabel);
		flexTable.setWidget(1, 1, passwordTextBox);
		flexTable.getCellFormatter().addStyleName(0, 0, css.tdTitle());
		flexTable.getCellFormatter().addStyleName(1, 0, css.tdTitle());
		flexTable.getCellFormatter().addStyleName(0, 1, css.tdInput());
		flexTable.getCellFormatter().addStyleName(1, 1, css.tdInput());
	}

	public class PPanel extends SimplePanel {
		public PPanel() {
			super(Document.get().createPElement());
		}
=======
		// loginButton must be in the document to handle the click() method
		mainContainer.add(loginButton);
		loginButton.setVisible(false);
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
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
<<<<<<< HEAD
=======
	public HasClickHandlers getResetClick() {
		return resetButton;
	}

	@Override
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
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

<<<<<<< HEAD
	// FIXME the ajax loader will not hidden after normal logout
	@Override
	public void setLoading(boolean load) {
		if (load) {
			message.addStyleName(style.loading());
			message.addStyleName(style.display());
		} else {
			message.removeStyleName(style.loading());
			message.removeStyleName(style.display());
			message.addStyleName(style.hidden());
		}
=======
	@Override
	public void setLoading(boolean load) {
		if (load) {
			loading.show();
		} else {
			loading.hide();
		}

>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
	}

	@Override
	public Widget asWidget() {
		return this;
	}

<<<<<<< HEAD
	interface LoginViewUiBinder extends UiBinder<FlowPanel, LoginView> {
	}

	private static LoginViewUiBinder binder = GWT
			.create(LoginViewUiBinder.class);

=======
>>>>>>> change the LOGIN progress using native MVP instead of gwt-presenter
}