package org.apache.hupa.client.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.hupa.client.activity.MessageSendActivity.Type;
import org.apache.hupa.client.place.IMAPMessagePlace;
import org.apache.hupa.client.place.MailFolderPlace;
import org.apache.hupa.client.place.MessageSendPlace;
import org.apache.hupa.client.rf.CreateFolderRequest;
import org.apache.hupa.client.rf.DeleteFolderRequest;
import org.apache.hupa.client.rf.FetchMessagesRequest;
import org.apache.hupa.client.rf.GetMessageDetailsRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.client.rf.ImapFolderRequest;
import org.apache.hupa.client.rf.RenameFolderRequest;
import org.apache.hupa.client.ui.WidgetContainerDisplayable;
import org.apache.hupa.client.widgets.HasDialog;
import org.apache.hupa.client.widgets.IMAPTreeItem;
import org.apache.hupa.shared.data.ImapFolderImpl;
import org.apache.hupa.shared.data.MessageImpl.IMAPFlag;
import org.apache.hupa.shared.domain.CreateFolderAction;
import org.apache.hupa.shared.domain.DeleteFolderAction;
import org.apache.hupa.shared.domain.GenericResult;
import org.apache.hupa.shared.domain.GetMessageDetailsAction;
import org.apache.hupa.shared.domain.GetMessageDetailsResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Message;
import org.apache.hupa.shared.domain.MessageDetails;
import org.apache.hupa.shared.domain.RenameFolderAction;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.BackEvent;
import org.apache.hupa.shared.events.BackEventHandler;
import org.apache.hupa.shared.events.DecreaseUnseenEvent;
import org.apache.hupa.shared.events.DecreaseUnseenEventHandler;
import org.apache.hupa.shared.events.ExpandMessageEvent;
import org.apache.hupa.shared.events.ExpandMessageEventHandler;
import org.apache.hupa.shared.events.FolderSelectionEvent;
import org.apache.hupa.shared.events.FolderSelectionEventHandler;
import org.apache.hupa.shared.events.ForwardMessageEvent;
import org.apache.hupa.shared.events.ForwardMessageEventHandler;
import org.apache.hupa.shared.events.IncreaseUnseenEvent;
import org.apache.hupa.shared.events.IncreaseUnseenEventHandler;
import org.apache.hupa.shared.events.LoadMessagesEvent;
import org.apache.hupa.shared.events.LoadMessagesEventHandler;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;
import org.apache.hupa.shared.events.MessagesReceivedEvent;
import org.apache.hupa.shared.events.MessagesReceivedEventHandler;
import org.apache.hupa.shared.events.NewMessageEvent;
import org.apache.hupa.shared.events.NewMessageEventHandler;
import org.apache.hupa.shared.events.ReplyMessageEvent;
import org.apache.hupa.shared.events.ReplyMessageEventHandler;
import org.apache.hupa.shared.events.SentMessageEvent;
import org.apache.hupa.shared.events.SentMessageEventHandler;
import org.apache.hupa.widgets.event.EditEvent;
import org.apache.hupa.widgets.event.EditHandler;
import org.apache.hupa.widgets.ui.HasEditable;
import org.apache.hupa.widgets.ui.HasEnable;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class WestActivity extends AbstractActivity {

	@Override
	public void start(AcceptsOneWidget container, EventBus eventBus) {
		display.setUser(user);
		loadTreeItems();
		bind();
		container.setWidget(display.asWidget());
	}

	@Inject private Displayable display;
	@Inject private EventBus eventBus;
	@Inject private PlaceController placeController;
	@Inject private Provider<IMAPMessagePlace> IMAPMessagePlaceProvider;
	@Inject private Provider<MessageSendPlace> messageSendPlaceProvider;
	@Inject private Provider<IMAPMessagePlace> messagePlaceProvider;
	@Inject private HupaRequestFactory requestFactory;

	private User user;
	private ImapFolder folder;
	private IMAPTreeItem tItem;
	private HasEditable editableTreeItem;
	private String searchValue;

	private Place currentPlace;

	public void setCurrentPlace(Place place) {
		this.currentPlace = place;
	}

	public WestActivity with(MailFolderPlace place) {
		this.currentPlace = place;
		this.user = place.getUser();
		return this;
	}

	protected void loadTreeItems() {
		display.setLoadingFolders(true);
		HupaRequestFactory rf = GWT.create(HupaRequestFactory.class);
		rf.initialize(eventBus);
		ImapFolderRequest folderRequest = rf.folderRequest();
		folderRequest.requestFolders().fire(new Receiver<List<ImapFolder>>() {
			@Override
			public void onSuccess(List<ImapFolder> response) {
				display.bindTreeItems(createTreeNodes(response));
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
	private List<IMAPTreeItem> createTreeNodes(List<ImapFolder> list) {
		List<IMAPTreeItem> tList = new ArrayList<IMAPTreeItem>();

		for (ImapFolder iFolder : list) {

			final IMAPTreeItem record = new IMAPTreeItem(iFolder);
			record.addEditHandler(new EditHandler() {

				public void onEditEvent(EditEvent event) {
					if (event.getEventType().equals(EditEvent.EventType.Stop)) {
						ImapFolder iFolder = new ImapFolderImpl((String) event.getOldValue());
						final String newName = (String) event.getNewValue();
						if (iFolder.getFullName().equalsIgnoreCase(newName) == false) {
							RenameFolderRequest req = requestFactory.renameFolderRequest();
							RenameFolderAction action = req.create(RenameFolderAction.class);
							action.setNewName(newName);
							action.setFolder(iFolder);
							req.rename(action).fire(new Receiver<GenericResult>() {
								@Override
								public void onSuccess(GenericResult response) {
									folder.setFullName(newName);
								}
								@Override
								public void onFailure(ServerFailure error) {
									record.cancelEdit();
									GWT.log("Error while renaming" + error.getStackTraceString());
								}
							});
						}
					}
				}

			});
			record.setUserObject(iFolder);

			List<ImapFolder> childFolders = iFolder.getChildren();
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

	private void bind() {
		eventBus.addHandler(LoadMessagesEvent.TYPE, new LoadMessagesEventHandler() {

			public void onLoadMessagesEvent(LoadMessagesEvent loadMessagesEvent) {
				showMessageTable(loadMessagesEvent.getUser(), loadMessagesEvent.getFolder(),
				        loadMessagesEvent.getSearchValue());
			}

		});
		eventBus.addHandler(ExpandMessageEvent.TYPE, new ExpandMessageEventHandler() {

			public void onExpandMessage(ExpandMessageEvent event) {
				final boolean decreaseUnseen;
				final Message message = event.getMessage();
				// check if the message was already seen in the past
				if (event.getMessage().getFlags().contains(IMAPFlag.SEEN) == false) {
					decreaseUnseen = true;
				} else {
					decreaseUnseen = false;
				}

				display.setLoadingMessage(true);
				GetMessageDetailsRequest req = requestFactory.messageDetailsRequest();
				GetMessageDetailsAction action = req.create(GetMessageDetailsAction.class);
				action.setFolder(event.getFolder());
				action.setUid(message.getUid());
				req.get(action).fire(new Receiver<GetMessageDetailsResult>() {

					@Override
					public void onSuccess(GetMessageDetailsResult response) {

						if (decreaseUnseen) {
							eventBus.fireEvent(new DecreaseUnseenEvent(user, folder));
						}
						display.setLoadingMessage(false);
//						showMessage(user, folder, message, response.getMessageDetails());

						placeController.goTo(messagePlaceProvider.get().with(user, folder, message,
						        response.getMessageDetails()));
					}
				});
				// dispatcher.execute(new
				// GetMessageDetails(event.getFolder(),
				// message.getUid()), new
				// HupaEvoCallback<GetMessageDetailsResult>(dispatcher,
				// eventBus, display) {
				// public void callback(GetMessageDetailsResult result)
				// {
				// if (decreaseUnseen) {
				// eventBus.fireEvent(new DecreaseUnseenEvent(user,
				// folder));
				// }
				// display.setLoadingMessage(false);
				// // showMessage(user, folder, message,
				// result.getMessageDetails());
				//
				// placeController.goTo(messagePlaceProvider.get().with(user,folder,
				// message,result.getMessageDetails()));
				// }
				// });
			}

		});
		eventBus.addHandler(NewMessageEvent.TYPE, new NewMessageEventHandler() {

			public void onNewMessageEvent(NewMessageEvent event) {
				showNewMessage();
			}

		});
		eventBus.addHandler(SentMessageEvent.TYPE, new SentMessageEventHandler() {

			public void onSentMessageEvent(SentMessageEvent ev) {
				showMessageTable(user, folder, searchValue);
			}

		});
		eventBus.addHandler(ForwardMessageEvent.TYPE, new ForwardMessageEventHandler() {

			public void onForwardMessageEvent(ForwardMessageEvent event) {
				showForwardMessage(event);
			}

		});
		eventBus.addHandler(ReplyMessageEvent.TYPE, new ReplyMessageEventHandler() {

			public void onReplyMessageEvent(ReplyMessageEvent event) {
				showReplyMessage(event);
			}

		});
		eventBus.addHandler(FolderSelectionEvent.TYPE, new FolderSelectionEventHandler() {

			public void onFolderSelectionEvent(FolderSelectionEvent event) {
				user = event.getUser();
				folder = event.getFolder();
				showMessageTable(user, event.getFolder(), searchValue);
			}

		});
		eventBus.addHandler(BackEvent.TYPE, new BackEventHandler() {

			public void onBackEvent(BackEvent event) {
				showMessageTable(user, folder, searchValue);
			}

		});
		eventBus.addHandler(ExpandMessageEvent.TYPE, new ExpandMessageEventHandler() {

			public void onExpandMessage(ExpandMessageEvent event) {
				if (editableTreeItem != null && editableTreeItem.isEdit()) {
					editableTreeItem.cancelEdit();
				}
			}

		});
		eventBus.addHandler(NewMessageEvent.TYPE, new NewMessageEventHandler() {

			public void onNewMessageEvent(NewMessageEvent event) {
				if (editableTreeItem != null && editableTreeItem.isEdit()) {
					editableTreeItem.cancelEdit();
				}
			}

		});
		eventBus.addHandler(DecreaseUnseenEvent.TYPE, new DecreaseUnseenEventHandler() {

			public void onDecreaseUnseenEvent(DecreaseUnseenEvent event) {
				display.decreaseUnseenMessageCount(event.getFolder(), event.getAmount());
			}

		});
		eventBus.addHandler(IncreaseUnseenEvent.TYPE, new IncreaseUnseenEventHandler() {

			public void onIncreaseUnseenEvent(IncreaseUnseenEvent event) {
				display.increaseUnseenMessageCount(event.getFolder(), event.getAmount());
			}

		});
		display.getTree().addSelectionHandler(new SelectionHandler<TreeItem>() {

			public void onSelection(SelectionEvent<TreeItem> event) {
				tItem = (IMAPTreeItem) event.getSelectedItem();
				if (tItem.isEdit())
					return;
				ImapFolder editableFolder = (ImapFolder) tItem.getUserObject();
				FetchMessagesRequest req = requestFactory.messagesRequest();
				folder = req.edit(editableFolder);

				// folder = (ImapFolder) tItem.getUserObject();
				eventBus.fireEvent(new LoadMessagesEvent(user, folder));
				if (folder.getFullName().equalsIgnoreCase(user.getSettings().getInboxFolderName())) {
					display.getDeleteEnable().setEnabled(false);
					display.getRenameEnable().setEnabled(false);
				} else {
					display.getDeleteEnable().setEnabled(true);
					display.getRenameEnable().setEnabled(true);
				}
			}

		});
		// FIXME why another?
		// display.getTree().addSelectionHandler(new
		// SelectionHandler<TreeItem>() {
		//
		// public void onSelection(SelectionEvent<TreeItem> event) {
		// tItem = (IMAPTreeItem) event.getSelectedItem();
		// if (tItem.isEdit())
		// return;
		// folder = (ImapFolder) tItem.getUserObject();
		// if
		// (folder.getFullName().equalsIgnoreCase(user.getSettings().getInboxFolderName()))
		// {
		// display.getDeleteEnable().setEnabled(false);
		// display.getRenameEnable().setEnabled(false);
		// } else {
		// display.getDeleteEnable().setEnabled(true);
		// display.getRenameEnable().setEnabled(true);
		// }
		// }
		//
		// });
		display.getRenameClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				tItem.startEdit();
			}

		});
		display.getDeleteClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				display.getDeleteConfirmDialog().show();
			}

		});
		display.getDeleteConfirmClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				DeleteFolderRequest req = requestFactory.deleteFolderRequest();
				final DeleteFolderAction action = req.create(DeleteFolderAction.class);
				action.setFolder(folder);
				req.delete(action).fire(new Receiver<GenericResult>() {
					@Override
					public void onSuccess(GenericResult response) {
						display.deleteSelectedFolder();
					}
					@Override
					public void onFailure(ServerFailure error) {
						GWT.log("Error while deleting" + error.getStackTraceString());
					}
				});
			}

		});
		display.getNewClick().addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				editableTreeItem = display.createFolder(new EditHandler() {
					public void onEditEvent(EditEvent event) {
						final IMAPTreeItem item = (IMAPTreeItem) event.getSource();
						final String newValue = (String) event.getNewValue();
						if (event.getEventType().equals(EditEvent.EventType.Stop)) {
							CreateFolderRequest req = requestFactory.createFolderRequest();
							final CreateFolderAction action = req.create(CreateFolderAction.class);
							ImapFolder folder = req.create(ImapFolder.class);
							folder.setFullName(newValue.trim());
							action.setFolder(folder);
							req.create(action).fire(new Receiver<GenericResult>() {
								@Override
								public void onSuccess(GenericResult response) {
									// Nothing todo
								}
								@Override
								public void onFailure(ServerFailure error) {
									GWT.log("Error while create folder" + error.getStackTraceString());
									item.cancelEdit();
								}
							});
						}
					}

				});
			}

		});
		eventBus.addHandler(MessagesReceivedEvent.TYPE, new MessagesReceivedEventHandler() {

			public void onMessagesReceived(MessagesReceivedEvent event) {
				ImapFolder f = event.getFolder();
				display.updateTreeItem(f);
			}

		});
		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {

			public void onLogin(LoginEvent event) {
				user = event.getUser();
				// folder = (IMAPFolderProxy)new
				// IMAPFolder(user.getSettings().getInboxFolderName());;
				searchValue = null;
				// showMessageTable(user, folder, searchValue);
			}

		});

		exportJSMethods(this);
	}

	public void openLink(String url) {
		Window.open(url, "_blank", "");
	}

	public void mailTo(String mailto) {
		// sendPresenter.revealDisplay(user, mailto);
	}

	private native void exportJSMethods(WestActivity westactivity) /*-{
	                                                               $wnd.openLink = function(url) {
	                                                               try {
	                                                               westactivity.@org.apache.hupa.client.activity.WestActivity::openLink(Ljava/lang/String;) (url);
	                                                               } catch(e) {}
	                                                               return false;
	                                                               };
	                                                               $wnd.mailTo = function(mail) {
	                                                               try {
	                                                               westactivity.@org.apache.hupa.client.activity.WestActivity::mailTo(Ljava/lang/String;) (mail);
	                                                               } catch(e) {}
	                                                               return false;
	                                                               };
	                                                               }-*/;

	private void showMessageTable(User user, ImapFolder folder, String searchValue) {
		this.user = user;
		this.folder = folder;
		this.searchValue = searchValue;

		// FIXME goto?
		placeController.goTo(new MailFolderPlace().with(user, folder, searchValue));
		// placeController.goTo(mailInboxPlaceProvider.get().with(user));
		// System.out.println("111");
		// placeController.goTo(new
		// MailInboxPlace(folder.getName()).with(user));
	}

	private void showMessage(User user, ImapFolder folder, Message message, MessageDetails details) {
		placeController.goTo(IMAPMessagePlaceProvider.get());
	}

	private void showNewMessage() {
		placeController.goTo(this.messageSendPlaceProvider.get().with(user, null, null, null, Type.NEW));
	}

	private void showForwardMessage(ForwardMessageEvent event) {
		placeController.goTo(this.messageSendPlaceProvider.get().with(event.getUser(), event.getFolder(),
		        event.getMessage(), event.getMessageDetails(), Type.FORWARD));
	}

	private void showReplyMessage(ReplyMessageEvent event) {
		placeController.goTo(this.messageSendPlaceProvider.get().with(event.getUser(), event.getFolder(),
		        event.getMessage(), event.getMessageDetails(), event.getReplyAll() ? Type.REPLY_ALL : Type.REPLY));
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

		public void updateTreeItem(ImapFolder folder);

		public void deleteSelectedFolder();

		public HasEditable createFolder(EditHandler handler);

		public void increaseUnseenMessageCount(ImapFolder folder, int amount);

		public void decreaseUnseenMessageCount(ImapFolder folder, int amount);

		public void setLoadingFolders(boolean loading);

		public void setLoadingMessage(boolean loading);

		public void setUser(User user);

	}

}
