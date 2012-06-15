package org.apache.hupa.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;

public class TopView extends Composite implements IsWidget {

	interface TopViewUiBinder extends UiBinder<FlowPanel, TopView> {
	}

	private static TopViewUiBinder binder = GWT.create(TopViewUiBinder.class);

	public TopView() {
		FlowPanel panel = binder.createAndBindUi(this);
		initWidget(panel);
	}

}
