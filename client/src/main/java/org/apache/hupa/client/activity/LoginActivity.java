package org.apache.hupa.client.activity;

import net.customware.gwt.dispatch.client.DispatchAsync;

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.HupaEvoCallback;
import org.apache.hupa.client.mvp.WidgetDisplayable;
import org.apache.hupa.client.place.MailInboxPlace;
import org.apache.hupa.shared.events.FlashEvent;
import org.apache.hupa.shared.events.SessionExpireEvent;
import org.apache.hupa.shared.events.SessionExpireEventHandler;
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

	private final Displayable display;
	private final EventBus eventBus;
	private final PlaceController placeController;
	private final Provider<MailInboxPlace> mailInboxPlaceProvider;
	private DispatchAsync dispatcher;
    private HupaConstants constants = GWT.create(HupaConstants.class);

	@Inject
	public LoginActivity(Displayable display, EventBus eventBus, PlaceController placeController,
			Provider<MailInboxPlace> mailInboxPlaceProvider, DispatchAsync dispatcher) {
		this.display = display;
		this.eventBus = eventBus;
		this.placeController = placeController;
		this.mailInboxPlaceProvider = mailInboxPlaceProvider;
		this.dispatcher = dispatcher;
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
		display.getResetClick().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                doReset();
            }
            
        });
		eventBus.addHandler(SessionExpireEvent.TYPE, new SessionExpireEventHandler() {

            public void onSessionExpireEvent(SessionExpireEvent event) {
                eventBus.fireEvent(new FlashEvent(constants.sessionTimedOut(), 4000));
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
				// eventBus.fireEvent(new LoginEvent(result.getUser()));
				LoginActivity.this.placeController.goTo(mailInboxPlaceProvider.get().with(result.getUser()));
				doReset();
			}

			public void callbackError(Throwable caught) {
				display.setLoading(false);
				Window.alert("error");
				// eventBus.fireEvent(new FlashEvent(constants.loginInvalid(),4000));
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

	public interface Displayable extends WidgetDisplayable {
		public HasClickHandlers getLoginClick();

		public HasClickHandlers getResetClick();

		public HasValue<String> getUserNameValue();

		public HasValue<String> getPasswordValue();

		public Focusable getUserNameFocus();

		public void setLoading(boolean loading);

		public Widget asWidget();
	}
}
