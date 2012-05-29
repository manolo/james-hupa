package org.apache.hupa.client.activity;

import net.customware.gwt.dispatch.client.DispatchAsync;

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.HupaEvoCallback;
import org.apache.hupa.client.place.LoginPlace;
import org.apache.hupa.shared.rpc.LoginUser;
import org.apache.hupa.shared.rpc.LoginUserResult;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class LoginActivity extends AbstractActivity {

	private HupaConstants constants = GWT.create(HupaConstants.class);

	private LoginPlace place;
	private final Display display;
	private final EventBus eventBus;
	// private final HupaEvoCallback loginRpcService;
	private PlaceController placeController;

	private DispatchAsync dispatcher;
	private Provider<LoginPlace> oldGoToPlaceProvider;
	private Provider<LoginPlace> newGoToPlaceProvider;

	@Inject
	public LoginActivity(Display display, EventBus eventBus, PlaceController placeController, DispatchAsync dispatcher,
			Provider<LoginPlace> newGoToPlaceProvider) {
		this.display = display;
		this.eventBus = eventBus;
		this.placeController = placeController;
		this.dispatcher = dispatcher;
		this.newGoToPlaceProvider = newGoToPlaceProvider;
		// this.loginRpcService = loginRpcService;
	}

	public void init(LoginPlace place) {
		this.place = place;
	}

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		bind();
		container.setWidget(display.asWidget());

	}

	public void bind() {
		display.getLoginClick().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				doLogin();
			}
		});

	}

	private void doLogin() {
		String user = display.getUserNameValue().getValue().trim();
		String pass = display.getPasswordValue().getValue().trim();

		if (user.isEmpty() || pass.isEmpty())
			return;

		display.setLoading(true);
		dispatcher.execute(new LoginUser(user, pass), new HupaEvoCallback<LoginUserResult>(dispatcher, eventBus,
				display) {
			public void callback(LoginUserResult result) {
				display.setLoading(false);
				Window.alert("success");
				// eventBus.fireEvent(new LoginEvent(result.getUser()));
//				LoginActivity.this.placeController.goTo(newGoToPlaceProvider.get());
				doReset();
			}

			public void callbackError(Throwable caught) {
				display.setLoading(false);
				Window.alert("failure");
//				LoginActivity.this.placeController.goTo(newGoToPlaceProvider.get());
				// eventBus.fireEvent(new FlashEvent(constants.loginInvalid(),
				// 4000));
				doReset();
			}
		});
	}

	/**
	 * Reset display
	 */
	private void doReset() {
		display.getUserNameValue().setValue("");
		display.getPasswordValue().setValue("");
		display.getUserNameFocus().setFocus(true);
	}

	public interface Display {
		public HasClickHandlers getLoginClick();

		public HasClickHandlers getResetClick();

		public HasValue<String> getUserNameValue();

		public HasValue<String> getPasswordValue();

		public Focusable getUserNameFocus();

		public void setLoading(boolean loading);

		public Widget asWidget();
	}
}
