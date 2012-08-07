package org.apache.hupa.client.ui;

import java.util.Date;

import org.apache.hupa.client.bundles.HupaImageBundle;
import org.apache.hupa.client.rf.FetchMessagesRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.MessageImpl;
import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.DecreaseUnseenEvent;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;
import org.apache.hupa.shared.events.LogoutEvent;
import org.apache.hupa.shared.events.LogoutEventHandler;
import org.apache.hupa.shared.events.MessagesReceivedEvent;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class MessagesCellTable extends CellTable<Message> {

	private static final int PAGE_SIZE = 15;
	
	private User user;
	private ImapFolder folder;
	private String searchValue;
	private EventBus eventBus;
	private HupaRequestFactory requestFactory;
	private HupaImageBundle imageBundle;
	private boolean pending;

	private SimplePager pager;
	private ListDataProvider<Message> dataProvider;
	
	public SimplePager getPager(){
		return pager;
	}
	@Inject
	public MessagesCellTable(final EventBus eventBus, final HupaRequestFactory requestFactory,
	        final HupaImageBundle imageBundle) {

		super(PAGE_SIZE);
		this.eventBus = eventBus;
		this.requestFactory = requestFactory;
		this.imageBundle = imageBundle;

		addColumn(new CheckboxColumn());
		addColumn(new FromColumn());
		addColumn(new SubjectColumn());
		addColumn(new AttachmentColumn());
		addColumn(new DateColumn());

		pager = new SimplePager();
		pager.setDisplay(this);
		dataProvider = new ListDataProvider<Message>();
		dataProvider.addDataDisplay(this);
		setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		addRangeChangeHandler(new RangeChangeEvent.Handler() {
			@Override
			public void onRangeChange(RangeChangeEvent event) {
				fetch(event.getNewRange().getStart());
			}
		});
		// bind some Events
		eventBus.addHandler(LoadMessagesEvent.TYPE, new LoadMessagesEventHandler() {

			public void onLoadMessagesEvent(LoadMessagesEvent loadMessagesEvent) {
				user = loadMessagesEvent.getUser();
				folder = loadMessagesEvent.getFolder();
				searchValue = loadMessagesEvent.getSearchValue();
				fetch(0);
//				if (!pending) {
//					pending = true;
//					Scheduler.get().scheduleFinally(new ScheduledCommand() {
//						@Override
//						public void execute() {
//							pending = false;
//							fetch(0);
//						}
//					});
//				}
				
			}
		});
		eventBus.addHandler(FolderSelectionEvent.TYPE, new FolderSelectionEventHandler() {

			public void onFolderSelectionEvent(FolderSelectionEvent event) {
				user = event.getUser();
				folder = event.getFolder();
				searchValue = null;
			}
		});
		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {

			public void onLogin(LoginEvent event) {
				user = event.getUser();
				folder = new ImapFolderImpl(user.getSettings().getInboxFolderName());
				searchValue = null;
				if (!pending) {
					pending = true;
					Scheduler.get().scheduleFinally(new ScheduledCommand() {
						@Override
						public void execute() {
							pending = false;
							fetch(0);
						}
					});
				}
			}
		});
		eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {

			public void onLogout(LogoutEvent logoutEvent) {
				user = null;
				folder = null;
				searchValue = null;
			}
		});

		// this.setRowData(values);

		setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      @Override
	      public void onSelectionChange(SelectionChangeEvent event) {
	    	  refreshSelection();
	      }
	    });

	}


	private final SingleSelectionModel<Message> selectionModel = new SingleSelectionModel<Message>();
	protected void refreshSelection() {
		Message message = selectionModel.getSelectedObject();
		if(message == null) return;
//		display.setExpandLoading(true);

		if (message.getFlags().contains(MessageImpl.IMAPFlag.SEEN) == false) {
			// add flag, fire event and redraw
			message.getFlags().add(MessageImpl.IMAPFlag.SEEN);
			eventBus.fireEvent(new DecreaseUnseenEvent(user, folder, 1));

//			display.redraw();

		}
		eventBus.fireEvent(new ExpandMessageEvent(user, folder, message));
	    selectionModel.setSelected(message, false);
	    
    }
	public void fetch(final int start) {
		FetchMessagesRequest messagesRequest = requestFactory.messagesRequest();
		FetchMessagesAction action = messagesRequest.create(FetchMessagesAction.class);
		final ImapFolder folder1 = messagesRequest.create(ImapFolder.class);
		folder1.setChildren(this.folder.getChildren());
		folder1.setDelimiter(this.folder.getDelimiter());
		folder1.setFullName(this.folder.getFullName());
		folder1.setMessageCount(this.folder.getMessageCount());
		folder1.setName(this.folder.getName());
		folder1.setSubscribed(this.folder.getSubscribed());
		folder1.setUnseenMessageCount(this.folder.getUnseenMessageCount());
		// FIXME cannot put setFolder to the first place
		action.setOffset(getPageSize());
		action.setFolder(folder1);
		action.setSearchString(searchValue);
		action.setStart(start);
		messagesRequest.fetch(action).fire(new Receiver<FetchMessagesResult>() {

			@Override
			public void onFailure(ServerFailure error) {
				if (error.isFatal()) {
					throw new RuntimeException(error.getMessage());
				}
			}
			@Override
			public void onSuccess(final FetchMessagesResult result) {
				assert result != null;
				MessagesCellTable.this.folder.setMessageCount(result.getRealCount());
				MessagesCellTable.this.folder.setUnseenMessageCount(result.getRealUnreadCount());
				dataProvider.setList(result.getMessages());
				setRowCount(result.getRealCount());
				if (result.getMessages() != null) {
					setRowData(start + getPageSize(), result.getMessages());
				} else {
					setRowData(start + getPageSize(), result.getMessages());
				}

	            pager.setPageStart(start);
	            if (start == 0 || !isRowCountExact()) {
	             setRowCount(start + result.getMessages().size(), result.getMessages().size() < getPageSize());
	            }
//				flush();
				// Notify presenter to update folder tree view
				eventBus.fireEvent(new MessagesReceivedEvent(folder1, result.getMessages()));
			}
		});
	}

	private class CheckboxColumn extends Column<Message, Boolean> {
		public CheckboxColumn() {
			super(new CheckboxCell());
		}
		@Override
		public Boolean getValue(Message object) {
			return true;
		}
	}

	private class FromColumn extends Column<Message, String> {
		public FromColumn() {
			super(new TextCell());
		}
		@Override
		public String getValue(Message object) {
			return object.getFrom();
		}
	}

	private class SubjectColumn extends Column<Message, String> {
		public SubjectColumn() {
			super(new TextCell());
		}
		@Override
		public String getValue(Message object) {
			return object.getSubject();
		}
	}

	private class AttachmentColumn extends Column<Message, ImageResource> {
		public AttachmentColumn() {
			super(new ImageResourceCell());
		}
		@Override
		public ImageResource getValue(Message object) {
			return object.hasAttachment() ? imageBundle.attachmentIcon() : null;
		}
	}

	private class DateColumn extends Column<Message, Date> {
		public DateColumn() {
			super(new DateCell(DateTimeFormat.getFormat("dd.MMM.yyyy")));
		}
		@Override
		public Date getValue(Message object) {
			return object.getReceivedDate();
		}
	}
}
