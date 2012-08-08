package org.apache.hupa.client.ioc;

import java.util.logging.Logger;

import org.apache.hupa.client.activity.IMAPMessageActivity;
import org.apache.hupa.client.activity.IMAPMessageListActivity;
import org.apache.hupa.client.activity.LoginActivity;
import org.apache.hupa.client.activity.MessageSendActivity;
import org.apache.hupa.client.activity.TopActivity;
import org.apache.hupa.client.activity.WestActivity;
import org.apache.hupa.client.dnd.PagingScrollTableRowDragController;
import org.apache.hupa.client.evo.AppController;
import org.apache.hupa.client.mapper.AppPlaceHistoryMapper;
import org.apache.hupa.client.mapper.CachingTopActivityMapper;
import org.apache.hupa.client.mapper.MainContentActivityMapper;
import org.apache.hupa.client.mapper.WestActivityMapper;
import org.apache.hupa.client.place.DefaultPlace;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.ui.AppLayout;
import org.apache.hupa.client.ui.AppLayoutImpl;
import org.apache.hupa.client.ui.IMAPMessageListView;
import org.apache.hupa.client.ui.IMAPMessageView;
import org.apache.hupa.client.ui.LoginView;
import org.apache.hupa.client.ui.MessageSendView;
import org.apache.hupa.client.ui.MessageTableModel;
import org.apache.hupa.client.ui.MessagesCellTable;
import org.apache.hupa.client.ui.TopView;
import org.apache.hupa.client.ui.WestView;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@SuppressWarnings("deprecation")
public class AppGinModule extends AbstractGinModule {
	public static Logger logger = Logger.getLogger(AppGinModule.class.getName());

	@Override
	protected void configure() {
		// Views
		bind(AppLayout.class).to(AppLayoutImpl.class).in(Singleton.class);

		// Activities
		bind(LoginActivity.Displayable.class).to(LoginView.class);
		bind(TopActivity.Displayable.class).to(TopView.class);
		bind(WestActivity.Displayable.class).to(WestView.class);
		bind(IMAPMessageListActivity.Displayable.class).to(IMAPMessageListView.class);
		bind(MessageSendActivity.Displayable.class).to(MessageSendView.class);
		bind(IMAPMessageActivity.Displayable.class).to(IMAPMessageView.class);
		
		bind(LoginActivity.class).in(Singleton.class);
		bind(TopActivity.class).in(Singleton.class);
		bind(WestActivity.class).in(Singleton.class);
		bind(IMAPMessageListActivity.class).in(Singleton.class);
		bind(MessageSendActivity.class).in(Singleton.class);
		bind(IMAPMessageActivity.class).in(Singleton.class);


		bind(PagingScrollTableRowDragController.class).in(Singleton.class);
		bind(MessageTableModel.class).in(Singleton.class);
		bind(MessagesCellTable.class).in(Singleton.class);
		// Places
		bind(PlaceHistoryMapper.class).to(AppPlaceHistoryMapper.class).in(Singleton.class);

		// Application EventBus
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);

		// Application Controller
		bind(AppController.class).in(Singleton.class);

		// bind(ExceptionHandler.class).to(DefaultExceptionHandler.class);
	}

	@Provides
	@Singleton
	@Named("TopRegion")
	public ActivityManager getTopRegionActivityMapper(CachingTopActivityMapper activityMapper, EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
	@Named("WestRegion")
	public ActivityManager getWestRegionActivityMapper(WestActivityMapper activityMapper, EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
	@Named("MainContentRegion")
	public ActivityManager getMainContentRegionActivityMapper(MainContentActivityMapper activityMapper,
	        EventBus eventBus) {
		return new ActivityManager(activityMapper, eventBus);
	}

	@Provides
	@Singleton
	public PlaceController getPlaceController(EventBus eventBus) {
		return new PlaceController(eventBus);
	}

	@Provides
	@Singleton
	public PlaceHistoryHandler getHistoryHandler(PlaceController placeController, PlaceHistoryMapper historyMapper,
	        EventBus eventBus) {
		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(placeController, eventBus, new DefaultPlace());
		return historyHandler;
	}

	@Provides
	@Singleton
	HupaRequestFactory getRequestFactory(EventBus eventBus) {
		HupaRequestFactory rf = GWT.create(HupaRequestFactory.class);
		rf.initialize(eventBus);
		return rf;
	}

}
