package org.apache.hupa.client.activity;

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.rf.LoginUserRequest;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.FlashEvent;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.SessionExpireEvent;
import org.apache.hupa.shared.events.SessionExpireEventHandler;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class LoginActivity extends AbstractActivity {

	@Inject private Displayable display;
	@Inject private EventBus eventBus;
	@Inject private PlaceController placeController;
	// @Inject private DispatchAsync dispatcher;
	@Inject private HupaConstants constants;
	@Inject private HupaRequestFactory requestFactory;

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

		LoginUserRequest loginRequest = requestFactory.loginRequest();
		loginRequest.login(user, pass).fire(new Receiver<User>() {
			@Override
			public void onSuccess(User response) {
				placeController.goTo(new MailFolderPlace().with(response));
                eventBus.fireEvent(new LoginEvent(response));
			}
			@Override
			public void onFailure(ServerFailure error){
				placeController.goTo(new DefaultPlace());
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
