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

package org.apache.hupa.client.mvp;

import org.apache.hupa.client.HupaConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AppView extends Composite implements AppPresenter.Display {
	private SimplePanel mainPanel = new SimplePanel();
	private HupaConstants constants = GWT.create(HupaConstants.class);
	private HorizontalPanel northTop = new HorizontalPanel();
	private Hyperlink logoutButton = new Hyperlink(constants.logoutButton(),"");
	private SimplePanel topNavigatorPanel = new SimplePanel();
	private HTML dummy = new HTML("&nbsp");
	private Label userName = new Label();
	private HorizontalPanel loginInfoPanel = new HorizontalPanel();

	public AppView() {
		VerticalPanel vPanel = new VerticalPanel();

		vPanel.setSpacing(3);
		vPanel.setWidth("100%");
		vPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		vPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);

		topNavigatorPanel.setHeight("20px");
		vPanel.add(topNavigatorPanel);

		loginInfoPanel.setSpacing(5);
		loginInfoPanel.add(new Label(constants.loginAs() + ":"));
		loginInfoPanel.add(userName);
		userName.getElement().getStyle().setProperty("textDecoration",
				"underline");
		userName.getElement().getStyle().setProperty("fontWeight", "bold");

		//northTop.setSpacing(5);
		northTop.setStyleName("hupa-MainButtonBar");
		northTop.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		northTop.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);

		northTop.setWidth("100%");
		northTop.setHeight("100%");
		northTop.add(loginInfoPanel);
		northTop.setCellHorizontalAlignment(loginInfoPanel,
				HorizontalPanel.ALIGN_LEFT);
		northTop.add(logoutButton);

		Label header = new Label(constants.productName());
		header.setHeight("25px");
		header.setStyleName("hupa-Header");
		vPanel.add(header);
		vPanel.setCellHorizontalAlignment(header, VerticalPanel.ALIGN_LEFT);
		vPanel.setCellVerticalAlignment(header, VerticalPanel.ALIGN_MIDDLE);

		vPanel.add(mainPanel);

		dummy.setHeight("100%");
		showTopNavigation(false);
		initWidget(vPanel);
	}

	public void setMain(Widget w) {
		mainPanel.setWidget(w);
	}

	public Widget asWidget() {
		return this;
	}

	public void startProcessing() {
		// TODO Auto-generated method stub

	}

	public void stopProcessing() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.AppPresenter.Display#getLogoutClick()
	 */
	public HasClickHandlers getLogoutClick() {
		return logoutButton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hupa.client.mvp.AppPresenter.Display#showTopNavigation(boolean
	 * )
	 */
	public void showTopNavigation(boolean show) {
		if (show) {
			topNavigatorPanel.setWidget(northTop);
		} else {
			topNavigatorPanel.setWidget(dummy);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hupa.client.mvp.AppPresenter.Display#getUserText()
	 */
	public HasText getUserText() {
		return userName;
	}

}