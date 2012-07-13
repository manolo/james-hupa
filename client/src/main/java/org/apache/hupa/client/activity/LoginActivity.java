package org.apache.hupa.client.activity;

import net.customware.gwt.dispatch.client.DispatchAsync;

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.evo.HupaEvoCallback;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.shared.events.FlashEvent;
import org.apache.hupa.shared.events.SessionExpireEvent;
import org.apache.hupa.shared.events.SessionExpireEventHandler;
import org.apache.hupa.shared.rpc.LoginUser;
import org.apache.hupa.shared.rpc.LoginUserResult;

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

public class LoginActivity extends AbstractActivity {

	@Inject private Displayable display;
	@Inject private EventBus eventBus;
	@Inject private PlaceController placeController;
	@Inject private DispatchAsync dispatcher;
	@Inject private HupaConstants constants;

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
//		HupaRequestFactory rf = GWT.create(HupaRequestFactory.class);
//		rf.initialize(eventBus);
//		UserRequest userRequest = rf.userRequest();
//		UserProxy user1 = userRequest.create(UserProxy.class);
//		user1.setName(user);
//		user1.setPassword(pass);
//		userRequest.save(user1).fire(new Receiver<UserProxy>(){
//
//			@Override
//			public void onSuccess(UserProxy user) {
//				display.setLoading(false);
//				LoginActivity.this.placeController.goTo(new MailFolderPlace().with(null));
//				doReset();
//			}
//			
//		});
		
		
		
		dispatcher.execute(new LoginUser(user, pass), new HupaEvoCallback<LoginUserResult>(dispatcher, eventBus,
				display) {
			public void callback(LoginUserResult result) {
				display.setLoading(false);
				// eventBus.fireEvent(new LoginEvent(result.getUser()));
//				LoginActivity.this.placeController.goTo(mailInboxPlaceProvider.get().with(result.getUser()));
				LoginActivity.this.placeController.goTo(new MailFolderPlace().with(result.getUser()));
				doReset();
			}

			public void callbackError(Throwable caught) {
				display.setLoading(false);
				eventBus.fireEvent(new FlashEvent(constants.loginInvalid(), 4000));
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
