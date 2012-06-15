package org.apache.hupa.client.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.customware.gwt.dispatch.client.DispatchAsync;

import org.apache.hupa.client.HupaEvoCallback;
import org.apache.hupa.client.mvp.WidgetContainerDisplayable;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.client.widgets.IMAPTreeItem;
import org.apache.hupa.shared.data.IMAPFolder;
import org.apache.hupa.shared.data.User;
import org.apache.hupa.shared.rpc.FetchFolders;
import org.apache.hupa.shared.rpc.FetchFoldersResult;
import org.apache.hupa.shared.rpc.GenericResult;
import org.apache.hupa.shared.rpc.RenameFolder;
import org.apache.hupa.widgets.event.EditEvent;
import org.apache.hupa.widgets.event.EditHandler;
import org.apache.hupa.widgets.ui.HasEditable;
import org.apache.hupa.widgets.ui.HasEnable;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.inject.Inject;

public class WestActivity extends AbstractActivity {

	private final Displayable display;
	private final EventBus eventBus;
	private final PlaceController placeController;
	
    private DispatchAsync dispatcher;
    private User user;
    private IMAPFolder folder;
    private IMAPTreeItem tItem;
    private HasEditable editableTreeItem;
    
    @Inject
    public WestActivity(Displayable display, EventBus eventBus, PlaceController placeController,
			DispatchAsync dispatcher){
    	this.dispatcher = dispatcher;
    	this.display = display;
    	this.eventBus = eventBus;
    	this.placeController = placeController;
    	
    }

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		display.setUser(user);
		loadTreeItems();
		bind();
		container.setWidget(display.asWidget());
	}
	
    public WestActivity with(User user){
    	this.user = user;
    	return this;
    }

    protected void loadTreeItems() {
        display.setLoadingFolders(true);
        dispatcher.execute(new FetchFolders(), new HupaEvoCallback<FetchFoldersResult>(dispatcher, eventBus, display) {
            public void callback(FetchFoldersResult result) {
                display.bindTreeItems(createTreeNodes(result.getFolders()));
                // disable
                display.getDeleteEnable().setEnabled(false);
                display.getRenameEnable().setEnabled(false);
                display.setLoadingFolders(false);

            }
        });
    }

    /**
     * Create recursive the TreeNodes with all childs
     * 
     * @param list
     * @return
     */
    private List<IMAPTreeItem> createTreeNodes(List<IMAPFolder> list) {
        List<IMAPTreeItem> tList = new ArrayList<IMAPTreeItem>();

        for (IMAPFolder iFolder : list) {

            final IMAPTreeItem record = new IMAPTreeItem(iFolder);
            record.addEditHandler(new EditHandler() {

                public void onEditEvent(EditEvent event) {
                    if (event.getEventType().equals(EditEvent.EventType.Stop)) {
                        IMAPFolder iFolder = new IMAPFolder((String) event.getOldValue());
                        final String newName = (String) event.getNewValue();
                        if (iFolder.getFullName().equalsIgnoreCase(newName) == false) {
                            dispatcher.execute(new RenameFolder(iFolder, newName), new HupaEvoCallback<GenericResult>(dispatcher, eventBus) {
                                public void callback(GenericResult result) {
                                    folder.setFullName(newName);
                                }
                                public void callbackError(Throwable caught) {
                                    record.cancelEdit();
                                }
                            }); 
                        }
                    }
                }

            });
            record.setUserObject(iFolder);

            List<IMAPFolder> childFolders = iFolder.getChildIMAPFolders();
            List<IMAPTreeItem> items = createTreeNodes(childFolders);
            for (IMAPTreeItem item : items) {
                record.addItem(item);
            }

            // Store the INBOX as starting point after first loading
            if (iFolder.getFullName().equals(user.getSettings().getInboxFolderName())) {
                folder = iFolder;
                tItem = record;
            }

            tList.add(record);
        }

        // Sort tree
        Collections.sort(tList, new Comparator<TreeItem>() {

            public int compare(TreeItem o1, TreeItem o2) {
                return o1.getText().compareTo(o2.getText());
            }

        });
        return tList;
    }
	private void bind(){
		
	}

    public interface Displayable extends WidgetContainerDisplayable {
        
        public HasSelectionHandlers<TreeItem> getTree();

        public void bindTreeItems(List<IMAPTreeItem> treeList);

        public HasClickHandlers getRenameClick();

        public HasClickHandlers getDeleteClick();

        public HasClickHandlers getNewClick();

        public HasDialog getDeleteConfirmDialog();

        public HasClickHandlers getDeleteConfirmClick();

        public HasEnable getRenameEnable();

        public HasEnable getDeleteEnable();

        public HasEnable getNewEnable();

        public void updateTreeItem(IMAPFolder folder);

        public void deleteSelectedFolder();

        public HasEditable createFolder(EditHandler handler);

        public void increaseUnseenMessageCount(IMAPFolder folder, int amount);

        public void decreaseUnseenMessageCount(IMAPFolder folder, int amount);
        
        public void setLoadingFolders(boolean loading);
        public void setLoadingMessage(boolean loading);
        
        public void setUser(User user);

    }		


}
