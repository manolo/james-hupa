package org.apache.hupa.client.activity;

import java.util.ArrayList;

import net.customware.gwt.dispatch.client.DispatchAsync;

import org.apache.hupa.client.mvp.WidgetDisplayable;
import org.apache.hupa.client.place.MailInboxPlace;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.Message;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.events.DecreaseUnseenEvent;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.events.LogoutEventHandler;
import org.apache.hupa.widgets.ui.HasEnable;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.gen2.table.event.client.HasPageChangeHandlers;
import com.google.gwt.gen2.table.event.client.HasPageLoadHandlers;
import com.google.gwt.gen2.table.event.client.HasRowSelectionHandlers;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.inject.Inject;
import com.google.inject.Provider;
@SuppressWarnings("deprecation")
public class IMAPMessageListActivity extends AbstractActivity {

    private String searchValue;
    private User user;
    private IMAPFolder folder;
    private ShowMessageTableListener tableListener = new ShowMessageTableListener();


	private final Displayable display;
	private final EventBus eventBus;
	private final PlaceController placeController;
	private final Provider<MailInboxPlace> mailInboxPlaceProvider;
	private DispatchAsync dispatcher;
    
	@Inject
    public IMAPMessageListActivity(Displayable display, EventBus eventBus, PlaceController placeController,
			Provider<MailInboxPlace> mailInboxPlaceProvider, DispatchAsync dispatcher){
		this.display = display;
		this.eventBus = eventBus;
		this.placeController = placeController;
		this.mailInboxPlaceProvider = mailInboxPlaceProvider;
		this.dispatcher = dispatcher;
		

        // add this event on constructor because we don't want to remove it on unbind
        eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {

            public void onLogout(LogoutEvent logoutEvent) {
            	IMAPMessageListActivity.this.display.reset();
            	IMAPMessageListActivity.this.display.getSearchValue().setValue("");
            }
            
        });
	}
	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		revealDisplay(user, folder, searchValue);
		bind();
		container.setWidget(display.asWidget());
	}
	
	private void bind(){
		
	}

	public IMAPMessageListActivity with(User user){
		this.user = user;
		this.folder = new IMAPFolder(user.getSettings().getInboxFolderName());
		return this;
	}

    protected void onRevealDisplay() {
        if (user != null && folder != null) {
            display.reloadData();  
        }
    }
    public void revealDisplay(User user, IMAPFolder folder, String searchValue) {
        this.user = user;
       
        if (this.user == null 
            || !this.user.getName().equals(user.getName()) 
            || this.folder == null 
            || !this.folder.getFullName().equals(folder.getFullName()) 
            || (searchValue == null && this.searchValue != null) 
            || (searchValue != null && searchValue.equals(this.searchValue) == false)) {
            display.reset();
            display.deselectAllMessages();
        }
        display.setExpandLoading(false);
        this.searchValue = searchValue;
        this.folder = folder;
        
        onRevealDisplay();
    }
	public interface Displayable extends WidgetDisplayable{
        public HasRowSelectionHandlers getDataTableSelection();
        public HasPageLoadHandlers getDataTableLoad();
        public void addTableListener(TableListener listener) ;
        public void removeTableListener(TableListener listener) ;
        public void setPostFetchMessageCount(int count);
        public HasClickHandlers getNewClick();
        public Message getData(int rowIndex);
        public HasClickHandlers getDeleteClick();
        public HasClickHandlers getDeleteAllClick();
        public HasEnable getDeleteEnable();
        public void reloadData();
        public void removeMessages(ArrayList<Message> messages);
        public ArrayList<Message> getSelectedMessages();
        public void reset();
        public HasDialog getConfirmDeleteDialog();
        public HasDialog getConfirmDeleteAllDialog();
        public HasClickHandlers getConfirmDeleteDialogClick();
        public HasClickHandlers getConfirmDeleteAllDialogClick();
        public void selectAllMessages();
        public void deselectAllMessages();
        public HasClickHandlers getSelectAllClick();
        public HasClickHandlers getSelectNoneClick();
        public HasClickHandlers getMarkSeenClick();
        public HasClickHandlers getMarkUnseenClick();
        public HasEnable getMarkSeenEnable();
        public HasEnable getMarkUnseenEnable();
        public HasClickHandlers getRefreshClick();
        public void redraw();
        public HasPageChangeHandlers getDataTablePageChange();
        public void goToPage(int page);
        public int getCurrentPage();
        public int getRowsPerPageIndex();
        public HasChangeHandlers getRowsPerPageChange();     
        public HasClickHandlers getSearchClick();
        public HasValue<String> getSearchValue();
        public void fillSearchOracle(ArrayList<Message> messages);
        public void setExpandLoading(boolean expanding);

    }

    
    private final class ShowMessageTableListener implements TableListener {

        public void onCellClicked(SourcesTableEvents sender, int row,
                int cell) {
            
            display.setExpandLoading(true);
            Message message = display.getData(row);
            
            // mark the message as seen and redraw the table to reflect this
            if (message.getFlags().contains(Message.IMAPFlag.SEEN) == false) {
                // add flag, fire event and redraw
                message.getFlags().add(Message.IMAPFlag.SEEN);
                eventBus.fireEvent(new DecreaseUnseenEvent(user,folder,1));
                
                display.redraw();

            }
            
            eventBus.fireEvent(new ExpandMessageEvent(user,folder,message));
        }

    }
}
