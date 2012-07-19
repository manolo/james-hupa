package org.apache.hupa.client.activity;

import org.apache.hupa.client.HupaConstants;
import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.ui.WidgetDisplayable;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.FlashEvent;
import org.apache.hupa.shared.events.FlashEventHandler;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;
import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.events.LogoutEventHandler;
import org.apache.hupa.shared.events.ServerStatusEvent;
import org.apache.hupa.shared.events.ServerStatusEvent.ServerStatus;
import org.apache.hupa.shared.events.ServerStatusEventHandler;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasText;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class TopActivity extends AbstractActivity {

	private static final int IDLE_INTERVAL = 150000;
	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		container.setWidget(display.asWidget());
		bind();
//		checkSession();
	}

	private void bind() {
		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {
			public void onLogin(LoginEvent event) {
				user = event.getUser();
				display.getUserText().setText(event.getUser().getName());
				noopTimer.scheduleRepeating(IDLE_INTERVAL);
				showMain(user);
				display.showMessage(constants.welcome(), 3000);
			}

		});
		eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {

			public void onLogout(LogoutEvent event) {
				User u = event.getUser();
				String username = null;
				if (u != null) {
					username = u.getName();
				}
				showLogin(username);
				noopTimer.cancel();
//				TopActivity.this.placeController.goTo(defaultPlaceProvider.get());
			}

		});
		display.getLogoutClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				doLogout();
			}

		});
		display.getContactsClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				showContacts();
			}

		});
		display.getMainClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				showMain(user);
			}

		});
		eventBus.addHandler(ServerStatusEvent.TYPE, new ServerStatusEventHandler() {

			public void onServerStatusChange(ServerStatusEvent event) {
				if (event.getStatus() != serverStatus) {
					GWT.log("Server status has hanged from " + serverStatus + " to" + event.getStatus(), null);
					serverStatus = event.getStatus();
					display.setServerStatus(serverStatus);
				}
			}

		});
		eventBus.addHandler(FlashEvent.TYPE, new FlashEventHandler() {

			public void onFlash(FlashEvent event) {
				display.showMessage(event.getMessage(), event.getMillisec());
			}

		});
	}

	private void checkSession() {
		System.out.println("+++++++++++++");
//		dispatcher.execute(new CheckSession(), new AsyncCallback<CheckSessionResult>() {
//			public void onFailure(Throwable caught) {
//				serverStatus = ServerStatus.Unavailable;
//				display.setServerStatus(serverStatus);
//				showLogin(null);
//			}
//			public void onSuccess(CheckSessionResult result) {
//				serverStatus = ServerStatus.Available;
//				display.setServerStatus(serverStatus);
//				if (result.isValid()) {
//					eventBus.fireEvent(new LoginEvent(result.getUser()));
//				} else {
//					showLogin(null);
//				}
//			}
//		});
	}
	private void doLogout() {
//		if (user != null) {
//			dispatcher.execute(new LogoutUser(), new HupaEvoCallback<LogoutUserResult>(dispatcher, eventBus) {
//				public void callback(LogoutUserResult result) {
//					eventBus.fireEvent(new LogoutEvent(result.getUser()));
//				}
//			});
//		}
	}

	private void showMain(User user) {
		display.showTopNavigation(true);
		display.showContactsButton();
	}

	private void showLogin(String username) {
		display.showTopNavigation(false);
	}

	private void showContacts() {
		display.showTopNavigation(true);
		display.showMainButton();
	}
	private Timer noopTimer = new IdleTimer();

	public interface Displayable extends WidgetDisplayable {
		public HasClickHandlers getLogoutClick();
		public HasClickHandlers getContactsClick();
		public HasClickHandlers getMainClick();
		public void showTopNavigation(boolean show);
		public void showContactsButton();
		public void showMainButton();
		public HasText getUserText();
		public void setServerStatus(ServerStatus status);
		public void showMessage(String message, int millisecs);
	}

	@Inject private Displayable display;
	@Inject private EventBus eventBus;
	@Inject private PlaceController placeController;
//	@Inject private DispatchAsync dispatcher;
//	@Inject private Provider<DefaultPlace> defaultPlaceProvider;
	@Inject private HupaConstants constants;
	
	private User user;
	private ServerStatus serverStatus = ServerStatus.Available;

	private class IdleTimer extends Timer {
		boolean running = false;
		public void run() {
			if (!running) {
				running = true;
//				dispatcher.execute(new Idle(), new HupaEvoCallback<IdleResult>(dispatcher, eventBus) {
//					public void callback(IdleResult result) {
//						running = false;
//						// check if the server is not supporting the Idle
//						// command.
//						// if so cancel this Timer
//						if (result.isSupported() == false) {
//							IdleTimer.this.cancel();
//						}
//						// Noop
//						// TODO: put code here to read new events from server
//						// (new messages ...)
//					}
//				});
			}
		}
	}

}
